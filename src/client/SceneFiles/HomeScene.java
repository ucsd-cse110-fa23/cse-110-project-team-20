package client.SceneFiles;

import client.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.LinkedList;

public class HomeScene extends BorderPane {
    private Header header;
    private RecipeList recipeList;

    public HomeScene (LinkedList<Recipe> recipes) {
        header = new Header();
        recipeList = new RecipeList(recipes);
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

class RecipeBox extends HBox {
    private Recipe recipe;
    private Label nameLabel;
    public RecipeBox(Recipe recipe) {
        this.recipe = recipe;
        this.nameLabel = new Label(recipe.getTitle());
        this.getChildren().add(nameLabel);
    }

    public Recipe getRecipe() {
        return recipe;
    }
}

class RecipeList extends VBox{

    public RecipeList(LinkedList<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            this.getChildren().add(new RecipeBox(recipe));
        }
    }
}