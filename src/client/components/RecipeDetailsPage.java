package client.components;

import client.Recipe;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


/*
 * Recipe details
 *
 * Handles the structure of the details page that is transitioned to once the user clicks on a recipe on the home page.
 * Contains a cancel, delete, and edit button, along with with their associated callbacks.
 */
public class RecipeDetailsPage extends BorderPane {
    private RecipeHeader header;
    private Label body;
    private DetailsFooter footer;

    public RecipeDetailsPage()
    {
        // get style sheet
        getStylesheets().add(getClass().getResource(
            "style.css"
        ).toExternalForm());

        // style header
        header = new RecipeHeader();
        header.getStyleClass().add("recipedetails-header");

        // style body
        body = new Label();
        body.setWrapText(true);
        body.getStyleClass().add("recipe-body");
        



        // style footer
        footer = new DetailsFooter();

        this.setTop(header);
        this.setCenter(new ScrollPane(body));
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
    displayRecipe(Recipe recipe)
    {
        Label recipeTitle = new Label(recipe.getTitle());
        recipeTitle.getStyleClass().add("recipe-title");
        this.header.setLeft(recipeTitle);
        header.setAlignment(recipeTitle, Pos.CENTER_LEFT);
        this.body.setText(recipe.getDescription());
    }
}

// class so that header type matches across pages
class RecipeHeader extends BorderPane {
    public RecipeHeader() {
        getStyleClass().add("recipedetails-header");
    }
}


class DetailsFooter extends HBox {
    private Button saveButton;
    private Button cancelButton;
    private Button editButton;
    private Button deleteButton;

    public DetailsFooter()
    {
        this.cancelButton = new Button("Go Back");
        this.cancelButton.getStyleClass().add("cancel-button");
        
        this.editButton = new Button("Edit Recipe");
        this.editButton.getStyleClass().add("edit-button");
        
        this.deleteButton = new Button("Delete Recipe");
        this.deleteButton.getStyleClass().add("delete-button");

        this.setSpacing(10); // Set the spacing between buttons
        this.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(cancelButton, editButton, deleteButton);
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
            Alert alert = new Alert(
                AlertType.WARNING,
                "Do you want to delete this recipe?",
                ButtonType.CANCEL,
                ButtonType.YES);

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
}
