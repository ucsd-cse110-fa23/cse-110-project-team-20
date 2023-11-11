package client.components;

/**
 * RecipeDetailsPageCallbacks
 *
 * This callback class is to carry over recipe details page callbacks
 * as a single object.
 */
public class RecipeDetailsPageCallbacks {
  private Runnable onGoBackButtonClicked;
  private Runnable onEditButtonClicked;
  private Runnable onDeleteButtonClicked;

  public RecipeDetailsPageCallbacks(
      Runnable onGoBackButtonClicked,
      Runnable onEditButtonClicked,
      Runnable onDeleteButtonClicked) {
    this.onGoBackButtonClicked = onGoBackButtonClicked;
    this.onEditButtonClicked = onEditButtonClicked;
    this.onDeleteButtonClicked = onDeleteButtonClicked;
  }

  public Runnable getOnGoBackButtonClicked() {
    return onGoBackButtonClicked;
  }

  public Runnable getOnEditButtonClicked() {
    return onEditButtonClicked;
  }

  public Runnable getOnDeleteButtonClicked() {
    return onDeleteButtonClicked;
  }
}
