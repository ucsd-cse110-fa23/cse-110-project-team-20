package client.recipe;

/**
 * Local Recipe Generator
 * 
 * GenerateRecipe implementation for handling recipe generation without remote
 * calling. It uses a thread
 * so that user interation is not interrupted during any generation.
 */
public class LocalRecipeGenerator implements GenerateRecipe {

  // based on DS5 #60 doc
  private static final String RECIPE_FORMAT = "{"
      + "  \"title\": \"Tomato, Cucumber, and Egg Salad\","
      + "  \"meal_type\": \"Dinner\","
      + "  \"ingredients\": \"Tomato, eggs, cucumber\","
      + "  \"description\": \"1. Boil the eggs until they are hard-boiled. Let them cool and then peel and chop them.\n2. Wash and dice the tomato and cucumber.\n3. In a large bowl, combine the chopped eggs, diced tomato, and cucumber.\n4. Toss the ingredients together.\n5. Season with salt and pepper to taste.\n6. Serve as a refreshing and healthy dinner salad.\""
      + "}";

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
        return;
      }

      String recipeResponse = String.format(RECIPE_FORMAT);
      onRecipeGenerated.onRecipeGenerated(recipeResponse);
    });
    t.start();
  }
}
