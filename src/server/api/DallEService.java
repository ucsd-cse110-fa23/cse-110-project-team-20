package server.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

import org.json.JSONObject;

public class DallEService implements ITextToImageService {
  private static final String API_ENDPOINT = "https://api.openai.com/v1/images/generations";
  private static final String MODEL = "dall-e-2";

  private static final String SIZE = "256x256";
  private static final double N = 1;

  IOpenAIConfiguration configuration;

  public DallEService(IOpenAIConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public String createImage(IRecipeQuery query) throws TextToImageServiceException {
    JSONObject response;
    try {
      response = request(query.toQueryableString());
    } catch (Exception e) {
      e.printStackTrace();
      throw new TextToImageServiceException(e);
    }

    if (response.has("error")) {
      throw new TextGenerateServiceException(response.getJSONObject("error").getString("message"));
    }

    String imageUrl = response.getJSONArray("data").getJSONObject(0).getString("url");

    File file = null;

    try {
      InputStream in = new URI(imageUrl).toURL().openStream();
      String path = "build/tmp/image-" + UUID.randomUUID().toString() + ".jpg";
      Files.copy(in, Paths.get(path));
      file = new File(path);
    } catch (Exception e) {
      e.printStackTrace();
      throw new TextToImageServiceException(e);
    }

    String result = "";

    try {
      FileInputStream in = new FileInputStream(file);
      result = Base64.getEncoder().encodeToString(in.readAllBytes());
    } catch (Exception e) {
      e.printStackTrace();
      throw new TextToImageServiceException(e);
    }
    return "data:image/jpg;base64," + result;
  }

  private JSONObject request(String prompt) throws IOException, InterruptedException {
    // Create a request body which you will pass into request object
    JSONObject requestBody = new JSONObject();
    requestBody.put("model", MODEL);
    requestBody.put("prompt", prompt);
    requestBody.put("n", N);
    requestBody.put("size", SIZE);

    // Create the HTTP client
    HttpClient client = HttpClient.newHttpClient();

    // Create the request object
    HttpRequest request = HttpRequest
        .newBuilder()
        .uri(URI.create(API_ENDPOINT))
        .header("Content-Type", "application/json")
        .header("Authorization", String.format("Bearer %s", configuration.apiKey()))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
        .build();

    // Send the request and receive the response
    HttpResponse<String> response = client.send(
        request,
        HttpResponse.BodyHandlers.ofString());

    // Process the response
    String responseBody = response.body();

    JSONObject responseJson = new JSONObject(responseBody);
    return responseJson;
  }
}
