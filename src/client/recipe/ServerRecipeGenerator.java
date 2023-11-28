package client.recipe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import client.Recipe;
import client.account.IAccountSession;

/*
 * Server recipe generator
 *
 * Contains methods that parse the user's voice input into text (request())
 * and sends that text to the API to generate an appropriate recipe (if mock data is disabled)
 */
public class ServerRecipeGenerator implements IRecipeGenerator {
  private String path = "/recipe/generate";
  private String API_ENDPOINT;
  private IAccountSession session;

  public ServerRecipeGenerator(IAccountSession session) {
    this(session, "http://localhost:8100");
  }

  public ServerRecipeGenerator(IAccountSession session, String baseUrl) {
    API_ENDPOINT = String.format("%s%s", baseUrl, path);
    this.session = session;
  }

  @Override
  public void requestGeneratingRecipe(
      RecipeRequestParameter parameter,
      IRecipeGenerated onRecipeGenerated,
      IRecipeGenerationFailed onRecipeGenerationFailed) {

    Thread t = new Thread(() -> {
      try {
        String recipeResponse = request(parameter);
        Recipe recipe = Recipe.fromJson(recipeResponse);
        onRecipeGenerated.onRecipeGenerated(recipe);
      } catch (Exception e) {
        e.printStackTrace();
        onRecipeGenerationFailed.onRecipeGenerationFailed(e.getMessage());
      }
    });
    t.start();
  }

  // ref: Lab 4
  private String request(RecipeRequestParameter parameter)
      throws IOException, InterruptedException, URISyntaxException {
    // @TODO load two files from param, stream out to the server

    File ingredientsFile = parameter.getIngredientsFile();
    File mealTypeFile = parameter.getMealTypeFile();

    if (! ingredientsFile.canRead()) {
      throw new IOException("Ingredients file cannot found.");
    }

    if (! mealTypeFile.canRead()) {
      throw new IOException("Meal type file cannot found.");
    }

    // Set up HTTP connection
    URL url = new URI(API_ENDPOINT).toURL();
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestProperty("Authorization", session.getToken());
    connection.setRequestMethod("POST");
    connection.setDoOutput(true);

    // Set up request headers
    String boundary = "Boundary-" + System.currentTimeMillis();
    connection.setRequestProperty(
        "Content-Type",
        "multipart/form-data; boundary=" + boundary);

    // Set up output stream to write request body
    OutputStream outputStream = connection.getOutputStream();

    // Write file parameter to request body
    writeFileToOutputStream(outputStream, "meal_type_file", mealTypeFile, boundary);
    writeFileToOutputStream(outputStream, "ingredients_file", ingredientsFile, boundary);

    // Write closing boundary to request body
    outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());

    // Flush and close output stream
    outputStream.flush();
    outputStream.close();

    // Get response code
    int responseCode = connection.getResponseCode();
    BufferedReader in = new BufferedReader(
        new InputStreamReader(connection.getInputStream()));
    String inputLine;
    StringBuilder response = new StringBuilder();
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();

    connection.disconnect();

    if (responseCode == HttpURLConnection.HTTP_OK) {
      return response.toString();
    }

    throw new GenerateRecipeException(response.toString());
  }

  // ref: Lab 4
  private static void writeFileToOutputStream(
      OutputStream outputStream,
      String fieldName,
      File file,
      String boundary) throws IOException {
    outputStream.write(("--" + boundary + "\r\n").getBytes());
    outputStream.write(
        ("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" +
            file.getName() +
            "\"\r\n").getBytes());
    outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());

    FileInputStream fileInputStream = new FileInputStream(file);
    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
    outputStream.write(("\r\n--" + boundary + "\r\n").getBytes());
    fileInputStream.close();
  }

}
