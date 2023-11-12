package client.components;

import java.io.IOException;
import java.io.InputStream;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/*
 * Recording page
 *
 * Handles formatting of the recording interface along with basic logic that changes interface based on user input (start/stopRecording)
 * 
 */
public class RecordingPage extends VBox {
    private Button recodingButton;
    private Label title;

    private ImageView micOnImage;
    private ImageView micOffImage;

    private boolean recordingInProgress = false;

    public
    RecordingPage(String message)
    {
        getStylesheets().add(getClass().getResource(
            "style.css"
        ).toExternalForm());
        getStyleClass().add("recording-page");

        title = new Label(message);
        title.getStyleClass().add("recording-title");

        micOnImage = createImageView("mic-on-icon.png");
        micOffImage = createImageView("mic-off-icon.png");
        micOnImage.setVisible(false);

        // ref: https://stackoverflow.com/questions/28558165
        micOnImage.managedProperty().bind(micOnImage.visibleProperty());
        micOffImage.managedProperty().bind(micOffImage.visibleProperty());

        HBox micImageContainer = new HBox(micOnImage, micOffImage);
        micImageContainer.getStyleClass().add("mic-image");

        // Add the buttons and text fields
        recodingButton = new Button("Start Recording");
        recodingButton.getStyleClass().add("recording-button");

        this.getChildren().addAll(title, micImageContainer, recodingButton);
    }

    public void
    setButtonCallbacks(RecordingPageCallbacks callbacks)
    {
        // Start Button
        recodingButton.setOnAction(e -> {
            if (recordingInProgress) {
                stopRecording();
                callbacks.getOnRecordingCompleted().run();
            } else {
                startRecording();
                recordingInProgress = true;
                callbacks.getOnRecordingStarted().run();
            }
        });
    }

    private void
    startRecording()
    {
        this.recodingButton.setText("Stop Recording");
        micOnImage.setVisible(true);
        micOffImage.setVisible(false);
    }

    private void
    stopRecording()
    {
        this.recodingButton.setText("Start Recording");
        micOnImage.setVisible(false);
        micOffImage.setVisible(true);
    }

    private ImageView
    createImageView(String resourceName) {
        ImageView imageView = new ImageView();

        try (InputStream imageStream = getClass().getResource(resourceName).openStream()) {
            Image image = new Image(imageStream);
            imageView = new ImageView(image);
            imageView.setFitWidth(92);
            imageView.setFitHeight(92);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageView;
    }
}
