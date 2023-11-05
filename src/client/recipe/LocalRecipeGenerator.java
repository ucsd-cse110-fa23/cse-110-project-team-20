package client.recipe;

/**
 * Local Recipe Generator
 * 
 * GenerateRecipe implementation for handling recipe generation without remote calling. It uses a thread
 * so that user interation is not interrupted during any generation.
 */
public class LocalRecipeGenerator implements GenerateRecipe {
  private static final String RECIPE_FORMAT = "This is a recipe that is generated with a given query";

  private boolean alwaysFail = false;

  public void setAlwaysFail(boolean alwaysFail) {
    this.alwaysFail = alwaysFail;
  }

  @Override
  public void requestGeneratingRecipe(
    RecipeRequestParameter parameter,
    RecipeGenerated onRecipeGenerated,
    RecipeGenerationFailed onRecipeGenerationFailed) {

    Thread t = new Thread(() -> {
      if (alwaysFail) {
        onRecipeGenerationFailed.onRecipeGenerationFailed("AlwaysFail is on");
      }

      String recipe = String.format(RECIPE_FORMAT);
      onRecipeGenerated.onRecipeGenerated(recipe);
    });
    t.start();
  }
}
