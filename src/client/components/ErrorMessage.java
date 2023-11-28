package client.components;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Error message component
 *
 * It will show the alert message with given string
 */
public class ErrorMessage {
    public ErrorMessage(String message) {
        Alert alert = new Alert(AlertType.WARNING, message);
        alert.show();
    }
}
