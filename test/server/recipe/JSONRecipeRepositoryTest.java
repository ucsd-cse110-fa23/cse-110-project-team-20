package server.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.Recipe;

public class JSONRecipeRepositoryTest {

  private String dbPath;

  @BeforeEach
  public void setupDatabase() {
    dbPath = "test/resources/work_database.json";

    // ref: https://www.baeldung.com/java-copy-file
    Path copied = Paths.get(dbPath);
    Path originalPath = new File("test/resources/database.json").toPath();
    try {
      Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterEach
  public void cleanDatabase() {
    new File(dbPath).delete();
  }

  @Test
  public void createDatabase() {
    new JSONRecipeRepository("test/resources/temp.json");
    File file = new File("test/resources/temp.json");
    assertTrue(file.exists());
    // clean up
    file.delete();
  }
  @Test
  public void getRecipes() {
    IRecipeRepository repo = new JSONRecipeRepository(dbPath);
    List<Recipe> recipes = repo.getRecipes();

    assertEquals(3, recipes.size());
    assertEquals("title_C", recipes.get(0).getTitle());
    assertEquals("title_B", recipes.get(1).getTitle());
    assertEquals("title_A", recipes.get(2).getTitle());
  }

  @Test
  public void getRecipe() {
    IRecipeRepository repo = new JSONRecipeRepository(dbPath);
    Recipe recipe = repo.getRecipe(1);

    assertEquals("title_B", recipe.getTitle());
    assertEquals("description_B", recipe.getDescription());
    assertEquals("ingredients_B", recipe.getIngredients());
    assertEquals("meal_type_B", recipe.getMealType());
  }

  @Test
  public void getRecipeNonExist() {
    IRecipeRepository repo = new JSONRecipeRepository(dbPath);
    Recipe recipe = repo.getRecipe(1111);
    assertNull(recipe);
  }

  @Test
  public void createRecipe() {
    IRecipeRepository repo = new JSONRecipeRepository(dbPath);
    int prevCount = repo.getRecipes().size();
    repo.createRecipe(new Recipe("new title", "new description", "new ingredients", "new meal type"));
    Recipe recipe = repo.getRecipe(0);
    int afterCount = repo.getRecipes().size();

    assertEquals("new title", recipe.getTitle());
    assertEquals("new description", recipe.getDescription());
    assertEquals("new ingredients", recipe.getIngredients());
    assertEquals("new meal type", recipe.getMealType());
    assertEquals(prevCount + 1, afterCount);
  }

  @Test
  public void updateRecipe() {
    IRecipeRepository repo = new JSONRecipeRepository(dbPath);
    repo.updateRecipe(1, new Recipe("new title_B", "new description_B"));
    Recipe recipe = repo.getRecipe(1);

    assertEquals("new title_B", recipe.getTitle());
    assertEquals("new description_B", recipe.getDescription());
    assertEquals("ingredients_B", recipe.getIngredients());
    assertEquals("meal_type_B", recipe.getMealType());
  }
}
