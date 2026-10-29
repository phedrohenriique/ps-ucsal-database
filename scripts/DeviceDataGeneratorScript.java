
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//  mvn -f script-pom.xml clean compile exec:java"
public class DeviceDataGeneratorScript {

    public static void main(String[] args) throws Exception {
        Database db = new Database();
        db.generateDataRow();

        System.out.println("SCRIPT WORKS");

    }

}

class Configuration {

    private final String dbURL;
    private final String dbUser;
    private final String dbPassword;
    private final String device;

    public Configuration() {

        // environment variables already passed in the container
        // Dotenv library loads from file, wich does not exist in script
        this.dbURL = System.getenv("DB_URL_EXTERNAL");
        this.dbUser = System.getenv("DB_USER");
        this.dbPassword = System.getenv("DB_PASSWORD");
        this.device = System.getenv("DEVICE");

        if (this.dbURL == null || this.dbURL.isBlank()) {
            throw new IllegalStateException("DB_URL IS NOT CONFIGURED");
        }
    }

    public String getDbUrl() {
        return this.dbURL;
    }

    public String getDbUser() {
        return this.dbUser;
    }

    public String getDbPassword() {
        return this.dbPassword;
    }

    public String getDevice(){
        return this.device;
    }

}

class DataSQL {

    public String addDataRow() {
        String sql = """
            INSERT INTO data (
                id_device, 
                temperature, 
                wind_speed_ms, 
                energy_generated
            )
            VALUES (?, ?, ?, ?);
        """;

        return sql;
    }

}

class Database {

    private final Configuration configuration = new Configuration();
    private final DataSQL sql = new DataSQL();
    Integer deviceId = Integer.valueOf(configuration.getDevice());
    Integer rows = 0;

    public void generateDataRow() {

        try (
                Connection connection = DriverManager.getConnection(
                        this.configuration.getDbUrl(),
                        this.configuration.getDbUser(),
                        this.configuration.getDbPassword()
                ); PreparedStatement stmt = connection.prepareStatement(sql.addDataRow());) {

                    for (Integer i = 0; i < 1000; i++) {

                        stmt.setInt(1, this.deviceId);
                        stmt.setInt(2, 100);
                        stmt.setInt(3, 100);
                        stmt.setInt(4, 100);

                        stmt.executeUpdate();

                        this.rows++;

                    }

                    System.out.println("SUCCESS INSERT IN TABLE - data ");
                    System.out.println("ROWS INSERTED - " + this.rows);

                } catch (SQLException exception) {

                    System.out.println("ERROR INSERT IN TABLE - data : " + exception.getMessage());
                }

    }

    public void connectionPool() {

    }

}
