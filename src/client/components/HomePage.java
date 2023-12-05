package client.components;

import client.Recipe;
import client.utils.runnables.RunnableWithId;
import client.utils.runnables.RunnableWithString;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
    private OptionsBar optionsBar;

    public HomePage(List<Recipe> recipes, Runnable createButtonCallback,
        RunnableWithId openRecipeDetailButtonCallback, Runnable logoutButtonCallback,
        RunnableWithString mealTypeFilterCallback, RunnableWithString sortCallback)
    {
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        header = new Header();
        header.setCreateButtonCallback(createButtonCallback);
        header.setLogoutButtonCallback(logoutButtonCallback);

        optionsBar = new OptionsBar();
        optionsBar.setFilterCallback(
            () -> { mealTypeFilterCallback.run(optionsBar.getSelectedValue()); });
        optionsBar.setAlphaSortButtonCallback(sortCallback);
        optionsBar.setChronoSortButtonCallback(sortCallback);

        recipeList = new RecipeList(recipes, openRecipeDetailButtonCallback);
        recipeList.setSortType(RecipeSortOrder.TIME_ASC.name());

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

    public void
    applyMealTypeFilter(String filterName)
    {
        System.out.println(String.format("[%s] meal type filter applied: %s", getClass().getName(),
            filterName != null ? filterName : "(not selected)"));

        recipeList.setMealTypeFilter(filterName);
        recipeList.refresh();
    }

    public void
    applySort(String sortType)
    {
        System.out.println(String.format("[%s] sort applied: %s", getClass().getName(), sortType));

        recipeList.setSortType(sortType);
        recipeList.refresh();
    }
}

class OptionsBar extends HBox {
    private ComboBox<String> filterComboBox;
    private ToggleButton chronoSortButton;
    private ToggleButton alphaSortButton;

    private RunnableWithString chronoButtonCallback;
    private RunnableWithString alphaButtonCallback;

    public OptionsBar()
    {
        // Create filter options list
        ObservableList<String> filterOptions =
            FXCollections.observableArrayList("Filter: None", "Breakfast", "Lunch", "Dinner");

        filterComboBox = new ComboBox<>(filterOptions);
        filterComboBox.setPromptText("Filter by meal type");
        filterComboBox.getStyleClass().add("filter-combo-box");
        this.getStyleClass().add("options-bar");

        // Create chronological sort button
        chronoSortButton = createButton("", "clock-icon.png", "clock-rev-icon.png");
        chronoSortButton.getStyleClass().addAll("chrono-button");
        Tooltip chronoTooltip = new Tooltip("Sort by oldest to newest");
        chronoSortButton.setTooltip(chronoTooltip);

        // default sort
        chronoSortButton.setActive(true);

        chronoSortButton.setOnAction(e -> {
            if (chronoSortButton.isActive()) {
                // flip toggle if it is already active
                chronoSortButton.setToggle(!chronoSortButton.isToggled());
            } else {
                chronoSortButton.setActive(true);
                alphaSortButton.setActive(false);
            }

            if (chronoSortButton.isToggled()) {
                chronoTooltip.setText("Sort by oldest to newest");
            } else {
                chronoTooltip.setText("Sort by newest to oldest");
            }
            chronoButtonCallback.run(chronoSortButton.isToggled()
                    ? RecipeSortOrder.TIME_DESC.name()
                    : RecipeSortOrder.TIME_ASC.name());
        });

        // Create alphabetical sort button
        alphaSortButton = createButton("", "alpha-icon.png", "alpha-rev-icon.png");
        alphaSortButton.getStyleClass().addAll("alpha-button");
        Tooltip alphaTooltip = new Tooltip("Sort alphabetically");
        alphaSortButton.setTooltip(alphaTooltip);

        alphaSortButton.setOnAction(e -> {
            if (alphaSortButton.isActive()) {
                // flip toggle if it is already active
                alphaSortButton.setToggle(!alphaSortButton.isToggled());
            } else {
                alphaSortButton.setActive(true);
                chronoSortButton.setActive(false);
            }

            if (chronoSortButton.isToggled()) {
                alphaTooltip.setText("Sort alphabetically (Descending)");
            } else {
                alphaTooltip.setText("Sort alphabetically");
            }
            alphaButtonCallback.run(alphaSortButton.isToggled() ? RecipeSortOrder.ALPHA_DESC.name()
                                                                : RecipeSortOrder.ALPHA_ASC.name());
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Set alignment to place sort buttons on the right side
        setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(filterComboBox, spacer, chronoSortButton, alphaSortButton);
    }

    public void
    setFilterCallback(Runnable r)
    {
        filterComboBox.setOnAction(event -> {
            if (r != null) {
                r.run();
            }
        });
    }

    public void
    setAlphaSortButtonCallback(RunnableWithString r)
    {
        alphaButtonCallback = r;
    }

    public void
    setChronoSortButtonCallback(RunnableWithString r)
    {
        chronoButtonCallback = r;
    }

    private ToggleButton
    createButton(String buttonLabel, String resourceName, String toggledResourceName)
    {
        return new ToggleButton(buttonLabel, resourceName, toggledResourceName);
    }

    public String
    getSelectedValue()
    {
        switch (filterComboBox.getValue()) {
        case "Breakfast":
            return "breakfast";
        case "Lunch":
            return "lunch";
        case "Dinner":
            return "dinner";
        default:
            return null;
        }
    }
}

class ToggleButton extends Button {
    ImageView imageView;
    ImageView toggledImageView;

    private boolean active = false;
    private boolean toggled = false;

    public ToggleButton(String buttonLabel, String resourceName, String toggledResourceName)
    {
        super(buttonLabel);

        imageView = getImageView(resourceName);
        toggledImageView = getImageView(toggledResourceName);

        setGraphic(imageView);
    }

    public void
    setActive(boolean status)
    {
        if (status) {
            getStyleClass().add("active");
        } else {
            getStyleClass().remove("active");
        }
        active = status;
    }

    public void
    setToggle(boolean status)
    {
        if (status) {
            setGraphic(toggledImageView);
        } else {
            setGraphic(imageView);
        }
        toggled = status;
    }

    public boolean
    isActive()
    {
        return active;
    }

    public boolean
    isToggled()
    {
        return toggled;
    }

    private ImageView
    getImageView(String resourceName)
    {
        try (InputStream imageStream = getClass().getResource(resourceName).openStream()) {
            Image image = new Image(imageStream);
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(24);
            imageView.setFitHeight(24);

            return imageView;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

        HBox rightBox = new HBox(logoutButton, createButton);
        rightBox.setAlignment(Pos.CENTER_RIGHT);
        rightBox.setSpacing(10);

        getStyleClass().add("homepage-header");
        setLeft(appName);
        setRight(rightBox);

        setAlignment(appName, Pos.CENTER_LEFT);
        setAlignment(rightBox, Pos.CENTER_RIGHT);
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
    private static final int MAX_TITLE_LEN = 38;

    private Recipe recipe;
    public RecipeBox(Recipe recipe)
    {
        this.recipe = recipe;
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

    private static String
    trimTitle(String title)
    {
        String trimmed = title.trim();
        if (trimmed.length() > MAX_TITLE_LEN) {
            return title.substring(0, MAX_TITLE_LEN - 3).trim() + "...";
        }
        return trimmed;
    }

    private static String
    getMealType(Recipe r)
    {
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

    private static String
    getColorHexCode(String mealType)
    {
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

    public Recipe
    getRecipe()
    {
        return recipe;
    }
}

class RecipeList extends VBox {
    private List<RecipeBox> _recipeBoxes;
    private String mealTypeForFilter = null;
    private RecipeSortOrder sortType = null;

    public RecipeList(List<Recipe> recipes, RunnableWithId openRecipeDetailButtonCallback)
    {
        this.setSpacing(10); // sets spacing between contacts
        this.setPrefSize(500, 460);
        getStyleClass().add("recipe-list");

        List<RecipeBox> recipeBoxes = new ArrayList<>();
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);

            final int id = i;
            RecipeBox newRecipeBox = new RecipeBox(recipe);
            newRecipeBox.setOnMouseClicked(event -> openRecipeDetailButtonCallback.run(id));

            recipeBoxes.add(newRecipeBox);
        }

        this.getChildren().addAll(recipeBoxes);
        // store it for later
        _recipeBoxes = recipeBoxes;
    }

    public void
    setMealTypeFilter(String filterText)
    {
        mealTypeForFilter = filterText;
    }

    public void
    setSortType(String srt)
    {
        sortType = RecipeSortOrder.valueOf(srt);
    }

    public List<RecipeBox>
    applySort(List<RecipeBox> recipes)
    {
        switch (sortType) {
        case TIME_ASC:
            // do nothing, it is a default order from the server
            break;
        case TIME_DESC:
            Collections.reverse(recipes);
            break;
        case ALPHA_ASC:
            Collections.sort(recipes, new AlphabeticalRecipeTitleComparator());
            break;
        case ALPHA_DESC:
            Collections.sort(recipes, new AlphabeticalRecipeTitleComparator());
            Collections.reverse(recipes);
            break;
        default:
            break;
        }
        return recipes;
    }

    public void
    refresh()
    {
        List<RecipeBox> recipeBoxes = getFilteredRecipeBoxes();
        recipeBoxes = applySort(recipeBoxes);

        getChildren().clear();
        getChildren().addAll(recipeBoxes);
    }

    private List<RecipeBox>
    getRecipeBoxes()
    {
        return new ArrayList<>(_recipeBoxes);
    }

    private List<RecipeBox>
    getFilteredRecipeBoxes()
    {
        List<RecipeBox> newRecipeBoxes;
        if (mealTypeForFilter == null) {
            newRecipeBoxes = getRecipeBoxes();
        } else {
            newRecipeBoxes = new ArrayList<>();
            for (RecipeBox recipeBox : getRecipeBoxes()) {
                if (recipeBox.getRecipe().getMealType().equalsIgnoreCase(mealTypeForFilter)) {
                    newRecipeBoxes.add(recipeBox);
                }
            }
        }
        return newRecipeBoxes;
    }
}

/**
 * Alphabetical sort for recipe box
 *
 * Since RecipeBox is in HomePage UI context, we will maintain the comparator here
 */
class AlphabeticalRecipeTitleComparator implements Comparator<RecipeBox> {
    @Override
    public int
    compare(RecipeBox o1, RecipeBox o2)
    {
        return o1.getRecipe().getTitle().compareTo(o2.getRecipe().getTitle());
    }
}

/**
 * Sort order
 *
 * RecipeSortOrder enum helps us not miss the name of sort order
 */
enum RecipeSortOrder {
    TIME_ASC,
    TIME_DESC,
    ALPHA_ASC,
    ALPHA_DESC;
}
