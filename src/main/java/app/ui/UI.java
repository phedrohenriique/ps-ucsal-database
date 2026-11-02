package app.ui;

import app.services.Database;
import app.ui.pages.PageDevices;
import app.ui.pages.PageDevicesDetail;
import app.ui.pages.PageOperations;
import app.ui.pages.PagePlants;
import io.javelit.core.Jt;

public class UI {

    private final Database database;
    private final PageDevices pageDevices;
    private final PageDevicesDetail pageDevicesDetail;
    private final PageOperations pageOperations;
    private final PagePlants pagePlants;

    public UI(Database database) {
        this.database = database;
        this.pageDevices = new PageDevices();
        this.pageDevicesDetail = new PageDevicesDetail();
        this.pageOperations = new PageOperations(this.database);
        this.pagePlants = new PagePlants(this.database);
    }

    public void navigation() {

        var page = Jt.navigation(
                Jt.page("/plants", () -> pagePlants.loadPage()) // () -> pagePlants()
                        .title("Plants")
                        .icon(":factory:"),
                Jt.page("/devices", () -> pageDevices.loadPage())
                        .title("Devices")
                        .icon(":wind_power:"),
                Jt.page("/operations", () -> pageOperations.loadPage())
                        .title("Operations")
                        .icon(":settings:"),
                Jt.page("/device-details", () -> pageDevicesDetail.loadPage()))
                .use();

        page.run();
    }

}
