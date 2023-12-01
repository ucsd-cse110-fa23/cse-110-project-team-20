package client;

import client.recipe.IRecipeGenerator;
import client.account.IAccountManager;
import client.account.IAccountSession;
import client.account.ServerAccountManager;
import client.account.SimpleAccountSession;
import client.audio.AudioRecorder;
import client.audio.IAudioRecorder;
import client.models.ServerRecipeModel;
import client.models.CachedRecipeModel;
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

        IAccountSession accountSession = new SimpleAccountSession();
        IRecipeGenerator recipeGenerator = new ServerRecipeGenerator(accountSession);
        IAudioRecorder audioRecorder = new AudioRecorder();
        IRecipeModel recipeModel = new CachedRecipeModel(new ServerRecipeModel(accountSession));
        IAccountManager accountManager = new ServerAccountManager();

        Controller controller = new Controller(transitioner, recipeGenerator, audioRecorder, recipeModel, accountManager, accountSession);

        transitioner
            .add(Routes.getRoutes(primaryStage, controller))
            .add(new PrintConsoleViewTransitioner());

        controller.start();
    }
}
