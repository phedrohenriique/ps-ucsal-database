package app.services;

import java.util.List;

import app.configuration.Configuration;
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

        record PlantSummary(Integer id, String name){}
        
        List<PlantSummary> plant_name_and_state = plants.stream().map(plant -> new PlantSummary(
            plant.getId_plant(),
            plant.getName_plant() + " / " + plant.getLocation_state_plant()
        )).toList();

        Jt.title("Plants Info").use();

        for(PlantSummary obj : plant_name_and_state){

            if (Jt.button(obj.name()).useContainerWidth(true).use()) {
                Jt.sessionState().put("selectedPlant", String.valueOf(obj.id()));
            }
        }

        String selectedPlant = Jt.sessionState().getString("selectedPlant");

        if (selectedPlant != null) {
            Jt.text("Selected plant: " + selectedPlant).use();
        }
    }

    public void pageDevices() {
        Jt.title("Devices Info").use();
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

}
