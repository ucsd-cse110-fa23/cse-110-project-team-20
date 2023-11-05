package client.recipe;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import client.chatgpt.IChatGPTConfiguration;

public class ChatGPTRecipeGenerator implements GenerateRecipe {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/completions";
  private static final String MODEL = "text-davinci-003";

  private static final int MAX_TOKENS = 100;
  private static final double TEMPERATURE = 1.0;

  IChatGPTConfiguration configuration;

  public ChatGPTRecipeGenerator(IChatGPTConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void requestGeneratingRecipe(
      RecipeQueryable query,
      RecipeGenerated onRecipeGenerated,
      RecipeGenerationFailed onRecipeGenerationFailed) {

    Thread t = new Thread(() -> {
      try {
        String recipe = request(query);
        onRecipeGenerated.onRecipeGenerated(recipe);
      } catch (Exception e) {
        e.printStackTrace();
        onRecipeGenerationFailed.onRecipeGenerationFailed();
      }
    });
    t.start();
  }

  private String request(RecipeQueryable query)
      throws IOException, InterruptedException, URISyntaxException {

    String prompt = query.toQueryableString();

    JSONObject requestBody = new JSONObject();

    requestBody.put("model", MODEL);
    requestBody.put("max_tokens", MAX_TOKENS);
    requestBody.put("temperature", TEMPERATURE);
    requestBody.put("prompt", prompt);

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", configuration.apiKey()))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

    HttpResponse<String> response = client.send(
        request, HttpResponse.BodyHandlers.ofString());

    String responseBody = response.body();

    JSONObject responseJson = new JSONObject(responseBody);

    JSONArray choices = responseJson.getJSONArray("choices");
    String generatedText = choices.getJSONObject(0).getString("text");

    return generatedText;
  }
}
