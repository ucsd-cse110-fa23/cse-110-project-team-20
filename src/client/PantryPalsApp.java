package client;

import client.recipe.GenerateRecipe;
import client.recipe.LocalRecipeGenerator;
import client.recipe.ServerRecipeGenerator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * PantryPals JavaFX Application
 */
public class PantryPalsApp extends Application {
    Controller controller;

    public static void
    main(String[] args)
    {
        launch(args);
    }

    @Override
    public void
    start(Stage primaryStage) throws Exception
    {
        // LocalRecipeGenerator generateRecipe = new LocalRecipeGenerator();
        // generateRecipe.setAlwaysFail(true); // if we need to test failing

        GenerateRecipe generateRecipe = new ServerRecipeGenerator();

        Controller controller = new Controller(primaryStage, generateRecipe);
        controller.start();
    }
}
