package client.components;

import client.Recipe;
import client.utils.runnables.RunnableWithId;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class HomePage extends BorderPane {
    private Header header;
    private RecipeList recipeList;

    public HomePage(List<Recipe> recipes, Runnable createButtonCallback, RunnableWithId openRecipeDetailButtonCallback) {
        getStylesheets().add(getClass().getResource(
            "style.css"
        ).toExternalForm());

        header = new Header();
        header.setCreateButtonCallback(createButtonCallback);

        recipeList = new RecipeList(recipes, openRecipeDetailButtonCallback);

        ScrollPane scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        this.setTop(header);
        this.setCenter(scrollPane);
        this.setPrefSize(500, 800);
    }
}

class Header extends BorderPane {
    private Label appName;
    private Button createButton;

    public Header() {
        createButton = new Button("\uff0b"); // unicode version of plus sign
        createButton.getStyleClass().add("create-button");
        createButton.setAlignment(Pos.CENTER);

        appName = new Label("PantryPal");
        appName.getStyleClass().add("app-name");

        getStyleClass().add("homepage-header");
        setLeft(appName);
        setRight(createButton);

        setAlignment(appName, Pos.CENTER_LEFT);
    }

    public void setCreateButtonCallback(Runnable r) {
        this.createButton.setOnAction(e -> r.run());
    }
}

class RecipeBox extends HBox {
    public RecipeBox(Recipe recipe) {
        VBox recipeDetails = new VBox(10);

        Label titleLabel = new Label(recipe.getTitle());
        titleLabel.getStyleClass().add("recipe-title");

        recipeDetails.getChildren().addAll(titleLabel);
        this.getChildren().add(recipeDetails);

        getStyleClass().add("recipe-box");
    }
}

class RecipeList extends VBox {
    public RecipeList(List<Recipe> recipes, RunnableWithId openRecipeDetailButtonCallback) {
        this.setSpacing(10); // sets spacing between contacts
        this.setPrefSize(500, 460);
        getStyleClass().add("recipe-list");

        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);

            final int id = i;
            RecipeBox newRecipeBox = new RecipeBox(recipe);
            newRecipeBox.setOnMouseClicked(event -> openRecipeDetailButtonCallback.run(id));

            this.getChildren().add(newRecipeBox);
        }
    }
}
