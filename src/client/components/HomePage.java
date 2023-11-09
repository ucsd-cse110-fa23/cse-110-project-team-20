package client.components;

import client.Recipe;
import client.utils.RunnableWithId;

import java.util.List;
import javafx.geometry.Insets;
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
    private String headerStyle = "-fx-background-color: #3498db;"
            + "-fx-text-fill: #fff;"
            + "-fx-font-size: 36px;"
            + "-fx-padding: 10px 20px;"
            + "-fx-alignment: center-left;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 1);"
            + "-fx-font-weight: bold;"
            + "-fx-border-color: #2980b9;"
            + "-fx-border-width: 0 0 2 0;"
            + "-fx-border-radius: 0;";

    private String createButtonStyle = "-fx-background-radius: 1.5em;"
            + "-fx-min-width: 1.5em; "
            + "-fx-min-height: 1.5em; "
            + "-fx-max-width: 1.5em; "
            + "-fx-max-height: 1.5em; "
            + "-fx-background-color: -fx-body-color;"
            + "-fx-background-insets: 0px; "
            + "-fx-padding: 0px;";

    public Header() {
        createButton = new Button("\uff0b"); // unicode version of plus sign
        createButton.setStyle(createButtonStyle);
        createButton.setAlignment(Pos.CENTER);

        appName = new Label("PantryPal");
        appName.setStyle("-fx-text-fill: #ffffff;");

        setStyle(headerStyle);
        setLeft(appName);
        setRight(createButton);

        setAlignment(appName, Pos.CENTER_LEFT);
    }

    public void setCreateButtonCallback(Runnable r) {
        this.createButton.setOnAction(e -> r.run());
    }
}

class RecipeBox extends HBox {
    private static String defaultStyle = "-fx-border-color: #aaaaaa;"
            + "-fx-border-width: 1px;"
            + "-fx-border-radius: 2px;"
            + "-fx-background-color: #ffffff;";

    public RecipeBox(Recipe recipe) {
        VBox recipeDetails = new VBox(10);
        this.setStyle(defaultStyle);
        recipeDetails.setPadding(new Insets(10, 10, 10, 10));

        Label titleLabel = new Label(recipe.getTitle());
        titleLabel.setStyle("-fx-font-size: 16px");

        recipeDetails.getChildren().addAll(titleLabel);
        this.getChildren().add(recipeDetails);

        setOnMouseEntered((e) -> {
            updateStyle("-fx-background-color: #f0f0f0");
        });

        setOnMouseExited((e) -> {
            updateStyle("");
        });
    }

    public void updateStyle(String additional) {
        setStyle(defaultStyle + additional);
    }
}

class RecipeList extends VBox {
    public RecipeList(List<Recipe> recipes, RunnableWithId openRecipeDetailButtonCallback) {
        this.setSpacing(10); // sets spacing between contacts
        this.setPrefSize(500, 460);
        this.setStyle("-fx-background-color: #F0F8FF; -fx-padding: 10px;");

        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);

            final int id = i;
            RecipeBox newRecipeBox = new RecipeBox(recipe);
            newRecipeBox.setOnMouseClicked(event -> openRecipeDetailButtonCallback.run(id));

            this.getChildren().add(newRecipeBox);
        }
    }
}
