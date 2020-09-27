package dao;


import utils.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoFactory {

    private static Logger log = Logger.getLogger(DaoFactory.class.getName());

    private String url;
    private String driver;
    private String username;
    private String password;

    //  ?????
    public DaoFactory(String programPath) throws IOException, ClassNotFoundException {
        setProperties(programPath);
        setDriver();
    }

    private void setDriver() throws ClassNotFoundException {
        try {
            if (driver != null) {
                Class.forName(driver);
            }
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "Database Driver not found ", e);
            throw e;
        }
    }

    private void setProperties(String filePath) throws IOException {

        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(filePath + "application.properties")
        ) {
            if (fis != null) {
                prop.load(fis);
            } else {
                throw new FileNotFoundException("Database properties file not found in a directory" + filePath);
            }
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "Database properties file not found in a directory" + filePath, e);
            throw e;
        } catch (IOException e) {
            log.log(Level.SEVERE, "IOException during setProperties ", e);
            throw e;
        }
        url = prop.getProperty(Constants.PROPERTY_URL);
        driver = prop.getProperty(Constants.PROPERTY_DRIVER);
        username = prop.getProperty(Constants.PROPERTY_USERNAME);
        password = prop.getProperty(Constants.PROPERTY_PASSWORD);
    }

    public Connection getConnection() throws SQLException {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Cannot getConnection ", e);
            throw e;
        }
        return connection;
    }


}
