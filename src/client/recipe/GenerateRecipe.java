package client.recipe;

public interface GenerateRecipe {

  /**
   * Request generating recipe with query. When the generator created a recipe, onRecipeGenerated will be called with
   * a generated recipe as string. When generation is failed, onRecipeGenerationFailed will be called.
   *
   * @param query
   * @param onRecipeGenerated
   * @param onRecipeGenerationFailed
   */
  public void requestGeneratingRecipe(String query, RecipeGenerated onRecipeGenerated, RecipeGenerationFailed onRecipeGenerationFailed);
}
