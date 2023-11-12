package server;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.json.JSONObject;

import client.Recipe;
import server.api.ITextGenerateService;
import server.api.IVoiceToTextService;
import server.api.RecipeQuery;
import server.request.FileRequestHelper;
import server.request.IHttpRequest;
import java.io.BufferedWriter;
import java.io.FileWriter;

// Primarily for console and debugging purposes; displays the status and result of server interactions.
public class GenerateRecipeHttpHandler extends HttpHandlerBase {
    private ITextGenerateService textGenerateService;
    private IVoiceToTextService voiceToTextService;

    public GenerateRecipeHttpHandler(ITextGenerateService textGenerateService, IVoiceToTextService voiceToTextService)
    {
        this.textGenerateService = textGenerateService;
        this.voiceToTextService = voiceToTextService;
    }

    @Override
    protected String
    handlePost(IHttpRequest request) throws UnsupportedMethodException
    {
        info("Recevied generating recipe request.");

        // accept 2 files: meal type, ingredients
        File ingredientsFile, mealTypeFile;
        try {
            Map<String, File> files = FileRequestHelper.findAllMultipartFiles(request);

            ingredientsFile = files.get("ingredients_file");
            mealTypeFile = files.get("meal_type_file");
        } catch (IOException e) {
            return e.getMessage();
        }

        info("Found two audio files from the request. Send to voice to text API.");

        // Run concurrently
        CompletableFuture<String> future1 =
            CompletableFuture.supplyAsync(() -> voiceToTextService.transcribe(ingredientsFile));
        CompletableFuture<String> future2 =
            CompletableFuture.supplyAsync(() -> voiceToTextService.transcribe(mealTypeFile));

        String ingredients = future1.join();
        String mealType = future2.join();

        info(String.format("Recieved: \"%s\", \"%s\" from voice to text service", ingredients, mealType));

        // create recipe query with mealtype and ingredients
        RecipeQuery query = new RecipeQuery(ingredients, mealType);

        info("Request receipe generation on text generate service: " + query.toQueryableString());

        // create recipe via ChatGPT
        String recipeText = textGenerateService.request(query);

        info("Received: " + recipeText.trim());
        // write(recipeText, "text-generated.txt");

        // clean up
        String result[] = recipeText
            .trim()
            .split("\n", 2);

        // write(result[0], "text-generated-0.txt");
        // write(result[1], "text-generated-1.txt");
        String title = result[0].trim();
        String description = result[1].trim();

        info("  - title: " + title);
        info("  - description: " + description);
        Recipe recipe = new Recipe(title, description, ingredients, mealType);
        String response = new JSONObject(recipe).toString(2);

        // write(response, "created.txt");
        return response;
    }

    private void info(String message) {
        System.out.println("[GenerateRecipeHttpHandler] " + message);
    }

    private void write(String data, String filename) {
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
        writer.write(data);
        writer.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
}
