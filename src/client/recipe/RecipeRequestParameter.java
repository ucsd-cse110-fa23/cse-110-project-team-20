package client.recipe;

import java.io.File;

public class RecipeRequestParameter {
    private File mealType;
    private File ingredients;

    public RecipeRequestParameter(File mealTypeFile, File ingredientsFile)
    {
        this.mealType = mealTypeFile;
        this.ingredients = ingredientsFile;
    }

    public File getMealTypeFile() {
        return mealType;
    }

    public File getIngredientsFile() {
        return ingredients;
    }
}
