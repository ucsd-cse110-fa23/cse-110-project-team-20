package client.recipe;

public interface RecipeGenerationFailed {
  /**
   * onRecipeGenerationFailed runs when the generator fails generating a recipe
   *
   * @param errorMessage
   */
  void onRecipeGenerationFailed(String errorMessage);
}