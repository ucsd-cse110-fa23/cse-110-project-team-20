package server.api;

/**
 * Prompt template for DallE query
 */
public class RecipeImageQuery implements IRecipeQuery {
  private static final String PROMPT_TEMPLATE = "%s served on a restaurant dish, food photography, 15mm, morning light";

  private String title;

  public RecipeImageQuery(String title) {
    this.title = title;
  }
  @Override
  public String toQueryableString() {
    return String.format(PROMPT_TEMPLATE, title);
  }
}
