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

}
