package app.models;

public class PlantCreate {

    private final String namePlant;
    private final String companyPlant;
    private final String locationStatePlant;
    private final String locationPlant;

    public PlantCreate(
            String namePlant,
            String companyPlant,
            String locationStatePlant,
            String locationPlant
    ) {
        this.namePlant = namePlant;
        this.companyPlant = companyPlant;
        this.locationStatePlant = locationStatePlant;
        this.locationPlant = locationPlant;
    }
}
