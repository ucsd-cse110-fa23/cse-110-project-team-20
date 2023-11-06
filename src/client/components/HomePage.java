package client.components;

import client.Recipe;
// import client.RecipeBox;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class HomePage extends BorderPane {
    private Header header;
    private RecipeList recipeList;
    private RecipeBox recipeBox;

    public HomePage(List<Recipe> recipes)
    {
        header = new Header();
        recipeList = new RecipeList(recipes);
        this.setTop(header);
        this.setCenter(new ScrollPane(recipeList));
        this.setPrefSize(500, 800);
    }

    public void
    setCreateButtonCallback(Runnable r)
    {
        this.header.setCreateButtonCallback(r);
    }

    public void
    setDetailsButtonCallback(Runnable r) 
    {
        this.recipeBox.setDetailsButtonCallback(r);
    }

    public void
    updateRecipeList(List<Recipe> recipes) {
        this.getChildren().remove(recipeList);
        this.recipeList = new RecipeList(recipes);
        this.setCenter(recipeList);
    }

}

class Header extends BorderPane {
    private Label appName;
    private Button createButton;

    public Header()
    {
        this.setPrefSize(500, 60); // Size of the header
        this.setStyle("-fx-background-color: #F0F8FF;");

        // titleText.setStyle("-fx-font-weight: bold; -fx-font-size: 20;");
        this.createButton = new Button("Create +");
        this.appName = new Label("Pantry Pals");
        this.setLeft(appName);
        this.setRight(createButton);
    }

    public void
    setCreateButtonCallback(Runnable r)
    {
        this.createButton.setOnAction(e -> r.run());
    }
}

// TODO: add transition to RecipeDetails
class RecipeBox extends HBox {
    private Button detailsButton;
    public RecipeBox(Recipe recipe) {
        Recipe myRecipe = recipe;
        Label titleLabel = new Label(recipe.getTitle());
        this.detailsButton = new Button("Details");
        this.getChildren().addAll(titleLabel, detailsButton);
    }

    
    public void
    setDetailsButtonCallback(Runnable r) 
    {
        this.detailsButton.setOnAction(e -> r.run());
    }

}

class RecipeList extends VBox {
    public RecipeList(List<Recipe> recipes) {
        for (Recipe recipe : recipes) {
            this.getChildren().add(new RecipeBox(recipe));
        }
    }
}
