package client.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import client.Recipe;
import client.account.IAccountSession;
import client.mock.MockHttpServer;

import java.io.IOException;
import java.util.List;

public class ServerRecipeModelTest {
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
    ServerRecipeModel model = new ServerRecipeModel(mockSession);
    assertInstanceOf(ServerRecipeModel.class, model);
  }

  @Test
  public void getRecipes() throws IOException {
    String mockedResponse = "{\"recipes\":[{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"},{\"meal_type\":\"meal_type_B\",\"description\":\"desc_B\",\"ingredients\":\"ingredients_B\",\"title\":\"title_B\"},{\"meal_type\":\"meal_type_A\",\"description\":\"desc_A\",\"ingredients\":\"ingredients_A\",\"title\":\"title_A\"}]}";
    server = new MockHttpServer("/recipe", mockedResponse);
    ServerRecipeModel model = new ServerRecipeModel(mockSession, "http://localhost:3100");

    server.start(3100);

    List<Recipe> recipes = model.getRecipes();
    assertEquals(3, recipes.size());
    assertEquals(recipes.get(0).getTitle(), "title_C");
  }

  @Test
  public void getRecipe() throws IOException {
    String mockedResponse = "{\"meal_type\":\"meal_type_C\",\"description\":\"desc_C\",\"ingredients\":\"ingredients_C\",\"title\":\"title_C\"}";
    server = new MockHttpServer("/recipe", mockedResponse);
    ServerRecipeModel model = new ServerRecipeModel(mockSession, "http://localhost:3101");

    server.start(3101);

    Recipe recipe = model.getRecipe(0);
    assertEquals("0", server.getLastHttpRequestQuery("id"));
    assertEquals(recipe.getTitle(), "title_C");
  }

  @Test
  public void createRecipe() throws IOException {
    server = new MockHttpServer("/recipe", null);
    ServerRecipeModel model = new ServerRecipeModel(mockSession, "http://localhost:3102");

    server.start(3102);

    Recipe recipe = new Recipe("title_test", "desc_test", "ingredients_test", "meal_type_test", "image_url_test");
    model.createRecipe(recipe);

    assertTrue(server.getLastHttpRequestBodyAsString().contains("title_test"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("desc_test"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("ingredients_test"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("meal_type_test"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("image_url_test"));
  }

  @Test
  public void updateRecipe() throws IOException {
    server = new MockHttpServer("/recipe", null);
    ServerRecipeModel model = new ServerRecipeModel(mockSession, "http://localhost:3103");

    server.start(3103);

    Recipe recipe = new Recipe("title_test_update", "desc_test_update", "ingredients_test_update", "meal_type_test_update", "image_url_update");
    model.updateRecipe(3, recipe);

    assertEquals("3", server.getLastHttpRequestQuery("id"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("title_test_update"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("desc_test_update"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("ingredients_test_update"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("meal_type_test_update"));
    assertTrue(server.getLastHttpRequestBodyAsString().contains("image_url_update"));
  }

  @Test
  public void deleteRecipe() throws IOException {
    server = new MockHttpServer("/recipe", null);
    ServerRecipeModel model = new ServerRecipeModel(mockSession, "http://localhost:3104");
    server.start(3104);

    model.deleteRecipe(2);

    assertEquals("2", server.getLastHttpRequestQuery("id"));
  }

  @Test
  public void shareRecipe() throws IOException {
    server = new MockHttpServer("/recipe/share", null);
    ServerRecipeModel model = new ServerRecipeModel(mockSession, "http://localhost:3105");
    server.start(3105);

    model.shareRecipe(2);

    assertEquals("2", server.getLastHttpRequestQuery("id"));
  }

  private Exception exceptionOccured = null;

  @Test
  public void errorHandlerTest() {
    ServerRecipeModel model = new ServerRecipeModel(mockSession, "http://localhost:3106");
    model.setErrorHandler((Exception e) -> {
      exceptionOccured = e;
    });
    // we expect to capture any exception during any request in the model.
    // we will get connection refused since we don't setup mock server on this test.
    model.shareRecipe(2);

    assertNotNull(exceptionOccured);
    assertTrue(exceptionOccured.getMessage().contains("Connection refused"));
  }
}
