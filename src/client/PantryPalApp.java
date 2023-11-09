package client;

import client.recipe.IGenerateRecipe;
import client.audio.AudioRecorder;
import client.audio.IAudioRecorder;
import client.recipe.ServerRecipeGenerator;
import client.utils.transitions.CompositeTransitioner;
import client.utils.transitions.PrintConsoleTransitioner;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * PantryPal JavaFX Application
 */
public class PantryPalApp extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override

    public void
    start(Stage primaryStage) throws Exception
    {
        CompositeTransitioner transitioner = new CompositeTransitioner();

        IGenerateRecipe generateRecipe = new ServerRecipeGenerator();
        IAudioRecorder audioRecorder = new AudioRecorder();

        Controller controller = new Controller(transitioner, generateRecipe, audioRecorder);

        transitioner
            .add(Routes.getRoutes(primaryStage, controller))
            .add(new PrintConsoleTransitioner());

        controller.start();
    }
}
