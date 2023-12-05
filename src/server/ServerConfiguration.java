package server;

import java.io.FileInputStream;
import java.util.Properties;
import server.api.IOpenAIConfiguration;
import server.mongodb.IMongoDBConfiguration;

/**
 * Server Configuration loaded from property file
 */
public class ServerConfiguration implements IOpenAIConfiguration, IMongoDBConfiguration {
    // default file name for the configuration file
    private static final String PROPERTIES_FILENAME = "app.properties";

    private String connectionString;
    private String apiKey;

    public ServerConfiguration()
    {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PROPERTIES_FILENAME));

            connectionString = properties.getProperty("mongodb.connection_string", "");
            apiKey = properties.getProperty("chatgpt.api_key", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String
    getConnectionString()
    {
        return connectionString;
    }

    @Override
    public String
    apiKey()
    {
        return apiKey;
    }
}
