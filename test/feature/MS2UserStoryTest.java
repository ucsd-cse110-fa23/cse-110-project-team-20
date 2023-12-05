package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import client.Recipe;
import client.components.ErrorMessage;
import client.components.ErrorPage;
import client.components.HomePage;
import client.components.HomePageMealTypeFiltered;
import client.components.LoginPage;
import client.components.SharedRecipeModal;
import client.components.NewRecipeConfirmPage;
import client.utils.runnables.RunnableForLogin;
import feature.mock.MockAccountManager;
import javafx.beans.binding.When;

import org.junit.jupiter.api.Test;

/**
 * MS2 UserStory test
 *
 * Userstory performs on controller with mock interfaces. Some of BDD scenarios are skipped in this
 * automated test due to UI dependency and duplications. These BDD scenarios are covered in manual
 * testing.
 *
 * - Scenario 1.2: Account exists (covered in scenario 1.1)
 * - Scenario 1.4: Log out (UI interaction, we need to close the app)
 * - Scenario 2.2: Automatic login not selected (covered in scenario 1.3)
 * - Scenario 3.2: Access shared recipe webpage (Web page test, done by SharedRecipeHttpHandlerTest and manual testing)
 * - Scenario 3.3: Update shared recipe webpage (Web page test, done by SharedRecipeHttpHandlerTest and manual testing)
 * - Scenario 3.4: Delete shared recipe webpage (Web page test, done by SharedRecipeHttpHandlerTest and manual testing)
 * - Scenario 7.1: View meal type (UI elements in home page, manually tested)
 * - Scenario 9.2: Save Button is clicked (the scenario covered by MS1 scenario 2.3)
 */

public class MS2UserStoryTest extends UserStoryTestBase {
    @Test
    public void
    scenario_1_1_accountCreatedOrLoggedIn()
    {
        // Scenario 1.1: Account created
        // Given the user is on the login page
        //   And the user has not created an account
        //   And the user types in username “Hello” and password “World” combo -- handled in when
        //   stage And account “Hello” doesn't exist
        // When The user clicks “Log in or Create Account”
        // Then The user is directed to the home page
        //   And An empty recipe list is shown -- assumed it is handled in home page already from
        //   MS1
        ((MockAccountManager) accountManager).setToken("expected token from the new account");

        GivenTheUserIsOnTheLoginPage();
        WhenTheUserClicksLoginOrCreateAccountButtonWith("Hello", "World");
        ThenTheUserIsDirectedToTheHomePage();
        ThenTheUserHasAValidToken();
    }

    private void
    GivenTheUserIsOnTheLoginPage()
    {
        controller.transitionToLoginScene();
    }

    private void
    WhenTheUserClicksLoginOrCreateAccountButtonWith(String username, String password)
    {
        RunnableForLogin runnable = (RunnableForLogin) viewTransitioner.params[0];
        runnable.run(username, password, false, () -> {});
    }

    private void
    ThenTheUserIsDirectedToTheHomePage()
    {
        assertEquals(HomePage.class, viewTransitioner.currentPageClass);
    }

    private void
    ThenTheUserHasAValidToken()
    {
        assertEquals(accountSession.getToken(), "expected token from the new account");
    }

    @Test
    public void
    scenario_1_3_loginWithInvalidCredentials()
    {
        // Scenario 1.3: Login with invalid credentials
        // Given the user is on the login page
        //   And the user had created an account with the “Hello” username and “World” password
        //   And the user has typed in “Hello” for username and “Universe” for password
        // When The user clicks “Log in”
        // Then A message that says “Invalid credentials” is displayed
        GivenTheUserIsOnTheLoginPage();
        ((MockAccountManager) accountManager).setToken("expected token from the existing account");
        ((MockAccountManager) accountManager).expectToThrowsIncorrectPassword();

        GivenTheUserIsOnTheLoginPage();
        WhenTheUserClicksLoginOrCreateAccountButtonWith("Hello", "Universe");
        ThenTheUserShouldSeeIncorrectPasswordErrorMessage();
    }

    private void
    ThenTheUserShouldSeeIncorrectPasswordErrorMessage()
    {
        assertEquals(ErrorMessage.class, viewTransitioner.currentPageClass);
        assertEquals("Incorrect Password", viewTransitioner.params[0]);
    }

    @Test
    public void scenario_2_1_automaticLoginSelected()
    {
        // Scenario 2.1: Automatic login selected
        // Given The user is on the login page
        //     And the user types in username “Hello” and password “World” combo
        // And account “Hello” does exist
        //     And the user checks “Stay logged in” button
        // When The user clicks “Log in”
        // Then The user is directed to the homepage
        // When The user exits and re-opens the app
        // Then The user is immediately directed to the homepage with all their recipes shown

        GivenTheUserIsOnTheLoginPage();

        WhenTheUserTryToLoginWithStayLoggedInCheckbox("Hello", "World");
        ThenTheUserIsDirectedToTheHomePage();

        WhenTheUserExistsAndReopensTheApp();
        ThenTheUserIsDirectedToTheHomePage();
    }

    private void
    WhenTheUserTryToLoginWithStayLoggedInCheckbox(String username, String password)
    {
        RunnableForLogin onLogin = (RunnableForLogin) viewTransitioner.params[0];
        onLogin.run(username, password, true, () -> {});
    }

    private void
    WhenTheUserExistsAndReopensTheApp()
    {
        // assume app is restarted
        controller.start();
    }
    
    @Test
    public void
    scenario_2_3_logOutWhenAutomaticLoginSelected()
    {
        // Scenario 2.3: Log out when automatic login selected
        // Given The user is logged in 
        //     And the user had checked “Stay logged in” button
        // When The user clicks “Log out”
        // Then The user is directed to the log in screen
        // When The user exits and re-opens the app
        // Then The user is directed to the login page
        GivenTheUserIsLoggedIn();
        WhenTheUserClicksLogOut();
        ThenTheUserIsOnTheLoginPage();
        WhenTheUserExistsAndReopensTheApp();
        ThenTheUserIsOnTheLoginPage();
    }

    private void
    GivenTheUserIsLoggedIn()
    {
        GivenTheUserIsOnTheLoginPage();
        WhenTheUserTryToLoginWithStayLoggedInCheckbox("Hello", "World");
    }

    private void
    WhenTheUserClicksLogOut()
    {
        controller.logoutButtonClicked();
    }

    private void
    ThenTheUserIsOnTheLoginPage()
    {
        assertEquals(LoginPage.class, viewTransitioner.currentPageClass);
    }

    @Test
    public void
    scenario_3_1_shareButtonIsClicked()
    {
        // Scenario 3.1: Share button is clicked
        // Given the user is on recipe detail page
        // And the recipe detail page displays Chicken Cream Pasta recipe
        // When the user clicks share button on the page
        // Then the unique URL to access this recipe is provided to the user
        GivenTheUserIsOnTheRecipeDetailPageWithChickenCreamPasta();
        WhenTheUserClicksShareButton();
        ThenTheUniqueURLToAccessThisRecipeIsProvidedToTheUser();
    }

    private void
    GivenTheUserIsOnTheRecipeDetailPageWithChickenCreamPasta() {
        recipeModel.recipes.add(
            new Recipe("Chicken Cream Pasta", "Chicken Cream Pasta recipe instruction."));
        controller.openRecipeDetailPage(0);
    }

    private void
    WhenTheUserClicksShareButton() {
        controller.shareRecipeClicked(0);
    }

    private void
    ThenTheUniqueURLToAccessThisRecipeIsProvidedToTheUser() {
        // expected to show shared url on shared recipe modal screen
        assertEquals(SharedRecipeModal.class, viewTransitioner.currentPageClass);
        assertTrue(((String) viewTransitioner.params[0]).contains("http://localhost/recipe/shared/?url="));
    }

    @Test
    public void
    scenario_4_1_selectFilterByMealType() {
        // Scenario 4.1: select filter by meal type
        // Given the user is on home page
        //     And the recipe list displays 2 lunch, 3 dinner meal type recipes
        // When the user clicks filter by meal type
        //     And select “Dinner” from the filter
        // Then the recipe list should display 3 recipe items
        //     And the recipe should be a dinner meal type recipes
        GivenTheUserIsOnHomePageWithMultipleRecipes();
        WhenTheUserClicksFilterByMealTypeAndSelect("dinner");
        ThenTheRecipeListShouldDisplayFilteredRecipeListAs("dinner");
    }

    private void
    GivenTheUserIsOnHomePageWithMultipleRecipes() {
        // add 2 lunch and 3 dinner meal type
        recipeModel.recipes.add(
            new Recipe(
                "Chicken Cream Pasta 1",
                "Chicken Cream Pasta recipe instruction.",
                "some ingredients",
                "lunch",
                "some image"));
        recipeModel.recipes.add(
            new Recipe(
                "Chicken Cream Pasta 2",
                "Chicken Cream Pasta recipe instruction.",
                "some ingredients",
                "lunch",
                "some image"));
        recipeModel.recipes.add(
            new Recipe(
                "Chicken Cream Pasta 3",
                "Chicken Cream Pasta recipe instruction.",
                "some ingredients",
                "dinner",
                "some image"));
        recipeModel.recipes.add(
            new Recipe(
                "Chicken Cream Pasta 4",
                "Chicken Cream Pasta recipe instruction.",
                "some ingredients",
                "dinner",
                "some image"));
        recipeModel.recipes.add(
            new Recipe(
                "Chicken Cream Pasta 5",
                "Chicken Cream Pasta recipe instruction.",
                "some ingredients",
                "dinner",
                "some image"));
    }

    private void
    WhenTheUserClicksFilterByMealTypeAndSelect(String filterText) {
        controller.mealTypeFilterButtonClicked(filterText);
    }

    private void
    ThenTheRecipeListShouldDisplayFilteredRecipeListAs(String filterText) {
        // Since filtering is handled in the UI context, we only check
        // if the right view transitioner is called or not.

        assertEquals(HomePageMealTypeFiltered.class, viewTransitioner.currentPageClass);
        assertEquals(filterText, viewTransitioner.params[0]);
    }

    @Test
    public void
    scenario_4_2_changeSelectedFilterOfMealType() {
        // Scenario 4.2: change selected filter of meal type
        // Given the user is on home page
        //     And the app have a list of recipes: 2 lunch, 3 dinner, 1 breakfast meal type recipes
        //     And the list is filtered with “dinner”
        //     And the list shows 3 filtered recipes
        // When the user clicks filter by meal type
        //     And select “Breakfast” from the filter
        // Then the recipe list should display 1 breakfast recipe item
        GivenPreSelectedMealTypeListOfHomePage("dinner");
        WhenTheUserClicksFilterByMealTypeAndSelect("breakfast");
        ThenTheRecipeListShouldDisplayFilteredRecipeListAs("breakfast");
    }

    private void
    GivenPreSelectedMealTypeListOfHomePage(String filterText) {
        GivenTheUserIsOnHomePageWithMultipleRecipes();
        WhenTheUserClicksFilterByMealTypeAndSelect(filterText);
        ThenTheRecipeListShouldDisplayFilteredRecipeListAs(filterText);
    }

    @Test
    public void
    scenario_4_3_unselectFilterOfMealType() {
        // Scenario 4.3: unselect filter of meal type
        // Given the user is on homepage
        //     And the app have a list of recipes: 2 lunch, 3 dinner meal type recipes
        //     And the list is filtered with “dinner”
        //     And the list shows 3 filtered recipes
        // When the user clicks filter by meal type
        //     And select “Show All” from the filter
        // Then the recipe list should display 5 recipes
        //     And the recipe list should contains 2 lunch meal type recipes
        //     And the recipe list should contains 3 dinner meal type recipes

        GivenPreSelectedMealTypeListOfHomePage("dinner");
        // we uses null as Show All
        WhenTheUserClicksFilterByMealTypeAndSelect(null);
        ThenTheRecipeListShouldDisplayFilteredRecipeListAs(null);
    }

    @Test
    public void
    scenario_5_1_regeneratingARecipe() {
        // Scenario 5.1 Regenerating a recipe
        // Given that the user has successfully generated the recipe with ingredients “basil and pasta”
        //     And is still on the original recipe save or discard page
        // When the user clicks on the “Reload” button
        // Then the app reloads the page, generating a new “basil and pasta” recipe using the same ingredients
        // When the user clicks “save”
        // Then  it is the new recipe that is saved to database
        Recipe newGeneratedRecipe = new Recipe("Some new recipe of basil and pasta", "some description");
        generateRecipe.setMockRecipe(newGeneratedRecipe);

        GivenTheUserIsOnNewRecipeConfirmPage();
        WhenTheUserClicksOnTheReloadButton();
        ThenTheNewRecipeWillDisappearOnNewRecipeConfirmPage();
    }

    private void
    GivenTheUserIsOnNewRecipeConfirmPage()
    {
        controller.transitionToNewRecipeConfirmPage(recipeStub);
    }

    private void
    WhenTheUserClicksOnTheReloadButton()
    {
        Runnable regenerateCallback = (Runnable) viewTransitioner.params[3];
        regenerateCallback.run();
    }

    private void
    ThenTheNewRecipeWillDisappearOnNewRecipeConfirmPage()
    {
        assertEquals(NewRecipeConfirmPage.class, viewTransitioner.currentPageClass);
        Recipe newRecipe = (Recipe) viewTransitioner.params[0];
        assertEquals("Some new recipe of basil and pasta", newRecipe.getTitle());
    }

    @Test
    public void
    scenario_6_1_generationFailed()
    {
        // Scenario 6.1: Generation failed
        // Given the user has said “breakfast” and “eggs” on the respective recording pages
        //  And there happens to be a OpenAI error
        // When the server returns an error
        // Then error message page pops up
        // When the user presses “Back to home page”
        // Then the app goes to home page, which remains unchanged from its original base state

        GivenTheServerIsExpectedToBeFailedByThirdPartyAPI();
        GivenTheUserIsRequestingToGenerateRecipe();
        WhenTheServerReturnsAnError();
        ThenErrorPageIsExpected();
    }

    private void
    GivenTheServerIsExpectedToBeFailedByThirdPartyAPI()
    {
        generateRecipe.mustFailWith("Some OpenAI error. Your token is expired.");
    }

    private void
    GivenTheUserIsRequestingToGenerateRecipe()
    {
        controller.requestTranscription();
    }

    private void
    WhenTheServerReturnsAnError()
    {
        // handled in given
    }

    private void
    ThenErrorPageIsExpected()
    {
        assertEquals(ErrorPage.class, viewTransitioner.currentPageClass);
        // message on the error page
        assertEquals("Generating new recipe is failed: Some OpenAI error. Your token is expired.", viewTransitioner.params[0]);
        // button label
        assertEquals("Go back to home", viewTransitioner.params[2]);
    }

    @Test
    public void
    scenario_6_2_connectionFailed()
    {
        // Scenario 6.2: Connection failed
        // Given there is no connection between client and server
        // When the user turns the app on
        // Then error message page pops up
        // When the user presses “Back to home page”
        // Then error message page pops up again

        GivenThereIsNoConnection();
        WhenErrorOccurs();
        ThenErrorMessagePageShouldAppear();
    }

    private void
    GivenThereIsNoConnection()
    {
        // assume no connection
    }

    private void
    WhenErrorOccurs()
    {
        controller.onRecipeModelError(new Exception("Connection Refused"));
    }

    private void
    ThenErrorMessagePageShouldAppear()
    {
        assertEquals(ErrorPage.class, viewTransitioner.currentPageClass);
        // message on the error page
        assertEquals("Server error occured: Connection Refused", viewTransitioner.params[0]);
        // button label
        assertEquals("Go to login page", viewTransitioner.params[2]);
    }

    @Test
    public void
    scenario_9_1_userGeneratesARecipe()
    {
        // Scenario 9.1 User generates a recipe
        // Given the user has just inputted their ingredients list "banana, flour, eggs"
        //     And the user already inputted "breakfast" for their meal type
        // When the user hits 'Stop Recording'
        // Then after generation completes a recipe generation page with an AI-generated image of "banana pancakes" should appear
        //     And there should also be a title labeled "Banana Pancakes" along with the appropriate ingredients list and instructions
        GivenTheUserIsGeneratedNewRecipe();
        WhenTheUserHitsStopRecording();
        ThenImageUrlShouldBeProvidedInTheRecipe();
    }

    private void
    GivenTheUserIsGeneratedNewRecipe() {
        // assume user provided appropriate input
        controller.requestTranscription();
    }

    private void
    WhenTheUserHitsStopRecording() {
        // assume user clicks stop recording button
    }

    private void
    ThenImageUrlShouldBeProvidedInTheRecipe() {
        assertEquals(NewRecipeConfirmPage.class, viewTransitioner.currentPageClass);
        Recipe recipe = (Recipe) viewTransitioner.params[0];
        assertNotNull(recipe.getImageUrl());
    }
}
