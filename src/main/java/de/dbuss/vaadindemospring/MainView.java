package de.dbuss.vaadindemospring;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;

import javax.naming.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Route
@PWA(name = "WeblogicVaadinGridExampleSpring", shortName = "WVGESpring")
public class MainView extends VerticalLayout {
    public MainView() {
        final Text errors = new Text("");
        final Grid<MonitoringEntry> grid = new Grid<MonitoringEntry>();
        grid.addColumn(MonitoringEntry::getId).setHeader("Id");
        grid.addColumn(MonitoringEntry::getSql).setHeader("Sql");
        grid.addColumn(MonitoringEntry::getTitel).setHeader("Titel");
        grid.addColumn(MonitoringEntry::getBeschreibung).setHeader("Beschreibung");
        grid.addColumn(MonitoringEntry::getHandlungsInfo).setHeader("Handlungsinfo");
        grid.addColumn(MonitoringEntry::getCheckIntervall).setHeader("Checkintervall");
        grid.addColumn(MonitoringEntry::isActive).setHeader("Aktiv?");
        final ComboBox<String[]> dataSourcesComboBox = new ComboBox<>("Datenquelle");
        dataSourcesComboBox.setItemLabelGenerator(strArray -> ((String[])strArray)[1]);
        dataSourcesComboBox.setItems(getDataSources());
        dataSourcesComboBox.addValueChangeListener(e -> {
            try {
                if (e.getValue()==null || ((String[])e.getValue()).length==0) {
                    grid.setItems(new ArrayList<>());
                } else {
                    grid.setItems(getData(((String[])e.getValue())[0]));
                }
            } catch (Exception ex) {
                errors.setText(ex.getMessage());
            }
        });
        add(dataSourcesComboBox);
        add(grid);
        add(errors);
    }

    private List<MonitoringEntry> getData(String dataSourceName) throws SQLException, NamingException {
        List<MonitoringEntry> result = new ArrayList<>();
        Context initCtx = new InitialContext();
        DataSource ds = (DataSource) initCtx.lookup(dataSourceName);
        if (ds != null) {
            try (Connection conn = ds.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, sql, titel, beschreibung, handlungs_info, " +
                         " check_intervall, warning_schwellwert, error_schwellwert, is_active, sql_detail " +
                         " FROM fvm_monitoring")) {
                while (rs.next()) {
                    result.add(new MonitoringEntry(rs.getLong("id"), rs.getString("sql"),
                            rs.getString("titel"), rs.getString("beschreibung"),
                            rs.getString("handlungs_info"), rs.getInt("check_intervall"),
                            rs.getInt("warning_schwellwert"), rs.getInt("error_schwellwert"),
                            "1".equals(rs.getString("is_active")), rs.getString("sql_detail")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            throw new SQLException("Datenquelle nicht gefunden für jdbc/" + dataSourceName);
        }
        return result;
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
}
