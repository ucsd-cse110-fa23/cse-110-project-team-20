package client;

import client.SceneFiles.HomeScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;

public class Controller {
    private enum State {
        HOME, RECORDING, EXPANDED, EDIT, DELETE, NEW
    }

    LinkedList<Recipe> recipes = new LinkedList<Recipe>();
    private State state;
    private Stage primaryStage;
    private Model model;

    public Controller(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.model = new Model();
        this.state = State.HOME;
        primaryStage.setScene(new Scene(new HomeScene(recipes)));
    }

    //TODO: Add methods for making requests through Model, and add button actions when adding the scenes
}