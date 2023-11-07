package client.components;

import client.Controller;
import client.Recipe;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
// import client.RecipeBox;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HomePage extends BorderPane {
    private Header header;
    private RecipeList recipeList;
    private Controller controller;
    private String headerStyle =
        "-fx-background-color: #3498db;-fx-text-fill: #fff;-fx-font-size: 36px;-fx-padding: 10px 20px;-fx-alignment: center-left;-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 1);-fx-font-weight: bold;-fx-border-color: #2980b9;-fx-border-width: 0 0 2 0;-fx-border-radius: 0;";

    public HomePage(List<Recipe> recipes, Controller controller)
    {
        this.controller = controller;
        header = new Header();
        header.setStyle(headerStyle);
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
    updateRecipeList(Recipe recipe)
    {
        this.recipeList.addRecipe(recipe);
    }

    public void
    openDetailedView(Recipe recipe)
    {
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
        this.createButton = new Button("+");
        createButton.setStyle("-fx-background-radius: 1.5em; "
            + "-fx-min-width: 1.5em; "
            + "-fx-min-height: 1.5em; "
            + "-fx-max-width: 1.5em; "
            + "-fx-max-height: 1.5em; "
            + "-fx-background-color: -fx-body-color;"
            + "-fx-background-insets: 0px; "
            + "-fx-padding: 0px;");
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
    public RecipeBox(Recipe recipe, Controller controller)
    {
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
    public RecipeList(List<Recipe> recipes, Controller controller)
    {
        myController = controller;
        this.setSpacing(10); // sets spacing between contacts
        this.setPrefSize(500, 460);
        this.setStyle("-fx-background-color: #F0F8FF;");
        for (Recipe recipe : recipes) {
            this.getChildren().add(new RecipeBox(recipe, myController));
        }
    }

    public void
    addRecipe(Recipe recipe)
    {
        RecipeBox newRecipeBox = new RecipeBox(recipe, myController);
        this.getChildren().add(newRecipeBox);
    }
}
