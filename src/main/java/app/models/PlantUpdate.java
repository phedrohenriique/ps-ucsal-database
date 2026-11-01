package app.models;

public class PlantUpdate {

    private final Integer idPlant;
    private final String namePlant;
    private final String companyPlant;
    private final String locationStatePlant;
    private final String locationPlant;

    public PlantUpdate(
            Integer idPlant,
            String namePlant,
            String companyPlant,
            String locationStatePlant,
            String locationPlant
    ) {
        this.idPlant = idPlant;
        this.namePlant = namePlant;
        this.companyPlant = companyPlant;
        this.locationStatePlant = locationStatePlant;
        this.locationPlant = locationPlant;
    }
}
