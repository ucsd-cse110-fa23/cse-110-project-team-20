package client.models;

import client.Recipe;
import java.util.List;

public interface IRecipeModel {
  public List<Recipe> getRecipes();
  public Recipe getRecipe(int id);
  public void createRecipe(Recipe recipe);
  public void updateRecipe(int id, Recipe recipe);
  public void deleteRecipe(int id);
}
