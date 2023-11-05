package client;

import client.SceneFiles.HomeScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Home screen for PantryPals
 */
public class PantryPalsApp extends Application{
    Controller controller;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PantryPals");
        primaryStage.setResizable(false);
        controller = new Controller(primaryStage);
        primaryStage.show();
    }
}

