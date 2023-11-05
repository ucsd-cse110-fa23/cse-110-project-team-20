package client;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class RecipeBox extends HBox {
    private Recipe recipe;
    private Label nameLabel;
    public RecipeBox(Recipe recipe) {
        this.recipe = recipe;
        this.nameLabel = new Label(recipe.getTitle());
        this.getChildren().add(nameLabel);
    }

    public Recipe getRecipe() {
        return recipe;
    }
}
