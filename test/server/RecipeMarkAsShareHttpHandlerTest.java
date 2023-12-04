package server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import org.junit.jupiter.api.Test;
import server.mock.MockHttpRequest;
import server.mock.MockSharedRecipeRepository;

public class RecipeMarkAsShareHttpHandlerTest {
  @Test
  public void markAsShare() {
    MockSharedRecipeRepository repository = new MockSharedRecipeRepository();

    RecipeMarkAsShareHttpHandler handler = new RecipeMarkAsShareHttpHandler(repository);

    MockHttpRequest mockRequest = new MockHttpRequest();
    HashMap<String, String> query = new HashMap<>();
    query.put("id", "1200");
    mockRequest.setQuery(query);

    String result = handler.handlePost(mockRequest);

    assertEquals(1200, repository.getLastMarkedAsSharedId());
    assertTrue(result.contains("success"));
  }
}
