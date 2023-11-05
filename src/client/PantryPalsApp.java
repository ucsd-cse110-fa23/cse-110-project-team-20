package client;

import client.components.HomeScene;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Home screen for PantryPals
 */
public class PantryPalsApp extends Application {
    Controller controller;

    public static void
    main(String[] args)
    {
        launch(args);
    }

    @Override
    public void
    start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("PantryPals");
        primaryStage.setResizable(false);
        controller = new Controller(primaryStage);
        primaryStage.show();
    }
}
