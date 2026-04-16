package ui;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import model.User;


public class MainView extends VBox {

    private User loggedInUser;

    public MainView() {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        showLogin();
    }

    private void showLogin() {
        this.getChildren().clear();

        LoginView loginView = new LoginView(user -> {
            this.loggedInUser = user;
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
        Button statusBtn = new Button("View current status of my tickets");
        Button logoutBtn = new Button("Logout");

        submitBtn.setOnAction(e -> this.getChildren().setAll(new SubmitTicketView(this::showHome, loggedInUser)));
        statusBtn.setOnAction(e -> this.getChildren().setAll(new TicketStatusView(this::showHome, loggedInUser)));
        logoutBtn.setOnAction(e -> showLogin());

        this.getChildren().addAll(title, submitBtn, statusBtn, logoutBtn);
    }

    private void showAdminDashboard() {
        this.getChildren().clear();
        this.getChildren().add(new AdminDashboardView(
            this::showLogin,
            ticketID -> showTicketDetail(ticketID),
            this::showAnalytics
        ));
    }

    private void showTicketDetail(int ticketID) {
        this.getChildren().clear();
        this.getChildren().add(new TicketDetailView(ticketID, loggedInUser.getUserID(), this::showAdminDashboard));
    }

    private void showAnalytics() {
        this.getChildren().clear();
        this.getChildren().add(new TicketAnalyticsView(this::showAdminDashboard));
    }
}