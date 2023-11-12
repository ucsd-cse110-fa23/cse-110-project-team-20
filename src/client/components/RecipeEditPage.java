package client.components;

import client.Recipe;
import client.utils.runnables.RunnableWithId;
import client.utils.runnables.RunnableWithRecipe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.TextArea;

import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;


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

    //TODO: Added 
    public void
    setOnSave(RunnableWithRecipe onSave, Label title, TextArea body)
    {
        this.saveButton.setOnAction(e -> onSave.run(new Recipe(title.getText(), body.getText())));
    }
}
