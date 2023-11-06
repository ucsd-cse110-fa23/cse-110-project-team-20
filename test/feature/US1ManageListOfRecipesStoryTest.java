package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import client.Controller;
import client.PantryPalsApp;
import client.components.HomePage;
import client.components.RecordingPage;
import javafx.stage.Stage;

public class US1ManageListOfRecipesStoryTest {
  @Test
  public void listDoesNotHaveRecipeThatTheUserWants() {
    /**
     * Scenario 1.2: The list does not have recipe that the user wants
     * Given that the user is on the list of recipes window
     * When the college student click create new recipe button
     * Then the list should navigate to the meal type prompt page
     */
    PantryPalsApp.inspect((PantryPalsApp app, Stage primaryStage, Controller controller) -> {
      GivenThatTheUserIsOnTheListOfRecipesWindow(primaryStage);
      WhenTheCollegeStudentClickCreateNewRecipeButton(controller);
      ThenTheListShouldNavigateToTheMealTypePromptPage(primaryStage);
    });
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
}
