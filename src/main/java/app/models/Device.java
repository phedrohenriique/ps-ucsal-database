package app.models;

import java.sql.Timestamp;

public class Device {

    public enum Status {
        ACTIVE,
        STOPPED,
        MAINTENANCE,
        DEPRECATED
    }

    private Integer idDevice;
    private Integer idPlant;
    private String modelDevice;
    private String typeDevice;
    private Timestamp createdAt;
    private Status statusDevice;

    public Device(
            Integer idDevice,
            Integer idPlant,
            String modelDevice,
            String typeDevice,
            Timestamp createdAt,
            Status statusDevice
    ) {
        this.idDevice = idDevice;
        this.idPlant = idPlant;
        this.modelDevice = modelDevice;
        this.typeDevice = typeDevice;
        this.createdAt = createdAt;
        this.statusDevice = statusDevice;
    }

    @Override
    public String toString() {

        return "(idDevice=" + this.idDevice + ",idPlant=" + this.idPlant + ",statusDevice=" + this.statusDevice + ")";
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Integer getIdDevice() {
        return idDevice;
    }

    public Integer getIdPlant() {
        return idPlant;
    }

    public String getModelDevice() {
        return modelDevice;
    }

    public Status getStatusDevice() {
        return statusDevice;
    }

    public String getTypeDevice() {
        return typeDevice;
    }

}
