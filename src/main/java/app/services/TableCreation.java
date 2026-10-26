package app.services;

public class TableCreation {

    public enum Status {
        ACTIVE,
        STOPPED,
        MAINTENANCE,
        DEPRECATED
    }

    public String createTablePlantsSQL(){

        String sql = """
            CREATE TABLE IF NOT EXISTS Plants (
                id_plant INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name_plant VARCHAR(100) NOT NULL,
                location_plant VARCHAR(100) UNIQUE
            );
        """;

        return sql;
    }

    public String createTableDevicesSQL(){

        String sql = """
            CREATE TABLE IF NOT EXISTS Devices (
                id_device INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                id_plant INT NOT NULL,
                name_device VARCHAR(100),
                type_device VARCHAR(100),
                status_device VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                    CHECK (status_device IN (
                        'ACTIVE',
                        'STOPPED',
                        'MAINTENANCE',
                        'DEPRECATED'
                    )),
                FOREIGN KEY (id_plant)
                    REFERENCES Plants(id_plant)
            );
        """;

        return sql;
    }

    public String createTableDataSQL(){

        String sql = """
            CREATE TABLE IF NOT EXISTS Data (
                id_device INT NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                temperature REAL,
                wind_speed REAL,
                energy_generated REAL,
                PRIMARY KEY (id_device, created_at),
                FOREIGN KEY (id_device)
                    REFERENCES Devices(id_device)
            );
        """;

        return sql;
    }


    

}