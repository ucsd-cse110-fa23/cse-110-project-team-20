package client.recipe;

import server.chatgpt.RecipeQueryable;

/**
 * Local Recipe Generator
 * 
 * GenerateRecipe implementation for handling recipe generation without remote calling. It uses a thread
 * so that user interation is not interrupted during any generation.
 */
// @TODO Remove, it will be placed in server
public class LocalRecipeGenerator implements GenerateRecipe {
  private static final String RECIPE_FORMAT = "This is a recipe that is generated with a given query (query: \"%s\")";

  private boolean alwaysFail = false;

  public void setAlwaysFail(boolean alwaysFail) {
    this.alwaysFail = alwaysFail;
  }

  @Override
  public void requestGeneratingRecipe(
    RecipeQueryable query,
    RecipeGenerated onRecipeGenerated,
    RecipeGenerationFailed onRecipeGenerationFailed) {

    Thread t = new Thread(() -> {
      if (alwaysFail) {
        onRecipeGenerationFailed.onRecipeGenerationFailed();
      }

      String recipe = String.format(RECIPE_FORMAT, query.toQueryableString());
      onRecipeGenerated.onRecipeGenerated(recipe);
    });
    t.start();
  }
}
