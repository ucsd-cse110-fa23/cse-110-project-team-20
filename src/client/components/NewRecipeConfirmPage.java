package client.components;

import client.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class NewRecipeConfirmPage extends BorderPane {
    private Label header;
    private Label body;
    private Footer footer;

    public NewRecipeConfirmPage(Recipe recipe)
    {
        header = new Label(recipe.getTitle());
        body = new Label(recipe.getDescription());
        body.setWrapText(true);
        footer = new Footer();


        this.setTop(header);
        this.setCenter(new ScrollPane(body));
        this.setBottom(footer);
        this.setPrefSize(500, 800);
    }

    public void setCancelCallback(Runnable r) 
    {
        this.footer.setOnCancel(r);
    }

    public void setSaveCallback(Runnable r)
    {
        this.footer.setOnSave(r);
    }


}

class Footer extends HBox {
    private Button saveButton;
    private Button cancelButton;

    public Footer()
    {
        this.cancelButton = new Button("Discard");
        this.saveButton = new Button("Save");

        this.getChildren().addAll(cancelButton, saveButton);
    }

    public void setOnCancel(Runnable onCancel) {
        this.cancelButton.setOnAction(e -> onCancel.run());
    }

    public void setOnSave(Runnable onSave) {
        this.saveButton.setOnAction(e-> onSave.run());
    }
}
