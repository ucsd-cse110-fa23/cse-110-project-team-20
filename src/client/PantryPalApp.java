package client;

import client.recipe.IRecipeGenerator;
import client.account.IAccountManager;
import client.account.IAccountSession;
import client.account.ServerAccountManager;
import client.account.SimpleAccountSession;
import client.audio.AudioRecorder;
import client.audio.IAudioRecorder;
import client.components.ErrorPage;
import client.models.ServerRecipeModel;
import client.models.CachedRecipeModel;
import client.models.IRecipeModel;
import client.recipe.ServerRecipeGenerator;
import client.utils.transitions.CompositeViewTransitioner;
import client.utils.transitions.IViewTransitioner;
import client.utils.transitions.PrintConsoleViewTransitioner;
import javafx.application.Application;
import javafx.stage.Stage;
import java.beans.PropertyChangeEvent;

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

        ServerRecipeModel serverRecipeModel = new ServerRecipeModel(accountSession);
        IRecipeModel recipeModel = new CachedRecipeModel(serverRecipeModel);

        IAccountManager accountManager = new ServerAccountManager();

        Controller controller = new Controller(transitioner, recipeGenerator, audioRecorder, recipeModel, accountManager, accountSession);

        transitioner
            .add(Routes.getRoutes(primaryStage, controller))
            .add(new PrintConsoleViewTransitioner());

        // added property change listener to refresh cached information in recipe model
        // when account session is changed
        ((SimpleAccountSession) accountSession).addPropertyChangeListener((PropertyChangeEvent evt) -> {
            if (evt.getPropertyName().equals("token")) {
                ((CachedRecipeModel) recipeModel).clearCache();
            }
        });

        // since we perform requests in server recipe model inside of the thread to prevent UI freezing,
        // we need to have different error handling than other models. Register the error handler
        // so that the error appears on UI via controller.
        serverRecipeModel.setErrorHandler((Exception e) -> {
            controller.onRecipeModelError(e);
        });

        controller.start();
    }
}
