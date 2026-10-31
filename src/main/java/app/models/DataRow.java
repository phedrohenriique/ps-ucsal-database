package app.models;

import java.sql.Timestamp;

public class DataRow {

    Integer id_device;
    Timestamp created_at;
    Float temperature;
    Float wind_speed_ms;
    Float wind_speed_kmh;
    Float energy_mw;

    public DataRow(
            Integer id_device,
            Timestamp created_at,
            Float temperature,
            Float wind_speed_ms,
            Float wind_speed_kmh,
            Float energy_mw
    ) {
        this.id_device = id_device;
        this.created_at = created_at;
        this.temperature = temperature;
        this.wind_speed_ms = wind_speed_ms;
        this.energy_mw = energy_mw;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public Float getEnergy_generated() {
        return energy_mw;
    }

    public Integer getId_device() {
        return id_device;
    }

    public Float getTemperature() {
        return temperature;
    }

    public Float getWind_speed_kmh() {
        return wind_speed_kmh;
    }

    public Float getWind_speed_ms() {
        return wind_speed_ms;
    }

}
