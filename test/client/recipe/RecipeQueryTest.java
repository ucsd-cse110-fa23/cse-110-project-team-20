package client.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

public class RecipeQueryTest {
  @Test
  public void instance() {
    RecipeQuery query = new RecipeQuery("Apple", "Breakfast");
    assertInstanceOf(RecipeQuery.class, query);
  }

  @Test
  public void queryAsToString() {
    RecipeQuery query = new RecipeQuery("Apple", "Breakfast");

    String expected = "Create a recipe with Apple as Breakfast";

    assertEquals(expected, query.toQueryableString());
  }
}
