package server.recipe;

import org.json.JSONPropertyName;

public class Recipe {
    private String title;
    private String description;
    private String ingredients;
    private String mealType;

    public Recipe(String title, String description)
    {
        this(title, description, null, null);
    }

    public Recipe(String title, String description, String ingredients, String mealType)
    {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.mealType = mealType;
    }

    public String
    getTitle()
    {
        return title;
    }

    public String
    getDescription()
    {
        return description;
    }

    public String
    getIngredients()
    {
        return ingredients;
    }

    @JSONPropertyName("meal_type")
    public String
    getMealType()
    {
        return mealType;
    }

    public void
    setDescription(String description)
    {
        this.description = description;
    }
}
