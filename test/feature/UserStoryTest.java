package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecordingPage;

public class UserStoryTest extends UserStoryTestBase {
  @Test
  public void scenario_1_2_listDoesNotHaveRecipeThatTheUserWants() {
    /**
     * Scenario 1.2: The list does not have recipe that the user wants
     * Given that the user is on the list of recipes window
     * When the college student click create new recipe button
     * Then the list should navigate to the meal type prompt page
     */
    GivenThatTheUserIsOnTheListOfRecipesWindow();
    WhenTheCollegeStudentClickCreateNewRecipeButton();
    ThenTheListShouldNavigateToTheMealTypePromptPage();
  }

  private void GivenThatTheUserIsOnTheListOfRecipesWindow() {
    // the app is just launched so that the list of recipes showing
  }

  private void WhenTheCollegeStudentClickCreateNewRecipeButton() {
    controller.createRecipeButtonClicked();
  }

  private void ThenTheListShouldNavigateToTheMealTypePromptPage() {
    assertEquals(viewTransitioner.currentPageClass, RecordingPage.class);
  }

  @Test
  public void scenario_2_1_mealTypeIsSelected() {
    /**
     * Scenario 2.1 Meal type is selected
     * Given the user is currently on the meal type prompt window
     * When a meal type, such as “dinner”, is given
     * Then the recipe generation prompt window becomes active
     */
    GivenTheUserIsCurrentlyOnTheMealTypePromptWindow();
    WhenAMealTypeDinnerIsGiven();
    ThenTheRecipeGenerationPromptWindowBecomesActive();
  }

  private void GivenTheUserIsCurrentlyOnTheMealTypePromptWindow() {
    controller.createRecipeButtonClicked();
  }

  private void WhenAMealTypeDinnerIsGiven() {
    controller.mealTypeRecordingStarted();
    // suppose say dinner on mic
    controller.mealTypeRecordingCompleted();
  }

  private void ThenTheRecipeGenerationPromptWindowBecomesActive() {
    assertEquals(viewTransitioner.currentPageClass, RecordingPage.class);
    assertEquals(viewTransitioner.param1, "What ingredients do you have?");
  }

  @Test
  public void scenario_2_2_recipeIsGenerated() {
    /**
     * Scenario 2.2: Recipe is generated
     * Given the user is on the ingredients prompt window
     * When the query has the ingredients “banana, flour, eggs”
     * Then a window with the new recipe (in this case “Banana Pancake”) along with
     * a list of instructions and ingredients becomes active
     */
    GivenTheUserIsOnTheIngredientsPromptWindow();
    WhenTheQueryHasTheIngredients();
    ThenAWindowWithTheNewRecipeAlongWithAListOfInstructionsAndIngredientsBecomesActive();
  }

  private void GivenTheUserIsOnTheIngredientsPromptWindow() {
    controller.createRecipeButtonClicked();
    controller.mealTypeRecordingStarted();
    // saying "dinner"
    controller.mealTypeRecordingCompleted();
  }

  private void WhenTheQueryHasTheIngredients() {
    controller.ingredientsRecordingStarted();
    // saying "“banana, flour, eggs”"
    controller.ingredientsRecordingCompleted();
  }

  private void ThenAWindowWithTheNewRecipeAlongWithAListOfInstructionsAndIngredientsBecomesActive() {
    assertEquals(viewTransitioner.currentPageClass, NewRecipeConfirmPage.class);
    assertEquals(viewTransitioner.param1, recipeStub.toString());
  }

  @Test
  @Disabled("Need to implement model")
  public void scenario_2_3_saveButtionIsClicked() {
    /**
     * Scenario 2.3: Save Button Is Clicked
     * Given the recipe has been generated
     * When the “save” button is pressed
     * Then the recipe list of recipes window becomes active and the generated recipe is at the top
     */
    GivenTheRecipeHasBeenGeneraated();
    WhenTheSaveButtonIsPressed();
    ThenTheRecipeListBecomesActive();
    ThenTheGeneratedRecipeIsAtTheTopOfTheList();
  }

  private void GivenTheRecipeHasBeenGeneraated() {
    GivenTheUserIsCurrentlyOnTheMealTypePromptWindow();
    WhenAMealTypeDinnerIsGiven();
    WhenTheQueryHasTheIngredients();
  }

  private void WhenTheSaveButtonIsPressed() {
    controller.saveRecipeClicked(recipeStub);
  }

  private void ThenTheRecipeListBecomesActive() {
    assertEquals(viewTransitioner.currentPageClass, HomePage.class);
  }

  private void ThenTheGeneratedRecipeIsAtTheTopOfTheList() {
    // @TODO we can check when we mock model
  }

  @Test
  @Disabled("Need to implement model")
  public void scenario_2_4_cancelButtionIsClicked() {
    /**
     * Scenario 2.4: Cancel Button Is Clicked
     * Given the recipe has been generated
     * When the “cancel” button is pressed
     * Then window exits, returning back to the list of her recipe(s) (the home page)
     * And the list of recipes is unchanged
     */
    GivenTheRecipeHasBeenGeneraated();
    WhenTheCancelButtonIsPressed();
    ThenTheRecipeListBecomesActive();
    ThenTheListOfRecipesIsUnchanged();
  }

  private void WhenTheCancelButtonIsPressed() {
    controller.discardGeneratedRecipeClicked();
  }

  private void ThenTheListOfRecipesIsUnchanged() {
    // @TODO we can check when we mock model
  }
}
