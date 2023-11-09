package feature.mock;

import client.Recipe;
import client.recipe.IRecipeGenerator;
import client.recipe.IRecipeGenerated;
import client.recipe.IRecipeGenerationFailed;
import client.recipe.RecipeRequestParameter;

public class MockGenerateRecipe implements IRecipeGenerator {
  private Recipe recipe;

  public void setMockRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void requestGeneratingRecipe(RecipeRequestParameter parameter, IRecipeGenerated onRecipeGenerated,
      IRecipeGenerationFailed onRecipeGenerationFailed) {
    onRecipeGenerated.onRecipeGenerated(recipe);
  }
}
