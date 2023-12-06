package client;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

/**
 * App Configuration loaded from property file
 */
public class AppConfiguration {
    // default file name for the configuration file
    private static final String PROPERTIES_FILENAME = "app.properties";

    private String apiUrl = "http://localhost:8100";

    public AppConfiguration()
    {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PROPERTIES_FILENAME));

            apiUrl = properties.getProperty("server.api_url", apiUrl);
        } catch (FileNotFoundException e) {
            System.out.println("[WARNING] app.properties is missing.");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String
    getApiUrl()
    {
        return apiUrl;
    }
}
