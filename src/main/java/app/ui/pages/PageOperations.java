package app.ui.pages;

import java.util.List;

import app.configuration.AppConstants;
import app.models.DeviceCreate;
import app.models.DeviceUpdate;
import app.models.PlantCreate;
import app.models.PlantUpdate;
import app.services.Database;
import io.javelit.components.layout.TabsComponent.Tabs;
import io.javelit.core.Jt;

public class PageOperations {

    private final Database database;

    public PageOperations(Database database) {
        this.database = database;
    }

    public void loadPage(){
        
        Jt.title("Operations").use();
        pageOperations();
    }

    public void pageOperations() {

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
                Jt.success("SUCCESS - CREATE").key("tooltipSuccessCreate").use();

            } catch (Exception exception) {
                Jt.error("ERROR - CREATE").key("tooltipErrorCrate").use();
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
                Jt.success("SUCCESS - UPDATE").key("tooltipSuccessUpdate").use();

            } catch (Exception exception) {
                Jt.error("ERROR - UPDATE").key("tooltipSuccessUpdate").use();
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
                Jt.success("SUCCESS - DELETE").key("tooltipSuccessDelete").use();

            } catch (Exception exception) {
                Jt.error("ERROR - DELETE").key("tooltipSuccessDelete").use();
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
                Jt.success("SUCCESS - CREATE").key(AppConstants.KEY_DEVICE_FORM_CREATE + "tooltip").use();

            } catch (Exception exception) {
                Jt.error("ERROR - CREATE").key("tooltipSuccessDelete").use();
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
        String StatusDeviceUpdate = Jt.selectbox("status",
                List.of("ACTIVE", "STOPPED", "MAINTENANCE", "DEPRECATED")).key(AppConstants.KEY_DEVICE_FORM_UPDATE + "status").use(formCreateDevice);

        boolean submittedUpdate = Jt.formSubmitButton("Update Device")
                .key(AppConstants.KEY_DEVICE_FORM_UPDATE + "button").use(formCreateDevice);

        if (submittedUpdate) {

            try {

                DeviceUpdate deviceUpdate = new DeviceUpdate(Integer.valueOf(idDeviceUpdate), Integer.valueOf(idPlantUpdate), modelDeviceUpdate, typeDeviceUpdate, DeviceUpdate.Status.valueOf(StatusDeviceUpdate));
                database.updateDevice(deviceUpdate);
                Jt.success("SUCCESS - UPDATE").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "tooltip").use();

            } catch (Exception exception) {
                Jt.error("ERROR - UPDATE").key(AppConstants.KEY_DEVICE_FORM_UPDATE + "tooltip").use();
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
                Jt.success("SUCCESS - DELETE").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();

            } catch (Exception exception) {
                Jt.error("ERROR - DELETE").key(AppConstants.KEY_DEVICE_FORM_DELETE + "tooltip").use();
                System.out.println(exception.getMessage());
            }

        }
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