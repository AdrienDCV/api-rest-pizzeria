package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DS {
    
    // attributes
    private Properties prop;
    private String driver;
    private String url;
    private String login;
    private String password;

    // constructor
    public DS() {
        this.prop = new Properties();
        try {
            prop.load(getClass().getResourceAsStream("config.prop"));
            driver = prop.getProperty("driver");
            url = prop.getProperty("url");
            login = prop.getProperty("login");
            password = prop.getProperty("password");
        }   
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // methods
    public Connection getConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, login, password);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}
