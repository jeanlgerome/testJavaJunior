package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class LoggerUtil {

    private Logger log = Logger.getLogger(LoggerUtil.class.getName());

    /**
     * Set logger configuration from logging.properties
     *
     * @param filePath The parameter of program location
     */
    public void init(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath + "logging.properties")
        ) {
            LogManager.getLogManager().readConfiguration(fis);
            log.log(Level.FINEST, "logger has been initialized");
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration. Cannot access logging.properties " + e.toString());
        }
    }

}