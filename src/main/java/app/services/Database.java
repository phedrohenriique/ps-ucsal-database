package app.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import app.configuration.Configuration;

public class Database {

    private final Configuration configuration;

    public Database(Configuration configuration) {

        this.configuration = configuration;
    }

    public void accessDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
            this.configuration.getDbUrl(),
            this.configuration.getDbUser(),
            this.configuration.getDbPassword()
        )) {

            System.out.println("DATABASE CONNECTION SUCCESSFUL");
        }
    }

    public void createDatabaseTables() throws SQLException{
        try(
        Connection connection = DriverManager.getConnection(
            this.configuration.getDbUrl(),
            this.configuration.getDbUser(),
            this.configuration.getDbPassword()
        ); 
        Statement stmt = connection.createStatement()) {
            
            TableCreation tableCreation = new TableCreation();
            String plantsTableSQL = tableCreation.createTablePlantsSQL();
            String devicesTableSQL = tableCreation.createTableDevicesSQL();
            String dataTableSQL = tableCreation.createTableDataSQL();

            stmt.executeUpdate(plantsTableSQL);
            System.out.println("TABLE - Plants CREATED");

            stmt.executeUpdate(devicesTableSQL);
            System.out.println("TABLE - Devices CREATED");

            stmt.executeUpdate(dataTableSQL);
            System.out.println("TABLE - Data CREATED");

        
    } catch (SQLException exception) {

        System.out.println("ERROR CRETING TABLES : "+ exception.getMessage());
    }
    }

}
