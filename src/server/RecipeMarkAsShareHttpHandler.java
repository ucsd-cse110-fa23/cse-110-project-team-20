package server;

import org.json.JSONObject;

import server.recipe.ISharedRecipeRepository;
import server.request.IHttpRequest;

public class RecipeMarkAsShareHttpHandler extends HttpHandlerBase {
  private ISharedRecipeRepository sharedRecipeRepository;

  public RecipeMarkAsShareHttpHandler(ISharedRecipeRepository sharedRecipeRepository) {
    this.sharedRecipeRepository = sharedRecipeRepository;
  }

  protected String handlePost(IHttpRequest request) throws UnsupportedMethodException {
    String id = request.getQuery("id");
    try {
      sharedRecipeRepository.markAsShared(Integer.parseInt(id));
    } catch (Exception e) {
      return fail(e.getMessage());
    }
    return success();
  }

  private String success() {
    JSONObject response = new JSONObject();
    response.put("success", true);
    return response.toString(2);
  }

  private String fail(String message) {
    JSONObject response = new JSONObject();
    response.put("error", message);
    return response.toString(2);
  }
}
