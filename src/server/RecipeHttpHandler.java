package server;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import client.Recipe;
import server.recipe.IRecipeRepository;
import server.request.IHttpRequest;
/*
 * Recipe HTTP handler
 *
 * Handles HTTP requests between the application and the HTTP server
 * 
 */
public class RecipeHttpHandler extends HttpHandlerBase {
  private IRecipeRepository recipeRepository;

  public RecipeHttpHandler(IRecipeRepository recipeRepository) {
    this.recipeRepository = recipeRepository;
  }

  @Override
  protected String handleGet(IHttpRequest request) throws UnsupportedMethodException {
    String id = request.getQuery("id");

    if (id == null) {
      return response(this.recipeRepository.getRecipes());
    }

    Recipe recipe = this.recipeRepository.getRecipe(Integer.parseInt(id));
    if (recipe == null) {
      return error("Recipe not found");
    }

    return response(recipe);
  }

  @Override
  protected String handlePost(IHttpRequest request) throws UnsupportedMethodException {
    Recipe recipe = convertRecipeFromRequest(request);
    this.recipeRepository.createRecipe(recipe);
    return success("Successfully added");
  }

  @Override
  protected String handlePut(IHttpRequest request) throws UnsupportedMethodException {
    String id = request.getQuery("id");
    if (id == null) {
      return error("id is missing");
    }
    Recipe recipe = convertRecipeFromRequest(request);

    this.recipeRepository.updateRecipe(Integer.parseInt(id), recipe);
    return success("Successfully updated");
  }

  @Override
  protected String handleDelete(IHttpRequest request) throws UnsupportedMethodException {
    String id = request.getQuery("id");
    if (id == null) {
      return error("id is missing");
    }
    this.recipeRepository.deleteRecipe(Integer.parseInt(id));
    return success("Successfully deleted");
  }

  private String success(String message) {
    JSONObject successObject = new JSONObject();
    successObject.put("success", message);
    return successObject.toString(2);
  }

  private String error(String message) {
    JSONObject errorObject = new JSONObject();
    errorObject.put("error", message);
    return errorObject.toString(2);
  }

  private String response(List<Recipe> recipes) {
    JSONObject listObject = new JSONObject();
    listObject.put("recipes", new JSONArray(recipes));
    return listObject.toString(2);
  }

  private String response(Recipe recipe) {
    return new JSONObject(recipe).toString(2);
  }

  private Recipe convertRecipeFromRequest(IHttpRequest request) {
    JSONObject requestObj = new JSONObject(request.getRequestBodyAsString());
    Recipe newRecipe = new Recipe(
      requestObj.has("title") ? requestObj.getString("title") : null,
      requestObj.has("description") ? requestObj.getString("description") : null,
      requestObj.has("ingredients") ? requestObj.getString("ingredients") : null,
      requestObj.has("meal_type") ? requestObj.getString("meal_type") : null
    );
    return newRecipe;
  }
}
