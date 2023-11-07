package client.recipe;

import client.Recipe;

/**
 * IRecipeGenerated event
 */
public interface IRecipeGenerated {
    /**
     * onRecipeGenerated runs when the generator completes generating a recipe
     *
     * @param recipe
     */
    void onRecipeGenerated(Recipe recipe);
}
