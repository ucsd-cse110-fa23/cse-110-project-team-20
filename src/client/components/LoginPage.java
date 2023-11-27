package client.components;

import client.utils.runnables.RunnableForLogin;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/*
 * Login Page
 *
 * Provides username and password field to log in or to create a new account.
 * Also, provides checkbox option for staying logged in.
 */
public class LoginPage extends BorderPane {
    private TextField usernameField;
    private PasswordField passwordField;
    private CheckBox stayLoggedInCheckBox;
    private RunnableForLogin loginCallback;

    public LoginPage(RunnableForLogin loginCallback) {
        this.loginCallback = loginCallback;

        // get style sheet
        getStylesheets().add(getClass().getResource(
                "style.css").toExternalForm());

        getStyleClass().add("login-page");

        // App name label on top
        Label label = new Label("PantryPal");
        label.getStyleClass().add("app-name");


        // Login form: labels and fields for username, password, and stay logged in checkbox
        Label usernameLabel = new Label("Username:");
        usernameLabel.getStyleClass().add("username-label");

        Label passwordLabel = new Label("Password:");
        passwordLabel.getStyleClass().add("password-label");

        usernameField = new TextField();
        usernameField.getStyleClass().add("username-field");
        passwordField = new PasswordField();
        passwordField.getStyleClass().add("password-field");

        stayLoggedInCheckBox = new CheckBox("Stay logged in");
        stayLoggedInCheckBox.getStyleClass().add("stay-logged-in-checkbox");

        VBox box = new VBox(
                usernameLabel,
                usernameField,
                passwordLabel,
                passwordField,
                stayLoggedInCheckBox);
        box.getStyleClass().add("login-form");

        // button for login or create account
        Button button = new Button("Log In\nor\nCreate Account");
        button.getStyleClass().add("login-button");
        button.setOnAction(e -> {
            onLoginButtonClicked();
        });

        // set those components on the page
        setTop(label);
        setCenter(box);
        setBottom(button);

        setAlignment(label, Pos.CENTER);
        setAlignment(button, Pos.CENTER);
    }

    private void onLoginButtonClicked() {
        // when the button is clicked, username and password fields will be checked if it is empty or not.
        // then, call loginCallback when the form validation passes.

        if (usernameField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING, "Username is missing. Please check the username.");
            alert.show();
            return;
        }

        if (passwordField.getText().isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING, "Password is missing. Please check the password.");
            alert.show();
            return;
        }

        loginCallback.run(
                usernameField.getText(),
                passwordField.getText(),
                stayLoggedInCheckBox.isSelected());
    }

    /**
     * display any errors from the server
     *
     * @param error
     */
    public void onError(String error) {
        Alert alert = new Alert(AlertType.WARNING, String.format("Error: %s", error));
        alert.show();
    }
}
