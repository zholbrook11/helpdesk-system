package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainView extends VBox {

    public MainView() {
        showHome();
    }

    private void showHome() {
        this.getChildren().clear();
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        Label title = new Label("Help Desk System");
        title.getStyleClass().add("title");

        Button submitBtn = new Button("Submit Ticket");
        Button adminBtn = new Button("Admin Dashboard");

        submitBtn.setOnAction(e -> {
            this.getChildren().setAll(new SubmitTicketView(this::showHome));
        });

        adminBtn.setOnAction(e -> {
            this.getChildren().setAll(new AdminDashboardView(this::showHome));
        });

        this.getChildren().addAll(title, submitBtn, adminBtn);
    }
}