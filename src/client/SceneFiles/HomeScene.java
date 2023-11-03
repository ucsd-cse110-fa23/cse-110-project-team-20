package client.SceneFiles;

import client.RecipeBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class HomeScene extends BorderPane {
    private static Header header = new Header();
    private static RecipeList recipeList = new RecipeList();

    public HomeScene () {
        this.setTop(header);
        this.setCenter(new ScrollPane(recipeList));
        this.setPrefSize(500, 800);
    }
}

class Header extends BorderPane {
    private Label appName;
    private Button createButton;

    public Header() {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        // titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.createButton = new Button("Create +");
        this.appName = new Label("Pantry Pals");
        this.setLeft(appName);
        this.setRight(createButton);
    }
}

class RecipeList extends VBox{
    private static LinkedList<RecipeBox> recipes = new LinkedList<RecipeBox>();

    public RecipeList() {
        for (int i = 0; i < recipes.size(); i++)
        {
            this.getChildren().add(recipes.get(i));
        }
    }
}