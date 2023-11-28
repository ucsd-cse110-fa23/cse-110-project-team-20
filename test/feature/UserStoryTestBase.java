package feature;

import org.junit.jupiter.api.BeforeEach;

import client.Controller;
import client.Recipe;
import client.account.IAccountManager;
import client.account.IAccountSession;
import feature.mock.MockAudioRecorder;
import feature.mock.MockGenerateRecipe;
import feature.mock.MockRecipeModel;
import feature.mock.MockViewTransitioner;

abstract public class UserStoryTestBase {
  Controller controller;
  MockViewTransitioner viewTransitioner;
  MockGenerateRecipe generateRecipe;
  MockAudioRecorder audioRecorder;
  MockRecipeModel recipeModel;
  Recipe recipeStub;

  @BeforeEach
  public void setupController() {
    viewTransitioner = new MockViewTransitioner();
    generateRecipe = new MockGenerateRecipe();
    audioRecorder = new MockAudioRecorder();
    recipeModel = new MockRecipeModel();

    // @TODO will be replaced as mock implementation when we do testing task for MS2-US1
    IAccountManager mockAccountManager = new IAccountManager() {
      @Override
      public String loginOrCreateAccount(String username, String password) {
        return "some mock token";
      }
    };
    IAccountSession mockAccountSession = new IAccountSession() {
      @Override
      public void setToken(String token) {
      }
      @Override
      public String getToken() {
        return "some token";
      }
    };

    recipeStub = new Recipe(
        "Banana Pancake",
        "Some generated recipe for banana pancake.",
        "banana, flour, eggs",
        "dinner");
    generateRecipe.setMockRecipe(recipeStub);

    controller = new Controller(viewTransitioner, generateRecipe, audioRecorder, recipeModel, mockAccountManager, mockAccountSession);
    controller.start();
  }
}
