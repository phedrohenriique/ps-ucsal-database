package app.services;

import java.util.List;

import org.icepear.echarts.Bar;

import app.configuration.AppConstants;
import app.configuration.Configuration;
import app.models.Device;
import app.models.Plant;
import io.javelit.components.layout.TabsComponent.Tabs;
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
        Jt.title("OPERATIONS").use();

        Jt.title("PLANT").use();

        var tabsPlant = Jt.tabs(List.of("Create", "Update", "Delete")).key("tabsPlant").use();

        // PLANTS - FORMS

        formCreatePlant(tabsPlant);
        formUpdatePlant(tabsPlant);
        formDeletePlant(tabsPlant);

        // DEVICE - FORMS

        
        Jt.title("DEVICE").use();

        var tabsDevice = Jt.tabs(List.of("Create", "Update", "Delete")).key("tabsDevice").use();

        formCreateDevice(tabsDevice);
        formUpdateDevice(tabsDevice);
        formDeleteDevice(tabsDevice);

    }

    public void navigation() {
        var page = Jt.navigation(
                Jt.page("/plants", () -> pagePlants())
                        .title("Plants")
                        .icon(":factory:"),
                Jt.page("/devices", () -> pageDevices())
                        .title("Devices")
                        .icon(":wind_power:"),
                Jt.page("/operations", () -> pageOperations())
                        .title("Operations")
                        .icon(":settings:")
        ).use();

        page.run();
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

    public void formCreatePlant(Tabs tab) {

        var formCreatePlant = Jt.form().key("createForm").use(tab.tab("Create"));

        String namePlantCreate = Jt.textInput("name").key("name").use(formCreatePlant);
        String companyPlantCreate = Jt.textInput("company").key("company").use(formCreatePlant);
        String locationPlantCreate = Jt.textInput("location").key("locationPlant").use(formCreatePlant);
        String locationStatePlantCreate = Jt.textInput("state").key("locationStatePlant").use(formCreatePlant);

        boolean submittedCreate = Jt.formSubmitButton("Create Plant").key("formButtonCreate").use(formCreatePlant); // add boxes for failure or success

        if (submittedCreate) {
            Jt.success("plant created").key("tooltipSuccessCreate").use();
        }

    }

    public void formUpdatePlant(Tabs tab) {
        var formUpdatePlant = Jt.form().key("updateForm").use(tab.tab("Update"));

        String idPlantUpdate = Jt.textInput("id").key("idUpdate").use(formUpdatePlant);
        String namePlantUpdate = Jt.textInput("name").key("nameUpdate").use(formUpdatePlant);
        String companyPlantUpdate = Jt.textInput("company").key("companyUpdate").use(formUpdatePlant);
        String locationPlantUpdate = Jt.textInput("location").key("locationPlantUpdate").use(formUpdatePlant);
        String locationStatePlantUpdate = Jt.textInput("state").key("locationStatePlantUpdate").use(formUpdatePlant);

        boolean submittedUpdate = Jt.formSubmitButton("Update Plant").key("formButtonUpdate").use(formUpdatePlant); // add boxes for failure or success

        if (submittedUpdate) {
            Jt.success("plant updated").key("tooltipSuccessUpdate").use();
        }
    }

    public void formDeletePlant(Tabs tab){
        var formUpdatePlant = Jt.form().key("deleteForm").use(tab.tab("Delete"));

        String idPlantDelete = Jt.textInput("id").key("idDelete").use(formUpdatePlant);

        boolean submittedDelete = Jt.formSubmitButton("Delete Plant").key("formButtonDelete").use(formUpdatePlant); // add boxes for failure or success

        if (submittedDelete) {
            Jt.success("plant deleted").key("tooltipSuccessDelete").use();
        }
    }

    public void formCreateDevice(Tabs tab) {

        var formCreateDevice = Jt.form().key(AppConstants.KEY_DEVICE_FORM_CREATE+"form").use(tab.tab("Create"));

        String idPlantCreate = Jt.textInput("id - plant").key(AppConstants.KEY_DEVICE_FORM_CREATE+"id").use(formCreateDevice);
        String nameDeviceCreate = Jt.textInput("name").key(AppConstants.KEY_DEVICE_FORM_CREATE+"name").use(formCreateDevice);
        String typeDeviceCreate = Jt.textInput("company").key(AppConstants.KEY_DEVICE_FORM_CREATE+"company").use(formCreateDevice);

        boolean submittedCreate = Jt.formSubmitButton("Create Plant").key(AppConstants.KEY_DEVICE_FORM_CREATE+"button").use(formCreateDevice); // add boxes for failure or success

        if (submittedCreate) {
            Jt.success("device created").key(AppConstants.KEY_DEVICE_FORM_CREATE+"tooltip").use();
        }
    }

    public void formUpdateDevice(Tabs tab) {

        var formCreateDevice = Jt.form().key(AppConstants.KEY_DEVICE_FORM_UPDATE+"form").use(tab.tab("Update"));

        String idDeviceUpdate = Jt.textInput("id").key(AppConstants.KEY_DEVICE_FORM_UPDATE+"id").use(formCreateDevice);
        String idPlantUpdate = Jt.textInput("id - plant").key(AppConstants.KEY_DEVICE_FORM_UPDATE+"idPlant").use(formCreateDevice);
        String nameDeviceUpdate = Jt.textInput("name").key(AppConstants.KEY_DEVICE_FORM_UPDATE+"name").use(formCreateDevice);
        String typeDeviceUpdate = Jt.textInput("company").key(AppConstants.KEY_DEVICE_FORM_UPDATE+"company").use(formCreateDevice);
        String StatusDeviceUpdate = Jt.textInput("status").key(AppConstants.KEY_DEVICE_FORM_UPDATE+"status").use(formCreateDevice);

        boolean submittedUpdate = Jt.formSubmitButton("Create Plant").key(AppConstants.KEY_DEVICE_FORM_UPDATE+"button").use(formCreateDevice); // add boxes for failure or success

        if (submittedUpdate) {
            Jt.success("device created").key(AppConstants.KEY_DEVICE_FORM_UPDATE+"tooltip").use();
        }

    }

    public void formDeleteDevice(Tabs tab){

        var formDeleteDevice = Jt.form().key(AppConstants.KEY_DEVICE_FORM_DELETE+"form").use(tab.tab("Delete"));

        String idDeviceDelete = Jt.textInput("id").key(AppConstants.KEY_DEVICE_FORM_DELETE+"id").use(formDeleteDevice);
        String idPlantDelete = Jt.textInput("id - plant").key(AppConstants.KEY_DEVICE_FORM_DELETE+"idPlant").use(formDeleteDevice);
        

        boolean submittedDelete = Jt.formSubmitButton("Create Plant").key(AppConstants.KEY_DEVICE_FORM_DELETE+"button").use(formDeleteDevice); // add boxes for failure or success

        if (submittedDelete) {
            Jt.success("device created").key(AppConstants.KEY_DEVICE_FORM_DELETE+"tooltip").use();
        }
    }

}
