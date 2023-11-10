package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import client.Recipe;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecipeDetailsPage;
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
  public void scenario_2_3_saveButtionIsClicked() {
    /**
     * Scenario 2.3: Save Button Is Clicked
     * Given the recipe has been generated
     * When the “save” button is pressed
     * Then the recipe list of recipes window becomes active and the generated recipe is at the top
     */
    GivenTheRecipeHasBeenGenerated();
    WhenTheSaveButtonIsPressed();
    ThenTheRecipeListBecomesActive();
    ThenTheGeneratedRecipeIsAtTheTopOfTheList();
  }

  private void GivenTheRecipeHasBeenGenerated() {
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
        // @TODO replace to the new one when #88 is merged
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
    Runnable supposeToCancelButton = (Runnable) viewTransitioner.rawParam2;
    supposeToCancelButton.run();
  }
}
