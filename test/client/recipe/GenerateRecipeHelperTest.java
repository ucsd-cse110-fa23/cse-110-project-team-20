package client.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import client.Recipe;

public class GenerateRecipeHelperTest {
    @Test
    public void convertJsonResponseToRecipe() {
        Recipe recipe = GenerateRecipeHelper.convertJsonResponseToRecipe("{\"title\": \"1\", \"meal_type\": \"2\", \"ingredients\": \"3\", \"description\": \"4\"}");
        assertEquals("1", recipe.getTitle());
        assertEquals("2", recipe.getMealType());
        assertEquals("3", recipe.getIngredients());
        assertEquals("4", recipe.getDescription());
    }
}
