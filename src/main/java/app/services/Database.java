package app.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import app.configuration.Configuration;

public class Database {

    private final Configuration configuration;

    public Database(Configuration configuration) {

        this.configuration = configuration;
    }

    public void accessDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(
            this.configuration.getDbUrl(),
            this.configuration.getDbUser(),
            this.configuration.getDbPassword()
        )) {

            System.out.println("DATABASE CONNECTION SUCCESSFUL");
        }
    }

}
