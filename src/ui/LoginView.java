package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.User;
import storage.UserDAO;

import java.util.function.Consumer;

public class LoginView extends VBox {

    public LoginView(Consumer<User> onLoginSuccess) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        Label title = new Label("Help Desk Login");
        title.getStyleClass().add("title");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setFocusTraversable(false);
        usernameField.setMaxWidth(300);

        TextField passwordField = new TextField();
        passwordField.setPromptText("Password");
        passwordField.setFocusTraversable(false);
        passwordField.setMaxWidth(300);

        Label messageLabel = new Label();
        messageLabel.getStyleClass().add("error");

        Button loginBtn = new Button("Login");
        loginBtn.setOnAction(e -> {
            UserDAO dao = new UserDAO();
            User user = dao.login(usernameField.getText(), passwordField.getText());

            if (user != null) {
                onLoginSuccess.accept(user);
            } else {
                messageLabel.setText("Invalid username or password");
            }
        });

        this.getChildren().addAll(title, usernameField, passwordField, loginBtn, messageLabel);
    }
}