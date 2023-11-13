package client.components;

import client.Recipe;
import client.utils.runnables.RunnableWithId;
import client.utils.runnables.RunnableWithRecipe;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


/*
 * Recipe details editing
 *
 * Controls the format of the editing interface along with how edits interact both locally and on the HTTP server.
 * Similar in format to the details page, except there is a save button and callback in lieu of delete and edit.
 */
public class RecipeEditPage extends BorderPane{
    private Label header;
    private TextArea body;
    private EditFooter footer;

    public RecipeEditPage()
    {
        /* Changed Labels to TextFields */
        header = new Label();
        body = new TextArea();
        // body.setWrapText(true);
        footer = new EditFooter();

        getStylesheets().add(getClass().getResource(
            "style.css"
        ).toExternalForm());

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

    /* Added Functions */
    public void
    setSaveCallBack(RunnableWithRecipe r)
    {
        this.footer.setOnSave(r, this.header, this.body);
    }

    public void
    displayRecipe(Recipe recipe)
    {
        header.setText(recipe.getTitle());
        body.setText(recipe.getDescription());
    }

    //TODO: added getter for new body
    public String
    getUpdatedBody()
    {
        return this.body.toString();
    }
}

class EditFooter extends HBox {
    private Button saveButton;
    private Button cancelButton;

    /** 
     * Constructor for Edit Page's footer
     */
    public EditFooter()
    {
        getStyleClass().add("recipe-edit-footer");
        this.cancelButton = new Button("Cancel");
        this.cancelButton.getStyleClass().add("cancel-button");

        this.saveButton = new Button("Save");
        this.saveButton.getStyleClass().add("save-button");

        this.setSpacing(10); // Set the spacing between buttons
        this.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(cancelButton, saveButton);
    }

    /**
     * The following set action of their buttons
     */
    public void
    setOnCancel(Runnable onCancel)
    {
        this.cancelButton.setOnAction(e -> onCancel.run());
    }

    //TODO: Added 
    public void
    setOnSave(RunnableWithRecipe onSave, Label title, TextArea body)
    {
        this.saveButton.setOnAction(e -> onSave.run(new Recipe(title.getText(), body.getText())));
    }
}
