package server.recipe;

import client.Recipe;
import java.util.List;

/*
 * Recipe Model interface
 *
 * Identical to IRecipeModel; however, this interface is used exclusively by JSONRecipeRepository
 *
 */
public interface IRecipeRepository {
    public List<Recipe> getRecipes();
    public Recipe getRecipe(int id);
    public void createRecipe(Recipe recipe);
    public void updateRecipe(int id, Recipe recipe);
    public void deleteRecipe(int id);
}
