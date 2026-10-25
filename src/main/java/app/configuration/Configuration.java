package app.configuration;
import io.github.cdimascio.dotenv.Dotenv;

public class Configuration {

    private final String dbURL;
    private final String dbUser;
    private final String dbPassword;

    public Configuration(){
        Dotenv dotenv = Dotenv.load();
        this.dbURL =  dotenv.get("DB_URL");
        this.dbUser = dotenv.get("DB_USER");
        this.dbPassword = dotenv.get("DB_PASSWORD");

        if (this.dbURL == null || this.dbURL.isBlank()) {
            throw new IllegalStateException("DB_URL IS NOT CONFIGURED");
        }
    }

    public String getDbUrl(){
        return this.dbURL;
    }

    public String getDbUser(){
        return this.dbUser;
    }

    public String getDbPassword(){
        return this.dbPassword;
    }

}