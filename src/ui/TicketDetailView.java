package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Comment;
import model.Ticket;
import storage.CommentDAO;
import storage.TicketDAO;

import java.util.List;

public class TicketDetailView extends VBox {

    private final Ticket ticket;
    private final int currentUserID;
    private final TicketDAO ticketDAO = new TicketDAO();
    private final CommentDAO commentDAO = new CommentDAO();
    private final Runnable onBack;
    private VBox commentsSection;

    public TicketDetailView(int ticketID, int currentUserID, Runnable onBack) {
        this.currentUserID = currentUserID;
        this.onBack = onBack;

        this.ticket = ticketDAO.getTicketById(ticketID);
        if (ticket == null) {
            this.getChildren().add(new Label("Ticket not found"));
            return;
        }

        this.setSpacing(15);
        this.setPadding(new javafx.geometry.Insets(20));
        this.setStyle("-fx-border-color: #ccc; -fx-border-width: 1;");

        // Header with title and back button
        VBox header = createHeader();
        this.getChildren().add(header);

        // Ticket details section
        VBox detailsSection = createDetailsSection();
        this.getChildren().add(detailsSection);

        // Comments section
        ScrollPane commentsScroll = new ScrollPane();
        this.commentsSection = createCommentsSection();
        commentsScroll.setContent(this.commentsSection);
        commentsScroll.setFitToWidth(true);
        commentsScroll.setPrefHeight(300);
        this.getChildren().add(this.commentsSection);

        // Comment input section
        VBox commentInputSection = createCommentInputSection();
        this.getChildren().add(commentInputSection);

        // Save and back buttons
        HBox buttons = createButtonsSection();
        this.getChildren().add(buttons);
    }

    private VBox createHeader() {
        VBox header = new VBox();
        header.setSpacing(5);

        HBox headerTop = new HBox();
        headerTop.setSpacing(15);
        headerTop.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> onBack.run());

        Label titleLabel = new Label("Ticket #" + ticket.getTicketID() + ": " + ticket.getTitle());
        titleLabel.getStyleClass().add("title");
        titleLabel.setWrapText(true);

        headerTop.getChildren().addAll(backBtn, titleLabel);
        header.getChildren().add(headerTop);

        return header;
    }

    private VBox createDetailsSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 10;");

        Label sectionTitle = new Label("Ticket Details");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        // Ticket info grid
        VBox infoBox = new VBox();
        infoBox.setSpacing(8);

        // Description
        Label descLabel = new Label("Description:");
        descLabel.setStyle("-fx-font-weight: bold;");
        TextArea descArea = new TextArea(ticket.getDescription());
        descArea.setEditable(false);
        descArea.setWrapText(true);
        descArea.setPrefRowCount(3);
        infoBox.getChildren().addAll(descLabel, descArea);

        // Created timestamp
        Label timestampLabel = new Label("Created: " + ticket.getTimestamp());
        infoBox.getChildren().add(timestampLabel);

        // Category, Priority, Status dropdowns in one row
        HBox editableFields = new HBox();
        editableFields.setSpacing(15);

        Label categoryLabel = new Label("Category:");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("Technical", "Account", "Billing", "General", "Other");
        categoryBox.setValue(ticket.getCategory() != null ? ticket.getCategory() : "Technical");

        Label priorityLabel = new Label("Priority:");
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("Low", "Medium", "High", "Critical");
        priorityBox.setValue(ticket.getPriority() != null ? ticket.getPriority() : "Medium");

        Label statusLabel = new Label("Status:");
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED");
        statusBox.setValue(ticket.getStatus() != null ? ticket.getStatus() : "OPEN");

        VBox catVBox = new VBox(categoryLabel, categoryBox);
        VBox priVBox = new VBox(priorityLabel, priorityBox);
        VBox statVBox = new VBox(statusLabel, statusBox);

        editableFields.getChildren().addAll(catVBox, priVBox, statVBox);
        infoBox.getChildren().add(editableFields);

        section.getChildren().addAll(sectionTitle, infoBox);

        // Store references for later use
        section.setUserData(new Object[]{categoryBox, priorityBox, statusBox});

        return section;
    }

    private VBox createCommentsSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 10;");

        Label sectionTitle = new Label("Comments");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        VBox commentsBox = new VBox();
        commentsBox.setSpacing(10);

        List<Comment> comments = commentDAO.getCommentsByTicketID(ticket.getTicketID());

        if (comments.isEmpty()) {
            Label noComments = new Label("No comments yet.");
            commentsBox.getChildren().add(noComments);
        } else {
            for (Comment comment : comments) {
                VBox commentBox = new VBox();
                commentBox.setSpacing(3);
                commentBox.setStyle("-fx-border-color: #eee; -fx-border-width: 1; -fx-padding: 8; -fx-background-color: #f9f9f9;");

                Label authorLabel = new Label(comment.getUsername() + " • " + comment.getTimestamp());
                authorLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 11;");

                Label contentLabel = new Label(comment.getContent());
                contentLabel.setWrapText(true);

                commentBox.getChildren().addAll(authorLabel, contentLabel);
                commentsBox.getChildren().add(commentBox);
            }
        }

        section.getChildren().addAll(sectionTitle, commentsBox);
        return section;
    }

    private VBox createCommentInputSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 10;");

        Label sectionTitle = new Label("Add Comment");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Enter your comment here...");
        commentArea.setWrapText(true);
        commentArea.setPrefRowCount(3);

        Button addCommentBtn = new Button("Add Comment");
        addCommentBtn.setOnAction(e -> {
            if (commentArea.getText().trim().isEmpty()) {
                showAlert("Please enter a comment");
                return;
            }
            try {
                Comment newComment = new Comment(ticket.getTicketID(), currentUserID, commentArea.getText());
                commentDAO.addComment(newComment);
                commentArea.clear();
                showAlert("Comment added successfully!");
                // Refresh the comments section immediately
                refreshCommentsSection();
            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert("Error adding comment: " + ex.getMessage());
            }
        });

        section.getChildren().addAll(sectionTitle, commentArea, addCommentBtn);
        return section;
    }

    private void refreshCommentsSection() {
        int commentsSectionIndex = this.getChildren().indexOf(commentsSection);
        if (commentsSectionIndex >= 0) {
            VBox newCommentsSection = createCommentsSection();
            this.getChildren().set(commentsSectionIndex, newCommentsSection);
            this.commentsSection = newCommentsSection;
        }
    }

    private HBox createButtonsSection() {
        HBox buttons = new HBox();
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-padding: 8 20;");
        saveBtn.setOnAction(e -> saveChanges());

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-padding: 8 20;");
        cancelBtn.setOnAction(e -> onBack.run());

        buttons.getChildren().addAll(saveBtn, cancelBtn);

        return buttons;
    }

    private void saveChanges() {
        // Get the values from the UI - need to find them
        VBox detailsSection = (VBox) this.getChildren().get(1);
        @SuppressWarnings("All")
        Object[] comboBoxes = (Object[]) detailsSection.getUserData();
        @SuppressWarnings("unchecked")
        ComboBox<String> categoryBox = (ComboBox<String>) comboBoxes[0];
        @SuppressWarnings("unchecked")
        ComboBox<String> priorityBox = (ComboBox<String>) comboBoxes[1];
        @SuppressWarnings("unchecked")
        ComboBox<String> statusBox = (ComboBox<String>) comboBoxes[2];

        ticket.setCategory(categoryBox.getValue());
        ticket.setPriority(priorityBox.getValue());
        ticket.setStatus(statusBox.getValue());

        try {
            // If status is set to CLOSED, delete the ticket from the database
            if ("CLOSED".equals(ticket.getStatus())) {
                ticketDAO.deleteTicket(ticket.getTicketID());
                showAlert("Ticket closed and deleted successfully!");
            } else {
                ticketDAO.updateTicket(ticket);
                showAlert("Ticket updated successfully!");
            }
            onBack.run();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error saving ticket: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
