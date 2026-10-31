
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

    public String getDevice() {
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
                energy_mw
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

                    for (Integer i = 0; i < 10000; i++) {
                        try {

                            stmt.setInt(1, this.deviceId);
                            stmt.setDouble(2, Math.random() * 50 + -10);
                            stmt.setDouble(3, Math.random() * 45 + 5);
                            stmt.setDouble(4, Math.random() * 10 + 8);

                            stmt.executeUpdate();

                            this.rows++;

                            Thread.sleep(2000); // Pause for 2000 milliseconds (2 seconds)
                        } catch (InterruptedException exception) {
                            System.out.println("ERROR: " + exception.getMessage());
                            Thread.currentThread().interrupt(); // Restore the interrupted status
                        } catch (Exception exception) {
                            System.out.println("ERROR: " + exception.getMessage());

                        }

                        System.out.println("ROWS INSERTED - " + this.rows);

                    }

                } catch (SQLException exception) {

                    System.out.println("ERROR INSERT IN TABLE - data : " + exception.getMessage());
                }

    }

    public void connectionPool() {

    }

}
