package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import client.Recipe;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecipeDetailsPage;
import client.components.RecipeDetailsPageCallbacks;
import client.components.RecipeEditPage;
import client.components.RecipeEditPageCallbacks;
import client.components.RecordingPage;

/**
 * UserStory test
 *
 * Userstory performs on controller with mock interfaces. Some of BDD scenarios are skipped in this
 * automated test due to UI dependency and duplications. These BDD scenarios are covered in manual
 * testing.
 *
 * - Scenario 1.1: The user scrolls for a recipe (UI scrolling interaction)
 * - Scenario 1.3: The user has initialized the application (JavaFX init steps)
 * - Scenario 4.2: User interacts with the instructions (UI form interaction)
 * - Scenario 4.5: User presses the cancel button (no cancel button)
 * - Scenario 5.2: User cancels deletion (confirmation popup related)
 * - Scenario 6.1: User records meal type (UI interaction, I/O interaction)
 * - Scenario 6.2: User records ingredient list (UI interaction, I/O interaction)
 */

public class MS1UserStoryTest extends UserStoryTestBase {
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
    controller.start();
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
  public void scenario_2_3_saveButtionIsClicked() {
    /**
     * Scenario 2.3: Save Button Is Clicked
     * Given the recipe has been generated
     * When the “save” button is pressed
     * Then the recipe list of recipes window becomes active and the generated recipe is at the top
     */
    GivenTheRecipeHasBeenGenerated();
    WhenTheSaveButtonIsPressedOnNewRecipeConfirmPage();
    ThenTheRecipeListBecomesActive();
    ThenTheGeneratedRecipeIsAtTheTopOfTheList();
  }

  private void GivenTheRecipeHasBeenGenerated() {
    GivenTheUserIsCurrentlyOnTheMealTypePromptWindow();
    WhenAMealTypeDinnerIsGiven();
    WhenTheQueryHasTheIngredients();
  }

  private void WhenTheSaveButtonIsPressedOnNewRecipeConfirmPage() {
    controller.saveRecipeClicked(recipeStub);
  }

  private void ThenTheRecipeListBecomesActive() {
    assertEquals(viewTransitioner.currentPageClass, HomePage.class);
  }

  private void ThenTheGeneratedRecipeIsAtTheTopOfTheList() {
    Recipe firstRecipe = recipeModel.recipes.get(0);
    assertEquals(recipeStub, firstRecipe);
  }

  @Test
  public void scenario_2_4_cancelButtionIsClicked() {
    /**
     * Scenario 2.4: Cancel Button Is Clicked
     * Given the recipe has been generated
     * When the “cancel” button is pressed
     * Then window exits, returning back to the list of her recipe(s) (the home page)
     * And the list of recipes is unchanged
     */
    GivenTheRecipeHasBeenGenerated();
    WhenTheCancelButtonIsPressed();
    ThenTheRecipeListBecomesActive();
    ThenTheListOfRecipesIsUnchanged();
  }

  private void WhenTheCancelButtonIsPressed() {
    controller.discardGeneratedRecipeClicked();
  }

  private void ThenTheListOfRecipesIsUnchanged() {
    assertEquals(0, recipeModel.recipes.size());
  }

  @Test
  public void scenario_3_1_userViewsAnExistingRecipe() {
    /**
     * Scenario 3.1: User views an existing recipe
     * Given the list of recipes is being viewed
     * When “Chicken with broccoli” gets clicked
     * Then expanded view for “Chicken with broccoli” shows
     * When back button gets clicked
     * Then expanded view for “Chicken with broccoli” disappears
     *   And the list of recipes is visible again
     */
    GivenTheListOfRecipesIsBeingViewed();
    WhenChickenWithBroccoliGetClicked();
    ThenExpandedViewForChickenWithBroccoliShows();
    WhenBackButtonGetsClicked();
    ThenTheRecipeListBecomesActive();
  }

  public void GivenTheListOfRecipesIsBeingViewed() {
    recipeModel.recipes.add(new Recipe("Chicken with carrot", "Chicken with carrot recipe instruction."));
    recipeModel.recipes.add(new Recipe("Chicken with broccoli", "Chicken with broccoli recipe instruction."));
    recipeModel.recipes.add(new Recipe("Chicken with chocolete", "Chicken with chocolete recipe instruction."));
  }

  public void WhenChickenWithBroccoliGetClicked() {
    for(int i = 0; i < recipeModel.recipes.size(); i++) {
      Recipe recipeInTheList = recipeModel.recipes.get(i);
      if (recipeInTheList.getTitle() == "Chicken with broccoli") {
        controller.openRecipeDetailPage(i);
        return;
      }
    }
  }

  public void ThenExpandedViewForChickenWithBroccoliShows() {
    assertEquals(viewTransitioner.currentPageClass, RecipeDetailsPage.class);

    Recipe recipe = (Recipe) viewTransitioner.rawParam1;
    assertEquals("Chicken with broccoli", recipe.getTitle());
  }

  public void WhenBackButtonGetsClicked() {
    RecipeDetailsPageCallbacks supposeToCallbacks = (RecipeDetailsPageCallbacks) viewTransitioner.rawParam2;
    supposeToCallbacks.getOnGoBackButtonClicked().run();
  }

  @Test
  public void scenario_5_1_userDeletesARecipe() {
    /**
     * Scenario 5.1: User deletes a recipe
     * Given the expanded view is currently active for “chicken with broccoli”
     *    And there are 3 total recipes in the list
     * When the delete button is pressed
     * Then the “Confirm delete” pop-up shows
     * When the second delete button is pressed
     * Then the pop-up and expanded view close
     *    And the “chicken with broccoli” recipe we were viewing is gone
     *    And the list no longer contains the recipe, with all other recipes appropriately shifted
     */
    GivenThereAre3TotalRecipesInTheList();
    GivenTheExpandedViewIsCurrentlyActiveForChickenWithBroccoli();
    WhenTheDeleteButtonIsPressed();
    // skip on confirm delete popup steps cause UI intraction cannot be tested in current setup
    // (skip) Then the “Confirm delete” pop-up shows
    // (skip) When the second delete button is pressed
    ThenThePopupAndExpandedViewClose();
    ThenTheChickenWithBroccoliRecipeWeWereViewingIsGone();
    ThenTheListNoLongerContainsTheRecipeWithAllOtherRecipesAppropriatelyShifted();
  }

  public void GivenTheExpandedViewIsCurrentlyActiveForChickenWithBroccoli() {
    for(int i = 0; i < recipeModel.recipes.size(); i++) {
      Recipe recipeInTheList = recipeModel.recipes.get(i);
      if (recipeInTheList.getTitle() == "Chicken with broccoli") {
        controller.openRecipeDetailPage(i);
        return;
      }
    }
  }

  public void GivenThereAre3TotalRecipesInTheList() {
    recipeModel.recipes.add(new Recipe("Chicken with potato", "Chicken with potato recipe instruction."));
    recipeModel.recipes.add(new Recipe("Chicken with broccoli", "Chicken with broccoli recipe instruction. Boil for 50 mins."));
    recipeModel.recipes.add(new Recipe("Chicken with cabbage", "Chicken with cabbage recipe instruction."));
  }

  public void WhenTheDeleteButtonIsPressed() {
    RecipeDetailsPageCallbacks supposeToCallbacks = (RecipeDetailsPageCallbacks) viewTransitioner.rawParam2;
    supposeToCallbacks.getOnDeleteButtonClicked().run();
  }

  public void ThenThePopupAndExpandedViewClose() {
    assertEquals(viewTransitioner.currentPageClass, HomePage.class);
  }

  public void ThenTheChickenWithBroccoliRecipeWeWereViewingIsGone() {
    boolean passed = true;
    for(int i = 0; i < recipeModel.recipes.size(); i++) {
      Recipe recipeInTheList = recipeModel.recipes.get(i);
      if (recipeInTheList.getTitle() == "Chicken with broccoli") {
        passed = false;
      }
    }
    assertTrue(passed);
  }

  public void ThenTheListNoLongerContainsTheRecipeWithAllOtherRecipesAppropriatelyShifted() {
    assertEquals(2, recipeModel.recipes.size());
    assertEquals(recipeModel.recipes.get(0).getTitle(), "Chicken with potato");
    assertEquals(recipeModel.recipes.get(1).getTitle(), "Chicken with cabbage");
  }

  @Test
  public void scenario_4_1_userPressesTheEditButton() {
    /**
     * Scenario 4.1: User presses the edit button
     * Given the expanded view interface is currently active on “Chicken with broccoli”
     * When the edit button is pressed
     * Then the instructions editing interface should become active
     */
    // for default list
    GivenThereAre3TotalRecipesInTheList();

    GivenTheExpandedViewIsCurrentlyActiveForChickenWithBroccoli();
    WhenTheEditButtonIsPressed();
    ThenTheEditingPageShouldBecomeActive();
  }

  public void WhenTheEditButtonIsPressed() {
    RecipeDetailsPageCallbacks supposeToCallbacks = (RecipeDetailsPageCallbacks) viewTransitioner.rawParam2;
    supposeToCallbacks.getOnEditButtonClicked().run();
  }

  private void ThenTheEditingPageShouldBecomeActive() {
    assertEquals(viewTransitioner.currentPageClass, RecipeEditPage.class);
  }

  @Test
  public void scenario_4_3_userPressesTheSaveButton() {
    // Scenario 4.3: User presses the save button
    // Given the instructions editing interface is currently active
    // When the save button is pressed
    // Then the expanded view’s detailed instructions update to having “Fry for 30 mins” text

    GivenTheRecipeEditPageIsCurrentlyActive();
    WhenTheSaveButtonIsPressedAfterUpdatingFryFor30Mins();
    ThenTheDetailPageShouldHaveFryFor30MinsText();
  }

  public void GivenTheRecipeEditPageIsCurrentlyActive() {
    // do same setup as scenario 4.1
    GivenThereAre3TotalRecipesInTheList();
    GivenTheExpandedViewIsCurrentlyActiveForChickenWithBroccoli();
    WhenTheEditButtonIsPressed();
  }

  public void WhenTheSaveButtonIsPressedAfterUpdatingFryFor30Mins() {
    Recipe recipe = (Recipe) viewTransitioner.rawParam1;
    RecipeEditPageCallbacks supposeToCallbacks = (RecipeEditPageCallbacks) viewTransitioner.rawParam2;

    String newDescription = recipe.getDescription().replace("Boil for 50 mins", "Fry for 30 mins");
    Recipe updatedRecipe = new Recipe(recipe.getTitle(), newDescription);
    supposeToCallbacks.getOnSaveButtonClicked().run(updatedRecipe);
  }

  public void ThenTheDetailPageShouldHaveFryFor30MinsText() {
    assertEquals(viewTransitioner.currentPageClass, RecipeDetailsPage.class);

    Recipe recipe = (Recipe) viewTransitioner.rawParam1;
    assertTrue(recipe.getDescription().contains("Fry for 30 mins"));
  }

  @Test
  public void sceenario_4_4_userPressesTheBackButton() {
    // Scenario 4.4: User presses the back button
    // Given the instructions editing interface is currently active
    // When the back button is pressed
    // Then recipe details page should become active
    // And And expanded view’s detailed instructions still have “Boil for 50 mins”
    GivenTheRecipeEditPageIsCurrentlyActive();
    WhenTheBackButtonIsPressedOnRecipeEditPage();
    ThenExpandedViewForChickenWithBroccoliShows();
    ThenExpandedViewDescriptionShouldHaveBoilFor50Mins();
  }

  public void WhenTheBackButtonIsPressedOnRecipeEditPage() {
    RecipeEditPageCallbacks supposeToCallbacks = (RecipeEditPageCallbacks) viewTransitioner.rawParam2;
    supposeToCallbacks.getOnGoBackButtonClicked().run();
  }

  public void ThenExpandedViewDescriptionShouldHaveBoilFor50Mins() {
    Recipe recipe = (Recipe) viewTransitioner.rawParam1;
    assertTrue(recipe.getDescription().contains("Boil for 50 mins"));
  }

}
