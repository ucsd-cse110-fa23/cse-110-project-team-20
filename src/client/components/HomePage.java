package client.components;

import client.Recipe;
// import client.RecipeBox;
import javafx.scene.Scene;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import client.Controller;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class HomePage extends BorderPane {
    private Header header;
    private RecipeList recipeList;
    private Controller controller;

    public HomePage(List<Recipe> recipes, Controller controller)
    {
        this.controller = controller;
        header = new Header();
        recipeList = new RecipeList(recipes, this.controller);
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
    updateRecipeList(Recipe recipe) {
    
       this.recipeList.addRecipe(recipe);
    }

    public void 
    openDetailedView(Recipe recipe) {
    controller.openRecipeDetails(recipe);
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

// TODO: add transition toRecipe
class RecipeBox extends HBox {
    private Button detailsButton;
    public RecipeBox(Recipe recipe, Controller controller) {
        Controller myController = controller;
        Recipe myRecipe = recipe;
        
        VBox recipeDetails = new VBox(10);
        this.setStyle("-fx-border-color: black; -fx-border-width: 1;");
        recipeDetails.setPadding(new Insets(10, 10, 10, 10));

        Label titleLabel = new Label(recipe.getTitle());
        this.detailsButton = new Button("Details");

        recipeDetails.getChildren().addAll(titleLabel, detailsButton);

        this.getChildren().add(recipeDetails);
        detailsButton.setOnAction(event -> myController.openRecipeDetails(myRecipe));

        
    }

    
    public void
    setDetailsButtonCallback(Runnable r) 
    {
        this.detailsButton.setOnAction(e -> r.run());
    }

}

class RecipeList extends VBox {
    Controller myController;
    public RecipeList(List<Recipe> recipes, Controller controller) {
        myController = controller;
        this.setSpacing(10); // sets spacing between contacts
        this.setPrefSize(500, 460);
        this.setStyle("-fx-background-color: #F0F8FF;");
        for (Recipe recipe : recipes) {
            this.getChildren().add(new RecipeBox(recipe, myController));
        }
    }

    public void
    addRecipe(Recipe recipe) {
        RecipeBox newRecipeBox = new RecipeBox(recipe, myController);
        this.getChildren().add(newRecipeBox);
    }
}
