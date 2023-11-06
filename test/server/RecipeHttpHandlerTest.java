package server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.Recipe;
import server.mock.MockHttpRequest;
import server.recipe.IRecipeRepository;

public class RecipeHttpHandlerTest {
  private static IRecipeRepository recipeRepositoryMock;

  @BeforeEach
  public void setUpMock() {
    recipeRepositoryMock = new RecipeRepositoryMock();
  }

  @Test
  public void testGetWithoutId() {
    RecipeHttpHandler handler = new RecipeHttpHandler(recipeRepositoryMock);
    MockHttpRequest requestMock = new MockHttpRequest();
    String response = handler.handleGet(requestMock);
    String expected = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"},{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"},{\"meal_type\":\"meal_type_A\",\"description\":\"desc_A\",\"ingredients\":\"ingredients_A\",\"title\":\"title_A\"}]}";
    assertEquals(expected, trimResponse(response));
  }

  @Test
  public void testGetWithId() {
    RecipeHttpHandler handler = new RecipeHttpHandler(recipeRepositoryMock);
    MockHttpRequest requestMock = new MockHttpRequest();

    HashMap<String, String> params = new HashMap<>();
    params.put("id", "1");
    requestMock.setQuery(params);

    String response = handler.handleGet(requestMock);
    String expected = "{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"}";
    assertEquals(expected, trimResponse(response));
  }

  @Test
  public void testPost() {
    RecipeHttpHandler handler = new RecipeHttpHandler(recipeRepositoryMock);
    MockHttpRequest requestMock = new MockHttpRequest();

    requestMock.setRequestBodyAsString("{\"meal_type\":\"meal_type_new\",\"description\":\"desc_new\",\"ingredients\":\"ingredients_new\",\"title\":\"title_new\"}");

    int prevCount = recipeRepositoryMock.getRecipes().size();
    handler.handlePost(requestMock);
    int afterCount = recipeRepositoryMock.getRecipes().size();
    
    assertEquals(prevCount + 1, afterCount);
    assertEquals("title_new", recipeRepositoryMock.getRecipes().get(0).getTitle());
  }

  @Test
  public void testUpdate() {
    RecipeHttpHandler handler = new RecipeHttpHandler(recipeRepositoryMock);
    MockHttpRequest requestMock = new MockHttpRequest();
    
    HashMap<String, String> params = new HashMap<>();
    params.put("id", "1");
    requestMock.setQuery(params);

    requestMock.setRequestBodyAsString("{\"meal_type\":\"meal_type_updated\",\"description\":\"desc_updated\",\"ingredients\":\"ingredients_updated\",\"title\":\"title_updated\"}");

    int prevCount = recipeRepositoryMock.getRecipes().size();
    handler.handlePut(requestMock);
    int afterCount = recipeRepositoryMock.getRecipes().size();
    
    assertEquals(prevCount, afterCount);
    assertEquals("title_updated", recipeRepositoryMock.getRecipes().get(1).getTitle());
  }

  @Test
  public void testDelete() {
    RecipeHttpHandler handler = new RecipeHttpHandler(recipeRepositoryMock);
    MockHttpRequest requestMock = new MockHttpRequest();
    
    HashMap<String, String> params = new HashMap<>();
    params.put("id", "1");
    requestMock.setQuery(params);

    int prevCount = recipeRepositoryMock.getRecipes().size();
    handler.handleDelete(requestMock);
    int afterCount = recipeRepositoryMock.getRecipes().size();
    
    assertEquals(prevCount - 1, afterCount);
  }

  private static String trimResponse(String response) {
    return new JSONObject(response).toString();
  }
}

class RecipeRepositoryMock implements IRecipeRepository {
  ArrayList<Recipe> list = new ArrayList<>();

  public RecipeRepositoryMock() {
    list.add(0, new Recipe("title_A", "desc_A", "ingredients_A", "meal_type_A"));
    list.add(0, new Recipe("title_B", "desc_B", "ingredients_B", "meal_type_B"));
    list.add(0, new Recipe("title_C", "desc_C", "ingredients_C", "meal_type_C"));
  }
  @Override
  public List<Recipe> getRecipes() {
    return list;
  }

  @Override
  public Recipe getRecipe(int id) {
    return list.get(id);
  }

  @Override
  public void createRecipe(Recipe recipe) {
    list.add(0, recipe);
  }

  @Override
  public void updateRecipe(int id, Recipe recipe) {
    list.set(id, recipe);
  }

  @Override
  public void deleteRecipe(int id) {
    list.remove(id);
  }
};