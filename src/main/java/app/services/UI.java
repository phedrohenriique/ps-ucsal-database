package app.services;

import java.util.List;

import org.icepear.echarts.Bar;

import app.configuration.AppConstants;
import app.configuration.Configuration;
import app.models.Device;
import app.models.DeviceDataTimeSeries;
import app.models.Plant;
import io.javelit.components.layout.TabsComponent.Tabs;
import io.javelit.core.Jt;

public class UI {

    Configuration configuration = new Configuration();
    Database database = new Database(configuration);

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
                        .icon(":settings:"),
                Jt.page("/device-details", () -> pageDeviceDetails()))
                .use();

        page.run();
    }

    public void pagePlants() {

        Jt.title("Plants Info").use();

        List<Plant> plantsList = database.getPlantList();

        if (plantsList == null || plantsList.isEmpty()) {
            Jt.warning("No plants found in the database.").use();
            return;
        }

        tablePlantsList(plantsList);

        Jt.title("Plants - Metrics").use();


        Jt.title("Plants - Device - Data/min").use();

        var metricsPlantForm = Jt.form().key("metricsPlantForm").use();
        String metricsPlantId = Jt.textInput("id - plant").key("metricsPlantId").use(metricsPlantForm);
        String metricsDeviceId = Jt.textInput("id - device").key("metricsDeviceId").use(metricsPlantForm);
        boolean submittedMetricsPlantId = Jt.formSubmitButton("get metrics").key("submittedMetricsPlantId")
                .use(metricsPlantForm); // add boxes for failure or success

        if (submittedMetricsPlantId) {

            addDeviceDataTimeSeriesHistogram(metricsDeviceId);
            // addHistogram(metricsPlantId);
        }

    }

    public void pageDevices() {

        Jt.title("Devices Info").use();

        // Jt.text(body)("Devices Info").use();
        List<Plant> plantsList = database.getPlantList();

        if (plantsList == null || plantsList.isEmpty()) {
            Jt.warning("No plants found in the database.").use();
            return;
        }

        tablePlantsList(plantsList);

        Jt.title("Device List from Plant").use();

        var getDeviceListByPlantForm = Jt.form().key("getDeviceListByPlant").use();
        String plantId = Jt.textInput("id").key("plantId").use(getDeviceListByPlantForm);
        boolean submittedDeviceList = Jt.formSubmitButton("Get Device List").key("submittedDeviceList")
                .use(getDeviceListByPlantForm); // add boxes for failure or success

        // process first form
        if (submittedDeviceList) {
            Integer plantIdValue = Integer.valueOf(plantId);
            List<Device> deviceList = database.getDeviceListbyPlantId(plantIdValue);
            Jt.sessionState().put("deviceList", deviceList);
            Jt.sessionState().put("selectedPlantId", plantIdValue);
        }

        if (Jt.sessionState().get("deviceList") != null) {

            List<Device> deviceList = (List<Device>) Jt.sessionState().get("deviceList");

            tableDevicesList(deviceList);

            Jt.title("Device Detail").use();

            var getDeviceDetailForm = Jt.form().key("getDeviceDetail").use();
            String deviceIdDetail = Jt.textInput("id device").key("deviceIdDetail").use(getDeviceDetailForm);
            String plantIdDetail = Jt.textInput("id plant").key("plantIdDetail").use(getDeviceDetailForm);
            boolean submittedDeviceDetail = Jt.formSubmitButton("Device Detail").key("submittedDeviceDetail")
                    .use(getDeviceDetailForm); // add boxes for failure or success

            if (submittedDeviceDetail) {

                Jt.sessionState().put("deviceId", deviceIdDetail);
                Jt.sessionState().put("plantId", plantIdDetail);
                Jt.switchPage("/device-details");
            }

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

    public void pageDeviceDetails() {

        if (Jt.sessionState().get("deviceId") == null | Jt.sessionState().get("plantId") == null) {
            Jt.warning("device Id and plant Id are required - go to Devices page or /devices").use();
        }
        

        if (Jt.sessionState().get("deviceId") != null & Jt.sessionState().get("plantId") != null) {
            Jt.success("device detail retrieved for deviceId:" + Jt.sessionState().getString("deviceId") + " plantId: " + Jt.sessionState().getString("plantId"))
                    .key("submittedMetricsPlantId" + "tooltip")
                    .use();
        }

    }

    public void addDeviceDataTimeSeriesHistogram(String deviceId){

        Jt.title("Plant - Device Data - 2026").use();

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

        // the series can be clicked
        // better interaction

        Bar bar = new Bar()
                .setLegend()
                .setTooltip("item")
                .addXAxis(deviceTrMinArray)
                .addYAxis()
                .addSeries("temperature", deviceTemperatureArray)
                .addSeries("wind_speed_ms", deviceWindSpeedArray)
                .addSeries("energy_mw", deviceEnergyArray);

        Jt.echarts(bar).key("chartTemperature").use();

    }

    // FORMS
    public void formCreatePlant(Tabs tab) {

        var formCreatePlant = Jt.form().key("createForm").use(tab.tab("Create"));

        String namePlantCreate = Jt.textInput("name").key("name").use(formCreatePlant);
        String companyPlantCreate = Jt.textInput("company").key("company").use(formCreatePlant);
        String locationPlantCreate = Jt.textInput("location").key("locationPlant").use(formCreatePlant);
        String locationStatePlantCreate = Jt.textInput("state").key("locationStatePlant").use(formCreatePlant);

        boolean submittedCreate = Jt.formSubmitButton("Create Plant").key("formButtonCreate").use(formCreatePlant);

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

        boolean submittedUpdate = Jt.formSubmitButton("Update Plant").key("formButtonUpdate").use(formUpdatePlant);

        if (submittedUpdate) {
            Jt.success("plant updated").key("tooltipSuccessUpdate").use();
        }
    }

    public void formDeletePlant(Tabs tab) {
        var formUpdatePlant = Jt.form().key("deleteForm").use(tab.tab("Delete"));

        String idPlantDelete = Jt.textInput("id").key("idDelete").use(formUpdatePlant);

        boolean submittedDelete = Jt.formSubmitButton("Delete Plant").key("formButtonDelete").use(formUpdatePlant);

        if (submittedDelete) {
            Jt.success("plant deleted").key("tooltipSuccessDelete").use();
        }
    }

    public void formCreateDevice(Tabs tab) {

        var formCreateDevice = Jt.form().key(AppConstants.KEY_DEVICE_FORM_CREATE + "form").use(tab.tab("Create"));

        String idPlantCreate = Jt.textInput("id - plant").key(AppConstants.KEY_DEVICE_FORM_CREATE + "id")
                .use(formCreateDevice);
        String modelDeviceCreate = Jt.textInput("model").key(AppConstants.KEY_DEVICE_FORM_CREATE + "model")
                .use(formCreateDevice);
        String typeDeviceCreate = Jt.textInput("type").key(AppConstants.KEY_DEVICE_FORM_CREATE + "type")
                .use(formCreateDevice);

        boolean submittedCreate = Jt.formSubmitButton("Create Plant")
                .key(AppConstants.KEY_DEVICE_FORM_CREATE + "button").use(formCreateDevice); // add boxes for failure or
        // success

        if (submittedCreate) {
            Jt.success("device created").key(AppConstants.KEY_DEVICE_FORM_CREATE + "tooltip").use();
        }
    }

    public void formUpdateDevice(Tabs tab) {

        var formCreateDevice = Jt.form().key(AppConstants.KEY_DEVICE_FORM_UPDATE + "form").use(tab.tab("Update"));

        String idDeviceUpdate = Jt.textInput("id - device").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "idDevice")
                .use(formCreateDevice);
        String idPlantUpdate = Jt.textInput("id - plant").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "idPlant")
                .use(formCreateDevice);
        String modelDeviceUpdate = Jt.textInput("model").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "model")
                .use(formCreateDevice);
        String typeDeviceUpdate = Jt.textInput("type").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "type")
                .use(formCreateDevice);
        String StatusDeviceUpdate = Jt.textInput("status").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "status")
                .use(formCreateDevice);

        boolean submittedUpdate = Jt.formSubmitButton("Create Plant")
                .key(AppConstants.KEY_DEVICE_FORM_UPDATE + "button").use(formCreateDevice);

        if (submittedUpdate) {
            Jt.success("device created").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "tooltip").use();
        }

    }

    public void formDeleteDevice(Tabs tab) {

        var formDeleteDevice = Jt.form().key(AppConstants.KEY_DEVICE_FORM_DELETE + "form").use(tab.tab("Delete"));

        String idDeviceDelete = Jt.textInput("id - device").key(AppConstants.KEY_DEVICE_FORM_DELETE + "id")
                .use(formDeleteDevice);
        String idPlantDelete = Jt.textInput("id - plant").key(AppConstants.KEY_DEVICE_FORM_DELETE + "idPlant")
                .use(formDeleteDevice);

        boolean submittedDelete = Jt.formSubmitButton("Create Plant")
                .key(AppConstants.KEY_DEVICE_FORM_DELETE + "button").use(formDeleteDevice); // add boxes for failure or
        // success

        if (submittedDelete) {
            Jt.success("device created").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();
        }
    }

    public void getPlantHistogram() {

    }

    public void tablePlantsList(List<Plant> plantsList) {

        var tableContainer = Jt.container().key("tablePlantsList")
                .height(300)
                .use();
        Jt.table(plantsList).use(tableContainer);

    }

    public void tableDevicesList(List<Device> devicesList) {
        System.out.println(devicesList);
        var tableContainer = Jt.container().key("tableDevicesList")
                .height(300)
                .use();
        Jt.table(devicesList).use(tableContainer);
    }

}
