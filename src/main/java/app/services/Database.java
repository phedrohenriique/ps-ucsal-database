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
import app.models.DeviceInfo;
import app.models.DeviceUpdate;
import app.models.Plant;
import app.models.PlantCreate;
import app.models.PlantInfo;
import app.models.PlantUpdate;

public class Database {

    private final Configuration configuration;

    public Database(Configuration configuration) throws SQLException {

        this.configuration = configuration;
        isDatabaseConnected();

    }

    public void isDatabaseConnected() throws SQLException {
        try (Connection _ = DriverManager.getConnection(
                this.configuration.getDbUrl(),
                this.configuration.getDbUser(),
                this.configuration.getDbPassword()
        )) {

            System.out.println("DATABASE CONNECTION SUCCESS");

        } catch (SQLException exception) {

            System.out.println("DATABASE CONNECTION ERROR");
            System.out.println(exception.getMessage());
            throw exception;

        }
    }

    public List<Plant> getPlantList() throws SQLException {

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
                    throw exception;
                }

                return listPlant;

    }

    public List<Device> getDeviceListbyPlantId(Integer id) throws SQLException {

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
                    throw exception;
                }

                return listDevice;

    }

    public List<DeviceDataTimeSeries> getDeviceDataTimeSeriesList(Integer IdDevice) throws SQLException {

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
                    throw exception;
                }

                return deviceDataList;

    }

    public void createPlant(PlantCreate plant) throws SQLException {

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
                    throw exception;
                }

    }

    public void createDevice(DeviceCreate device) throws SQLException {

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
                    throw exception;
                }

    }

    public void updatePlant(PlantUpdate plant) throws SQLException {

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
                    throw exception;
                }

    }

    public void updateDevice(DeviceUpdate device) throws SQLException {
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
                    throw exception;
                }
    }

    public void deletePlant(Integer idPlant) throws SQLException {

        String sql = new DataSQL().deletePlant();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setInt(1, idPlant);
                    stmt.setInt(2, idPlant);
                    stmt.setInt(3, idPlant);

                    stmt.executeUpdate();

                    System.out.println("DELETE TABLE - plants ");

                } catch (SQLException exception) {

                    System.out.println("ERROR UPDATE IN TABLE - plants : " + exception.getMessage());
                    throw exception;

                }

    }

    public void deleteDevice(Integer idDevice) throws SQLException {
        String sql = new DataSQL().deleteDevice();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setInt(1, idDevice);
                    stmt.setInt(2, idDevice);

                    stmt.executeUpdate();

                    System.out.println("DELETE TABLE - devices ");

                } catch (SQLException exception) {

                    System.out.println("ERROR UPDATE IN TABLE - devices : " + exception.getMessage());
                    throw exception;
                }
    }

    public PlantInfo getPlantInfoById(Integer id) throws SQLException {

        PlantInfo plantInfo = null;
        String sql = new DataSQL().getPlantInfoById();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setInt(1, id);
                    stmt.setInt(2, id);
                    ResultSet result = stmt.executeQuery();

                    if (result.next()) {
                        plantInfo = new PlantInfo(
                                result.getInt("id_plant"),
                                result.getInt("total_devices"),
                                result.getFloat("max_temperature"),
                                result.getFloat("max_wind_speed"),
                                result.getFloat("total_energy")
                        );

                    }

                } catch (SQLException exception) {

                    System.out.println("ERROR READ IN TABLE - plants : " + exception.getMessage());
                    throw exception;
                }

                return plantInfo;
    }

    public DeviceInfo getDeviceInfoById(Integer id) throws SQLException {

        DeviceInfo deviceInfo = null;
        String sql = new DataSQL().getDeviceInfoById();

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql);) {

                    stmt.setInt(1, id);
                    stmt.setInt(2, id);

                    ResultSet result = stmt.executeQuery();

                    if (result.next()) {
                        deviceInfo = new DeviceInfo(
                                result.getInt("id_device"),
                                result.getInt("id_plant"),
                                result.getString("model_device"),
                                result.getString("type_device"),
                                result.getTimestamp("created_at"),
                                DeviceInfo.Status.valueOf(result.getString("status_device")),
                                result.getFloat("total_energy")
                        );

                    }

                } catch (SQLException exception) {

                    System.out.println("ERROR READ IN TABLE - plants : " + exception.getMessage());
                    throw exception;
                }

                return deviceInfo;
    }

}
