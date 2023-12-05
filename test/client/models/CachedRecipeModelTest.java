package client.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import client.Recipe;
import client.account.IAccountSession;
import client.mock.MockHttpServer;
import client.models.mock.MockGetRecipesRecipeModel;
import feature.mock.MockRecipeModel;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class CachedRecipeModelTest {
  private MockHttpServer server;
  IAccountSession mockSession = new IAccountSession() {
    @Override
    public void setToken(String token) {
    }

    @Override
    public String getToken() {
      return "some token";
    }
  };

  @AfterEach
  public void tearDownServer() {
    if (server != null) {
      server.stop();
      server = null;
    }
  }

  @Test
  public void instnace() {
    CachedRecipeModel model = new CachedRecipeModel(new ServerRecipeModel(mockSession));
    assertInstanceOf(CachedRecipeModel.class, model);
  }

  @Test
  public void getRecipes() throws IOException {
    String mockedResponse = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"},{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"},{\"meal_type\":\"meal_type_A\",\"description\":\"desc_A\",\"ingredients\":\"ingredients_A\",\"title\":\"title_A\"}]}";
    server = new MockHttpServer("/recipe", mockedResponse);
    CachedRecipeModel model = new CachedRecipeModel(new ServerRecipeModel(mockSession, "http://localhost:3100"));

    server.start(3100);

    List<Recipe> recipes = model.getRecipes();
    assertEquals(3, recipes.size());
    assertEquals(recipes.get(0).getTitle(), "title_C");
  }

  @Test
  public void getRecipe() throws IOException {
    String mockedResponse = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"},{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"},{\"meal_type\":\"meal_type_A\",\"description\":\"desc_A\",\"ingredients\":\"ingredients_A\",\"title\":\"title_A\"}]}";
    server = new MockHttpServer("/recipe", mockedResponse);
    CachedRecipeModel model = new CachedRecipeModel(new ServerRecipeModel(mockSession, "http://localhost:3101"));

    server.start(3101);

    Recipe recipe = model.getRecipe(0);
    assertEquals(recipe.getTitle(), "title_C");
  }

  @Test
  public void createRecipe() throws IOException {
    String mockedResponse = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"},{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"},{\"meal_type\":\"meal_type_A\",\"description\":\"desc_A\",\"ingredients\":\"ingredients_A\",\"title\":\"title_A\"}]}";
    server = new MockHttpServer("/recipe", mockedResponse);
    CachedRecipeModel model = new CachedRecipeModel(new ServerRecipeModel(mockSession, "http://localhost:3102"));

    server.start(3102);

    Recipe recipe = new Recipe("title_test", "desc_test", "ingredients_test", "meal_type_test", "image_url_test");
    model.createRecipe(recipe);

    Recipe actual = model.getRecipe(0);
    assertEquals(actual.getTitle(), "title_test");
  }

  @Test
  public void updateRecipe() throws IOException {
    String mockedResponse = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"},{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"},{\"meal_type\":\"meal_type_A\",\"description\":\"desc_A\",\"ingredients\":\"ingredients_A\",\"title\":\"title_A\"}]}";
    server = new MockHttpServer("/recipe", mockedResponse);
    CachedRecipeModel model = new CachedRecipeModel(new ServerRecipeModel(mockSession, "http://localhost:3103"));

    server.start(3103);

    Recipe recipe = new Recipe("title_test_update", "desc_test_update", "ingredients_test_update", "meal_type_test_update", "image_url_update");
    model.updateRecipe(2, recipe);

    Recipe actual = model.getRecipe(2);
    assertEquals(actual.getTitle(), "title_test_update");
    assertEquals(actual.getDescription(), "desc_test_update");
  }

  @Test
  public void deleteRecipe() throws IOException {
    String mockedResponse = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"},{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"},{\"meal_type\":\"meal_type_A\",\"description\":\"desc_A\",\"ingredients\":\"ingredients_A\",\"title\":\"title_A\"}]}";
    server = new MockHttpServer("/recipe", mockedResponse);
    CachedRecipeModel model = new CachedRecipeModel(new ServerRecipeModel(mockSession, "http://localhost:3104"));
    server.start(3104);

    model.deleteRecipe(2);
    
    assertEquals(2, model.getRecipes().size());
  }

  @Test
  public void clearCache() {
    MockGetRecipesRecipeModel innerModel = new MockGetRecipesRecipeModel();
    CachedRecipeModel model = new CachedRecipeModel(innerModel);

    model.getRecipes(); // loaded into cache, count increases
    model.getRecipes(); // used cache, count not change
    model.clearCache();
    model.getRecipes(); // loaded into cache, count increases
    model.getRecipes(); // used cache, count not change

    assertEquals(2, innerModel.getNumberOfGetRecipesCalled());
  }

  @Test
  public void sharedRecipe() throws IOException, InterruptedException, ExecutionException {
    MockRecipeModel innerRecipeModel = new MockRecipeModel();
    innerRecipeModel.createRecipe(new Recipe("test recipe title", "test recipe desc"));

    CachedRecipeModel model = new CachedRecipeModel(innerRecipeModel);

    CompletableFuture<Boolean> future = new CompletableFuture<>();

    model.shareRecipe(0, () -> {
      future.complete(true);
    });

    future.completeOnTimeout(null, 3, TimeUnit.SECONDS);
    assertTrue(future.get());
    // it can be only true if mock recipe model was called by the cached recipe model
    assertTrue(innerRecipeModel.getRecipe(0).getSharedUrl().contains("http://localhost/recipe/shared/?url="));
  }

  @Test
  public void sharedUrlMissingIssueWhenUpdatingRecipe() throws IOException {
    // Issue reference: DS5-10 #230
    String mockedResponse = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\", \"image_url\":\"base 64 image string\", \"shared_url\": \"ABCD-EFG0\"}]}";
    server = new MockHttpServer("/recipe", mockedResponse);
    CachedRecipeModel model = new CachedRecipeModel(new ServerRecipeModel(mockSession, "http://localhost:3110"));

    server.start(3110);

    Recipe recipe = new Recipe("title_test_update", "desc_test_update");
    model.updateRecipe(0, recipe);

    Recipe actual = model.getRecipe(0);
    // server should update only title and description so the cache one
    // should mimic same behavior -- updating title, desc only.
    assertEquals("title_test_update", actual.getTitle());
    assertEquals("desc_test_update", actual.getDescription());

    assertEquals("meal_type_C", actual.getMealType());
    assertEquals("ingredients_C", actual.getIngredients());
    assertEquals("base 64 image string", actual.getImageUrl());
    assertEquals("ABCD-EFG0", actual.getSharedUrl());
  }
}
