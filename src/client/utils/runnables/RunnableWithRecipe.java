package client.utils.runnables;

import client.Recipe;

// Class that acts as a runnable that can take a Recipe
public interface RunnableWithRecipe {
    public void run(Recipe recipe);
}