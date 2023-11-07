package server;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import server.api.ITextGenerateService;
import server.api.RecipeQuery;
import server.request.FileRequestHelper;
import server.request.IHttpRequest;

public class GenerateRecipeHttpHandler extends HttpHandlerBase {
    private ITextGenerateService textGenerateService;

    public GenerateRecipeHttpHandler(ITextGenerateService textGenerateService)
    {
        this.textGenerateService = textGenerateService;
    }

    @Override
    protected String
    handlePost(IHttpRequest request) throws UnsupportedMethodException
    {
        // @TODO accept 2 files: meal type, ingredients
        File ingredientsFile, mealTypeFile;
        try {
            Map<String, File> files = FileRequestHelper.findAllMultipartFiles(request);

            ingredientsFile = files.get("ingredients_file");
            mealTypeFile = files.get("meal_type_file");
        } catch (IOException e) {
            return e.getMessage();
        }

        // Run concurrently
        CompletableFuture<String> future1 =
            CompletableFuture.supplyAsync(() -> Whisper.transcribe(ingredientsFile));
        CompletableFuture<String> future2 =
            CompletableFuture.supplyAsync(() -> Whisper.transcribe(mealTypeFile));

        String ingredients = future1.join();
        String mealType = future2.join();

        // create recipe query with mealtype and ingredients
        RecipeQuery query = new RecipeQuery(ingredients, mealType);

        // create recipe via ChatGPT
        String recipeText = textGenerateService.request(query);

        return recipeText;
    }
}
