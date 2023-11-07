package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import client.Controller;
import client.Recipe;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecordingPage;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

@DisabledIfSystemProperty(
  named = "user_story_test",
  matches = "excluded",
  disabledReason = "GitHub Actions cannot handle the UI interaction, so we need to do manually on local for this case"
)
@TestMethodOrder(OrderAnnotation.class)
public class UserStoryTest extends UserStoryTestBase {
  @Test
  @Order(12)
  public void scenario_1_2_listDoesNotHaveRecipeThatTheUserWants() {
    /**
     * Scenario 1.2: The list does not have recipe that the user wants
     * Given that the user is on the list of recipes window
     * When the college student click create new recipe button
     * Then the list should navigate to the meal type prompt page
     */
    condition(() -> {
      GivenThatTheUserIsOnTheListOfRecipesWindow(primaryStage);
      WhenTheCollegeStudentClickCreateNewRecipeButton(controller);
    });

    ThenTheListShouldNavigateToTheMealTypePromptPage(primaryStage);
  }

  private void GivenThatTheUserIsOnTheListOfRecipesWindow(Stage primaryStage) {
    // the app is just launched so that the list of recipes showing
  }

  private void WhenTheCollegeStudentClickCreateNewRecipeButton(Controller controller) {
    controller.createRecipeButtonClicked();
  }

  private void ThenTheListShouldNavigateToTheMealTypePromptPage(Stage primaryStage) {
    assertEquals(primaryStage.getScene().getRoot().getClass(), RecordingPage.class);
  }

  @Test
  @Order(21)
  public void scenario_2_1_mealTypeIsSelected() {
    /**
     * Scenario 2.1 Meal type is selected
     * Given the user is currently on the meal type prompt window
     * When a meal type, such as “dinner”, is given
     * Then the recipe generation prompt window becomes active
     */
    condition(() -> {
      GivenTheUserIsCurrentlyOnTheMealTypePromptWindow(controller);
      WhenAMealTypeDinnerIsGiven(controller, null);
    }, 5000);

    ThenTheRecipeGenerationPromptWindowBecomesActive(primaryStage);
  }

  private void GivenTheUserIsCurrentlyOnTheMealTypePromptWindow(Controller controller) {
    controller.createRecipeButtonClicked();
  }

  private void WhenAMealTypeDinnerIsGiven(Controller controller, Runnable next) {
    controller.mealTypeRecordingButtonClicked();
    setTimeout(() -> {
      // saying "dinner"
      controller.mealTypeRecordingButtonClicked();
      if (next != null) next.run();
    }, 500);
  }

  private void ThenTheRecipeGenerationPromptWindowBecomesActive(Stage primaryStage) {
    RecordingPage ingredientsRecordingPage = (RecordingPage) primaryStage.getScene().getRoot();
    Label title = (Label) ingredientsRecordingPage.getChildren().get(0);

    assertInstanceOf(RecordingPage.class, ingredientsRecordingPage);
    assertEquals("What ingredients do you have?", title.getText());
  }

  @Test
  @Order(22)
  public void scenario_2_2_recipeIsGenerated() {
    /**
     * Scenario 2.2: Recipe is generated
     * Given the user is on the ingredients prompt window
     * When the query has the ingredients “banana, flour, eggs”
     * Then a window with the new recipe (in this case “Banana Pancake”) along with
     * a list of instructions and ingredients becomes active
     */
    condition(() -> {
      GivenTheUserIsOnTheIngredientsPromptWindow(controller, () -> {
        WhenTheQueryHasTheIngredients(controller, null);
      });
    }, 5000);

    ThenAWindowWithTheNewRecipeAlongWithAListOfInstructionsAndIngredientsBecomesActive(primaryStage);
  }

  private void GivenTheUserIsOnTheIngredientsPromptWindow(Controller controller, Runnable next) {
    controller.createRecipeButtonClicked();
    controller.mealTypeRecordingButtonClicked();
    setTimeout(() -> {
      // saying "dinner"
      controller.mealTypeRecordingButtonClicked();
      next.run();
    }, 300);
  }

  private void WhenTheQueryHasTheIngredients(Controller controller, Runnable next) {
    controller.ingredientsRecordingButtonClicked();
    setTimeout(() -> {
      // saying "“banana, flour, eggs”"
      controller.ingredientsRecordingButtonClicked();
      if (next != null) next.run();
    }, 500);
  }

  private void ThenAWindowWithTheNewRecipeAlongWithAListOfInstructionsAndIngredientsBecomesActive(Stage primaryStage) {
    Parent newRecipeConfirmPage = primaryStage.getScene().getRoot();
    assertInstanceOf(NewRecipeConfirmPage.class, newRecipeConfirmPage);
  }

  @Test
  @Order(24)
  public void scenario_2_3_saveButtionIsClicked() {
    /**
     * Scenario 2.3: Save Button Is Clicked
     * Given the recipe has been generated
     * When the “save” button is pressed
     * Then the recipe list of recipes window becomes active and the generated recipe is at the top
     */

    condition(() -> {
      GivenTheRecipeHasBeenGeneraated(controller, () -> {
        WhenTheSaveButtonIsPressed(controller);
      });
    }, 5000);

    ThenTheRecipeListBecomesActive(primaryStage);
    ThenTheGeneratedRecipeIsAtTheTopOfTheList(primaryStage);
  }

  private void GivenTheRecipeHasBeenGeneraated(Controller controller, Runnable next) {
    GivenTheUserIsCurrentlyOnTheMealTypePromptWindow(controller);
    WhenAMealTypeDinnerIsGiven(controller, () -> {
      WhenTheQueryHasTheIngredients(controller, next);
    });
  }

  private void WhenTheSaveButtonIsPressed(Controller controller) {
    // assume the recipe is generated
    Recipe recipe = new Recipe("Banana Pancake", "Some generated recipe for banana pancake.");
    setTimeout(() -> {
      controller.saveRecipeClicked(recipe);
    }, 200);
  }

  private void ThenTheRecipeListBecomesActive(Stage primaryStage) {
    assertEquals(primaryStage.getScene().getRoot().getClass(), HomePage.class);
  }

  private void ThenTheGeneratedRecipeIsAtTheTopOfTheList(Stage primaryStage) {
    HomePage homePage = (HomePage) primaryStage.getScene().getRoot();
    ScrollPane scrollPane = (ScrollPane) homePage.getCenter();
    Pane recipeList = (Pane) scrollPane.getContent();

    // supposed to new one
    assertEquals(1, recipeList.getChildren().size());
  }

  @Test
  @Order(23)
  public void scenario_2_4_cancelButtionIsClicked() {
    /**
     * Scenario 2.4: Cancel Button Is Clicked
     * Given the recipe has been generated
     * When the “cancel” button is pressed
     * Then window exits, returning back to the list of her recipe(s) (the home page)
     * And the list of recipes is unchanged
     */

    condition(() -> {
      GivenTheRecipeHasBeenGeneraated(controller, () -> {
        WhenTheCancelButtonIsPressed(controller);
      });
    }, 5000);

    ThenTheRecipeListBecomesActive(primaryStage);
    ThenTheListOfRecipesIsUnchanged(primaryStage);
  }

  private void WhenTheCancelButtonIsPressed(Controller controller) {
    setTimeout(() -> {
      controller.discardGeneratedRecipeClicked();
    }, 200);
  }

  private void ThenTheListOfRecipesIsUnchanged(Stage primaryStage) {
    HomePage homePage = (HomePage) primaryStage.getScene().getRoot();
    ScrollPane scrollPane = (ScrollPane) homePage.getCenter();
    Pane recipeList = (Pane) scrollPane.getContent();

    assertEquals(0, recipeList.getChildren().size());
  }

}
