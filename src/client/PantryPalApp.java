package client;

import client.account.CredentialManager;
import client.account.IAccountManager;
import client.account.IAccountSession;
import client.account.ServerAccountManager;
import client.account.SimpleAccountSession;
import client.audio.AudioRecorder;
import client.audio.IAudioRecorder;
import client.models.CachedRecipeModel;
import client.models.IRecipeModel;
import client.models.ServerRecipeModel;
import client.recipe.IRecipeGenerator;
import client.recipe.ServerRecipeGenerator;
import client.utils.transitions.CompositeViewTransitioner;
import client.utils.transitions.PrintConsoleViewTransitioner;
import java.beans.PropertyChangeEvent;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * PantryPal JavaFX Application
 */
public class PantryPalApp extends Application {
    public static void
    main(String[] args)
    {
        launch(args);
    }

    @Override

    public void
    start(Stage primaryStage) throws Exception
    {
        AppConfiguration config = new AppConfiguration();

        CompositeViewTransitioner transitioner = new CompositeViewTransitioner();

        IAccountSession accountSession = new SimpleAccountSession();
        IRecipeGenerator recipeGenerator = new ServerRecipeGenerator(accountSession, config.getApiUrl());
        IAudioRecorder audioRecorder = new AudioRecorder();

        ServerRecipeModel serverRecipeModel = new ServerRecipeModel(accountSession, config.getApiUrl());
        IRecipeModel recipeModel = new CachedRecipeModel(serverRecipeModel);

        IAccountManager accountManager = new ServerAccountManager(config.getApiUrl());
        CredentialManager credentialManager = new CredentialManager("credentials.json");

        Controller controller = new Controller(transitioner, recipeGenerator, audioRecorder,
            recipeModel, accountManager, accountSession, credentialManager);

        transitioner.add(Routes.getRoutes(primaryStage, controller))
            .add(new PrintConsoleViewTransitioner());

        // added property change listener to refresh cached information in recipe model
        // when account session is changed
        ((SimpleAccountSession) accountSession)
            .addPropertyChangeListener((PropertyChangeEvent evt) -> {
                if (evt.getPropertyName().equals("token")) {
                    ((CachedRecipeModel) recipeModel).clearCache();
                }
            });

        // since we perform requests in server recipe model inside of the thread to prevent UI
        // freezing, we need to have different error handling than other models. Register the error
        // handler so that the error appears on UI via controller.
        serverRecipeModel.setErrorHandler((Exception e) -> { controller.onRecipeModelError(e); });

        controller.start();
    }
}
