package app.services;

import java.util.List;

import org.icepear.echarts.Bar;

import app.configuration.Configuration;
import app.models.Device;
import app.models.Plant;
import io.javelit.core.Jt;

public class UI {

    Configuration configuration = new Configuration();
    Database database = new Database(configuration);

    public void pagePlants() {

        List<Plant> plants = database.getPlantList();

        if (plants == null || plants.isEmpty()) {
            Jt.warning("No plants found in the database.").use();
            return;
        }

        record PlantSummary(Integer id, String name) {}

        List<PlantSummary> plant_name_and_state = plants.stream().map(plant -> new PlantSummary(
                plant.getId_plant(),
                plant.getName_plant() + " / " + plant.getLocation_state_plant()
        )).toList();

        Jt.title("Plants Info").use();

        for (PlantSummary obj : plant_name_and_state) {

            if (Jt.button(obj.name()).useContainerWidth(true).use()) {
                Jt.sessionState().put("selectedPlant", String.valueOf(obj.id()));
            }
        }

        String selectedPlant = Jt.sessionState().getString("selectedPlant");

        if (selectedPlant != null) {
            Jt.text("Selected plant: " + selectedPlant).use();
            addHistogram(selectedPlant);
        }

    }

    public void pageDevices() {
        Jt.title("Devices Info").use();
        String selectedPlant = Jt.sessionState().getString("selectedPlant");
        Integer selectedPlantNumber = Integer.valueOf(selectedPlant);
        System.out.println("SELECTED NUMBER:" + selectedPlantNumber);
        List<Device> deviceList = database.getDeviceListbyPlantId(selectedPlantNumber);

        if (deviceList == null || deviceList.isEmpty()) {
            Jt.warning("No device list found in database.").use();
            return;
        }

        System.out.println("LIST DEVICE SIZE:" + deviceList.size());

        record DeviceSummary(Integer device_id, Integer plant_id, Device.Status status) {}

        List<DeviceSummary> deviceListSummary = deviceList.stream().map(device -> new DeviceSummary(
                device.getId_device(),
                device.getId_plant(),
                device.getStatus_device()
        )).toList();

        for (DeviceSummary obj : deviceListSummary) {

            if (Jt.button("Device: " + String.valueOf(obj.device_id)).useContainerWidth(true).use()) {
                Jt.sessionState().put("selectedDevice", String.valueOf(obj.device_id));
            }
        }

        String selectedDevice = Jt.sessionState().getString("selectedDevice");

        if (selectedPlant != null) {
            Jt.text("Selected device: " + selectedDevice).use();
        }

    }

    public void pageOperations() {
        Jt.title("Operations").use();
    }

    public void navigation() {
        var page = Jt.navigation(
                Jt.page("/page1", () -> pagePlants())
                        .title("Plants")
                        .icon(":factory:"),
                Jt.page("/page2", () -> pageDevices())
                        .title("Devices")
                        .icon(":wind_power:"),
                Jt.page("/page3", () -> pageOperations())
                        .title("Operations")
                        .icon(":settings:")
        ).use();

        page.run();
    }

    public void appUI() {

        Jt.title("Plant Monitoring").use();
        Jt.markdown("## Wind Power Plants").use();

        // Fetch plants from the database
        List<Plant> plants = database.getPlantList();

        if (plants == null || plants.isEmpty()) {
            Jt.warning("No plants found in the database.").use();
            return;
        }

        // Create a tab label for each plant.
        // Including the state distinguishes Oitis PI from Oitis BA.
        List<String> tabLabels = plants.stream().map(plant -> plant.getName_plant() + " / " + plant.getLocation_state_plant()).toList();

        // Upper navigation menu
        var tabs = Jt.tabs(tabLabels).use();

        // Display each plant's information inside its tab
        for (int i = 0; i < plants.size(); i++) {

            Plant plant = plants.get(i);
            var tab = tabs.tab(i);

            Jt.markdown("### " + plant.getName_plant()).use(tab);
            Jt.divider().use(tab);

            Jt.text("Plant ID: " + plant.getId_plant()).use(tab);
            Jt.text("Company: " + plant.getCompany_plant()).use(tab);
            Jt.text("State: " + plant.getLocation_state_plant()).use(tab);
            Jt.text("Location: " + plant.getLocation_plant()).use(tab);
        }
    }

    public void addHistogram(String plantId) {
        Jt.title("Average Data per Device").use();

        // plantId =  (plantId != null) ? plantId : "1";
        Integer id = Integer.valueOf(plantId);
        List<Device> devices = database.getDeviceListbyPlantId(id);

        List<String> deviceNames = devices.stream().map(device -> Integer.toString(device.getId_device())).toList();
        Integer size = deviceNames.size();

        String[] deviceNamesConverted = new String[size];
        Number[] mockDataSpeed = new Number[size];
        Number[] mockDataEnergy = new Number[size];

        int i = 0;
        for (String name : deviceNames) {

            deviceNamesConverted[i] = name;
            i++;
        }

        int j = 0;
        for (String _ : deviceNames) {
            mockDataEnergy[j] = Math.random() * 300 + 100;
            j++;
        }

        int k = 0;
        for (String _ : deviceNames) {
            mockDataSpeed[k] = Math.random() * 100 + 10;
            k++;
        }

        Bar bar = new Bar()
                .setLegend()
                .setTooltip("item")
                .addXAxis(deviceNamesConverted)
                .addYAxis()
                // .addSeries("2015", new Number[]{43.3, 83.1, 86.4, 72.4})
                .addSeries("Wind Speed m/s - 2026", mockDataSpeed)
                .addSeries("Energy Kw/h - 2026", mockDataEnergy);
                

        Jt.echarts(bar).use();
    }

}
