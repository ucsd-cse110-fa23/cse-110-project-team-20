package server;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import server.chatgpt.IChatGPTService;
import server.chatgpt.RecipeQuery;
import server.request.FileRequestHelper;
import server.request.IHttpRequest;

public class GenerateRecipeHttpHandler extends HttpHandlerBase {
    private IChatGPTService chatGPTService;

    public GenerateRecipeHttpHandler(IChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    @Override
    protected String handlePost(IHttpRequest request) throws UnsupportedMethodException {
        // @TODO accept 2 files: meal type, ingredients
        File ingredientsFile, mealTypeFile;
        try {
            Map<String, File> files = FileRequestHelper.findAllMultipartFiles(request);

            ingredientsFile = files.get("ingredients_file");
            mealTypeFile = files.get("meal_type_file");
        } catch (IOException e) {
            return e.getMessage();
        }

        // @TODO convert sound files to text via Whisper
        String ingredients = null;
        String mealType = null;

        // create recipe query with mealtype and ingredients
        RecipeQuery query = new RecipeQuery(ingredients, mealType);

        // create recipe via ChatGPT
        String recipeText = chatGPTService.request(query);

        return recipeText;
    }
}
