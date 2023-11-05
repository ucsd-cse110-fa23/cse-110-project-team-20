package server.chatgpt;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ChatGPTServiceTest {
  @Test
  public void instance() {
    IChatGPTConfiguration config = new IChatGPTConfiguration() {
      @Override
      public String apiKey() {
        return "";
      }
    };

    ChatGPTService service = new ChatGPTService(config);
    assertInstanceOf(ChatGPTService.class, service);
  }

  @Test
  @Disabled // due to limited ChatGPT credit, disabled for now
  public void generateRecipeWithChatGPT() throws ChatGPTServiceException, IOException, InterruptedException, URISyntaxException {
    IChatGPTConfiguration config = new ChatGPTConfiguration();
    ChatGPTService service = new ChatGPTService(config);

    RecipeQuery query = new RecipeQuery(
        "Tomato, garlic, cucumber, and watermelon",
        "dinner");

    String response = service.request(query);

    assertTrue(response.length() > 20,
        "Expected a recipe with at least 20 characters from the ChatGPT response.");

    // quick check with ingredients
    assertTrue(response.toLowerCase().contains("tomato"));
    assertTrue(response.toLowerCase().contains("garlic"));
    assertTrue(response.toLowerCase().contains("cucumber"));
    assertTrue(response.toLowerCase().contains("watermelon"));
  }

  @Test
  public void generatingRecipeInvalidKey() {

    IChatGPTConfiguration config = new IChatGPTConfiguration() {
      @Override
      public String apiKey() {
        return "api-key-does-not-exist";
      }
    };

    ChatGPTService service = new ChatGPTService(config);

    RecipeQuery query = new RecipeQuery(
        "Tomato, garlic, cucumber, and watermelon",
        "dinner");

    Exception exception = assertThrows(ChatGPTServiceException.class, () -> {
      service.request(query);
    });

    String expected = "Incorrect API key provided";
    String actual = exception.getMessage();
    assertTrue(actual.contains(expected));
  }
}
