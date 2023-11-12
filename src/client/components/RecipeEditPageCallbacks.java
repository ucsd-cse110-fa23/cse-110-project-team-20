package client.components;

import client.utils.runnables.RunnableWithRecipe;

//TODO: Added Edit page callbacks
public class RecipeEditPageCallbacks {
    private Runnable onGoBackButtonClicked;
    private RunnableWithRecipe onSaveButtonClicked;

    public RecipeEditPageCallbacks(
        Runnable onGoBackButtonClicked,
        RunnableWithRecipe onSaveButtonClicked) {
        this.onGoBackButtonClicked = onGoBackButtonClicked;
        this.onSaveButtonClicked = onSaveButtonClicked;
    }

    public Runnable getOnGoBackButtonClicked() {
        return onGoBackButtonClicked;
    }

    public RunnableWithRecipe getOnSaveButtonClicked() {
        return onSaveButtonClicked;
    }
}
