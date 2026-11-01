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

    public String getCompanyPlant() {
        return companyPlant;
    }

    public Integer getIdPlant() {
        return idPlant;
    }

    public String getLocationPlant() {
        return locationPlant;
    }

    public String getLocationStatePlant() {
        return locationStatePlant;
    }

    public String getNamePlant() {
        return namePlant;
    }

}
