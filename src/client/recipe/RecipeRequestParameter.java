package client.recipe;

import java.io.File;

// Used to maintain the two user input types as an object
public class RecipeRequestParameter {
    private File mealType;
    private File ingredients;

    public RecipeRequestParameter(File mealTypeFile, File ingredientsFile)
    {
        this.mealType = mealTypeFile;
        this.ingredients = ingredientsFile;
    }

    public File
    getMealTypeFile()
    {
        return mealType;
    }

    public File
    getIngredientsFile()
    {
        return ingredients;
    }
}
