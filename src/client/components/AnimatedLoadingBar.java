package client.components;
import java.io.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.sampled.*;

public class AnimatedLoadingBar extends VBox {
    private ProgressIndicator loadingBar;
    private Label loadingText;

    String titleStyle = "-fx-font-size: 24px; -fx-font-weight: bold;";
    String indStyle = "-fx-fill: null; ";
    public AnimatedLoadingBar()
    {
        this.setPrefSize(370, 120);
        this.setSpacing(100);
        this.setAlignment(Pos.CENTER);
        this.setFillWidth(true);

        loadingText = new Label("Loading...");
        loadingText.setStyle(titleStyle);
        loadingBar = new ProgressIndicator();
        loadingBar.setMinSize(200, 200);

        // Create an animation for the loading bar
        Timeline loadingAnimation = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(loadingBar.progressProperty(), 0)),
            new KeyFrame(Duration.seconds(2), new KeyValue(loadingBar.progressProperty(), 1)));
        loadingAnimation.setCycleCount(Timeline.INDEFINITE);
        loadingAnimation.play();

        this.getChildren().addAll(loadingText, loadingBar);
        this.setSpacing(20); // Adjust spacing between children
    }

    public void
    setLoadingText(String text)
    {
        loadingText.setText(text);
    }
}
