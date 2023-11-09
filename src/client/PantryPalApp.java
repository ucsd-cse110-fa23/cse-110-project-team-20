package client;

import client.recipe.IRecipeGenerator;
import client.audio.AudioRecorder;
import client.audio.IAudioRecorder;
import client.models.ServerRecipeModel;
import client.models.IRecipeModel;
import client.recipe.ServerRecipeGenerator;
import client.utils.transitions.CompositeViewTransitioner;
import client.utils.transitions.PrintConsoleViewTransitioner;
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
        CompositeViewTransitioner transitioner = new CompositeViewTransitioner();

        IRecipeGenerator recipeGenerator = new ServerRecipeGenerator();
        IAudioRecorder audioRecorder = new AudioRecorder();
        IRecipeModel recipeModel = new ServerRecipeModel();

        Controller controller = new Controller(transitioner, recipeGenerator, audioRecorder, recipeModel);

        transitioner
            .add(Routes.getRoutes(primaryStage, controller))
            .add(new PrintConsoleViewTransitioner());

        controller.start();
    }
}
