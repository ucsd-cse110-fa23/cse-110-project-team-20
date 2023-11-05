package client;

import client.components.AnimatedLoadingBar;
import client.components.RecordingPage;
import java.util.ArrayList;
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
    private Scene recordMealType, recordIngredients, loading;

    private String mealTypeTranscription, ingredientsTranscription;

    private boolean recording;

    public Controller(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.model = new Model();
        this.recording = false;
        this.state = State.RECORDING_MEAL_TYPE;

        RecordingPage mealTypePage = new RecordingPage(
            "What kind of meal do you want?\nLunch, Dinner, Snack etc.", MEAL_TYPE_AUDIO);
        mealTypePage.setButtonCallback(() -> mealTypeRecordingButtonClicked());
        this.recordMealType = new Scene(mealTypePage);

        RecordingPage ingredientsPage =
            new RecordingPage("What ingredients do you have?", INGREDIENTS_AUDIO);
        ingredientsPage.setButtonCallback(() -> ingredientsRecordingButtonClicked());
        this.recordIngredients = new Scene(ingredientsPage);

        AnimatedLoadingBar loadingPage = new AnimatedLoadingBar();
        loadingPage.setLoadingText("Finding the perfect recipe...");
        this.loading = new Scene(loadingPage);

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(recordMealType);
        // Make window non-resizable
        primaryStage.setResizable(false);
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
    requestMealTypeTranscription()
    {
        // TODO: send API request through model
        // this.model.requestTranscription(audioFilePath);
    }

    public void
    requestIngredientsTranscription()
    {
        // TODO: send API request through model
        // this.model.requestTranscription(audioFilePath);
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
        recording = !recording;
        if (!recording) {
            // Called after second click
            this.requestIngredientsTranscription();
            this.transitionToLoadingScene();
        }
    }

    public void
    mealTypeRecordingButtonClicked()
    {
        recording = !recording;
        if (!recording) {
            // Called after second click
            this.requestMealTypeTranscription();
            this.transitionToIngredientsScene();
        }
    }

    // TODO: Add methods for making requests through Model, and add button actions when adding the
    // scenes
}
