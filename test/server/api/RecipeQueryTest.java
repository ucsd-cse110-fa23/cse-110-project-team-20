package server.api;

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

    String expected = "Create a recipe with limited ingredients: Apple. I want to make a meal for Breakfast. Response step by step recipe instructions in details for cooking beginner. Put the title of recipe in the first line of the response.";

    assertEquals(expected, query.toQueryableString());
  }
}
