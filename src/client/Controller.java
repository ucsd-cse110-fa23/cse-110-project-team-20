package client;

import client.components.AnimatedLoadingBar;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecipeDetails;
import client.components.RecordingPage;
import client.recipe.GenerateRecipe;
import client.recipe.RecipeRequestParameter;
import client.utils.transitions.ITransitioner;

import java.io.File;
import java.util.ArrayList;

public class Controller {
    private final String MEAL_TYPE_AUDIO = "meal_type.wav";
    private final String INGREDIENTS_AUDIO = "ingredients.wav";

    // @TODO need to replace with model
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();

    private GenerateRecipe generateRecipe;
    private ITransitioner transitioner;

    public Controller(ITransitioner transitioner, GenerateRecipe generateRecipe)
    {
        this.transitioner = transitioner;
        this.generateRecipe = generateRecipe;
    }

    public void
    start()
    {
        transitionToHomeScene();
    }

    public void
    transitionToMealTypeScene()
    {
        Runnable buttonCallback = () -> mealTypeRecordingCompleted();

        transitioner.transitionTo(
            RecordingPage.class,
            "What kind of meal do you want?\nLunch, Dinner, Snack etc.",
            MEAL_TYPE_AUDIO,
            buttonCallback
        );
    }

    public void
    transitionToIngredientsScene()
    {
        Runnable buttonCallback = () -> ingredientsRecordingCompleted();

        transitioner.transitionTo(
            RecordingPage.class,
            "What ingredients do you have?",
            INGREDIENTS_AUDIO,
            buttonCallback
        );
    }

    public void
    transitionToHomeScene()
    {
        // @TODO need to get recipes from the model
        Runnable createButtonCallback = () -> createRecipeButtonClicked();
        transitioner.transitionTo(HomePage.class, recipes, createButtonCallback);
    }

    public void
    requestTranscription()
    {
        File mealTypeFile = new File(MEAL_TYPE_AUDIO);
        File ingredientsFile = new File(INGREDIENTS_AUDIO);

        if (mealTypeFile != null && ingredientsFile != null) {
            RecipeRequestParameter params =
                new RecipeRequestParameter(mealTypeFile, ingredientsFile);

            generateRecipe.requestGeneratingRecipe(params,
                (recipe) -> {
                    transitionToNewRecipeConfirmPage(recipe);
                },
                (errorMessage) -> {
                    transitionToHomeScene();
                });
        }
    }

    public void
    openRecipeDetails(Recipe recipe)
    {
        Runnable cancelCallback = () -> backToHomeScene();
        transitioner.transitionTo(RecipeDetails.class, recipe, cancelCallback);
    }

    public void
    backToHomeScene()
    {
        transitionToHomeScene();
    }

    public void
    transitionToLoadingScene()
    {
        transitioner.transitionTo(AnimatedLoadingBar.class, "Finding the perfect recipe...");
    }

    public void
    ingredientsRecordingCompleted()
    {
        this.requestTranscription();
        this.transitionToLoadingScene();
    }

    public void
    mealTypeRecordingCompleted()
    {
        this.transitionToIngredientsScene();
    }

    public void
    createRecipeButtonClicked()
    {
        this.transitionToMealTypeScene();
    }

    public void
    transitionToNewRecipeConfirmPage(Recipe recipe)
    {
        Runnable saveCallback = () -> saveRecipeClicked(recipe);
        Runnable discardCallback = () -> discardGeneratedRecipeClicked();
        transitioner.transitionTo(NewRecipeConfirmPage.class, recipe, saveCallback, discardCallback);
    }

    public void
    discardGeneratedRecipeClicked()
    {
        this.transitionToHomeScene();
    }

    public void
    saveRecipeClicked(Recipe recipe)
    {
        // @TODO need to move into model
        recipes.add(recipe);
        this.transitionToHomeScene();
    }
}
