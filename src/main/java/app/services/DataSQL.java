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
        WHERE plant_id = ?;
        """;

        return sql;
    }

}
