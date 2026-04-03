package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Ticket;
import service.TicketService;

public class TicketDetailView extends VBox {

    private Ticket ticket;
    private TicketService service;
    private Runnable goBack;

    public TicketDetailView(Ticket ticket, Runnable goBack) {
        this.ticket = ticket;
        this.service = new TicketService();
        this.goBack = goBack;

        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(15);
        this.setPadding(new javafx.geometry.Insets(20));

        Label title = new Label("Ticket Details");
        title.getStyleClass().add("title");

        VBox details = new VBox(10);
        details.setAlignment(Pos.TOP_LEFT);
        details.setMaxWidth(600);

        Label idLabel = new Label("ID: " + ticket.getId());
        Label titleLabel = new Label("Title: " + ticket.getTitle());
        Label descLabel = new Label("Description: " + ticket.getDescription());
        descLabel.setWrapText(true);

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Open", "In Progress", "Closed");
        statusBox.setValue(ticket.getStatus());

        ComboBox<String> catBox = new ComboBox<>();
        catBox.getItems().addAll("Hardware", "Software", "Network", "Other");
        catBox.setValue(ticket.getCategory());

        ComboBox<String> priBox = new ComboBox<>();
        priBox.getItems().addAll("Low", "Medium", "High", "Urgent");
        priBox.setValue(ticket.getPriority());

        HBox controls = new HBox(10);
        controls.getChildren().addAll(new Label("Status:"), statusBox, new Label("Category:"), catBox, new Label("Priority:"), priBox);

        Button saveBtn = new Button("Save Changes");
        Label saveStatus = new Label();
        saveBtn.setOnAction(e -> {
            ticket.setStatus(statusBox.getValue());
            ticket.setCategory(catBox.getValue());
            ticket.setPriority(priBox.getValue());
            service.updateTicket(ticket);
            saveStatus.setText("Changes saved!");
        });

        Label commentsTitle = new Label("Comments:");
        ListView<String> commentsList = new ListView<>();
        for (var c : ticket.getComments()) {
            commentsList.getItems().add(c.getAuthor() + " (" + c.getTimestamp() + "): " + c.getText());
        }
        commentsList.setMaxHeight(200);

        TextArea commentField = new TextArea();
        commentField.setPromptText("Add a comment...");
        commentField.setMaxHeight(60);

        Button addCommentBtn = new Button("Add Comment");
        addCommentBtn.setOnAction(e -> {
            if (!commentField.getText().isEmpty()) {
                service.addComment(ticket.getId(), commentField.getText(), "Admin"); // assume admin
                commentField.clear();
                // refresh comments
                commentsList.getItems().clear();
                Ticket updatedTicket = service.getTicketById(ticket.getId()); // reload
                for (var c : updatedTicket.getComments()) {
                    commentsList.getItems().add(c.getAuthor() + " (" + c.getTimestamp() + "): " + c.getText());
                }
            }
        });

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> goBack.run());

        details.getChildren().addAll(idLabel, titleLabel, descLabel, controls, saveBtn, saveStatus, commentsTitle, commentsList, commentField, addCommentBtn);

        this.getChildren().addAll(title, details, backBtn);
    }
}