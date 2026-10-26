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

    public void createDatabaseTables() throws SQLException {
        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); Statement stmt = connection.createStatement()) {

                    DataSQL dataSQL = new DataSQL();
                    String plantsTableSQL = dataSQL.createTablePlantsSQL();
                    String devicesTableSQL = dataSQL.createTableDevicesSQL();
                    String dataTableSQL = dataSQL.createTableDataSQL();

                    stmt.executeUpdate(plantsTableSQL);
                    System.out.println("TABLE CREATED - plants");

                    stmt.executeUpdate(devicesTableSQL);
                    System.out.println("TABLE CREATED - devices");

                    stmt.executeUpdate(dataTableSQL);
                    System.out.println("TABLE CREATED - data");

                } catch (SQLException exception) {

                    System.out.println("ERROR CREATING TABLES : " + exception.getMessage());
                }
    }

    public void generateDataPlants() throws SQLException {
        try (
            Connection connection = DriverManager.getConnection(
                    this.configuration.getDbUrl(),
                    this.configuration.getDbUser(),
                    this.configuration.getDbPassword()
            ); Statement stmt = connection.createStatement()) {

                DataSQL dataSQL = new DataSQL();
                String generatePlantsData = dataSQL.generatePlantsData();

                stmt.executeUpdate(generatePlantsData);
                System.out.println("SUCCESS INSERT TABLE - plants");

            } catch (SQLException exception) {

                System.out.println("ERROR INSERT IN TABLE - plants : " + exception.getMessage());
            }
    }

    public void generateDataDevices() throws SQLException {
        try (
            Connection connection = DriverManager.getConnection(
                    this.configuration.getDbUrl(),
                    this.configuration.getDbUser(),
                    this.configuration.getDbPassword()
            ); Statement stmt = connection.createStatement()) {

                DataSQL dataSQL = new DataSQL();
                String generateDevicesData = dataSQL.generateDevicesData();

                stmt.executeUpdate(generateDevicesData);
                System.out.println("SUCCESS INSERT TABLE - devices");

            } catch (SQLException exception) {

                System.out.println("ERROR INSERT IN TABLE - devices : " + exception.getMessage());
            }
    }

    // public void generateDataRows() throws SQLException {
    //     try (
    //         Connection connection = DriverManager.getConnection(
    //                 this.configuration.getDbUrl(),
    //                 this.configuration.getDbUser(),
    //                 this.configuration.getDbPassword()
    //         ); Statement stmt = connection.createStatement()) {

    //             DataSQL dataSQL = new DataSQL();

    //             stmt.executeUpdate(dataTableSQL);
    //             System.out.println("TABLE - Data CREATED");

    //         } catch (SQLException exception) {

    //             System.out.println("ERROR CRETING TABLES : " + exception.getMessage());
    //         }
    // }

}
