package client.models;

import java.util.List;
import java.util.ArrayList;
import java.lang.Thread;

import client.Recipe;

/**
 * CachedRecipeModel
 *
 * When we uses remote database such as MongoDB Atlas, waiting time from the remote server is significant,
 * and it causes usability issue on client app. This CachedRecipeModel fetches the all recipes onces and
 * maintain recipes up-to-date in local memory. While storing the recipe info in local, all CRUD operations
 * are still applied on the recipeModel via Thread.
 */
public class CachedRecipeModel implements IRecipeModel {
  IRecipeModel recipeModel;

  List<Recipe> recipes = new ArrayList<>();

  boolean loadedOnce = false;

  public CachedRecipeModel(IRecipeModel recipeModel) {
    this.recipeModel = recipeModel;
  }

  @Override
  public List<Recipe> getRecipes() {
    if (! loadedOnce) {
      recipes = recipeModel.getRecipes();
      loadedOnce = true;
    }

    return recipes;
  }

  private void ensureLoadedOnce() {
    if (! loadedOnce) {
      getRecipes();
    }
  }

  @Override
  public Recipe getRecipe(int id) {
    ensureLoadedOnce();
    return recipes.get(id);
  }

  @Override
  public void createRecipe(Recipe recipe) {
    ensureLoadedOnce();
    new Thread(() -> {
      recipeModel.createRecipe(recipe);
    }).start();

    recipes.add(0, recipe);
  }

  @Override
  public void updateRecipe(int id, Recipe recipe) {
    ensureLoadedOnce();

    new Thread(() -> {
      recipeModel.updateRecipe(id, recipe);
    }).start();

    Recipe originalRecipe = recipes.get(id);
    Recipe updatedRecipe = new Recipe(
      recipe.getTitle(),
      recipe.getDescription(),
      originalRecipe.getIngredients(),
      originalRecipe.getMealType());

    recipes.set(id, updatedRecipe);
  }

  @Override
  public void deleteRecipe(int id) {
    ensureLoadedOnce();

    new Thread(() -> {
      recipeModel.deleteRecipe(id);
    }).start();

    recipes.remove(id);
  }
}
