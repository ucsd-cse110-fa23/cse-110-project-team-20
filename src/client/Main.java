package client;
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

class AnimatedLoadingBar extends VBox {
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

class RecordingPage extends VBox {
    private Button startButton;
    private Button stopButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private Label title;

    private boolean recording;
    private Runnable doneCallback;
    private String fileName;
    private String message;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle =
        "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle =
        "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    String titleStyle = "-fx-font-size: 24px; -fx-font-weight: bold;";

    RecordingPage(String message, String fileName, Runnable doneCallback)
    {
        this.recording = false;
        // File to save audio to
        this.fileName = fileName;
        this.doneCallback = doneCallback;
        this.message = message;
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
        startButton = new Button("Start recording");
        startButton.setStyle(defaultButtonStyle);

        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);

        this.getChildren().addAll(title, startButton, recordingLabel);

        // Get the audio format
        audioFormat = getAudioFormat();

        // Add the listeners to the buttons
        addListeners();
    }

    public void
    addListeners()
    {
        // Start Button
        startButton.setOnAction(e -> { toggleRecording(); });
    }

    private AudioFormat
    getAudioFormat()
    {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;

        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;

        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;

        // whether the data is signed or unsigned.
        boolean signed = true;

        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;

        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    private void
    toggleRecording()
    {
        if (this.recording) {
            stopRecording();
            this.doneCallback.run();
        } else {
            startRecording();
            this.startButton.setText("Stop recording");
        }
        this.recording = !this.recording;
    }

    private void
    startRecording()
    {
        Thread t = new Thread(new Runnable() {
            @Override public void run()
            {
                try {
                    // the format of the TargetDataLine
                    DataLine.Info dataLineInfo =
                        new DataLine.Info(TargetDataLine.class, audioFormat);
                    // the TargetDataLine used to capture audio data from the microphone
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();
                    recordingLabel.setVisible(true);

                    // the AudioInputStream that will be used to write the audio data to a file
                    AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);

                    // the file that will contain the audio data
                    File audioFile = new File(fileName);
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                    recordingLabel.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void
    stopRecording()
    {
        targetDataLine.stop();
        targetDataLine.close();
    }
}

public class Main extends Application {
    private Stage primaryStage;
    private Scene ingredients, mealType, loading;

    public static void
    main(String[] args)
    {
        launch(args);
    }

    @Override
    public void
    start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;

        RecordingPage mealTypePage =
            new RecordingPage("What kind of meal do you want?\nLunch, Dinner, Snack etc.",
                "meal_type.wav", () -> { primaryStage.setScene(ingredients); });
        RecordingPage ingredientsPage = new RecordingPage("What ingredients do you have?",
            "ingredients.wav", () -> primaryStage.setScene(loading));

        AnimatedLoadingBar loadingPage = new AnimatedLoadingBar();
        loadingPage.setLoadingText("Finding the perfect recipe...");

        mealType = new Scene(mealTypePage, 500, 500);
        ingredients = new Scene(ingredientsPage, 500, 500);
        loading = new Scene(loadingPage, 500, 500);

        // Set the title of the app
        primaryStage.setTitle("PantryPal");
        // Create scene of mentioned size with the border pane
        primaryStage.setScene(mealType);
        // Make window non-resizable
        primaryStage.setResizable(false);
        // Show the app
        primaryStage.show();
    }
}
