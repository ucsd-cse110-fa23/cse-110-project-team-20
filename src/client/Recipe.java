package com.example.project;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class Recipe extends HBox {
    private String name;
    private String description;
    private Label nameLabel;
    public Recipe(String name, String description) {
        this.name = name;
        this.description =description;
        this.nameLabel = new Label(name);
        this.getChildren().add(nameLabel);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
