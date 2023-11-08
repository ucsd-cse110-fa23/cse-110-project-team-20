package client;

import client.recipe.IGenerateRecipe;
import client.audio.AudioRecorder;
import client.audio.IAudioRecorder;
import client.helpers.AppInspection;
import client.recipe.LocalRecipeGenerator;
import client.recipe.ServerRecipeGenerator;
import client.utils.transitions.CompositeTransitioner;
import client.utils.transitions.PrintConsoleTransitioner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

/**
 * PantryPals JavaFX Application
 */
public class PantryPalsApp extends Application {
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

        CompositeTransitioner transitioner = new CompositeTransitioner();
        IAudioRecorder audioRecorder = new AudioRecorder();
        Controller controller = new Controller(transitioner, generateRecipe, audioRecorder);

        transitioner
            .add(Routes.getRoutes(primaryStage, controller))
            .add(new PrintConsoleTransitioner());

        controller.start();

        // debug purposes
        handleInspect(primaryStage, controller);
    }

    private static PantryPalsApp _app;
    private static Controller _controller;
    private static Stage _primeStage;
    private static AppInspection inspector;

    private void handleInspect(Stage primaryStage, Controller controller) throws Exception {
        _app = this;
        _controller = controller;
        _primeStage = primaryStage;
        if (inspector != null) {
            inspector.inspect(this, primaryStage, controller);
        }
    }

    public static Thread inspect(AppInspection thisInspector) {
        return inspect(thisInspector, null);
    }

    public static Thread inspect(AppInspection thisInspector, String[] args) {
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
