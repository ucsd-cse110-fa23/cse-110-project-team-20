package feature.mock;

import java.util.List;
import java.util.ArrayList;

import client.Recipe;
import client.models.IRecipeModel;

public class MockRecipeModel implements IRecipeModel {
  public ArrayList<Recipe> recipes = new ArrayList<>();

  @Override
  public List<Recipe> getRecipes() {
    return recipes;
  }

  @Override
  public Recipe getRecipe(int id) {
    return recipes.get(id);
  }

  @Override
  public void createRecipe(Recipe recipe) {
    recipes.add(0, recipe);
  }

  @Override
  public void updateRecipe(int id, Recipe recipe) {
    recipes.set(id, recipe);
  }

  @Override
  public void deleteRecipe(int id) {
    recipes.remove(id);
  }
}
