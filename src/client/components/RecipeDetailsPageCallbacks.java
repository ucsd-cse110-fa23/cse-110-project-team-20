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
  private Runnable onShareButtonClicked;

  public RecipeDetailsPageCallbacks(
      Runnable onGoBackButtonClicked,
      Runnable onEditButtonClicked,
      Runnable onDeleteButtonClicked,
      Runnable onShareButtonClicked) {
    this.onGoBackButtonClicked = onGoBackButtonClicked;
    this.onEditButtonClicked = onEditButtonClicked;
    this.onDeleteButtonClicked = onDeleteButtonClicked;
    this.onShareButtonClicked = onShareButtonClicked;
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

  public Runnable getOnShareButtonClicked() {
    return onShareButtonClicked;
  }
}
