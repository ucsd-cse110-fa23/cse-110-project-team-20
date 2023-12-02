package client;

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
import client.utils.runnables.RunnableForLogin;
import client.utils.runnables.RunnableWithId;
import client.utils.transitions.javafx.JavaFXViewTransitioner;
import java.util.List;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Routes class
 *
 * We register each page on transitioner and wait until it is called by transitionTo.
 * Transitioner holds all page information and run the page creation when it called through
 * transitionTo method. transitionTo method must be called in Controller only.
 */
public class Routes {
    private static final int WIDTH = 500, HEIGHT = 500;

    public static JavaFXViewTransitioner
    getRoutes(Stage primaryStage, Controller controller)
    {
        JavaFXViewTransitioner routes = new JavaFXViewTransitioner();

        /**
         * Login Page
         *
         * Display username and password fields to accomodate login/creating account feature.
         */
        routes.register(LoginPage.class, (Object[] params) -> {
            RunnableForLogin onLogin = (RunnableForLogin) params[0];
            LoginPage loginPage = new LoginPage(onLogin);

            // Set the title of the app
            primaryStage.setTitle("PantryPal");
            // Create scene of mentioned size with the border pane
            primaryStage.setScene(new Scene(loginPage, WIDTH, HEIGHT));
            // Make window non-resizable
            primaryStage.setResizable(false);
            // show
            primaryStage.show();
        });

        /**
         * Home Page
         *
         * Display a list of recipes, provides button for creating new recipe.
         */
        routes.register(HomePage.class, (Object[] params) -> {
            List<Recipe> recipes = (List<Recipe>) params[0];
            Runnable createButtonCallback = (Runnable) params[1];
            RunnableWithId openRecipeDetailButtonCallback = (RunnableWithId) params[2];
            Runnable logoutButtonCallback = (Runnable) params[3];
            HomePage homePage = new HomePage(recipes, createButtonCallback,
                openRecipeDetailButtonCallback, logoutButtonCallback);

            primaryStage.setScene(new Scene(homePage, WIDTH, HEIGHT));
        });

        /**
         * Recording Page
         *
         * Display recording page with given message and filename.
         */
        routes.register(RecordingPage.class, (Object[] params) -> {
            String message = (String) params[0];
            RecordingPageCallbacks callbacks = (RecordingPageCallbacks) params[1];
            RecordingPage mealTypePage = new RecordingPage(message);

            mealTypePage.setButtonCallbacks(callbacks);
            primaryStage.setScene(new Scene(mealTypePage, WIDTH, HEIGHT));
        });

        /**
         * Loading Page
         *
         * Display loading screen.
         */
        routes.register(LoadingPage.class, (Object[] params) -> {
            String message = (String) params[0];
            LoadingPage loadingPage = new LoadingPage();
            loadingPage.setLoadingText(message);
            primaryStage.setScene(new Scene(loadingPage, WIDTH, HEIGHT));
        });

        /**
         * New Recipe Confirm Page
         *
         * Prompt generated recipe with title. User can decide to save or discard
         */
        routes.register(NewRecipeConfirmPage.class, (Object[] params) -> {
            Recipe recipe = (Recipe) params[0];
            Runnable saveCallback = (Runnable) params[1];
            Runnable discardCallback = (Runnable) params[2];
            NewRecipeConfirmPage newRecipeConfirmPage = new NewRecipeConfirmPage(recipe);

            newRecipeConfirmPage.setCancelCallback(discardCallback);
            newRecipeConfirmPage.setSaveCallback(saveCallback);

            primaryStage.setScene(new Scene(newRecipeConfirmPage, WIDTH, HEIGHT));
        });

        /**
         * Recipe detail page
         *
         * Show expanded recipe information
         */
        routes.register(RecipeDetailsPage.class, (Object[] params) -> {
            Recipe recipe = (Recipe) params[0];
            RecipeDetailsPageCallbacks callbacks = (RecipeDetailsPageCallbacks) params[1];
            RecipeDetailsPage recipeDetailsPage = new RecipeDetailsPage();

            recipeDetailsPage.displayRecipe(recipe);
            recipeDetailsPage.setCancelCallback(callbacks.getOnGoBackButtonClicked());
            recipeDetailsPage.setEditCallback(callbacks.getOnEditButtonClicked());
            recipeDetailsPage.setDeleteCallback(callbacks.getOnDeleteButtonClicked());
            recipeDetailsPage.setShareCallback(callbacks.getOnShareButtonClicked());

            primaryStage.setScene(new Scene(recipeDetailsPage, WIDTH, HEIGHT));
        });

        // TODO: Add edit page

        routes.register(RecipeEditPage.class, (Object[] params) -> {
            Recipe recipe = (Recipe) params[0];
            RecipeEditPageCallbacks callbacks = (RecipeEditPageCallbacks) params[1];
            RecipeEditPage recipeEditPage = new RecipeEditPage();

            recipeEditPage.displayRecipe(recipe);
            recipeEditPage.setCancelCallback(callbacks.getOnGoBackButtonClicked());
            recipeEditPage.setSaveCallBack(callbacks.getOnSaveButtonClicked());

            primaryStage.setScene(new Scene(recipeEditPage, WIDTH, HEIGHT));
        });

        /**
         * Error page
         *
         * Show any critical error message
         */
        routes.register(ErrorPage.class, (Object[] params) -> {
            String message = (String) params[0];
            Runnable retry = (Runnable) params[1];
            ErrorPage errorPage = new ErrorPage(message, retry);

            primaryStage.setTitle("PantryPal");
            primaryStage.setScene(new Scene(errorPage, WIDTH, HEIGHT));
            primaryStage.show();
        });

        /**
         * Error Message
         *
         * Show any simple error message
         */
        routes.register(ErrorMessage.class, (Object[] params) -> {
            String message = (String) params[0];
            // error message will be appeared without assigning any stage
            new ErrorMessage(message);
        });

        /**
         * Share URL Modal
         * 
         * show share URL of recipe.
         */
        routes.register(SharedRecipeModal.class, (Object[] params) -> {
            String sharedUrl = (String) params[0];
            new SharedRecipeModal(sharedUrl);
        });

    return routes;
  }
}
