package app.models;

public class Plant {

    private Integer idPlant;
    private String namePlant;
    private String companyPlant;
    private String locationStatePlant;
    private String locationPlant;

    public Plant(
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

    public String getCompanyPlant() {
        return companyPlant;
    }

    @Override
    public String toString() {
        return "(idPlant=" + this.idPlant + ",namePlant=" + this.namePlant + ")";
    }

}
