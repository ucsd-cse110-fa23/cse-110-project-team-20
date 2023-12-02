package client.components;

import client.Recipe;
import client.utils.runnables.RunnableWithId;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * Home Page
 *
 * Handles formatting of homepage and houses callbacks to recipe creation & details page.
 * Also stores the list of recipes locally, synchronously updated with the HTTP server.
 */
public class HomePage extends BorderPane {
    private Header header;
    private RecipeList recipeList;

    public HomePage(List<Recipe> recipes, Runnable createButtonCallback,
        RunnableWithId openRecipeDetailButtonCallback, Runnable logoutButtonCallback)
    {
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        header = new Header();
        header.setCreateButtonCallback(createButtonCallback);
        header.setLogoutButtonCallback(logoutButtonCallback);

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
    private Button logoutButton;

    public Header()
    {
        createButton = new Button("\uff0b"); // unicode version of plus sign
        createButton.getStyleClass().add("create-button");
        createButton.setAlignment(Pos.CENTER);

        appName = new Label("PantryPal");
        appName.getStyleClass().add("app-name");

        logoutButton = new Button("Log out");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setAlignment(Pos.CENTER);

        getStyleClass().add("homepage-header");
        setLeft(appName);
        setCenter(logoutButton);
        setRight(createButton);

        setAlignment(appName, Pos.CENTER_LEFT);
        setAlignment(logoutButton, Pos.CENTER_RIGHT);
    }

    public void
    setCreateButtonCallback(Runnable r)
    {
        this.createButton.setOnAction(e -> r.run());
    }

    public void
    setLogoutButtonCallback(Runnable r)
    {
        this.logoutButton.setOnAction(e -> r.run());
    }
}

class RecipeBox extends HBox {
    public RecipeBox(Recipe recipe)
    {
        VBox recipeDetails = new VBox(10);

        Label titleLabel = new Label(recipe.getTitle());
        titleLabel.getStyleClass().add("recipe-title");

        recipeDetails.getChildren().addAll(titleLabel);
        this.getChildren().add(recipeDetails);

        getStyleClass().add("recipe-box");
    }
}

class RecipeList extends VBox {
    public RecipeList(List<Recipe> recipes, RunnableWithId openRecipeDetailButtonCallback)
    {
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
