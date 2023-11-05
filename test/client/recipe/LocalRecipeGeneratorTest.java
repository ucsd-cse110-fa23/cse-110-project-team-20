package client.recipe;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
    }, (errorMessage) -> {
      fail(errorMessage);
    });

    future.completeOnTimeout(null, 3, TimeUnit.SECONDS);

    String actual = future.get();
    String expectedTitle = "Tomato, Cucumber, and Egg Salad";
    
    assertTrue(actual.contains(expectedTitle));
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
