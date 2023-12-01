package feature;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.components.ErrorMessage;
import client.components.HomePage;
import client.utils.runnables.RunnableForLogin;
import feature.mock.MockAccountManager;
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
}
