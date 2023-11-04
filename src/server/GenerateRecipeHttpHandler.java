package server;

import server.chatgpt.IChatGPTService;
import server.chatgpt.RecipeQuery;
import server.request.IHttpRequest;

public class GenerateRecipeHttpHandler extends HttpHandlerBase {
    private IChatGPTService chatGPTService;

    public GenerateRecipeHttpHandler(IChatGPTService chatGPTService)
    {
        this.chatGPTService = chatGPTService;
    }

    @Override
    protected String handlePost(IHttpRequest request) throws UnsupportedMethodException {
        // @TODO accept 2 files: meal type, ingredients
        // @TODO convert sound files to text via Whisper

        // @TODO create recipe query with mealtype and ingredients
        RecipeQuery query = new RecipeQuery(null, null);
        
        // create recipe via ChatGPT
        String recipeText = chatGPTService.request(query);

        return recipeText;
    }
}
