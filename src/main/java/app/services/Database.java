package app.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import app.configuration.Configuration;
import app.models.Device;
import app.models.Plant;

public class Database {

    private final Configuration configuration;

    public Database(Configuration configuration) {

        this.configuration = configuration;
    }

    public void accessDatabase() throws SQLException {
        try (Connection _ = DriverManager.getConnection(
                this.configuration.getDbUrl(),
                this.configuration.getDbUser(),
                this.configuration.getDbPassword()
        )) {

            System.out.println("DATABASE CONNECTION SUCCESSFUL");
        }
    }

    // public void createDatabaseTables() throws SQLException {
    //     try (
    //             Connection connection = DriverManager.getConnection(
    //                     this.configuration.getDbUrl(),
    //                     this.configuration.getDbUser(),
    //                     this.configuration.getDbPassword()
    //             ); Statement stmt = connection.createStatement()) {
    //                 DataSQL dataSQL = new DataSQL();
    //                 String plantsTableSQL = dataSQL.createTablePlantsSQL();
    //                 String devicesTableSQL = dataSQL.createTableDevicesSQL();
    //                 String dataTableSQL = dataSQL.createTableDataSQL();
    //                 stmt.executeUpdate(plantsTableSQL);
    //                 System.out.println("TABLE CREATED - plants");
    //                 stmt.executeUpdate(devicesTableSQL);
    //                 System.out.println("TABLE CREATED - devices");
    //                 stmt.executeUpdate(dataTableSQL);
    //                 System.out.println("TABLE CREATED - data");
    //             } catch (SQLException exception) {
    //                 System.out.println("ERROR CREATING TABLES : " + exception.getMessage());
    //             }
    // }
    // public void generateDataPlants() throws SQLException {
    //     try (
    //             Connection connection = DriverManager.getConnection(
    //                     this.configuration.getDbUrl(),
    //                     this.configuration.getDbUser(),
    //                     this.configuration.getDbPassword()
    //             ); Statement stmt = connection.createStatement()) {
    //                 DataSQL dataSQL = new DataSQL();
    //                 String generatePlantsData = dataSQL.generatePlantsData();
    //                 stmt.executeUpdate(generatePlantsData);
    //                 System.out.println("SUCCESS INSERT TABLE - plants");
    //             } catch (SQLException exception) {
    //                 System.out.println("ERROR INSERT IN TABLE - plants : " + exception.getMessage());
    //             }
    // }
    // public void generateDataDevices() throws SQLException {
    //     try (
    //             Connection connection = DriverManager.getConnection(
    //                     this.configuration.getDbUrl(),
    //                     this.configuration.getDbUser(),
    //                     this.configuration.getDbPassword()
    //             ); Statement stmt = connection.createStatement()) {
    //                 DataSQL dataSQL = new DataSQL();
    //                 String generateDevicesData = dataSQL.generateDevicesData();
    //                 stmt.executeUpdate(generateDevicesData);
    //                 System.out.println("SUCCESS INSERT TABLE - devices");
    //             } catch (SQLException exception) {
    //                 System.out.println("ERROR INSERT IN TABLE - devices : " + exception.getMessage());
    //             }
    // }
    public List<Plant> getPlantList() {

        List<Plant> listPlant = new ArrayList<>();
        String sql = new DataSQL().getPlantList();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); Statement stmt = connection.createStatement(); ResultSet result = stmt.executeQuery(sql);) {

                    while (result.next()) {
                        Plant plant = new Plant(
                                result.getInt("id_plant"),
                                result.getString("name_plant"),
                                result.getString("company_plant"),
                                result.getString("location_state_plant"),
                                result.getString("location_plant")
                        );
                        listPlant.add(plant);
                    }

                    System.out.println("ROWS RETRIEVED - " + listPlant.size());

                } catch (SQLException exception) {

                    System.out.println("ERROR INSERT IN TABLE - devices : " + exception.getMessage());
                }

                return listPlant;

    }

    public List<Device> getDeviceListbyPlantId(Integer id) {

        List<Device> listDevice = new ArrayList<>();
        String sql = new DataSQL().getDeviceListByPlantId();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setInt(1, id);
                    ResultSet result = stmt.executeQuery();

                    while (result.next()) {
                        Device device = new Device(
                                result.getInt("id_device"),
                                result.getInt("id_plant"),
                                result.getString("name_device"),
                                result.getString("type_device"),
                                result.getTimestamp("created_at"),
                                Device.Status.valueOf(result.getString("status_device"))
                        );
                        listDevice.add(device);
                    }

                    System.out.println("ROWS RETRIEVED - " + listDevice.size());

                } catch (SQLException exception) {

                    System.out.println("ERROR INSERT IN TABLE - devices : " + exception.getMessage());
                }

                return listDevice;

    }

}
