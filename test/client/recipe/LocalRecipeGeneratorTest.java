package client.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

public class LocalRecipeGeneratorTest {
  @Test
  public void generateRecipe() throws InterruptedException, ExecutionException {
    CompletableFuture<String> future = new CompletableFuture<>();

    LocalRecipeGenerator generator = new LocalRecipeGenerator();
    generator.requestGeneratingRecipe("Tomato, garlic, cucumber, and watermelon for dinner", (recipe) -> {
      future.complete(recipe);
    });

    String expected = "This is a recipe that is generated with a given query (query: \"Tomato, garlic, cucumber, and watermelon for dinner\")";
    String actual = future.get();

    assertEquals(expected, actual);
  }
}
