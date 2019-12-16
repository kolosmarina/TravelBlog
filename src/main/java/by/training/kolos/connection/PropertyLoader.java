package by.training.kolos.connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyLoader {
    private static final Logger logger = LogManager.getLogger();

    public Properties loadFile(String fileName) {
        Properties properties = new Properties();
        InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream(fileName);
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Error loadFile", e);
        }
        return properties;
    }
}
