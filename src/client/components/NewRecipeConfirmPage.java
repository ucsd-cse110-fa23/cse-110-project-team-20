package client.components;

import client.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.io.InputStream;

/*
 * New recipe page for recipe generated by the API
 *
 * Displays the title, ingredients, and instructions of the recipe generated by the API.
 * Also houses callbacks for cancelling and saving, both of which lead to the home page.
 */

// Primary class that controls styling + callable callbacks
public class NewRecipeConfirmPage extends BorderPane {
    private Label header;
    private TextArea body;
    private Footer footer;

    public NewRecipeConfirmPage(Recipe recipe)
    {
        getStylesheets().add(getClass().getResource(
            "style.css"
        ).toExternalForm());

        header = new Label(recipe.getTitle());
        header.getStyleClass().add("recipe-title");

        body = new TextArea(recipe.getDescription());
        body.getStyleClass().add("recipe-description");

        footer = new Footer();
        footer.getStyleClass().add("recipe-actions");

        body.setEditable(false);
        body.setWrapText(true);

        this.setTop(header);
        this.setCenter(body);
        this.setBottom(footer);
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
    private Button regenButton;

    public Footer()
    {
        cancelButton = createButton("Discard", "cross-icon.png");
        cancelButton.getStyleClass().addAll(
            "action-button", "discard-button");

        saveButton = createButton("Save", "plus-icon.png");
        saveButton.getStyleClass().addAll(
            "action-button", "save-button");
            
        regenButton = createButton("Regenerate Recipe", "regen-icon.png");
        regenButton.getStyleClass().addAll(
            "action-button", "save-button");

        Region spacer = new Region();
        setHgrow(spacer, Priority.ALWAYS);

        getChildren().addAll(cancelButton, saveButton, regenButton);
    }

    public void setOnCancel(Runnable onCancel) {
        cancelButton.setOnAction(e -> onCancel.run());
    }

    public void setOnSave(Runnable onSave) {
        saveButton.setOnAction(e-> onSave.run());
    }

    public void setOnRegen(Runnable onRegen){
        regenButton.setOnAction(e-> onRegen.run());
    }

    // ref: https://jenkov.com/tutorials/javafx/button.html
    private Button createButton(String buttonLabel, String resourceName) {
        Button btn = new Button(buttonLabel);

        try (InputStream imageStream = getClass().getResource(resourceName).openStream()) {
            Image image = new Image(imageStream);
            ImageView imageView = new ImageView(image);

            imageView.setFitWidth(16);
            imageView.setFitHeight(16);

            btn = new Button(buttonLabel, imageView);
            btn.setGraphicTextGap(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return btn;
    }
}
