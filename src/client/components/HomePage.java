package client.components;

import client.Recipe;
import client.utils.runnables.RunnableWithId;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;

/*
 * Home Page
 *
 * Handles formatting of homepage and houses callbacks to recipe creation & details page.
 * Also stores the list of recipes locally, synchronously updated with the HTTP server.
 */
public class HomePage extends BorderPane {
    private Header header;
    private RecipeList recipeList;
    private HBox optionsBar;

    public HomePage(List<Recipe> recipes, Runnable createButtonCallback,
            RunnableWithId openRecipeDetailButtonCallback, Runnable logoutButtonCallback) {
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        header = new Header();
        header.setCreateButtonCallback(createButtonCallback);
        header.setLogoutButtonCallback(logoutButtonCallback);

        optionsBar = new OptionsBar();

        recipeList = new RecipeList(recipes, openRecipeDetailButtonCallback);

        ScrollPane scrollPane = new ScrollPane(recipeList);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Create a VBox to hold header and optionsBar
        VBox topContainer = new VBox();
        topContainer.getChildren().addAll(header, optionsBar);

        // Set the VBox as the top of the BorderPane
        this.setTop(topContainer);
        this.setCenter(scrollPane);
        this.setPrefSize(500, 800);
    }
}

class OptionsBar extends HBox {
    private ComboBox<String> filterComboBox;
    private Runnable r;
    private Button chronoSortButton;
    private Button alphaSortButton;

    public OptionsBar() {
        // Create filter options list
        ObservableList<String> filterOptions = FXCollections.observableArrayList("Filter: None", "Breakfast", "Lunch",
                "Dinner");

        filterComboBox = new ComboBox<>(filterOptions);
        filterComboBox.setPromptText("Filter by meal type");
        filterComboBox.getStyleClass().add("filter-combo-box");
        this.getStyleClass().add("options-bar");

        // Create chronological sort button
        chronoSortButton = createButton("", "clock-icon.png");
        chronoSortButton.getStyleClass().addAll("chrono-button");
        Tooltip chronoTooltip = new Tooltip("Sort by reverse chronological Order");
        chronoSortButton.setTooltip(chronoTooltip);

        // Create alphabetical sort button
        alphaSortButton = createButton("", "alpha-icon.png");
        alphaSortButton.getStyleClass().addAll("alpha-button");
        Tooltip alphaTooltip = new Tooltip("Sort alphabetically");
        alphaSortButton.setTooltip(alphaTooltip);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Set alignment to place sort buttons on the right side
        setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(filterComboBox, spacer, chronoSortButton, alphaSortButton);
    }

    private Button createButton(String buttonLabel, String resourceName) {
        Button btn = new Button(buttonLabel);

        try (InputStream imageStream = getClass().getResource(resourceName).openStream()) {
            Image image = new Image(imageStream);
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(24);
            imageView.setFitHeight(24);

            btn = new Button(buttonLabel, imageView);
            btn.setGraphicTextGap(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return btn;
    }

    public void setFilterCallback(Runnable r) {
        filterComboBox.setOnAction(event -> {
            if (r != null) {
                r.run();
            }
        });
    }

    public void setAlphaSortButtonCallback(Runnable r) {
        this.alphaSortButton.setOnAction(event -> {
            if (r != null) {
                r.run();
            }
        });
    }
    
    public void setChronoSortButtonCallback(Runnable r) {
        this.chronoSortButton.setOnAction(event -> {
            if (r != null) {
                r.run();
            }
        });
    }

}

class Header extends BorderPane {
    private Label appName;
    private Button createButton;
    private Button logoutButton;

    public Header() {
        createButton = new Button("\uff0b"); // unicode version of plus sign
        createButton.getStyleClass().add("create-button");
        createButton.setAlignment(Pos.CENTER);

        appName = new Label("PantryPal");
        appName.getStyleClass().add("app-name");

        logoutButton = new Button("Log out");
        logoutButton.getStyleClass().add("logout-button");
        logoutButton.setAlignment(Pos.CENTER);

        HBox rightBox = new HBox(logoutButton, createButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setSpacing(10);

        getStyleClass().add("homepage-header");
        setLeft(appName);
        setRight(rightBox);

        setAlignment(appName, Pos.CENTER_LEFT);
        setAlignment(rightBox, Pos.CENTER_RIGHT);
    }

    public void setCreateButtonCallback(Runnable r) {
        this.createButton.setOnAction(e -> r.run());
    }

    public void setLogoutButtonCallback(Runnable r) {
        this.logoutButton.setOnAction(e -> r.run());
    }
}

class RecipeBox extends HBox {
    private static final int MAX_TITLE_LEN = 38;

    public RecipeBox(Recipe recipe) {
        VBox recipeDetails = new VBox(10);

        Label titleLabel = new Label(trimTitle(recipe.getTitle()));

        titleLabel.getStyleClass().add("recipe-title");

        recipeDetails.getChildren().addAll(titleLabel);
        this.getChildren().add(recipeDetails);

        String mealType = getMealType(recipe);

        Label mealTypeLabel = new Label(mealType);
        mealTypeLabel.getStyleClass().addAll("meal-type-label", "bubble-label");
        mealTypeLabel.setStyle("-fx-background-color: #" + getColorHexCode(mealType) + ";");

        HBox.setHgrow(recipeDetails, Priority.ALWAYS);
        this.getChildren().add(mealTypeLabel);

        getStyleClass().add("recipe-box");
    }

    private static String trimTitle(String title) {
        String trimmed = title.trim();
        if (trimmed.length() > MAX_TITLE_LEN) {
            return title.substring(0, MAX_TITLE_LEN - 3).trim() + "...";
        }
        return trimmed;
    }

    private static String getMealType(Recipe r) {
        switch (r.getMealType().toLowerCase()) {
            case "breakfast":
                return "Breakfast";
            case "lunch":
                return "Lunch";
            case "dinner":
                return "Dinner";
            default:
                return "Unknown";
        }
    }

    private static String getColorHexCode(String mealType) {
        switch (mealType.toLowerCase()) {
            case "breakfast":
                return "FAEDCB";
            case "lunch":
                return "C9E4DE";
            case "dinner":
                return "DBCDF0";
            default:
                return "808080";
        }
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
