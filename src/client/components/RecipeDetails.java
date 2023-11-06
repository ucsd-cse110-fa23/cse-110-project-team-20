package client.components;
//JUST A COPY OF NewRecipeConfirmPage.java
import client.Recipe;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class RecipeDetails extends BorderPane {
    private Label header;
    private Label body;
    private DetailsFooter footer;

    public RecipeDetails(Recipe recipe)
    {
        header = new Label(recipe.getTitle());
        body = new Label(recipe.getDescription());
        body.setWrapText(true);
        footer = new DetailsFooter();


        this.setTop(header);
        this.setCenter(new ScrollPane(body));
        this.setBottom(footer);
        this.setPrefSize(500, 800);
    }

    public void setCancelCallback(Runnable r) 
    {
        this.footer.setOnCancel(r);
    }

}

class DetailsFooter extends HBox {
    private Button saveButton;
    private Button cancelButton;

    public DetailsFooter()
    {
        this.cancelButton = new Button("Discard");
        this.getChildren().addAll(cancelButton);
    }

    public void setOnCancel(Runnable onCancel) {
        this.cancelButton.setOnAction(e -> onCancel.run());
    }

    public void setOnSave(Runnable onSave) {
        this.saveButton.setOnAction(e-> onSave.run());
    }
}
