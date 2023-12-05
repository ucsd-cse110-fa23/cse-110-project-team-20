package client.components;

import java.io.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;

/**
 * SharedRecipeModal
 *
 * Display given shared url as a modal form. Allows to copy the given url or close the window.
 */
public class SharedRecipeModal {
    public SharedRecipeModal(String sharedUrl)
    {
        TextInputDialog dialog = new TextInputDialog(sharedUrl);
        dialog.setTitle("");

        Button copyButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        Button closeButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.CANCEL);

        copyButton.setText("Copy to clipboard");
        copyButton.setOnAction(e -> {
            // ref: https://docs.oracle.com/javafx/2/api/javafx/scene/input/Clipboard.html
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(sharedUrl);
            content.put(DataFormat.URL, sharedUrl);
            clipboard.setContent(content);
        });

        closeButton.setText("Close");

        dialog.setHeaderText("Share your Recipe!");
        dialog.getDialogPane().setMinWidth(400);
        ImageView copyIcon = createImageView("share-icon.png");
        dialog.setGraphic(copyIcon);
        dialog.show();
    }

    private ImageView
    createImageView(String resourceName)
    {
        ImageView imageView = new ImageView();

        try (InputStream imageStream = getClass().getResource(resourceName).openStream()) {
            Image image = new Image(imageStream);
            imageView = new ImageView(image);
            imageView.setFitWidth(40);
            imageView.setFitHeight(50);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageView;
    }
}
