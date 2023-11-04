package server.chatgpt;

public interface RecipeQueryable {
  /**
   * Convert RecipeQueryable as a string that queryable in some other system
   *
   * @return queryable string
   */
  public String toQueryableString();
}
