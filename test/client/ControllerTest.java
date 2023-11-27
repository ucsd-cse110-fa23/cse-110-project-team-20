package client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import client.account.IAccountManager;
import feature.mock.MockAudioRecorder;
import feature.mock.MockGenerateRecipe;
import feature.mock.MockRecipeModel;
import feature.mock.MockViewTransitioner;

public class ControllerTest {
  @Test
  public void deleteRecipe() {
    MockViewTransitioner viewTransitioner = new MockViewTransitioner();
    MockGenerateRecipe generateRecipe = new MockGenerateRecipe();
    MockAudioRecorder audioRecorder = new MockAudioRecorder();
    MockRecipeModel recipeModel = new MockRecipeModel();

    // @TODO will be replaced as mock implementation when we do testing task for MS2-US1
    IAccountManager mockAccountManager = new IAccountManager() {
      @Override
      public String loginOrCreateAccount(String username, String password) {
        return "some mock token";
      }
    };

    recipeModel.recipes.add(new Recipe("Chicken with carrot", "Chicken with carrot recipe instruction."));
    recipeModel.recipes.add(new Recipe("Chicken with broccoli", "Chicken with broccoli recipe instruction."));
    recipeModel.recipes.add(new Recipe("Chicken with chocolete", "Chicken with chocolete recipe instruction."));

    Controller controller = new Controller(viewTransitioner, generateRecipe, audioRecorder, recipeModel, mockAccountManager);

    controller.deleteRecipeClicked(1);

    assertEquals(2, recipeModel.recipes.size());
    assertEquals(recipeModel.recipes.get(0).getTitle(), "Chicken with carrot");
    assertEquals(recipeModel.recipes.get(1).getTitle(), "Chicken with chocolete");
  }
}
