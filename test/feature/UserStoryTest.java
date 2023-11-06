package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.condition.DisabledIfSystemProperty;

import client.Controller;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecordingPage;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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
    assertEquals(primaryStage.getScene().getRoot().getClass(), HomePage.class);
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
      WhenAMealTypeDinnerIsGiven(controller, () -> {});
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
      next.run();
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
        WhenTheQueryHasTheIngredients(controller, () -> {});
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
      next.run();
    }, 500);
  }

  private void ThenAWindowWithTheNewRecipeAlongWithAListOfInstructionsAndIngredientsBecomesActive(Stage primaryStage) {
    Parent newRecipeConfirmPage = primaryStage.getScene().getRoot();
    assertInstanceOf(NewRecipeConfirmPage.class, newRecipeConfirmPage);
  }
}
