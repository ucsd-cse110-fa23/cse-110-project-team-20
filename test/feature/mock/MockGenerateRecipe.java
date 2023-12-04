package feature.mock;

import client.Recipe;
import client.recipe.IRecipeGenerator;
import client.recipe.IRecipeGenerated;
import client.recipe.IRecipeGenerationFailed;
import client.recipe.RecipeRequestParameter;

public class MockGenerateRecipe implements IRecipeGenerator {
  private Recipe recipe;
  private String errorMessage = null;

  public void setMockRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public void mustFailWith(String e) {
    this.errorMessage = e;
  }

  @Override
  public void requestGeneratingRecipe(RecipeRequestParameter parameter, IRecipeGenerated onRecipeGenerated,
      IRecipeGenerationFailed onRecipeGenerationFailed) {
    if (errorMessage == null) {
      onRecipeGenerated.onRecipeGenerated(recipe);
    } else {
      onRecipeGenerationFailed.onRecipeGenerationFailed(errorMessage);
    }
  }
}
