package server.chatgpt;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPTService implements IChatGPTService {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
  private static final String MODEL = "text-davinci-003";

  private static final int MAX_TOKENS = 100;
  private static final double TEMPERATURE = 1.0;

  IChatGPTConfiguration configuration;

  public ChatGPTService(IChatGPTConfiguration configuration) {
    this.configuration = configuration;
  }

  public String request(RecipeQueryable query) throws ChatGPTServiceException {

    String prompt = query.toQueryableString();

    JSONObject requestBody = new JSONObject();

    requestBody.put("model", MODEL);
    requestBody.put("max_tokens", MAX_TOKENS);
    requestBody.put("temperature", TEMPERATURE);
    requestBody.put("prompt", prompt);

    URI uri;

    try {
      uri = new URI(API_ENDPOINT);
    } catch (URISyntaxException e) {
      throw new ChatGPTServiceException(e);
    }

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(uri)
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", configuration.apiKey()))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

    HttpResponse<String> response;

    try {
      response = client.send(
          request, HttpResponse.BodyHandlers.ofString());

    } catch (IOException e) {
      throw new ChatGPTServiceException(e);
    } catch (InterruptedException e) {
      throw new ChatGPTServiceException(e);
    }

    String responseBody = response.body();

    JSONObject responseJson = new JSONObject(responseBody);

    if (responseJson.has("error")) {
      throw new ChatGPTServiceException(responseJson.getJSONObject("error").getString("message"));
    }

    JSONArray choices = responseJson.getJSONArray("choices");
    String generatedText = choices.getJSONObject(0).getString("text");

    return generatedText;
  }
}
