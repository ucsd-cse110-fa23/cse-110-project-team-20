package client;

import client.SceneFiles.HomeScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class Controller {
    private enum State {
        HOME, RECORDING, EXPANDED, EDIT, DELETE, NEW
    }

    LinkedList<Recipe> recipes;
    private State state;
    private Stage primaryStage;
    private Model model;

    //TODO: This is a stub to show basic idea of the recipe list being set up, the full version should work with multiple recipes
    private void ListSetup() {
        String res = model.performRequest("GET", null);
        String[] result = res.split("|");
        recipes.add(new Recipe(result[0], result[1]));
    }

    public Controller(Stage primaryStage) {
        recipes = new LinkedList<Recipe>();
        this.primaryStage = primaryStage;
        this.model = new Model();
        this.state = State.HOME;
        ListSetup();
        primaryStage.setScene(new Scene(new HomeScene(recipes)));
    }

    //TODO: Add methods for making requests through Model, and add button actions when adding the scenes
}