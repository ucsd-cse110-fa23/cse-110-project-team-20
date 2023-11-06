package client;

import client.recipe.GenerateRecipe;
import client.helpers.AppInspection;
import client.recipe.LocalRecipeGenerator;
import client.recipe.ServerRecipeGenerator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * PantryPals JavaFX Application
 */
public class PantryPalsApp extends Application {
    private static AppInspection inspector;

    public static void
    main(String[] args)
    {
        launch(args);
    }

    public static void
    inspect(AppInspection thisInspector)
    {
        inspect(thisInspector, null);
    }

    public static void
    inspect(AppInspection thisInspector, String[] args)
    {
        inspector = thisInspector;
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

        if (inspector != null) {
            inspector.inspect(this, primaryStage, controller);
            Platform.exit();
        }
    }
}
