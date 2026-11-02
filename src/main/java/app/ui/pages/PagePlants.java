package app.ui.pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import app.configuration.AppConstants;
import app.models.Device;
import app.models.Plant;
import app.models.PlantInfo;
import app.services.Database;
import io.javelit.core.Jt;

public class PagePlants {

    private final Database database;

    public PagePlants(Database database) {
        this.database = database;
    }

    public void loadPage() {

        Jt.title("Plants").use();
        pagePlants();
    }

    public void pagePlants() {

        try {

            List<Plant> plantsList = database.getPlantList();

            if (plantsList == null || plantsList.isEmpty()) {
                Jt.error("ERROR LOADING PLANTS LIST").use();
                return;
            }

            tablePlantList(plantsList);

            Jt.text("Choose Plant - ID to get avaiable Devices").use();

            var plantInfoForm = Jt.form().key("plantInfoForm").use();
            String plantInfoId = Jt.textInput("id - plant").key("plantInfoId").use(plantInfoForm);
            boolean submmitedPlantInfoForm = Jt.formSubmitButton("Plant - Info").key("submittedPlantInfo")
                    .use(plantInfoForm);

            if (submmitedPlantInfoForm) {

                try {

                    PlantInfo plantInfo = database.getPlantInfoById(Integer.valueOf(plantInfoId));
                    tablePlantInfo(plantInfo);

                    List<Device> deviceList = database.getDeviceListbyPlantId(Integer.valueOf(plantInfoId));
                    tableDeviceListByPlantId(deviceList);


                } catch (Exception exception) {
                    Jt.error("ERROR").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();
                    System.out.println(exception.getMessage());
                }

            }

            // Jt.title("Plants - Device - Data/min").use();
            // Jt.text("Information on the device data ").use();

            // var metricsPlantForm = Jt.form().key("metricsPlantForm").use();
            // String metricsDeviceId = Jt.textInput("id - device").key("metricsDeviceId").use(metricsPlantForm);
            // boolean submittedMetricsPlantId = Jt.formSubmitButton("get metrics").key("submittedMetricsPlantId")
            //         .use(metricsPlantForm);

            // if (submittedMetricsPlantId) {

            //     addDeviceDataTimeSeriesHistogram(metricsDeviceId);
            //     // addHistogram(metricsPlantId);
            // }

        } catch (Exception exception) {

            Jt.error("ERROR").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();
            System.out.println(exception.getMessage());

        }
    }

    public void tablePlantList(List<Plant> plantList) {

        List<Map<String, Object>> paramsList = new ArrayList<>();

        for (Plant plant : plantList) {

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Id - Plant", String.valueOf(plant.getIdPlant()));
            row.put("Name", plant.getNamePlant());
            row.put("Company", plant.getCompanyPlant());
            row.put("State", plant.getLocationStatePlant());
            row.put("Location", plant.getLocationPlant());

            paramsList.add(row);
        }

        var tablePlantList = Jt.container().key("tablePlantList")
                .height(300)
                .use();
        Jt.table(paramsList).use(tablePlantList);

    }

    public void tablePlantInfo(PlantInfo plantInfo) {

        if (plantInfo == null) {
            
            plantInfo = new PlantInfo(0, 0, 0f, 0f, 0f);
        }

        Map<String, Object> row = new LinkedHashMap<>();
        row.put("Devices - Total", String.valueOf(plantInfo.getTotalDevices()));
        row.put("Temperature °C - MAX", String.format(Locale.US, "%.2f", plantInfo.getMaxWindSpeed()));
        row.put("Wind Speed km/h - MAX", String.format(Locale.US, "%.2f", plantInfo.getMaxWindSpeed()));
        row.put("Energy MW - Total", String.format(Locale.US, "%.2f", plantInfo.getTotalEnergy()));

        List<Map<String, Object>> paramsList = new ArrayList<>();
        paramsList.add(row);
        Jt.table(paramsList).use();
    }

    public void tableDeviceListByPlantId(List<Device> deviceList){

        List<Map<String, Object>> paramsList = new ArrayList<>();

        for (Device device : deviceList) {

            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Id - Plant", String.valueOf(device.getIdPlant()));
            row.put("Id - Device", String.valueOf(device.getIdDevice()));
            row.put("Model", String.valueOf(device.getModelDevice()));
            row.put("Type", String.valueOf(device.getTypeDevice()));
            row.put("Status", String.valueOf(device.getStatusDevice()));

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            row.put("Deploy Date", formatter.format(device.getCreatedAt()));

            paramsList.add(row);
        }

        var tableDeviceListByPlantId = Jt.container().key("tableDeviceList")
                .height(300)
                .use();
        Jt.table(paramsList).use(tableDeviceListByPlantId);

    }

}
