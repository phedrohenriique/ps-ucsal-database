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

import java.sql.SQLException;
import java.util.List;

import app.configuration.Configuration;
import app.models.Plant;
import app.services.Database;

public class App {

    public static void main(String[] args) throws Exception {

        System.out.println("STARTING APP");

        Configuration configuration = new Configuration();
        Database database = new Database(configuration);
        try {

            database.accessDatabase();
            // database.createDatabaseTables();
            // database.generateDataPlants();
            // database.generateDataDevices();
            List<Plant> listPlant = database.getPlantList();
            System.err.println(listPlant);

            // UI interface app
            // UI ui = new UI();
            // ui.runUI();


        } catch (SQLException exception) {

            System.err.println("APP ERROR : " + exception.getMessage());
        }

    }

}
