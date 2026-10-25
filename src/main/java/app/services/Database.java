
package app.services;
import io.github.cdimascio.dotenv.Dotenv;

class Config {

    public void getEnv() {
        Dotenv dotenv = Dotenv.load();

        // String dbUser = dotenv.get("DB_USER");
        // String dbPassword = dotenv.get("DB_PASSWORD");
        String dbUrl = dotenv.get("DB_URL");
        System.out.println("URL : " + dbUrl);
    }

}

public class Database {

    public Database() {

        Config config = new Config();
        config.getEnv();
    }

}
