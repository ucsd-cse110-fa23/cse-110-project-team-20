package client.components;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/*
 * Error Page
 *
 * Display given message with the title.
 */
public class ErrorPage extends BorderPane {
    public ErrorPage(String message, Runnable retry)
    {
        this(message, retry, "Retry");
    }

    public ErrorPage(String message, Runnable retry, String buttonLabel)
    {
        getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        // Load the error icon
        ImageView errorIcon =
            new ImageView(new Image(getClass().getResourceAsStream("cross-icon.png")));
        errorIcon.setFitWidth(100); // Adjust the size as needed
        errorIcon.setFitHeight(100);

        Label title = new Label("Oops, something went wrong.");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: 600;");

        Label desc = new Label(message);
        desc.setWrapText(true);
        desc.setStyle("-fx-padding: 20px");

        Button retryButton = new Button(buttonLabel);
        retryButton.setOnAction((e) -> retry.run());


        container.getChildren().addAll(errorIcon, title, spacer(), desc, spacer(), retryButton);
        setCenter(container);
    }

    Label
    spacer()
    {
        Label spacer = new Label();
        spacer.setStyle("-fx-height: 10px;");
        return spacer;
    }
}
