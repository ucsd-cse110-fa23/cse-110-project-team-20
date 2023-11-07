package client.recipe;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import client.Recipe;
import client.recipe.mock.MockHttpServer;

public class ServerRecipeGeneratorTest {
  @Test
  public void generateRecipeWithServer() throws InterruptedException, ExecutionException, IOException
  {
    String mockRecipeResponse = "{\"description\": \"This is AI generated recipe with tomato, garlic, cucumber, watermelon.\"}";
    MockHttpServer server = new MockHttpServer("/recipe/generate", mockRecipeResponse);

    CompletableFuture<Recipe> future = new CompletableFuture<>();
    ServerRecipeGenerator generator = new ServerRecipeGenerator();

    server.start();

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
    ServerRecipeGenerator generator = new ServerRecipeGenerator();

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
    ServerRecipeGenerator generator = new ServerRecipeGenerator();

    RecipeRequestParameter params = new RecipeRequestParameter(new File("not-exist-file.mp3"), new File("test/resources/silence.mp3"));

    generator.requestGeneratingRecipe(params, null, (errorMessage) -> {
      future.complete(errorMessage);
    });

    // if the test takes more than 3 seconds, consider empty string is returned.
    future.completeOnTimeout("", 15, TimeUnit.SECONDS);

    String actual = future.get();

    assertEquals("Meal type file cannot found.", actual);
  }
}
