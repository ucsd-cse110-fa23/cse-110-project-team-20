package client.recipe;

import org.json.JSONObject;

import client.Recipe;

public class GenerateRecipeHelper {
    public static Recipe convertJsonResponseToRecipe(String response) {
        JSONObject recipeJson = new JSONObject(response.trim());

        String title = recipeJson.has("title") ? recipeJson.getString("title") : null;
        String mealType = recipeJson.has("meal_type") ? recipeJson.getString("meal_type") : null;
        String ingredients = recipeJson.has("ingredients") ? recipeJson.getString("ingredients") : null;
        String description = recipeJson.has("description") ? recipeJson.getString("description") : null;

        return new Recipe(title, description, ingredients, mealType);
    }
}
