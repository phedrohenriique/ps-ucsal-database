package app.services;

public class DataSQL {

    public enum Status {
        ACTIVE,
        STOPPED,
        MAINTENANCE,
        DEPRECATED
    }

    public String createTablePlantsSQL() {

        String sql = """
            CREATE TABLE IF NOT EXISTS plants (
                id_plant INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                name_plant VARCHAR(100) NOT NULL,
                company_plant VARCHAR(100) NOT NULL,
                location_state_plant VARCHAR(10) NOT NULL,
                location_plant VARCHAR(100) UNIQUE
            );
        """;

        return sql;
    }

    public String createTableDevicesSQL() {

        String sql = """
            CREATE TABLE IF NOT EXISTS devices (
                id_device INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                id_plant INT NOT NULL,
                name_device VARCHAR(100),
                type_device VARCHAR(100),
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
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

    public String createTableDataSQL() {

        String sql = """
            CREATE TABLE IF NOT EXISTS data (
                id_device INT NOT NULL,
                created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                temperature REAL,
                wind_speed_ms REAL,
                wind_speed_kmh REAL,
                energy_generated REAL,
                PRIMARY KEY (id_device, created_at),
                FOREIGN KEY (id_device)
                    REFERENCES Devices(id_device)
            );
        """;

        return sql;
    }

    public String generatePlantsData() {
        String sql = """
            INSERT INTO Plants (
                name_plant,
                company_plant,
                location_state_plant,
                location_plant
            )
            VALUES
                (
                    'Complexo Eólico Lagoa dos Ventos',
                    'Enel Green Power',
                    'PI',
                    'Lagoa do Barro do Piauí, Queimada Nova e Dom Inocêncio'
                ),
                (
                    'Complexo Eólico Campo Largo',
                    'Engie',
                    'BA',
                    'Sento Sé'
                ),
                (
                    'Complexo Eólico do Chuí',
                    'Omega Energia',
                    'RS',
                    'Chuí e Santa Vitória do Palmar'
                ),
                (
                    'Complexo Eólico Oitis',
                    'Neoenergia',
                    'PI',
                    'Dom Inocêncio'
                ),
                (
                    'Complexo Eólico Oitis',
                    'Neoenergia',
                    'BA',
                    'Casa Nova'
                );
        """;
        return sql;
    }

    public String generateDevicesData() {
        String sql = """

        INSERT INTO Devices (
            id_plant,
            name_device,
            type_device
        )
        VALUES
            -- Complexo Eólico Lagoa dos Ventos (PI)
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                   AND location_state_plant = 'PI'),
                'Lagoa dos Ventos - Device 01', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                   AND location_state_plant = 'PI'),
                'Lagoa dos Ventos - Device 02', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                   AND location_state_plant = 'PI'),
                'Lagoa dos Ventos - Device 03', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                   AND location_state_plant = 'PI'),
                'Lagoa dos Ventos - Device 04', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                   AND location_state_plant = 'PI'),
                'Lagoa dos Ventos - Device 05', 'Wind Turbine'
            ),
        
            -- Complexo Eólico Campo Largo (BA)
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Campo Largo'
                   AND location_state_plant = 'BA'),
                'Campo Largo - Device 01', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Campo Largo'
                   AND location_state_plant = 'BA'),
                'Campo Largo - Device 02', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Campo Largo'
                   AND location_state_plant = 'BA'),
                'Campo Largo - Device 03', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Campo Largo'
                   AND location_state_plant = 'BA'),
                'Campo Largo - Device 04', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Campo Largo'
                   AND location_state_plant = 'BA'),
                'Campo Largo - Device 05', 'Wind Turbine'
            ),
        
            -- Complexo Eólico do Chuí (RS)
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico do Chuí'
                   AND location_state_plant = 'RS'),
                'Chuí - Device 01', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico do Chuí'
                   AND location_state_plant = 'RS'),
                'Chuí - Device 02', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico do Chuí'
                   AND location_state_plant = 'RS'),
                'Chuí - Device 03', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico do Chuí'
                   AND location_state_plant = 'RS'),
                'Chuí - Device 04', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico do Chuí'
                   AND location_state_plant = 'RS'),
                'Chuí - Device 05', 'Wind Turbine'
            ),
        
            -- Complexo Eólico Oitis (PI)
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'PI'),
                'Oitis PI - Device 01', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'PI'),
                'Oitis PI - Device 02', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'PI'),
                'Oitis PI - Device 03', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'PI'),
                'Oitis PI - Device 04', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'PI'),
                'Oitis PI - Device 05', 'Wind Turbine'
            ),
        
            -- Complexo Eólico Oitis (BA)
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'BA'),
                'Oitis BA - Device 01', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'BA'),
                'Oitis BA - Device 02', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'BA'),
                'Oitis BA - Device 03', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'BA'),
                'Oitis BA - Device 04', 'Wind Turbine'
            ),
            (
                (SELECT id_plant FROM Plants
                 WHERE name_plant = 'Complexo Eólico Oitis'
                   AND location_state_plant = 'BA'),
                'Oitis BA - Device 05', 'Wind Turbine'
            );
        """;

        return sql;
    }

    public String generateDataRow() {
        String sql = """

        """;

        return sql;
    }

}
