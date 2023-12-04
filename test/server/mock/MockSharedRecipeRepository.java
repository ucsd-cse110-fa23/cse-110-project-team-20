package server.mock;

import client.Recipe;
import server.recipe.ISharedRecipeRepository;
import java.util.HashMap;

/**
 * mock shared recipe repository behavior
 */
public class MockSharedRecipeRepository implements ISharedRecipeRepository {
  private int id;
  private HashMap<String, Recipe> recipes = new HashMap<>();

  public int getLastMarkedAsSharedId() {
    return id;
  }

  public void setRecipe(Recipe recipe, String sharedUrl) {
    recipes.put(sharedUrl, recipe);
  }

  @Override
  public void markAsShared(int id) {
    this.id = id;
  }

  @Override
  public Recipe getRecipeBySharedUrl(String sharedUrl) {
    return recipes.getOrDefault(sharedUrl, null);
  }
}
