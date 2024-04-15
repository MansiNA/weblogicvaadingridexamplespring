# WeblogicVaadinGridExampleSpring

Dieses erweiterte Projekt demonstriert die Integration des offiziellen Weblogic Maven Plugins sowie die Umsetzung als Spring Boot Applikation mit Vaadin Progressive Web Application (PWA) Technologie. Es ermöglicht das Debuggen auf der Entwicklermaschine unter Verwendung des eingebetteten Tomcat-Servers von Spring Boot, inklusive Features wie Live Reload der Vaadin-Oberflächen bzw. der Spring Java-Klassen. Zusätzlich lässt sich die von Maven erzeugte WAR-Datei nahtlos auf dem Weblogic Server deployen.

## Erweiterte Voraussetzungen

Zusätzlich zu den bereits genannten Voraussetzungen für die einfache Version benötigt dieses Projekt spezifische Abhängigkeiten von **Spring Boot** sowie das **Weblogic Maven Plugin** in der `pom.xml`. Das Weblogic Maven Plugin kann gemäß der [offiziellen Oracle-Dokumentation](https://docs.oracle.com/en/middleware/standalone/weblogic-server/14.1.1.0/wlprg/maven.html#GUID-C6FC7582-2D1C-4EA5-B000-71AE9A2F2B05) in das lokale Maven Repository installiert werden. Es ist entscheidend zu verstehen, dass die Datenquellen in Spring Boot, wenn sie mit dem eingebetteten Tomcat Server verwendet werden, als JNDI-Einträge in der `Application.java` definiert und somit im Applikationskontext existieren. Im Kontrast dazu befinden sich die Datenquellen auf dem Weblogic Server im globalen Kontext.

## Integration des Weblogic Maven Plugins

Dieses Projekt nutzt das Weblogic Maven Plugin, um die WAR-Datei direkt aus dem Maven-Lifecycle heraus auf einem Weblogic Server zu deployen. Hierfür ist eine Konfiguration im `pom.xml` der Anwendung notwendig, die das Plugin und seine Abhängigkeiten definiert.

## Projekt erstellen und ausführen

### Lokales Setup

Zur lokalen Entwicklung und zum Testen können Sie das Projekt direkt über die Run/Debug Configurations von IntelliJ starten. Dies startet die eingebettete Tomcat-Instanz und ermöglicht das Debugging und Testen der Anwendung auf Ihrer lokalen Maschine.

### WAR-Datei erzeugen und deployen

Um eine WAR-Datei zu erzeugen, die speziell für den Einsatz auf dem Weblogic Server vorbereitet ist, verwenden Sie den folgenden Befehl:

```bash
mvn -Pproduction clean package
```

Anschließend können Sie das Weblogic Maven Plugin nutzen, um die Anwendung direkt zu deployen:

```bash
mvn weblogic:deploy
```

## Projektstruktur

Die Struktur dieses Projekts ist ähnlich der der einfachen Version, jedoch erweitert um spezielle Konfigurationen für Spring Boot und das Weblogic Maven Plugin. Die Kernlogik der Anwendung ist nach wie vor in der Klasse MainView implementiert, ergänzt durch spezifische Konfigurationen in der Application.java für Spring Boot. In einem echten Projekt würde man für Weblogic typischerweise eine lokale Referenz auf eine JNDI DataSource in der web.xml definieren, die anschließend über einen sogenannten Deployment Plan mittels der Weblogic-Benutzeroberfläche nach dem Deployment konfiguriert wird. Innerhalb der Applikation, insbesondere bei der Nutzung von JPA mit einem ORM-Framework wie Hibernate, würde man konstant mit vordefinierten und während der Laufzeit unveränderlichen Datenquellen arbeiten, schon alleine weil die JPA-Validierung beim Wechsel der Datenquelle zeitaufwendig ist.
