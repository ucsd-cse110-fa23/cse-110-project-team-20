package client;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class RecipeBox extends HBox {
    private Recipe recipe;
    private Label nameLabel;
    public RecipeBox(String title, String description) {
        recipe = new Recipe(title, description);
        this.nameLabel = new Label(title);
        this.getChildren().add(nameLabel);
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
