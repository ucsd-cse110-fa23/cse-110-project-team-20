package client.utils.runnables;

import client.Recipe;

public interface RunnableWithRecipe {
    public void run(int id, Recipe recipe);
}