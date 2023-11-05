package client;
import client.Controller;
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
        Controller controller = new Controller(primaryStage);
    }
}
