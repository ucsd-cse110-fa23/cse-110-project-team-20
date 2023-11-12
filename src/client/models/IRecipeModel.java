package client.models;

import client.Recipe;
import java.util.List;

/*
 * Recipe Model interface
 *
 * Interface that contains the basic methods and containers needed to access and interact with recipes.
 * Primarily for use by the HTTP server.
 */
public interface IRecipeModel {
  public List<Recipe> getRecipes();
  public Recipe getRecipe(int id);
  public void createRecipe(Recipe recipe);
  public void updateRecipe(int id, Recipe recipe);
  public void deleteRecipe(int id);
}
