package feature.mock;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

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

  @Override
  public void shareRecipe(int id) {
    // mimic behavior of share recipe
    Recipe r = recipes.get(id);
    if (r.getSharedUrl() != null) {
      return;
    }
    // generate random string and set as shared url
    String sharedUrl = "http://localhost/recipe/shared/?url=" + UUID.randomUUID().toString();
    Recipe newRecipe = new Recipe(r.getTitle(), r.getDescription(), r.getIngredients(), r.getMealType(), r.getImageUrl(), sharedUrl);
    recipes.set(id, newRecipe);
  }

  @Override
  public void shareRecipe(int id, Runnable onComplete) {
    shareRecipe(id);
    onComplete.run();
  }
}
