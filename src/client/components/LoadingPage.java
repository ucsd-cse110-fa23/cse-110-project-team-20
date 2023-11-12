package client.components;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/*
 * Loading page for transitions
 *
 * Primarily used during the transition between the user delivering their second input and the API
 * generating the appropriate recipe
 */
public class LoadingPage extends VBox {
    private ProgressIndicator loadingBar;
    private Label loadingText;

    String titleStyle = "-fx-font-size: 24px; -fx-font-weight: bold;";
    String indStyle = "-fx-fill: null; ";
    public LoadingPage()
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
