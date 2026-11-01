package app.models;

public class DeviceUpdate {

    public enum Status {
        ACTIVE,
        STOPPED,
        MAINTENANCE,
        DEPRECATED
    }

    private final Integer idDevice;
    private final Integer idPlant;
    private final String modelDevice;
    private final String typeDevice;
    private final Status statusDevice;

    public DeviceUpdate(
            Integer idDevice,
            Integer idPlant,
            String modelDevice,
            String typeDevice,
            Status statusDevice
    ) {
        this.idDevice = idDevice;
        this.idPlant = idPlant;
        this.modelDevice = modelDevice;
        this.typeDevice = typeDevice;
        this.statusDevice = statusDevice;
    }

}
