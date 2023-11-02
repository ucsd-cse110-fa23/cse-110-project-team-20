package client.recipe;

/**
 * RecipeGenerated event
 */
public interface RecipeGenerated {
    /**
     * onRecipeGenerated runs when the generator completes generating a recipe
     *
     * @param recipe
     */
    void onRecipeGenerated(String recipe);
}
