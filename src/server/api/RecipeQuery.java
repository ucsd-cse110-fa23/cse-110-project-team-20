package server.api;

public class RecipeQuery implements IRecipeQuery {
  /**
   * Prompt template for ChatGPT query
   */
  private static final String PROMPT_TEMPLATE = "Create a recipe with limited ingredients: %s. I want to make a meal for %s. Response step by step recipe instructions in details for cooking beginner. Put the title of recipe in the first line of the response.";

  private String ingredients;
  private String mealType;

  public RecipeQuery(String ingredients, String mealType) {
    this.ingredients = ingredients;
    this.mealType = mealType;
  }

  @Override
  public String toQueryableString() {
    return String.format(PROMPT_TEMPLATE, this.ingredients, this.mealType);
  }
}
