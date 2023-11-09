package client.recipe;

public interface IRecipeGenerator {

  /**
   * Request generating recipe with RecipeRequestParameter. When the generator created a recipe, onRecipeGenerated
   * will be called with a generated recipe as string. When generation is failed, onRecipeGenerationFailed will be called.
   *
   * @param parameter
   * @param onRecipeGenerated
   * @param onRecipeGenerationFailed
   */
  public void requestGeneratingRecipe(
    RecipeRequestParameter parameter,
    IRecipeGenerated onRecipeGenerated,
    IRecipeGenerationFailed onRecipeGenerationFailed
  );
}
