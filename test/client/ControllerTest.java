package client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import client.account.IAccountManager;
import client.account.IAccountSession;
import client.account.ICredentialManager;
import feature.mock.MockAudioRecorder;
import feature.mock.MockCredentialManager;
import feature.mock.MockGenerateRecipe;
import feature.mock.MockRecipeModel;
import feature.mock.MockViewTransitioner;
import org.junit.jupiter.api.Test;

public class ControllerTest {
    @Test
    public void
    deleteRecipe()
    {
        MockViewTransitioner viewTransitioner = new MockViewTransitioner();
        MockGenerateRecipe generateRecipe = new MockGenerateRecipe();
        MockAudioRecorder audioRecorder = new MockAudioRecorder();
        MockRecipeModel recipeModel = new MockRecipeModel();

        // @TODO will be replaced as mock implementation when we do testing task for MS2-US1
        IAccountManager mockAccountManager = new IAccountManager() {
            @Override public String loginOrCreateAccount(String username, String password)
            {
                return "some mock token";
            }
        };
        IAccountSession mockSession = new IAccountSession() {
            @Override public void setToken(String token) {}

            @Override public String getToken()
            {
                return "some token";
            }
        };

        ICredentialManager credentialManager = new MockCredentialManager();

        recipeModel.recipes.add(
            new Recipe("Chicken with carrot", "Chicken with carrot recipe instruction."));
        recipeModel.recipes.add(
            new Recipe("Chicken with broccoli", "Chicken with broccoli recipe instruction."));
        recipeModel.recipes.add(
            new Recipe("Chicken with chocolete", "Chicken with chocolete recipe instruction."));

        Controller controller = new Controller(viewTransitioner, generateRecipe, audioRecorder,
            recipeModel, mockAccountManager, mockSession, credentialManager);

        controller.deleteRecipeClicked(1);

        assertEquals(2, recipeModel.recipes.size());
        assertEquals(recipeModel.recipes.get(0).getTitle(), "Chicken with carrot");
        assertEquals(recipeModel.recipes.get(1).getTitle(), "Chicken with chocolete");
    }
}
