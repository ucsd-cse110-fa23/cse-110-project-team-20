package client;

import org.json.JSONObject;
import org.json.JSONPropertyName;

// Handles the json formatting of a recipe for HTTP server use
public class Recipe {
    private String title;
    private String description;
    private String ingredients;
    private String mealType;

    public Recipe (String title, String description) {
        this(title, description, null, null);
    }

    public Recipe (String title, String description, String ingredients, String mealType) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.mealType = mealType;
    }

    public static Recipe fromJson(String jsonResponse) {
        JSONObject recipeJson = new JSONObject(jsonResponse.trim());

        String title = recipeJson.has("title") ? recipeJson.getString("title") : null;
        String mealType = recipeJson.has("meal_type") ? recipeJson.getString("meal_type") : null;
        String ingredients = recipeJson.has("ingredients") ? recipeJson.getString("ingredients") : null;
        String description = recipeJson.has("description") ? recipeJson.getString("description") : null;

        return new Recipe(title, description, ingredients, mealType);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIngredients() {
        return ingredients;
    }

    @JSONPropertyName("meal_type")
    public String getMealType() {
        return mealType;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
