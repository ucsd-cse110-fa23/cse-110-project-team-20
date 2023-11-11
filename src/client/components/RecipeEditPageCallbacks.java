package client.components;

//TODO: Added Edit page callbacks
public class RecipeEditPageCallbacks {
    private Runnable onGoBackButtonClicked;
    private Runnable onSaveButtonClicked;

    public RecipeEditPageCallbacks(
        Runnable onGoBackButtonClicked,
        Runnable onDeleteButtonClicked) {
        this.onGoBackButtonClicked = onGoBackButtonClicked;
        this.onSaveButtonClicked = onDeleteButtonClicked;
    }

    public Runnable getOnGoBackButtonClicked() {
        return onGoBackButtonClicked;
    }

    public Runnable getOnSaveButtonClicked() {
        return onSaveButtonClicked;
    }
}
