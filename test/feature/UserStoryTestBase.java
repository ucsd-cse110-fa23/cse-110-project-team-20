package feature;

import client.Controller;
import client.Recipe;
import client.account.IAccountManager;
import client.account.IAccountSession;
import feature.mock.MockAccountManager;
import feature.mock.MockAccountSession;
import feature.mock.MockAudioRecorder;
import feature.mock.MockGenerateRecipe;
import feature.mock.MockRecipeModel;
import feature.mock.MockViewTransitioner;
import org.junit.jupiter.api.BeforeEach;

abstract public class UserStoryTestBase {
    Controller controller;
    MockViewTransitioner viewTransitioner;
    MockGenerateRecipe generateRecipe;
    MockAudioRecorder audioRecorder;
    MockRecipeModel recipeModel;
    Recipe recipeStub;
    IAccountManager accountManager;
    IAccountSession accountSession;

    @BeforeEach
    public void
    setupController()
    {
        viewTransitioner = new MockViewTransitioner();
        generateRecipe = new MockGenerateRecipe();
        audioRecorder = new MockAudioRecorder();
        recipeModel = new MockRecipeModel();
        accountManager = new MockAccountManager();
        accountSession = new MockAccountSession();

        recipeStub = new Recipe("Banana Pancake", "Some generated recipe for banana pancake.",
            "banana, flour, eggs", "dinner", "data:image/gif;base64,R0lGODlhAQABAAAAACw=");
        generateRecipe.setMockRecipe(recipeStub);

        controller = new Controller(viewTransitioner, generateRecipe, audioRecorder, recipeModel,
            accountManager, accountSession);
        controller.start();
    }
}
