-- init.sql
-- Creates the schema and seeds the five plants and 25 devices.
-- Run against an empty database, or use the reset section below if needed.

BEGIN;

-- 1. Create tables in foreign-key dependency order.

CREATE TABLE IF NOT EXISTS plants (
    id_plant INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name_plant VARCHAR(100) NOT NULL,
    company_plant VARCHAR(100) NOT NULL,
    location_state_plant VARCHAR(10) NOT NULL,
    location_plant VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS devices (
    id_device INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    id_plant INT NOT NULL,
    model_device VARCHAR(100),
    type_device VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status_device VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        CHECK (status_device IN (
            'ACTIVE',
            'STOPPED',
            'MAINTENANCE',
            'DEPRECATED'
        )),
    FOREIGN KEY (id_plant) REFERENCES plants(id_plant)
);

CREATE TABLE IF NOT EXISTS data (
    id_device INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    temperature REAL,
    wind_speed_ms REAL,
    wind_speed_kmh REAL,
    energy_mw REAL,
    PRIMARY KEY (id_device, created_at),
    FOREIGN KEY (id_device) REFERENCES devices(id_device)
);

-- 2. Insert plants only if their unique locations are not already present.
INSERT INTO plants (
    name_plant,
    company_plant,
    location_state_plant,
    location_plant
)
VALUES
    ('Complexo Eólico Lagoa dos Ventos', 'Enel Green Power', 'PI','Lagoa do Barro do Piauí, Queimada Nova e Dom Inocêncio'),
    ('Complexo Eólico Campo Largo', 'Engie', 'BA', 'Sento Sé'),
    ('Complexo Eólico do Chuí', 'Omega Energia', 'RS','Chuí e Santa Vitória do Palmar'),
    ('Complexo Eólico Oitis', 'Neoenergia', 'PI', 'Dom Inocêncio'),
    ('Complexo Eólico Oitis', 'Neoenergia', 'BA', 'Casa Nova'
);

-- 3. Insert five devices for each plant.
-- Resolve plant IDs from plant name and state instead of hard-coding identity values.
INSERT INTO devices (
            id_plant,
            model_device,
            type_device
        )
        VALUES
            -- Complexo Eólico Lagoa dos Ventos (PI)
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                    AND location_state_plant = 'PI'
                ),
                'SI-270150', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                    AND location_state_plant = 'PI'
                ),
                'SI-230100', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                    AND location_state_plant = 'PI'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                    AND location_state_plant = 'PI'
                ),
                'WD200-7700', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Lagoa dos Ventos'
                    AND location_state_plant = 'PI'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
        
            -- Complexo Eólico Campo Largo (BA)
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Campo Largo'
                    AND location_state_plant = 'BA'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Campo Largo'
                    AND location_state_plant = 'BA'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Campo Largo'
                    AND location_state_plant = 'BA'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Campo Largo'
                    AND location_state_plant = 'BA'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Campo Largo'
                    AND location_state_plant = 'BA'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
        
            -- Complexo Eólico do Chuí (RS)
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico do Chuí'
                    AND location_state_plant = 'RS'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico do Chuí'
                    AND location_state_plant = 'RS'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico do Chuí'
                    AND location_state_plant = 'RS'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico do Chuí'
                    AND location_state_plant = 'RS'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico do Chuí'
                    AND location_state_plant = 'RS'
                ),
                'V172-7.2', 
                'wind_turbine'
            ),
        
            -- Complexo Eólico Oitis (PI)
            (
                (   
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'PI'
                ),
                'SI-270150', 
                'wind_turbine'
            ),
            (
                (   
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'PI'
                ),
                'SI-270150', 
                'wind_turbine'
            ),
            (
                (   
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'PI'
                ),
                'SI-270150', 
                'wind_turbine'
            ),
            (
                (   
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'PI'
                ),
                'SI-270150', 
                'wind_turbine'
            ),
            (
                (   
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'PI'
                ),
                'SI-270150', 
                'wind_turbine'
            ),
        
            -- Complexo Eólico Oitis (BA)
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'BA'
                ),
                'SI-230100', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'BA'
                ),
                'SI-230100', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'BA'
                ),
                'SI-230100', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'BA'
                ),
                'SI-230100', 
                'wind_turbine'
            ),
            (
                (
                    SELECT id_plant FROM Plants
                    WHERE name_plant = 'Complexo Eólico Oitis'
                    AND location_state_plant = 'BA'
                ),
                'SI-230100', 
                'wind_turbine'
            );

COMMIT;

-- The data table is created but no sensor readings are inserted because
-- no measurement values were provided.
