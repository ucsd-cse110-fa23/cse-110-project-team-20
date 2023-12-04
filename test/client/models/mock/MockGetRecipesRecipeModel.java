package client.models.mock;

import java.util.List;
import java.util.ArrayList;

import client.Recipe;
import feature.mock.MockRecipeModel;

/**
 * Mock for clearing cache
 *
 * We cached getRecipes only so we only provides the mock count for getRecipes method
 */
public class MockGetRecipesRecipeModel extends MockRecipeModel {

  private int called = 0;

  public int getNumberOfGetRecipesCalled() {
    return called;
  }

  @Override
  public List<Recipe> getRecipes() {
    called += 1;
    return super.getRecipes();
  }
}
