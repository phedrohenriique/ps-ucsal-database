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
import app.models.DeviceCreate;
import app.models.DeviceDataTimeSeries;
import app.models.DeviceUpdate;
import app.models.Plant;
import app.models.PlantCreate;
import app.models.PlantUpdate;

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
                                result.getString("model_device"),
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

    public List<DeviceDataTimeSeries> getDeviceDataTimeSeriesList(Integer IdDevice) {

        List<DeviceDataTimeSeries> deviceDataList = new ArrayList<>();
        String sql = new DataSQL().getDeviceDataTimeSeries();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setInt(1, IdDevice);
                    ResultSet result = stmt.executeQuery();

                    while (result.next()) {
                        DeviceDataTimeSeries data = new DeviceDataTimeSeries(
                                result.getInt("id_device"),
                                result.getTimestamp("tr_min"),
                                result.getFloat("avg_temperature"),
                                result.getFloat("avg_wind_speed"),
                                result.getFloat("avg_energy")
                        );
                        deviceDataList.add(data);
                    }

                    System.out.println("ROWS RETRIEVED - " + deviceDataList.size());

                } catch (SQLException exception) {

                    System.out.println("ERROR INSERT IN TABLE - devices : " + exception.getMessage());
                }

                return deviceDataList;

    }

    public void createPlant(PlantCreate plant) {

        String sql = new DataSQL().createPlant();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setString(1, plant.getNamePlant());
                    stmt.setString(2, plant.getCompanyPlant());
                    stmt.setString(3, plant.getLocationStatePlant());
                    stmt.setString(4, plant.getLocationPlant());

                    stmt.executeUpdate();

                    System.out.println("INSERT TABLE - plants ");

                } catch (SQLException exception) {

                    System.out.println("ERROR INSERT IN TABLE - plants : " + exception.getMessage());
                }

    }

    public void createDevice(DeviceCreate device) {

        String sql = new DataSQL().createDevice();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setInt(1, device.getIdPlant());
                    stmt.setString(2, device.getModelDevice());
                    stmt.setString(3, device.getTypeDevice());

                    stmt.executeUpdate();

                    System.out.println("INSERT TABLE - devices ");

                } catch (SQLException exception) {

                    System.out.println("ERROR INSERT IN TABLE - devices : " + exception.getMessage());
                }

    }

    public void updatePlant(PlantUpdate plant) {

        String sql = new DataSQL().updatePlant();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setString(1, plant.getNamePlant());
                    stmt.setString(2, plant.getCompanyPlant());
                    stmt.setString(3, plant.getLocationStatePlant());
                    stmt.setString(4, plant.getLocationPlant());
                    stmt.setInt(5, plant.getIdPlant());

                    stmt.executeUpdate();

                    System.out.println("UPDATE TABLE - plants ");

                } catch (SQLException exception) {

                    System.out.println("ERROR UPDATE IN TABLE - plants : " + exception.getMessage());
                }

    }

    public void updateDevice(DeviceUpdate device) {
        String sql = new DataSQL().updateDevice();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setString(1, device.getModelDevice());
                    stmt.setString(2, device.getTypeDevice());
                    stmt.setString(3, device.getStatusDevice().name());
                    stmt.setInt(4, device.getIdPlant());
                    stmt.setInt(5, device.getIdDevice());

                    stmt.executeUpdate();

                    System.out.println("UPDATE TABLE - devices ");

                } catch (SQLException exception) {

                    System.out.println("ERROR UPDATE IN TABLE - devices : " + exception.getMessage());
                }
    }

}
