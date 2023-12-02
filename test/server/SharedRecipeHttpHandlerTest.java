package server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import client.Recipe;

import java.util.HashMap;
import server.mock.MockHttpRequest;
import server.mock.MockSharedRecipeRepository;

public class SharedRecipeHttpHandlerTest {
  @Test
  public void sharedPageWithEmptyUrl() {

    MockSharedRecipeRepository repository = new MockSharedRecipeRepository();

    SharedRecipeHttpHandler handler = new SharedRecipeHttpHandler(repository);
    MockHttpRequest mockRequest = new MockHttpRequest();
    String result = handler.handleGet(mockRequest);

    assertTrue(result.contains("not found"));
  }

  @Test
  public void sharedPageWithNonExistRecipeSharedUrl() {

    MockSharedRecipeRepository repository = new MockSharedRecipeRepository();

    SharedRecipeHttpHandler handler = new SharedRecipeHttpHandler(repository);
    MockHttpRequest mockRequest = new MockHttpRequest();
    HashMap<String, String> query = new HashMap<>();
    query.put("url", "some-test-url");
    mockRequest.setQuery(query);

    String result = handler.handleGet(mockRequest);

    assertTrue(result.contains("not found"));
  }

  @Test
  public void sharedPageWithSharedUrl() {

    MockSharedRecipeRepository repository = new MockSharedRecipeRepository();
    // setup test recipe on repository
    Recipe recipe = new Recipe("some title", "some desc", "some ingredients", "some meal type", "some image url");
    repository.setRecipe(recipe, "some-test-url");

    SharedRecipeHttpHandler handler = new SharedRecipeHttpHandler(repository);

    MockHttpRequest mockRequest = new MockHttpRequest();
    HashMap<String, String> query = new HashMap<>();
    query.put("url", "some-test-url");
    mockRequest.setQuery(query);

    String result = handler.handleGet(mockRequest);

    assertTrue(result.contains(recipe.getTitle()), "title is missing from the recipe web page");
    assertTrue(result.contains(recipe.getDescription()), "description is missing from the recipe web page");
    assertTrue(result.contains(recipe.getMealType()), "meal type is missing from the recipe web page");
  }
}
