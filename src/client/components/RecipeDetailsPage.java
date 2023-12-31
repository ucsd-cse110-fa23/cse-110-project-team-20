package client.components;

import client.Recipe;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/*
 * Recipe details
 *
 * Handles the structure of the details page that is transitioned to once the user clicks on a
 * recipe on the home page. Contains a cancel, delete, and edit button, along with with their
 * associated callbacks.
 */
public class RecipeDetailsPage extends BorderPane {
    private RecipeHeader header;
    private VBox body;
    private DetailsFooter footer;

    public RecipeDetailsPage()
    {
        // get style sheet
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        // style header
        header = new RecipeHeader();
        header.getStyleClass().add("recipedetails-header");

        // style body
        body = new VBox();

        ScrollPane scrollPane = new ScrollPane(body);
        scrollPane.getStyleClass().add("recipe-scroll-pane");
        scrollPane.setFitToWidth(true);

        // style footer
        footer = new DetailsFooter();

        this.setTop(header);
        this.setCenter(scrollPane);
        this.setBottom(footer);
        this.setPrefSize(500, 800);
    }

    public void
    setCancelCallback(Runnable r)
    {
        this.footer.setOnCancel(r);
    }

    public void
    setDeleteCallback(Runnable r)
    {
        this.footer.setOnDelete(r);
    }

    public void
    setEditCallback(Runnable r)
    {
        this.footer.setOnEdit(r);
    }

    public void
    setShareCallback(Runnable r)
    {
        this.footer.setOnShare(r);
    }

    public void
    displayRecipe(Recipe recipe)
    {
        Label recipeTitle = new Label(recipe.getTitle());
        recipeTitle.setWrapText(true);
        recipeTitle.getStyleClass().add("recipe-title");
        this.header.setCenter(recipeTitle);

        Label recipeDescription = new Label(recipe.getDescription());
        recipeDescription.getStyleClass().add("recipe-body");
        String imageurl = recipe.getImageUrl();
        ImageView imageView = new ImageView(new Image(imageurl));
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.getStyleClass().add("recipe-image");
        BorderPane pane = new BorderPane();
        pane.setPrefSize(500, 200);
        pane.setCenter(imageView);

        this.body.getChildren().addAll(pane, recipeDescription);
    }
}

// class so that header type matches across pages
class RecipeHeader extends BorderPane {
    // private Button cancelButton;

    public RecipeHeader()
    {
        getStyleClass().add("recipedetails-header");
        // this.cancelButton = new Button("\u2190");
        // this.cancelButton.getStyleClass().add("cancel-button");
        // setLeft(cancelButton);
        // setAlignment(cancelButton, Pos.TOP_LEFT);
        // cancelButton.setMaxWidth(Region.USE_PREF_SIZE);
    }
    // public void
    // setOnCancel(Runnable onCancel)
    // {
    //     this.cancelButton.setOnAction(e -> onCancel.run());
    // }
}

class DetailsFooter extends HBox {
    private Button saveButton;
    private Button cancelButton;
    private Button editButton;
    private Button deleteButton;
    private Button shareButton;

    public DetailsFooter()
    {
        this.cancelButton = new Button("\u2190");
        this.cancelButton.getStyleClass().add("cancel-button");

        this.editButton = new Button("Edit Recipe");
        this.editButton.getStyleClass().add("edit-button");

        this.deleteButton = new Button("Delete Recipe");
        this.deleteButton.getStyleClass().add("delete-button");

        this.shareButton = new Button("Share Recipe");
        this.shareButton.getStyleClass().add("share-button");

        this.setSpacing(10); // Set the spacing between buttons
        this.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(cancelButton, editButton, deleteButton, shareButton);
    }

    public void
    setOnCancel(Runnable onCancel)
    {
        this.cancelButton.setOnAction(e -> onCancel.run());
    }

    public void
    setOnEdit(Runnable onEdit)
    {
        this.editButton.setOnAction(e -> onEdit.run());
    }

    public void
    setOnDelete(Runnable onDelete)
    {
        // delete event only triggers when user clicks YES button on the confirmation
        // ref: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Alert.html
        this.deleteButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.WARNING, "Do you want to delete this recipe?",
                ButtonType.CANCEL, ButtonType.YES);

            // Get the DialogPane
            DialogPane dialogPane = alert.getDialogPane();

            // Customize the appearance
            dialogPane.setBackground(
                new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
            dialogPane.getStyleClass().add("delete-alert");

            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                onDelete.run();
            }
        });
    }

    public void
    setOnSave(Runnable onSave)
    {
        this.saveButton.setOnAction(e -> onSave.run());
    }

    public void
    setOnShare(Runnable onShare)
    {
        this.shareButton.setOnAction(e -> onShare.run());
    }
}
