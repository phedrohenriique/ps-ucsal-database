package app;

// $ choco install maven ( for windows users )
// go to scrapper/app-java
// run script with
// $ mvn compile
// $ java -cp target/classes App
// to run directly
// mvn compile exec:java
// mvn clean compile exec:java
// mvn dependency:tree - check for maven dependencies
// mvn -f scripts/script-pom.xml clean compile exec:java
import java.sql.SQLException;

import app.configuration.Configuration;
import app.services.Database;
import app.ui.UI;
import io.javelit.core.Server;

public class App {

    public static void main(String[] args) throws Exception {

        System.out.println("STARTING APP");

        try {

            Configuration configuration = new Configuration();
            Database database = new Database(configuration);
            UI ui = new UI(database);

            var server = Server.builder(
                    () -> ui.navigation(),
                    8080
            ).build();

            server.start();

        } catch (SQLException exception) {

            System.err.println("ERROR : " + exception.getMessage());

        } catch (Exception exception) {

            System.err.println("ERROR : " + exception.getMessage());
        }

    }

}
