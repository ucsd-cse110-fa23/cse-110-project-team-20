package server.chatgpt;

import java.io.FileInputStream;
import java.util.Properties;

public class ChatGPTConfiguration implements IChatGPTConfiguration {

  // default file name for the configuration file
  private static final String PROPERTIES_FILENAME = "app.properties";

  private String apiKey;

  public ChatGPTConfiguration() {
    Properties properties = new Properties();

    try {
      properties.load(new FileInputStream(PROPERTIES_FILENAME));

      apiKey = properties.getProperty("chatgpt.api_key", "");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public String apiKey() {
    return apiKey;
  }
}
