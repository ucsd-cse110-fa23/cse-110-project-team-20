package client.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class RecordingPage extends VBox {
    private Button recodingButton;
    private Label title;

    private boolean recordingInProgress = false;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle =
        "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle =
        "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    String titleStyle = "-fx-font-size: 24px; -fx-font-weight: bold;";

    public RecordingPage(String message)
    {
        // Set properties for the flowpane
        this.setPrefSize(370, 120);
        this.setPadding(new Insets(5, 0, 5, 5));
        this.setSpacing(100);
        this.setAlignment(Pos.CENTER);
        // this.setVgap(10);
        // this.setHgap(10);
        // this.setPrefWrapLength(170);

        title = new Label(message);
        title.setStyle(titleStyle);

        // Add the buttons and text fields
        recodingButton = new Button("Start recording");
        recodingButton.setStyle(defaultButtonStyle);

        this.getChildren().addAll(title, recodingButton);
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

    public void
    startRecording()
    {
        this.recodingButton.setText("Stop recording");
    }

    public void
    stopRecording()
    {
        this.recodingButton.setText("Start Recording");
    }
}
