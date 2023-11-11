package client.components;

import client.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class RecipeDetailsPage extends BorderPane {
    private Label header;
    private Label body;
    private DetailsFooter footer;

    public RecipeDetailsPage()
    {
        header = new Label();
        body = new Label();
        body.setWrapText(true);
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
    displayRecipe(Recipe recipe)
    {
        header.setText(recipe.getTitle());
        body.setText(recipe.getDescription());
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
        this.getChildren().addAll(cancelButton);

        this.editButton = new Button("Edit Recipe");
        this.getChildren().addAll(editButton);
        
        this.deleteButton = new Button("Delete Recipe");
        this.getChildren().addAll(deleteButton);
    }

    public void
    setOnCancel(Runnable onCancel)
    {
        this.cancelButton.setOnAction(e -> onCancel.run());
    }

    public void
    setOnSave(Runnable onSave)
    {
        this.saveButton.setOnAction(e -> onSave.run());
    }
}
