package server.api;

public class RecipeQuery implements IRecipeQuery {
  /**
   * Prompt template for ChatGPT query
   */
  private static final String PROMPT_TEMPLATE = "Create a recipe with %s as %s";

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
