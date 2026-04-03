package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.Ticket;

public class MainView extends VBox {

    public MainView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        showLogin();
    }

    private void showLogin() {
        this.getChildren().clear();

        LoginView loginView = new LoginView(user -> {
            // after successful login, show corresponding dashboard
            if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                showAdminDashboard();
            } else {
                showHome();
            }
        });

        this.getChildren().setAll(loginView);
    }

    private void showHome() {
        this.getChildren().clear();
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        Label title = new Label("Help Desk System");
        title.getStyleClass().add("title");

        Button submitBtn = new Button("Submit Ticket");
        Button logoutBtn = new Button("Logout");

        submitBtn.setOnAction(e -> this.getChildren().setAll(new SubmitTicketView(this::showHome)));
        logoutBtn.setOnAction(e -> showLogin());

        this.getChildren().addAll(title, submitBtn, logoutBtn);
    }

    private void showAdminDashboard() {
        this.getChildren().clear();
        this.getChildren().add(new AdminDashboardView(this::showLogin, ticket -> showTicketDetail(ticket)));
    }

    private void showTicketDetail(Ticket ticket) {
        this.getChildren().clear();
        this.getChildren().add(new TicketDetailView(ticket, this::showAdminDashboard));
    }
}