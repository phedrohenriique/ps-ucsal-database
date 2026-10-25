package app;


// $ choco install maven ( for windows users )
// go to scrapper/app-java
// run script with
// $ mvn compile
// $ java -cp target/classes App
// to run directly
// mvn compile exec:java

import app.services.Database;

public class App {

    public static void main(String[] args) throws Exception {

        Database database = new Database();

        System.out.println("STARTING APP");
    }
}
