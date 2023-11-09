package feature;

import org.junit.jupiter.api.BeforeEach;

import client.Controller;
import client.Recipe;
import feature.mock.MockAudioRecorder;
import feature.mock.MockGenerateRecipe;
import feature.mock.MockViewTransitioner;

abstract public class UserStoryTestBase {
  Controller controller;
  MockViewTransitioner viewTransitioner;
  MockGenerateRecipe generateRecipe;
  MockAudioRecorder audioRecorder;
  Recipe recipeStub;

  @BeforeEach
  public void setupController() {
    viewTransitioner = new MockViewTransitioner();
    generateRecipe = new MockGenerateRecipe();
    audioRecorder = new MockAudioRecorder();

    recipeStub = new Recipe(
        "Banana Pancake",
        "Some generated recipe for banana pancake.",
        "banana, flour, eggs",
        "dinner");
    generateRecipe.setMockRecipe(recipeStub);

    controller = new Controller(viewTransitioner, generateRecipe, audioRecorder);
    controller.start();
  }
}
