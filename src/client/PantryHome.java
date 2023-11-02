package com.example.project;

import com.example.project.SceneFiles.HomeScene;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Home screen for PantryPals
 */
public class PantryHome extends Application{
    //ArrayList<String> recipleList;
    Stage mainStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PantryPals");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(new HomeScene()));
        primaryStage.show();
    }
}

