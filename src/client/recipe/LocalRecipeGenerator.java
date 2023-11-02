package client.recipe;

/**
 * Local Recipe Generator
 * 
 * GenerateRecipe implementation for handling recipe generation without remote calling. It uses a thread
 * so that user interation is not interrupted during any generation.
 */
public class LocalRecipeGenerator implements GenerateRecipe {
  private String format = "This is a recipe that is generated with a given query (query: \"%s\")";
 
  @Override
  public void requestGeneratingRecipe(String query, RecipeGenerated onRecipeGenerated) {
    Thread t = new Thread(() -> {
      String recipe = String.format(format, query);
      onRecipeGenerated.onRecipeGenerated(recipe);
    });
    t.start();
  }
}
