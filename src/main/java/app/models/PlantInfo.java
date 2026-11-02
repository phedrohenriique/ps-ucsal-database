package app.models;

public class PlantInfo {

    private final Integer idPlant;
    private final Integer totalDevices;
    private final Float maxTemperature;
    private final Float maxWindSpeed;
    private final Float totalEnergy;

    public PlantInfo(
            Integer idPlant,
            Integer totalDevices,
            Float maxTemperature,
            Float maxWindSpeed,
            Float totalEnergy
    ) {
        this.idPlant = idPlant;
        this.totalDevices = totalDevices;
        this.maxTemperature = maxTemperature;
        this.maxWindSpeed = maxWindSpeed;
        this.totalEnergy = totalEnergy;
    }

    public Integer getIdPlant() {
        return idPlant;
    }

    public Float getMaxTemperature() {
        return maxTemperature;
    }

    public Float getMaxWindSpeed() {
        return maxWindSpeed;
    }

    public Integer getTotalDevices() {
        return totalDevices;
    }

    public Float getTotalEnergy() {
        return totalEnergy;
    }

}
