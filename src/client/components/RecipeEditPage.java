package client.components;

import client.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


public class RecipeEditPage extends BorderPane{
    private TextField header;
    private TextField body;
    private EditFooter footer;

    public RecipeEditPage()
    {
        /* Changed Labels to TextFields */
        header = new TextField();
        body = new TextField();
        // body.setWrapText(true);
        footer = new EditFooter();

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
    setSaveCallBack(Runnable r)
    {
        this.footer.setOnSave(r);
    }

    public void
    displayRecipe(Recipe recipe)
    {
        header.setText(recipe.getTitle() + " (Editing)");
        body.setText(recipe.getDescription());
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
        this.cancelButton = new Button("Go Back");
        this.saveButton = new Button("Save");
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

    public void
    setOnSave(Runnable onSave)
    {
        this.saveButton.setOnAction(e -> onSave.run());
    }
}
