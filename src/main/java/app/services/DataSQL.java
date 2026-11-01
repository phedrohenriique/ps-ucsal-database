package app.services;

public class DataSQL {

    public String getPlantList() {
        String sql = """
            SELECT * 
            FROM plants;
        """;

        return sql;
    }

    public String getDeviceListByPlantId() {
        String sql = """
            SELECT * 
            FROM devices
            WHERE id_plant=?;
        """;

        return sql;
    }

    public String getDataRowsByPlantId() {
        String sql = """
            SELECT plants.id_plant, devices.id_device, date_trunc('hour',data.created_at) AS per_hour, COUNT(*) AS qty, SUM(energy_generated) AS total_energy
            FROM plants
            JOIN devices ON devices.id_plant=plants.id_plant
            JOIN data ON devices.id_device=data.id_device
            WHERE plants.id_plant=?
            GROUP BY plants.id_plant, devices.id_device, date_trunc('hour',data.created_at)
            ORDER BY devices.id_device DESC;
        """;

        return sql;
    }

    public String getDeviceDataTimeSeries() {
        String sql = """
            SELECT 
            devices.id_device, 
            date_trunc('minute',data.created_at) AS tr_min, -- tr = time range
            AVG(temperature) AS avg_temperature,
            AVG(wind_speed_ms) AS avg_wind_speed,
            AVG(energy_mw) AS avg_energy
            FROM devices
            JOIN data ON devices.id_device=data.id_device
            WHERE devices.id_device=?
            GROUP BY devices.id_device, date_trunc('minute',data.created_at)
            ORDER BY tr_min ASC;
        """;

        return sql;
    }

    public String createPlant() {
        String sql = """
            INSERT INTO plants (
                name_plant,
                company_plant,
                location_state_plant,
                location_plant
            )
            VALUES
                (?, ?, ?, ?);
        """;

        return sql;
    }

    public String createDevice() {
        String sql = """
            INSERT INTO devices (
                id_plant,
                model_device,
                type_device
            )
            VALUES
                (?, ?, ?);
        """;

        return sql;
    }

    public String updatePlant() {
        String sql = """
            UPDATE plants 
            SET 
                name_plant = COALESCE(NULLIF(?, ''), name_plant),
                company_plant = COALESCE(NULLIF(?, ''), company_plant),
                location_state_plant = COALESCE(NULLIF(?, ''), location_state_plant),
                location_plant = COALESCE(NULLIF(?, ''), location_plant)
            WHERE id_plant=?;
        """;

        return sql;
    }

    public String updateDevice() {
        String sql = """
            UPDATE devices 
            SET 
                model_device = COALESCE(NULLIF(?, ''), model_device),
                type_device = COALESCE(NULLIF(?, ''), type_device),
                status_device = COALESCE(NULLIF(?, ''), status_device)
            WHERE id_plant=? AND id_device=?;
        """;

        return sql;
    }

    public String deletePlant() {
        String sql = """

        BEGIN;

        -- 1. DELETE DATA FROM DEVICES

        DELETE FROM data
        USING devices
        WHERE data.id_device = devices.id_device
        AND devices.id_plant=?;
        
        -- 2. DELETE DEVICES

        DELETE FROM devices
        WHERE id_plant=?;
        
        -- DELETE PLANTS

        DELETE FROM plants
        WHERE id_plant=?;

        COMMIT;

        """;

        return sql;
    }

    public String deleteDevice() {
        String sql = """

        BEGIN;

            DELETE FROM data
            WHERE id_device=?;

            DELETE FROM devices
            WHERE id_device=?;
        
        COMMIT;

        """;

        return sql;
    }

}
