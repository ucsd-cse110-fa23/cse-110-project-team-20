package client.recipe;

public interface GenerateRecipe {

  /**
   * Request generating recipe with query. When the generator created a recipe, onRecipeGenerated
   * will be called with a generated recipe as string.
   *
   * @param query
   * @param onRecipeGenerated
   */
  public void requestGeneratingRecipe(String query, RecipeGenerated onRecipeGenerated);
}
