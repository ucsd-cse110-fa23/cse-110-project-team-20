package client.recipe;

import client.Recipe;

/**
 * RecipeGenerated event
 */
public interface RecipeGenerated {
    /**
     * onRecipeGenerated runs when the generator completes generating a recipe
     *
     * @param recipe
     */
    void onRecipeGenerated(Recipe recipe);
}
