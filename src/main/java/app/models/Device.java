package app.models;

import java.sql.Timestamp;

public class Device {

    public enum Status {
        ACTIVE,
        STOPPED,
        MAINTENANCE,
        DEPRECATED
    }

    Integer id_device;
    Integer id_plant;
    String model_device;
    String type_device;
    Timestamp created_at;
    Status status_device;

    public Device (
        Integer id_device, 
        Integer id_plant, 
        String model_device, 
        String type_device, 
        Timestamp created_at, 
        Status status_device
    ){
        this.id_device = id_device;
        this. id_plant = id_plant;
        this.model_device = model_device;
        this.type_device = type_device;
        this.created_at = created_at;
        this.status_device = status_device;
    }

    @Override
    public String toString(){

        return "(id_device="+this.id_device+",id_plant="+this.id_plant+",status_device="+this.status_device+")";
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Integer getId_device() {
        return id_device;
    }

    public Integer getId_plant() {
        return id_plant;
    }

    public String getName_device() {
        return model_device;
    }

    public Status getStatus_device() {
        return status_device;
    }

    public String getType_device() {
        return type_device;
    }

}
