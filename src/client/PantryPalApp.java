package client;

import client.helpers.IAppInspection;
import client.recipe.IGenerateRecipe;
import client.recipe.LocalRecipeGenerator;
import client.recipe.ServerRecipeGenerator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * PantryPals JavaFX Application
 */
public class PantryPalApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void
    start(Stage primaryStage) throws Exception
    {
        IGenerateRecipe generateRecipe;

        if (inspector != null) {
            generateRecipe = new LocalRecipeGenerator();
        } else {
            generateRecipe = new ServerRecipeGenerator();
        }

        Controller controller = new Controller(primaryStage, generateRecipe);
        controller.start();

        // debug purposes
        handleInspect(primaryStage, controller);
    }

    private static PantryPalApp _app;
    private static Controller _controller;
    private static Stage _primeStage;
    private static IAppInspection inspector;

    private void handleInspect(Stage primaryStage, Controller controller) throws Exception {
        _app = this;
        _controller = controller;
        _primeStage = primaryStage;
        if (inspector != null) {
            inspector.inspect(this, primaryStage, controller);
        }
    }

    public static Thread inspect(IAppInspection thisInspector) {
        return inspect(thisInspector, null);
    }

    public static Thread inspect(IAppInspection thisInspector, String[] args) {
        inspector = thisInspector;
        Thread appRunner = new Thread(() -> {
            if (_app == null) {
                launch(args);
            } else {
                Platform.runLater(() -> {
                    try {
                        inspector.inspect(_app, _primeStage, _controller);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });
        return appRunner;
    }

}
