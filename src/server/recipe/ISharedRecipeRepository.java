package server.recipe;

import client.Recipe;

/**
 * Sharing related recipe operation
 */
public interface ISharedRecipeRepository {
  public void markAsShared(int id);
  public Recipe getRecipeBySharedUrl(String sharedUrl);
}
