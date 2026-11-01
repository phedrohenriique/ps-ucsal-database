package app.models;

public class DeviceCreate {

    private final Integer idPlant;
    private final String modelDevice;
    private final String typeDevice;

    public DeviceCreate(
            Integer idPlant,
            String modelDevice,
            String typeDevice
    ) {
        this.idPlant = idPlant;
        this.modelDevice = modelDevice;
        this.typeDevice = typeDevice;
    }

    public Integer getIdPlant() {
        return idPlant;
    }

    public String getModelDevice() {
        return modelDevice;
    }

    public String getTypeDevice() {
        return typeDevice;
    }

}
