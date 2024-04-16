package de.dbuss.vaadindemospring.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import javax.naming.*;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class MetricsService {

    private DataSource dataSource;
    private final MeterRegistry meterRegistry;
    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        try {
            Context ctx = new InitialContext();
            List<String[]> dataSourcesList = getDataSources();
            String [] datasourceName = dataSourcesList.get(0);
            dataSource = (DataSource) ctx.lookup(datasourceName[0]);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private List<String[]> getDataSources() {
        List<String[]> result = new ArrayList<>();

        // Erst globale Resourcen über JNDI mach DataSources durchsuchen
        try {
            Context initCtx = new InitialContext();
            try {
                NamingEnumeration<NameClassPair> enumeration = initCtx.list("jdbc");
                while (enumeration.hasMoreElements()) {
                    NameClassPair p = enumeration.next();
                    result.add(new String[]{ "jdbc/"+p.getName(), p.getName() });
                }
            } catch (NamingException e) {
                e.fillInStackTrace();
            }
            // Jetzt die Resourcen des Applikationskontext nach DataSources durchsuchen
            Context envCtx = (Context) initCtx.lookup("java:comp/env");
            NamingEnumeration<NameClassPair> enumeration = envCtx.list("jdbc");
            while (enumeration.hasMoreElements()) {
                NameClassPair p = enumeration.next();
                result.add(new String[]{ "java:comp/env/jdbc/"+p.getName(), p.getName() });
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        return result;
    }
    public String fetchMetrics() {
        if (dataSource == null) {
            return "DataSource not available";
        }

        StringBuilder metricsBuilder = new StringBuilder();

        String sqlQuery = "SELECT b.titel, a.result FROM ekp.fvm_monitor_result a, ekp.fvm_monitoring b WHERE a.id = b.id AND a.is_active = 1";

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                String title = resultSet.getString("titel");
                BigDecimal result = resultSet.getBigDecimal("result");

                // Record metrics using Micrometer
                Counter.builder("ekp_metric")
                        .description("EKPMetric")
                        .tags("title", title)
                        .register(meterRegistry)
                        .increment(result.doubleValue());

                // Append metrics to the metrics string in Prometheus format
                metricsBuilder.append("ekp_metric{title=\"")
                        .append(escapeString(title))
                        .append("\"} ")
                        .append(result)
                        .append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Append # EOF at the end
        metricsBuilder.append("# EOF\n");

        return metricsBuilder.toString();
    }

    public String fetchMetricsold() {
        if (dataSource == null) {
            return "DataSource not available";
        }

        String sqlQuery = "SELECT b.titel, a.result FROM ekp.fvm_monitor_result a, ekp.fvm_monitoring b WHERE a.id = b.id AND a.is_active = 1";

        StringBuilder metricsBuilder = new StringBuilder();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)) {

            while (resultSet.next()) {
                String title = resultSet.getString("titel");
                BigDecimal result = resultSet.getBigDecimal("result");

                // Format the metrics into Prometheus format
                String metricLine = String.format("ekp_metric{title=\"%s\"} %s\n", escapeString(title), result);

                metricsBuilder.append(metricLine);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Ensure the last line ends with a line feed character
        if (metricsBuilder.length() > 0 && metricsBuilder.charAt(metricsBuilder.length() - 1) != '\n') {
            metricsBuilder.append('\n');
        }

        // Append # EOF at the end
        metricsBuilder.append("# EOF\n");

        return metricsBuilder.toString();
    }

    // Helper method to escape special characters in label values
    private String escapeString(String str) {
        // Escape backslashes, double-quotes, and line feeds
        return str
                .replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\"", "\\\\\"")
                .replaceAll("\n", "\\\\n");
    }
}
