package app.services;
import java.util.List;

import app.configuration.Configuration;
import app.models.Plant;
import io.javelit.core.Jt;
import io.javelit.core.Server;

public class UI {

    Configuration configuration = new Configuration();
    Database database = new Database(configuration);

    public void runUI() {
        final var server = Server.builder(() -> appUI(), 8080).build();
        server.start();
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
