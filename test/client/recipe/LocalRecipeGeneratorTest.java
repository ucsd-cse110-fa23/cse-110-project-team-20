package client.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

public class LocalRecipeGeneratorTest {
  @Test
  public void generateRecipe() throws InterruptedException, ExecutionException {
    CompletableFuture<String> future = new CompletableFuture<>();

    LocalRecipeGenerator generator = new LocalRecipeGenerator();
    RecipeRequestParameter params = new RecipeRequestParameter(new File("a.mp3"), new File("b.mp3"));

    generator.requestGeneratingRecipe(params, (recipe) -> {
      future.complete(recipe);
    }, null);

    String expected = "This is a recipe that is generated with a given query";
    String actual = future.get();

    assertEquals(expected, actual);
  }

  @Test
  public void generatingRecipeFail() throws InterruptedException, ExecutionException {
    CompletableFuture<Boolean> future = new CompletableFuture<>();

    LocalRecipeGenerator generator = new LocalRecipeGenerator();
    generator.setAlwaysFail(true);

    RecipeRequestParameter params = new RecipeRequestParameter(new File("a.mp3"), new File("b.mp3"));

    generator.requestGeneratingRecipe(params, null, (errorMessage) -> {
      future.complete(true);
    });

    future.completeOnTimeout(false, 1, TimeUnit.SECONDS);
    boolean actual = future.get();

    assertTrue(actual, "RecipeGenerationFailed is not called.");
  }
}
