package client;

import client.account.CredentialManager;
import client.account.IAccountManager;
import client.account.IAccountSession;
import client.account.ICredentialManager;
import client.account.IncorrectPassword;
import client.account.LoginFailed;
import client.account.SimpleAccountSession;
import client.audio.IAudioRecorder;
import client.components.ErrorMessage;
import client.components.ErrorPage;
import client.components.HomePage;
import client.components.LoadingPage;
import client.components.LoginPage;
import client.components.NewRecipeConfirmPage;
import client.components.RecipeDetailsPage;
import client.components.RecipeDetailsPageCallbacks;
import client.components.RecipeEditPage;
import client.components.RecipeEditPageCallbacks;
import client.components.RecordingPage;
import client.components.RecordingPageCallbacks;
import client.components.SharedRecipeModal;
import client.models.IRecipeModel;
import client.recipe.IRecipeGenerator;
import client.recipe.RecipeRequestParameter;
import client.utils.runnables.RunnableForLogin;
import client.utils.runnables.RunnableWithId;
import client.utils.runnables.RunnableWithRecipe;
import client.utils.transitions.IViewTransitioner;
import java.io.File;
import java.util.List;

public class Controller {
    private final String MEAL_TYPE_AUDIO = "meal_type.wav";
    private final String INGREDIENTS_AUDIO = "ingredients.wav";

    private IRecipeGenerator recipeGenerator;
    private IViewTransitioner viewTransitioner;
    private IAudioRecorder audioRecorder;
    private IRecipeModel recipeModel;
    private IAccountManager accountManager;
    private IAccountSession accountSession;
    private ICredentialManager credentialManager;

    /**
     * Controller glues all functions together.
     *
     * @param viewTransitioner
     * @param recipeGenerator
     * @param audioRecorder
     * @param accountManager
     * @param accountSession
     */
    public Controller(IViewTransitioner viewTransitioner, IRecipeGenerator recipeGenerator,
        IAudioRecorder audioRecorder, IRecipeModel recipeModel, IAccountManager accountManager,
        IAccountSession accountSession, ICredentialManager credentialManager)
    {
        this.viewTransitioner = viewTransitioner;
        this.recipeGenerator = recipeGenerator;
        this.audioRecorder = audioRecorder;
        this.recipeModel = recipeModel;
        this.accountManager = accountManager;
        this.accountSession = accountSession;
        this.credentialManager = credentialManager;
    }

    public void
    start()
    {
        // transitionToHomeScene();
        transitionToLoginScene();
    }

    public void
    transitionToLoginScene()
    {
        RunnableForLogin onLogin =
            (String username, String password, boolean stayLoggedIn, Runnable onLoaded) ->
        {
            try {
                String token = accountManager.loginOrCreateAccount(username, password);
                if (token != null) {
                    accountSession.setToken(token);
                }
                if (stayLoggedIn) {
                    credentialManager.setCredentials(username, password);
                }
            } catch (IncorrectPassword e) {
                viewTransitioner.transitionTo(ErrorMessage.class, e.getMessage());
                onLoaded.run();
                return;
            } catch (LoginFailed e) {
                viewTransitioner.transitionTo(
                    ErrorMessage.class, "Login Failed: " + e.getMessage());
                onLoaded.run();
                return;
            }

            // @TODO remove this debug line
            System.out.println(String.format("login button is clicked: %s, %s%s", username,
                password, stayLoggedIn ? ", stay logged in" : ""));

            transitionToHomeScene();
        };

        if (credentialManager.hasCredentials()) {
            String username = credentialManager.getUsername();
            String password = credentialManager.getPassword();
            // It will not work if you remove this line for some reason
            viewTransitioner.transitionTo(LoginPage.class, onLogin);
            onLogin.run(username, password, true, () -> {});
        } else {
            viewTransitioner.transitionTo(LoginPage.class, onLogin);
        }
    }

    public void
    transitionToMealTypeScene()
    {
        RecordingPageCallbacks callbacks = new RecordingPageCallbacks(
            () -> mealTypeRecordingStarted(), () -> mealTypeRecordingCompleted());

        viewTransitioner.transitionTo(RecordingPage.class,
            "What kind of meal do you want?\nLunch, Dinner, Snack etc.", callbacks);
    }

    public void
    transitionToIngredientsScene()
    {
        RecordingPageCallbacks callbacks = new RecordingPageCallbacks(
            () -> ingredientsRecordingStarted(), () -> ingredientsRecordingCompleted());

        viewTransitioner.transitionTo(
            RecordingPage.class, "What ingredients do you have?", callbacks);
    }

    public void
    transitionToHomeScene()
    {
        List<Recipe> recipes;
        try {
            recipes = recipeModel.getRecipes();
        } catch (Exception e) {
            Runnable retryButtonCallback = () -> transitionToHomeScene();
            viewTransitioner.transitionTo(ErrorPage.class,
                "The app cannot reach the server. Check the server is up and running.",
                retryButtonCallback);

            return;
        }

        Runnable createButtonCallback = () -> createRecipeButtonClicked();
        Runnable logoutButtonCallback = () -> logoutButtonClicked();

        RunnableWithId openRecipeDetailButtonCallback = (int id) -> openRecipeDetailPage(id);
        System.out.println("TransitionTo homepage.class");
        viewTransitioner.transitionTo(HomePage.class, recipes, createButtonCallback,
            openRecipeDetailButtonCallback, logoutButtonCallback);
    }

    public void
    requestTranscription()
    {
        File mealTypeFile = new File(MEAL_TYPE_AUDIO);
        File ingredientsFile = new File(INGREDIENTS_AUDIO);

        if (mealTypeFile != null && ingredientsFile != null) {
            RecipeRequestParameter params =
                new RecipeRequestParameter(mealTypeFile, ingredientsFile);

            recipeGenerator.requestGeneratingRecipe(params,
                (recipe)
                    -> { transitionToNewRecipeConfirmPage(recipe); },
                (errorMessage) -> {
                    Runnable retryButtonCallback = () ->
                    {
                        transitionToHomeScene();
                    };
                    viewTransitioner.transitionTo(ErrorPage.class,
                        "Generating new recipe is failed: " + errorMessage, retryButtonCallback,
                        "Go back to home");
                });
        }
    }

    public void
    openRecipeDetailPage(int id)
    {
        Recipe recipe = recipeModel.getRecipe(id);
        Runnable cancelCallback = () -> backToHomeScene();

        // TODO: Added transition to edit
        Runnable editCallback = () -> transitionToEditScene(id);
        Runnable deleteCallback = () -> deleteRecipeClicked(id);
        Runnable shareCallback = () -> shareRecipeClicked(id);

        RecipeDetailsPageCallbacks callbacks = new RecipeDetailsPageCallbacks(
            cancelCallback, editCallback, deleteCallback, shareCallback);
        viewTransitioner.transitionTo(RecipeDetailsPage.class, recipe, callbacks);
    }

    // TODO: Add in edit page
    public void
    transitionToEditScene(int id)
    {
        // Add in elements
        Recipe recipe = recipeModel.getRecipe(id);
        Runnable cancelCallback = () -> openRecipeDetailPage(id);
        RunnableWithRecipe saveCallback = (Recipe recipe1) -> updateRecipeClicked(id, recipe1);
        RecipeEditPageCallbacks callbacks =
            new RecipeEditPageCallbacks(cancelCallback, saveCallback);
        viewTransitioner.transitionTo(RecipeEditPage.class, recipe, callbacks);
    }

    public void
    backToHomeScene()
    {
        transitionToHomeScene();
    }

    public void
    transitionToLoadingScene()
    {
        viewTransitioner.transitionTo(LoadingPage.class, "Finding the perfect recipe...");
    }

    public void
    ingredientsRecordingStarted()
    {
        this.audioRecorder.startRecording(INGREDIENTS_AUDIO);
    }

    public void
    logoutButtonClicked()
    {
        this.credentialManager.setCredentials(null, null);
        this.transitionToLoginScene();
    }

    public void
    ingredientsRecordingCompleted()
    {
        this.audioRecorder.stopRecording();
        this.transitionToLoadingScene();
        this.requestTranscription();
    }

    public void
    mealTypeRecordingStarted()
    {
        this.audioRecorder.startRecording(MEAL_TYPE_AUDIO);
    }

    public void
    mealTypeRecordingCompleted()
    {
        this.audioRecorder.stopRecording();
        this.transitionToIngredientsScene();
    }

    public void
    createRecipeButtonClicked()
    {
        this.transitionToMealTypeScene();
    }

    public void
    transitionToNewRecipeConfirmPage(Recipe recipe)
    {
        Runnable saveCallback = () -> saveRecipeClicked(recipe);
        Runnable discardCallback = () -> discardGeneratedRecipeClicked();
        viewTransitioner.transitionTo(
            NewRecipeConfirmPage.class, recipe, saveCallback, discardCallback);
    }

    public void
    discardGeneratedRecipeClicked()
    {
        this.transitionToHomeScene();
    }

    public void
    saveRecipeClicked(Recipe recipe)
    {
        recipeModel.createRecipe(recipe);
        this.transitionToHomeScene();
    }

    // TODO: Added update functionality
    public void
    updateRecipeClicked(int id, Recipe recipe)
    {
        recipeModel.updateRecipe(id, recipe);
        openRecipeDetailPage(id);
    }

    public void
    deleteRecipeClicked(int id)
    {
        recipeModel.deleteRecipe(id);
        this.transitionToHomeScene();
    }

    public void
    shareRecipeClicked(int id)
    {
        Recipe recipe = recipeModel.getRecipe(id);
        if (recipe.getSharedUrl() == null) {
            recipeModel.shareRecipe(id, () -> { shareRecipeClicked(id); });
        } else {
            viewTransitioner.transitionTo(SharedRecipeModal.class, recipe.getSharedUrl());
        }
    }

    public void
    onRecipeModelError(Exception e)
    {
        Runnable callback = () -> start();
        viewTransitioner.transitionTo(ErrorPage.class, "Server error occured: " + e.getMessage(),
            callback, "Go to login page");
    }
}
