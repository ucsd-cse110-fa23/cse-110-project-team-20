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
    private static final String PROPERTIES_FILENAME = "server.properties";

    private String connectionString;
    private String apiKey;

    private String hostname = "localhost";
    private int port = 8100;

    public ServerConfiguration()
    {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PROPERTIES_FILENAME));

            hostname = properties.getProperty("server.hostname", hostname);
            port = Integer.parseInt(properties.getProperty("server.port", String.valueOf(port)));

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

    public String
    getHostname()
    {
        return hostname;
    }

    public int
    getPort()
    {
        return port;
    }
}
