package client.components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/*
 * Error Page
 *
 * Display given message with the title.
 */
public class ErrorPage extends BorderPane {
  public ErrorPage(String message, Runnable retry) {
    VBox container = new VBox();
    container.setAlignment(Pos.CENTER);

    Label title = new Label("Sorry, we couldn't display the app");
    title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600;");

    Label desc = new Label(message);

    Button retryButton = new Button("Retry");
    retryButton.setOnAction((e) -> retry.run());

    container.getChildren().addAll(title, spacer(), desc, spacer(), retryButton);
    setCenter(container);
  }

  Label spacer() {
    Label spacer = new Label();
    spacer.setStyle("-fx-height: 10px;");
    return spacer;
  }
}
