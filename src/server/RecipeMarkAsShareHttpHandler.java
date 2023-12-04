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
}
