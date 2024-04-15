package de.dbuss.vaadindemospring;

import org.apache.catalina.Context;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.descriptor.web.ContextResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatWebServer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ServletWebServerFactory tomcatFactory() {
        return new TomcatServletWebServerFactory() {
            @Override
            protected TomcatWebServer getTomcatWebServer(Tomcat tomcat) {
                tomcat.enableNaming();
                return super.getTomcatWebServer(tomcat);
            }

            @Override
            protected void postProcessContext(Context context) {
                super.postProcessContext(context);
                ContextResource resource = new ContextResource();
                resource.setName("jdbc/ApplicationDataSource");
                resource.setType(DataSource.class.getName());
                resource.setProperty("driverClassName", "oracle.jdbc.OracleDriver");
                resource.setProperty("url", "jdbc:oracle:thin:@//37.120.189.200:1521/XE");
                resource.setProperty("username", "weblogic");
                resource.setProperty("password", "web20240401");
                context.getNamingResources().addResource(resource);
            }
        };
    }
}
