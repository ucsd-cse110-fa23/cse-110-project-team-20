package client;

import client.components.HomePage;
import java.util.List;
import client.utils.transitions.javafx.JavaFXTransitioner;
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

  public static JavaFXTransitioner getRoutes(Stage primaryStage, Controller controller) {
    JavaFXTransitioner routes = new JavaFXTransitioner();

    /**
     * Home Page
     * 
     * Display a list of recipes, provides button for creating new recipe.
     */
    routes.register(HomePage.class, (List<Recipe> recipes) -> {
      HomePage homePage = new HomePage(recipes, controller);

      // Set the title of the app
      primaryStage.setTitle("PantryPal");
      // Create scene of mentioned size with the border pane
      primaryStage.setScene(new Scene(homePage, WIDTH, HEIGHT));
      // Make window non-resizable
      primaryStage.setResizable(false);
      // show
      primaryStage.show();
    });

    return routes;
  }
}
