package client.recipe;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import client.Recipe;
import client.account.IAccountSession;
import client.mock.MockHttpServer;

public class ServerRecipeGeneratorTest {
  MockHttpServer server = null;
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
  public void generateRecipeWithServer() throws InterruptedException, ExecutionException, IOException
  {
    String mockRecipeResponse = "{\"description\": \"This is AI generated recipe with tomato, garlic, cucumber, watermelon.\"}";
    server = new MockHttpServer("/recipe/generate", mockRecipeResponse);

    CompletableFuture<Recipe> future = new CompletableFuture<>();
    ServerRecipeGenerator generator = new ServerRecipeGenerator(mockSession, "http://localhost:4100");

    server.start(4100);

    RecipeRequestParameter params = new RecipeRequestParameter(new File("test/resources/silence.mp3"), new File("test/resources/silence.mp3"));

    generator.requestGeneratingRecipe(params, (recipe) -> {
      future.complete(recipe);
      server.stop();
    }, null);

    // if the test takes more than 3 seconds, consider empty string is returned.
    future.completeOnTimeout(null, 15, TimeUnit.SECONDS);

    Recipe actual = future.get();

    assertTrue(actual.getDescription().length() > 20,
      "Expected a recipe with at least 20 characters from the ChatGPT response.");

    // quick check with ingredients
    assertTrue(actual.getDescription().toLowerCase().contains("tomato"));
    assertTrue(actual.getDescription().toLowerCase().contains("garlic"));
    assertTrue(actual.getDescription().toLowerCase().contains("cucumber"));
    assertTrue(actual.getDescription().toLowerCase().contains("watermelon"));
  }

  @Test
  public void generateRecipeWithMissingIngredientsFile() throws InterruptedException, ExecutionException
  {
    CompletableFuture<String> future = new CompletableFuture<>();
    ServerRecipeGenerator generator = new ServerRecipeGenerator(mockSession);

    RecipeRequestParameter params = new RecipeRequestParameter(new File("test/resources/silence.mp3"), new File("not-exist-flie.mp3"));

    generator.requestGeneratingRecipe(params, null, (errorMessage) -> {
      future.complete(errorMessage);
    });

    // if the test takes more than 3 seconds, consider empty string is returned.
    future.completeOnTimeout("", 15, TimeUnit.SECONDS);

    String actual = future.get();

    assertEquals("Ingredients file cannot found.", actual);
  }


  @Test
  public void generateRecipeWithMissingMealTypeFile() throws InterruptedException, ExecutionException
  {
    CompletableFuture<String> future = new CompletableFuture<>();
    ServerRecipeGenerator generator = new ServerRecipeGenerator(mockSession);

    RecipeRequestParameter params = new RecipeRequestParameter(new File("not-exist-file.mp3"), new File("test/resources/silence.mp3"));

    generator.requestGeneratingRecipe(params, null, (errorMessage) -> {
      future.complete(errorMessage);
    });

    // if the test takes more than 3 seconds, consider empty string is returned.
    future.completeOnTimeout("", 15, TimeUnit.SECONDS);

    String actual = future.get();

    assertEquals("Meal type file cannot found.", actual);
  }

  @Test
  public void anyServerErrorOccurs() throws InterruptedException, ExecutionException, IOException
  {
    String mockRecipeResponse = "{\"error\": \"ingredients are missing\"}";
    server = new MockHttpServer("/recipe/generate", mockRecipeResponse);

    CompletableFuture<String> future = new CompletableFuture<>();
    ServerRecipeGenerator generator = new ServerRecipeGenerator(mockSession, "http://localhost:4105");

    server.start(4105);

    RecipeRequestParameter params = new RecipeRequestParameter(new File("test/resources/silence.mp3"), new File("test/resources/silence.mp3"));

    generator.requestGeneratingRecipe(params, (recipe) -> {
      server.stop();
    }, (String errorMessage) -> {
      future.complete(errorMessage);
      server.stop();
    });

    // if the test takes more than 3 seconds, consider empty string is returned.
    future.completeOnTimeout(null, 15, TimeUnit.SECONDS);

    String actual = future.get();
    assertEquals("ingredients are missing", actual);
  }

}
