package server.mongodb;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Provides MongoDB configuration from the app.properties file
 */
public class MongoDBConfiguration implements IMongoDBConfiguration {
  // default file name for the configuration file
  private static final String PROPERTIES_FILENAME = "app.properties";

  private String connectionString;

  public MongoDBConfiguration() {
    Properties properties = new Properties();

    try {
      properties.load(new FileInputStream(PROPERTIES_FILENAME));

      connectionString = properties.getProperty("mongodb.connection_string", "");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String getConnectionString() {
    return connectionString;
  }
}
