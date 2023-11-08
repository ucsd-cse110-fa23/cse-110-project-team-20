package client;

import client.components.AnimatedLoadingBar;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecipeDetailsPage;
import client.recipe.IGenerateRecipe;
import client.recipe.RecipeRequestParameter;
import java.io.File;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Controller {
    private enum State {
        HOME,
        RECORDING_MEAL_TYPE,
        RECORDING_INGREDIENTS,
        LOADING_RECIPE,
        EXPANDED,
        EDIT,
        DELETE,
        NEW
    }
    private final String MEAL_TYPE_AUDIO = "meal_type.wav";
    private final String INGREDIENTS_AUDIO = "ingredients.wav";

    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    private State state;
    private Stage primaryStage;
    private Model model;
    private Scene recordMealType, recordIngredients, loading, home;
    private RecordingPage mealTypePage, ingredientsPage;
    private HomePage homePage;
    private Scene newRecipeConfirm, details;
    private NewRecipeConfirmPage newRecipeConfirmPage;
    private RecipeDetailsPage newRecipeDetails;

    private AnimatedLoadingBar loadingPage;
    private static final int WIDTH = 500, HEIGHT = 500;

    private String mealTypeTranscription, ingredientsTranscription;

    private boolean recording;

    private IGenerateRecipe generateRecipe;

    public Controller(Stage primaryStage, IGenerateRecipe generateRecipe)
    {
        this.primaryStage = primaryStage;
        this.generateRecipe = generateRecipe;

        this.model = new Model();
        this.recording = false;
        this.state = State.HOME;

        mealTypePage = new RecordingPage(
            "What kind of meal do you want?\nLunch, Dinner, Snack etc.", MEAL_TYPE_AUDIO);

        mealTypePage.setButtonCallback(() -> mealTypeRecordingButtonClicked());
        this.recordMealType = new Scene(mealTypePage, WIDTH, HEIGHT);

        ingredientsPage = new RecordingPage("What ingredients do you have?", INGREDIENTS_AUDIO);
        ingredientsPage.setButtonCallback(() -> ingredientsRecordingButtonClicked());
        this.recordIngredients = new Scene(ingredientsPage, WIDTH, HEIGHT);

        loadingPage = new AnimatedLoadingBar();
        loadingPage.setLoadingText("Finding the perfect recipe...");
        this.loading = new Scene(loadingPage, WIDTH, HEIGHT);

        homePage = new HomePage(new ArrayList(), this);
        homePage.setCreateButtonCallback(() -> createRecipeButtonClicked());
        this.home = new Scene(homePage, WIDTH, HEIGHT);

        this.detailsPage = new RecipeDetails();
        this.details = new Scene(detailsPage, WIDTH, HEIGHT);

        detailsPage.setCancelCallback(() -> backToHomeScene());

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(this.home);
        // Make window non-resizable
        primaryStage.setResizable(false);
    }

    public void
    start()
    {
        // Show the app
        primaryStage.show();
    }

    public void
    transitionToMealTypeScene()
    {
        this.state = State.RECORDING_MEAL_TYPE;
        primaryStage.setScene(recordMealType);
    }

    public void
    transitionToIngredientsScene()
    {
        this.state = State.RECORDING_INGREDIENTS;
        primaryStage.setScene(recordIngredients);
    }

    public void
    transitionToHomeScene()
    {
        this.state = State.HOME;
        primaryStage.setScene(home);
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
                (recipe)
                    -> { Platform.runLater(() -> { transitionToNewRecipeConfirmPage(recipe); }); },
                (errorMessage)
                    -> {

                    });
        }
    }
// =======
//     public void 
//     openRecipeDetails(Recipe recipe) {
//         RecipeDetailsPage detailsPage = new RecipeDetailsPage(recipe);
//         Scene detailsScene = new Scene(detailsPage, WIDTH, HEIGHT);
        
//         detailsPage.setCancelCallback(() -> backToHomeScene());

    public void
    openRecipeDetails(Recipe recipe)
    {
        this.detailsPage.displayRecipe(recipe);
        primaryStage.setScene(this.details);
    }

    public void
    backToHomeScene()
    {
        primaryStage.setScene(home);
    }

    public void
    transitionToLoadingScene()
    {
        this.state = State.LOADING_RECIPE;
        primaryStage.setScene(loading);
    }

    public void
    ingredientsRecordingButtonClicked()
    {
        if (recording) {
            // Called after second click
            this.ingredientsPage.stopRecording();
            this.requestTranscription();
            this.transitionToLoadingScene();
        } else {
            this.ingredientsPage.startRecording();
        }
        recording = !recording;
    }

    public void
    mealTypeRecordingButtonClicked()
    {
        if (recording) {
            // Called after second click
            this.mealTypePage.stopRecording();
            this.transitionToIngredientsScene();
        } else {
            this.mealTypePage.startRecording();
        }
        recording = !recording;
    }

    public void
    createRecipeButtonClicked()
    {
        this.transitionToMealTypeScene();
    }

    public void
    transitionToNewRecipeConfirmPage(Recipe recipe)
    {
        newRecipeConfirmPage = new NewRecipeConfirmPage(recipe);
        newRecipeConfirm = new Scene(newRecipeConfirmPage);
        primaryStage.setScene(newRecipeConfirm);
        newRecipeConfirmPage.setCancelCallback(() -> discardGeneratedRecipeClicked());
        newRecipeConfirmPage.setSaveCallback(() -> saveRecipeClicked(recipe));
    }

    public void
    discardGeneratedRecipeClicked()
    {
        this.transitionToHomeScene();
    }

    public void
    saveRecipeClicked(Recipe recipe)
    {
        recipes.add(recipe);
        homePage.updateRecipeList(recipe);
        this.transitionToHomeScene();
    }

    // public void
    // detailsButtonClicked(Recipe recipe) {
    //     RecipeDetailsPage detailsPage = new RecipeDetailsPage(recipe);
    //     Scene detailsScene = new Scene(detailsPage);
    //     primaryStage.setScene(detailsScene);
    // }

    // TODO: Add methods for making requests through Model, and add button actions when adding the
    // scenes
}
