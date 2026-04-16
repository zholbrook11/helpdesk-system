package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Comment;
import model.Ticket;
import model.User;
import storage.CommentDAO;
import storage.TicketDAO;

import java.util.List;

public class TicketStatusView extends VBox {

    private final TicketDAO ticketDAO = new TicketDAO();
    private final CommentDAO commentDAO = new CommentDAO();

    private final TableView<Ticket> table = new TableView<>();
    private final VBox commentsBox = new VBox();
    private final Label placeholderLabel = new Label("Select a ticket to view comments");

    public TicketStatusView(Runnable onBack, User user) {

        this.setSpacing(20);
        this.setPadding(new javafx.geometry.Insets(20));
        this.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("My Tickets");
        title.getStyleClass().add("title");

        // Columns
        TableColumn<Ticket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Ticket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        TableColumn<Ticket, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        descCol.setPrefWidth(300);
        
        descCol.setCellFactory(tc -> {
            TableCell<Ticket, String> cell = new TableCell<>();
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(300);
            cell.setGraphic(label);

            cell.itemProperty().addListener((obs, oldText, newText) -> {
                label.setText(newText);
            });

            return cell;
        });
        
        table.getColumns().addAll(titleCol, statusCol, descCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Load tickets
        table.getItems().addAll(ticketDAO.getTicketsByUser(user.getUserID()));

        // When selecting a ticket → load comments
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
            if (selected != null) {
                loadComments(selected.getTicketID());
            }
        });

        // Comments section
        Label commentsTitle = new Label("Comments");
        commentsTitle.setStyle("-fx-font-weight: bold;");

        commentsBox.setSpacing(10);

        placeholderLabel.setStyle("-fx-text-fill: gray; -fx-font-style: italic;");
        commentsBox.getChildren().add(placeholderLabel);

        ScrollPane scrollPane = new ScrollPane(commentsBox);
        scrollPane.setPrefHeight(250);
        scrollPane.setFitToWidth(true);

        // Back button
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> onBack.run());

        HBox buttonBox = new HBox(backBtn);
        buttonBox.setAlignment(Pos.CENTER);

        this.getChildren().addAll(title, table, commentsTitle, scrollPane, buttonBox);
    }

    private void loadComments(int ticketID) {
        commentsBox.getChildren().clear();

        List<Comment> comments = commentDAO.getCommentsByTicketID(ticketID);

        if (comments.isEmpty()) {
            commentsBox.getChildren().add(new Label("No comments yet."));
            return;
        }

        for (Comment c : comments) {
            VBox box = new VBox();
            box.setSpacing(3);
            box.setStyle("-fx-border-color: #ccc; -fx-padding: 8;");

            Label author = new Label(c.getUsername() + " • " + c.getTimestamp());
            author.setStyle("-fx-font-size: 11; -fx-font-weight: bold;");

            Label content = new Label(c.getContent());
            content.setWrapText(true);

            box.getChildren().addAll(author, content);
            commentsBox.getChildren().add(box);
        }
    }
}