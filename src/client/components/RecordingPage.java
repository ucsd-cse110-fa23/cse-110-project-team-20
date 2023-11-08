package client.components;
import java.io.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javax.sound.sampled.*;

public class RecordingPage extends VBox {
    private Button recodingButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label title;

    private String fileName;
    private boolean recordingInProgress = false;

    // Set a default style for buttons and fields - background color, font size,
    // italics
    String defaultButtonStyle =
        "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle =
        "-fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    String titleStyle = "-fx-font-size: 24px; -fx-font-weight: bold;";

    public RecordingPage(String message, String fileName)
    {
        // File to save audio to
        this.fileName = fileName;
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

        // Get the audio format
        audioFormat = getAudioFormat();
    }

    public void
    setButtonCallback(Runnable r)
    {
        // Start Button
        recodingButton.setOnAction(e -> {
            if (recordingInProgress) {
                stopRecording();
                r.run();
            } else {
                startRecording();
                recordingInProgress = true;
            }
        });
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

    public void
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

                    // the AudioInputStream that will be used to write the audio data to a file
                    AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);

                    // the file that will contain the audio data
                    File audioFile = new File(fileName);
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.recodingButton.setText("Stop recording");
        t.start();
    }

    public void
    stopRecording()
    {
        targetDataLine.stop();
        targetDataLine.close();
        this.recodingButton.setText("Start Recording");
    }
}
