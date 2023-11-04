package client.recipe;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import server.chatgpt.ChatGPTConfiguration;
import server.chatgpt.IChatGPTConfiguration;
import server.chatgpt.RecipeQuery;

public class ChatGPTRecipeGeneratorTest {
  @Test
  @Disabled // due to limited ChatGPT credit, disabled for now
  public void generateRecipeWithChatGPT() throws InterruptedException, ExecutionException
  {
    CompletableFuture<String> future = new CompletableFuture<>();

    IChatGPTConfiguration config = new ChatGPTConfiguration();
    ChatGPTRecipeGenerator generator = new ChatGPTRecipeGenerator(config);

    RecipeQuery query = new RecipeQuery(
      "Tomato, garlic, cucumber, and watermelon",
      "dinner"
    );

    generator.requestGeneratingRecipe(query, (recipe) -> {
      future.complete(recipe);
    }, null);

    // if the test takes more than 3 seconds, consider empty string is returned.
    future.completeOnTimeout("", 10, TimeUnit.SECONDS);

    String actual = future.get();

    assertTrue(actual.length() > 20,
      "Expected a recipe with at least 20 characters from the ChatGPT response.");

    // quick check with ingredients
    assertTrue(actual.toLowerCase().contains("tomato"));
    assertTrue(actual.toLowerCase().contains("garlic"));
    assertTrue(actual.toLowerCase().contains("cucumber"));
    assertTrue(actual.toLowerCase().contains("watermelon"));
  }

  @Test
  public void generatingRecipeFailed() throws InterruptedException, ExecutionException
  {
    CompletableFuture<Boolean> future = new CompletableFuture<>();

    IChatGPTConfiguration config = new IChatGPTConfiguration() {
      @Override
      public String apiKey() {
        return "api_key_does_not_exist";
      }
    };

    ChatGPTRecipeGenerator generator = new ChatGPTRecipeGenerator(config);

    RecipeQuery query = new RecipeQuery(
      "Tomato, garlic, cucumber, and watermelon",
      "dinner"
    );

    generator.requestGeneratingRecipe(query, null, () -> {
      future.complete(true);
    });

    // if the test takes more than 3 seconds, consider empty string is returned.
    future.completeOnTimeout(false, 10, TimeUnit.SECONDS);

    boolean actual = future.get();

    assertTrue(actual,
      "Expected to be failed due to the incorrect API key");
  }
}
