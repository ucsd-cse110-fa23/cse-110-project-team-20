package server.recipe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONObject;

import client.Recipe;

/*
 * JSON recipe repository
 *
 * Handles how input from other parts of the application should be formatted in order to meet JSON specifications.
 * Also converts from server-appropriate formatted data to one used by local methods
 */
public class JSONRecipeRepository implements IRecipeRepository {
  String path;

  public JSONRecipeRepository(String jsonFilePath) {
    path = jsonFilePath;

    if (!new File(path).exists()) {
      initDatabase(jsonFilePath);
    }
  }

  private void initDatabase(String jsonFilePath) {
    JSONObject object = new JSONObject();
    object.put("recipes", new JSONArray(0));

    write(object.toString(2));
  }

  @Override
  public List<Recipe> getRecipes() {
    return getListOfRecipes();
  }

  @Override
  public Recipe getRecipe(int id) {
    List<Recipe> recipes = getListOfRecipes();
    if (id >= recipes.size() || id < 0) {
      return null;
    } else {
      return recipes.get(id);
    }
  }

  @Override
  public void createRecipe(Recipe recipe) {
    List<Recipe> recipes = getListOfRecipes();
    Recipe newRecipe = new Recipe(
        Optional.ofNullable(recipe.getTitle()).orElse(""),
        Optional.ofNullable(recipe.getDescription()).orElse(""),
        Optional.ofNullable(recipe.getIngredients()).orElse(""),
        Optional.ofNullable(recipe.getMealType()).orElse(""));
    recipes.add(0, newRecipe);
    commit(recipes);
  }

  @Override
  public void updateRecipe(int id, Recipe recipe) {
    List<Recipe> recipes = getListOfRecipes();
    Recipe originalRecipe = recipes.get(id);

    Recipe newRecipe = new Recipe(
        Optional.ofNullable(recipe.getTitle()).orElse(""),
        Optional.ofNullable(recipe.getDescription()).orElse(""),
        Optional.ofNullable(originalRecipe.getIngredients()).orElse(""),
        Optional.ofNullable(originalRecipe.getMealType()).orElse(""));

    recipes.set(id, newRecipe);
    commit(recipes);
  }

  @Override
  public void deleteRecipe(int id) {
    List<Recipe> recipes = getListOfRecipes();
    recipes.remove(id);
    commit(recipes);
  }

  private void commit(List<Recipe> recipes) {
    JSONObject object = new JSONObject();
    object.put("recipes", new JSONArray(recipes));

    write(object.toString(2));
  }

  private void write(String data) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
      writer.write(data);
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // ref: https://www.baeldung.com/reading-file-in-java
  private String readFile(InputStream inputStream)
      throws IOException {
    StringBuilder resultStringBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = br.readLine()) != null) {
        resultStringBuilder.append(line).append("\n");
      }
    }
    return resultStringBuilder.toString();
  }

  private ArrayList<Recipe> getListOfRecipes() {
    JSONObject obj = new JSONObject();
    try {
      String fileString = readFile(new FileInputStream(new File(path)));
      obj = new JSONObject(fileString);
    } catch (Exception e) {
      e.printStackTrace();
    }
    JSONArray recipesArr = obj.getJSONArray("recipes");
    ArrayList<Recipe> recipes = new ArrayList<>();

    for (int i = 0; i < recipesArr.length(); i++) {
      JSONObject recipeJson = recipesArr.getJSONObject(i);
      recipes.add(Recipe.fromJson(recipeJson.toString()));
    }

    return recipes;
  }

}
