package feature;

import org.junit.jupiter.api.BeforeEach;

import client.Controller;
import client.Recipe;
import feature.mock.MockAudioRecorder;
import feature.mock.MockGenerateRecipe;
import feature.mock.MockTransitioner;

abstract public class UserStoryTestBase {
  Controller controller;
  MockTransitioner transitioner;
  MockGenerateRecipe generateRecipe;
  MockAudioRecorder audioRecorder;
  Recipe recipeStub;

  @BeforeEach
  public void setupController() {
    transitioner = new MockTransitioner();
    generateRecipe = new MockGenerateRecipe();
    audioRecorder = new MockAudioRecorder();

    recipeStub = new Recipe(
        "Banana Pancake",
        "Some generated recipe for banana pancake.",
        "banana, flour, eggs",
        "dinner");
    generateRecipe.setMockRecipe(recipeStub);

    controller = new Controller(transitioner, generateRecipe, audioRecorder);
    controller.start();
  }
}
