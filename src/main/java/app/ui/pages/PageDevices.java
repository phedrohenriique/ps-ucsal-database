package app.ui.pages;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.icepear.echarts.Bar;

import app.models.Device;
import app.models.DeviceDataTimeSeries;
import app.services.Database;
import io.javelit.core.Jt;

public class PageDevices {

    private final Database database;

    public PageDevices(Database database) {
        this.database = database;
    }

    public void loadPage() {

        Jt.title("Devices").use();
        pageDevices();
    }

    public void pageDevices() {

        Jt.text("Choose Device - ID to see avaiable information").use();

        var deviceMetricsForm = Jt.form().key("deviceMetricsForm").use();
        String deviceId = Jt.textInput("id - device").key("deviceMetricsFormId").use(deviceMetricsForm);
        boolean submittedMetricsPlantId = Jt.formSubmitButton("Device Info").key("submittedMetricsPlantId")
                .use(deviceMetricsForm);

        if (submittedMetricsPlantId) {

            try {

                Device deviceInfo = database.getDeviceInfoById(Integer.valueOf(deviceId));
                tableDeviceInfo(deviceInfo);

                Jt.text("Device time series information - data below is the average per minute").use();
                addDeviceDataTimeSeriesHistogram(deviceId);

            } catch (Exception exception) {

                Jt.error("ERROR").key("deviceMetricsForm" + "tooltip").use();
                System.out.println(exception.getMessage());
            }

        }

    }

    public void addDeviceDataTimeSeriesHistogram(String deviceId) {

        try {

            Integer id = Integer.valueOf(deviceId);
            List<DeviceDataTimeSeries> deviceTimeSeries = database.getDeviceDataTimeSeriesList(id);

            System.out.println(deviceTimeSeries.size());

            List<String> deviceTrMin = deviceTimeSeries.stream().map(device -> "").toList();
            List<Float> deviceTemperature = deviceTimeSeries.stream().map(device -> device.getAvgTemperature()).toList();
            List<Float> deviceWindSpeed = deviceTimeSeries.stream().map(device -> device.getAvgWindSpeedMs()).toList();
            List<Float> deviceEnergy = deviceTimeSeries.stream().map(device -> device.getAvgEnergyMw()).toList();

            String[] deviceTrMinArray = deviceTrMin.toArray(new String[0]);
            Number[] deviceTemperatureArray = deviceTemperature.toArray(new Number[0]);
            Number[] deviceWindSpeedArray = deviceWindSpeed.toArray(new Number[0]);
            Number[] deviceEnergyArray = deviceEnergy.toArray(new Number[0]);

            Bar bar = new Bar()
                    .setLegend()
                    .setTooltip("item")
                    .addXAxis(deviceTrMinArray)
                    .addYAxis()
                    .addSeries("temperature", deviceTemperatureArray)
                    .addSeries("wind_speed_ms", deviceWindSpeedArray)
                    .addSeries("energy_mw", deviceEnergyArray);

            Jt.echarts(bar).key("chartTemperature").use();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }

    public void tableDeviceInfo(Device device) {

        if (device == null) {
            device = new Device(0, 0, "", "", Timestamp.from(Instant.now()), Device.Status.STOPPED);
        }

        Map<String, Object> row = new LinkedHashMap<>();
        row.put("Id - Device", String.valueOf(device.getIdDevice()));
        row.put("Id - Plant", String.valueOf(device.getIdPlant()));
        row.put("Model", device.getModelDevice());
        row.put("Type", device.getTypeDevice());
        row.put("Status", String.valueOf(device.getStatusDevice()));

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        row.put("Deploy Date", formatter.format(device.getCreatedAt()));

        List<Map<String, Object>> paramsList = new ArrayList<>();
        paramsList.add(row);
        Jt.table(paramsList).use();
    }

}
