package client;

import client.components.LoadingPage;
import client.components.ErrorPage;
import client.components.HomePage;
import client.components.NewRecipeConfirmPage;
import client.components.RecipeDetailsPage;
import client.components.RecordingPage;
import client.components.RecordingPageCallbacks;

import java.util.List;

import client.utils.RunnableWithId;
import client.utils.transitions.javafx.JavaFXViewTransitioner;
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

  public static JavaFXViewTransitioner getRoutes(Stage primaryStage, Controller controller) {
    JavaFXViewTransitioner routes = new JavaFXViewTransitioner();

    /**
     * Home Page
     * 
     * Display a list of recipes, provides button for creating new recipe.
     */
    routes.register(HomePage.class, (List<Recipe> recipes, Runnable createButtonCallback, RunnableWithId openRecipeDetailButtonCallback) -> {
      HomePage homePage = new HomePage(recipes, createButtonCallback, openRecipeDetailButtonCallback);

      // Set the title of the app
      primaryStage.setTitle("PantryPal");
      // Create scene of mentioned size with the border pane
      primaryStage.setScene(new Scene(homePage, WIDTH, HEIGHT));
      // Make window non-resizable
      primaryStage.setResizable(false);
      // show
      primaryStage.show();
    });

    /**
     * Recording Page
     *
     * Display recording page with given message and filename.
     */
    routes.register(RecordingPage.class, (
      String message, RecordingPageCallbacks callbacks) -> {
      
      RecordingPage mealTypePage = new RecordingPage(message);

      mealTypePage.setButtonCallbacks(callbacks);
      primaryStage.setScene(new Scene(mealTypePage, WIDTH, HEIGHT));
    });

    /**
     * Loading Page
     * 
     * Display loading screen.
     */
    routes.register(LoadingPage.class, (String message) -> {
      LoadingPage loadingPage = new LoadingPage();
      loadingPage.setLoadingText(message);
      primaryStage.setScene(new Scene(loadingPage, WIDTH, HEIGHT));
    });

    /**
     * New Recipe Confirm Page
     * 
     * Prompt generated recipe with title. User can decide to save or discard
     */
    routes.register(NewRecipeConfirmPage.class, (Recipe recipe, Runnable saveCallback, Runnable discardCallback) -> {
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
    routes.register(RecipeDetailsPage.class, (Recipe recipe, Runnable cancelCallback) -> {
      RecipeDetailsPage recipeDetailsPage = new RecipeDetailsPage();

      recipeDetailsPage.displayRecipe(recipe);
      recipeDetailsPage.setCancelCallback(cancelCallback);

      primaryStage.setScene(new Scene(recipeDetailsPage, WIDTH, HEIGHT));
    });

    /**
     * Error page
     *
     * Show any critical error message
     */
    routes.register(ErrorPage.class, (String message, Runnable retry) -> {
      ErrorPage errorPage = new ErrorPage(message, retry);

      primaryStage.setTitle("PantryPal");
      primaryStage.setScene(new Scene(errorPage, WIDTH, HEIGHT));
      primaryStage.show();
    });

    return routes;
  }
}
