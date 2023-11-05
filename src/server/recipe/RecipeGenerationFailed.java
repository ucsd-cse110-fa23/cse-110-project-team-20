package server.recipe;

public interface RecipeGenerationFailed {
  /**
   * onRecipeGenerationFailed runs when the generator fails generating a recipe
   *
   * @param recipe
   */
  void onRecipeGenerationFailed();
}
