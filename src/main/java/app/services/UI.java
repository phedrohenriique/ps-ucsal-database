package app.services;

import java.util.List;

import org.icepear.echarts.Bar;

import app.configuration.AppConstants;
import app.configuration.Configuration;
import app.models.Device;
import app.models.DeviceCreate;
import app.models.DeviceDataTimeSeries;
import app.models.DeviceUpdate;
import app.models.Plant;
import app.models.PlantCreate;
import app.models.PlantInfo;
import app.models.PlantUpdate;
import io.javelit.components.layout.TabsComponent.Tabs;
import io.javelit.core.Jt;

public class UI {

    Configuration configuration = new Configuration();
    Database database = null;

    public UI(){

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
                        .icon(":settings:"),
                Jt.page("/device-details", () -> pageDeviceDetails()))
                .use();

        page.run();
    }

    public void pagePlants() {

        Jt.title("Plants - List").use();

        try {

            List<Plant> plantsList = database.getPlantList();

            if (plantsList == null || plantsList.isEmpty()) {
                Jt.warning("No plants found in the database.").use();
                return;
            }

            tablePlantsList(plantsList);

            Jt.title("Plants - Metrics").use();

            var plantInfoForm = Jt.form().key("plantInfoForm").use();
            String plantInfoId = Jt.textInput("id - plant").key("plantInfoId").use(plantInfoForm);
            boolean submmitedPlantInfoForm = Jt.formSubmitButton("Plant - Info").key("submittedPlantInfo")
                    .use(plantInfoForm);

            if (submmitedPlantInfoForm) {
                // PlantInfo plantInfo = database.getPlantInfoById(Integer.valueOf(plantInfoId));
                // System.out.println(plantInfo);
                // tablePlantInfo(plantInfo);
                System.out.println(plantInfoId);
            }

            Jt.title("Plants - Device - Data/min").use();

            var metricsPlantForm = Jt.form().key("metricsPlantForm").use();
            String metricsPlantId = Jt.textInput("id - plant").key("metricsPlantId").use(metricsPlantForm);
            String metricsDeviceId = Jt.textInput("id - device").key("metricsDeviceId").use(metricsPlantForm);
            boolean submittedMetricsPlantId = Jt.formSubmitButton("get metrics").key("submittedMetricsPlantId")
                    .use(metricsPlantForm);

            if (submittedMetricsPlantId) {

                addDeviceDataTimeSeriesHistogram(metricsDeviceId);
                // addHistogram(metricsPlantId);
            }

        } catch (Exception exception) {

            Jt.error("ERROR").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();
            System.out.println(exception.getMessage());

        }

    }

    public void pageDevices() {

        Jt.title("Devices Info").use();

        try {

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

        } catch (Exception exception) {
        }

    }

    public void pageOperations() {
        Jt.title("Operations").use();

        Jt.title("Plant - Management").use();

        var tabsPlant = Jt.tabs(List.of("Create", "Update", "Delete")).key("tabsPlant").use();

        // PLANTS - FORMS
        formCreatePlant(tabsPlant);
        formUpdatePlant(tabsPlant);
        formDeletePlant(tabsPlant);

        // DEVICE - FORMS
        Jt.title("Device - Management").use();

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

    public void addDeviceDataTimeSeriesHistogram(String deviceId) {

        Jt.title("Plant - Device Data - 2026").use();

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

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

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

            try {

                formCreatePlantCheck(namePlantCreate, companyPlantCreate, locationStatePlantCreate, locationPlantCreate);

                PlantCreate plantCreate = new PlantCreate(namePlantCreate, companyPlantCreate, locationPlantCreate, locationStatePlantCreate);
                database.createPlant(plantCreate);
                Jt.success("SUCCESS").key("tooltipSuccessCreate").use();

            } catch (Exception exception) {
                Jt.error("ERROR").key("tooltipErrorCrate").use();
                System.out.println(exception.getMessage());
            }
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

            try {

                PlantUpdate plantUpdate = new PlantUpdate(Integer.valueOf(idPlantUpdate), namePlantUpdate, companyPlantUpdate, locationPlantUpdate, locationStatePlantUpdate);
                database.updatePlant(plantUpdate);
                Jt.success("SUCCESS").key("tooltipSuccessUpdate").use();

            } catch (Exception exception) {
                Jt.error("ERROR").key("tooltipSuccessUpdate").use();
                System.out.println(exception.getMessage());
            }

        }
    }

    public void formDeletePlant(Tabs tab) {
        var formUpdatePlant = Jt.form().key("deleteForm").use(tab.tab("Delete"));

        String idPlantDelete = Jt.textInput("id").key("idDelete").use(formUpdatePlant);

        boolean submittedDelete = Jt.formSubmitButton("Delete Plant").key("formButtonDelete").use(formUpdatePlant);

        if (submittedDelete) {

            try {

                if (idPlantDelete == null || idPlantDelete.isBlank()) {
                    throw new Exception("Plant ID - NEED VALUE");
                }

                database.deletePlant(Integer.valueOf(idPlantDelete));
                Jt.success("SUCCESS").key("tooltipSuccessDelete").use();

            } catch (Exception exception) {
                Jt.error("ERROR").key("tooltipSuccessDelete").use();
                System.out.println(exception.getMessage());
            }

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

        boolean submittedCreate = Jt.formSubmitButton("Create Device")
                .key(AppConstants.KEY_DEVICE_FORM_CREATE + "button").use(formCreateDevice);

        if (submittedCreate) {
            try {

                formCreateDeviceCheck(idPlantCreate, modelDeviceCreate, typeDeviceCreate);

                DeviceCreate deviceCreate = new DeviceCreate(Integer.valueOf(idPlantCreate), modelDeviceCreate, typeDeviceCreate);
                database.createDevice(deviceCreate);
                Jt.success("SUCCESS").key(AppConstants.KEY_DEVICE_FORM_CREATE + "tooltip").use();

            } catch (Exception exception) {
                Jt.error("ERROR").key("tooltipSuccessDelete").use();
                System.out.println(exception.getMessage());
            }
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
        // String StatusDeviceUpdate = Jt.textInput("status").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "status")
        //         .use(formCreateDevice);
        String StatusDeviceUpdate = Jt.selectbox("status",
                List.of("ACTIVE", "STOPPED", "MAINTENANCE", "DEPRECATED")).key(AppConstants.KEY_DEVICE_FORM_UPDATE + "status").use(formCreateDevice);

        boolean submittedUpdate = Jt.formSubmitButton("Update Device")
                .key(AppConstants.KEY_DEVICE_FORM_UPDATE + "button").use(formCreateDevice);

        if (submittedUpdate) {

            try {

                DeviceUpdate deviceUpdate = new DeviceUpdate(Integer.valueOf(idDeviceUpdate), Integer.valueOf(idPlantUpdate), modelDeviceUpdate, typeDeviceUpdate, DeviceUpdate.Status.valueOf(StatusDeviceUpdate));
                database.updateDevice(deviceUpdate);
                Jt.success("SUCCESS").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "tooltip").use();

            } catch (Exception exception) {
                Jt.error("ERROR").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "tooltip").use();
                System.out.println(exception.getMessage());
            }

        }

    }

    public void formDeleteDevice(Tabs tab) {

        var formDeleteDevice = Jt.form().key(AppConstants.KEY_DEVICE_FORM_DELETE + "form").use(tab.tab("Delete"));

        String idDeviceDelete = Jt.textInput("id - device").key(AppConstants.KEY_DEVICE_FORM_DELETE + "id")
                .use(formDeleteDevice);

        boolean submittedDelete = Jt.formSubmitButton("Delete Device")
                .key(AppConstants.KEY_DEVICE_FORM_DELETE + "button").use(formDeleteDevice);

        if (submittedDelete) {

            try {

                if (idDeviceDelete == null || idDeviceDelete.isBlank()) {
                    throw new Exception("Device ID - NEED VALUE");
                }

                database.deleteDevice(Integer.valueOf(idDeviceDelete));
                Jt.success("SUCCESS").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();

            } catch (Exception exception) {
                Jt.error("ERROR").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();
                System.out.println(exception.getMessage());
            }

        }
    }

    public void tablePlantInfo(PlantInfo plantInfo){

        var tableContainer = Jt.container().key("tablePlantInfo")
                .use();
        Jt.table(plantInfo).use(tableContainer);

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

    public void formCreatePlantCheck(String name, String company, String state, String location) throws Exception {

        if (name == null || name.isBlank()) {
            throw new Exception("Plant name - NEED VALUE");
        }

        if (company == null || company.isBlank()) {
            throw new Exception("Plant company - NEED VALUE");
        }

        if (state == null || state.isBlank()) {
            throw new Exception("Plant state - NEED VALUE");
        }

        if (location == null || location.isBlank()) {
            throw new Exception("Plant location - NEED VALUE");
        }

    }

    public void formCreateDeviceCheck(String idPlant, String model, String type) throws Exception {

        if (idPlant == null || idPlant.isBlank()) {
            throw new Exception("Plant ID - NEED VALUE");
        }

        if (model == null || model.isBlank()) {
            throw new Exception("Device model - NEED VALUE");
        }

        if (type == null || type.isBlank()) {
            throw new Exception("Device type - NEED VALUE");
        }

    }

}
