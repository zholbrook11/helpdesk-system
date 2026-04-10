package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Ticket;
import model.User;
import service.TicketService;

public class SubmitTicketView extends VBox {

    public SubmitTicketView(Runnable goBack, User loggedInUser) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(15);

        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setMaxWidth(400);
        card.setAlignment(Pos.CENTER);

        Label title = new Label("Submit a Ticket");
        title.getStyleClass().add("title");

        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        TextArea descField = new TextArea();
        descField.setPromptText("Description");

        Button submitBtn = new Button("Submit");
        Button backBtn = new Button("← Back");

        Label result = new Label();

        TicketService service = new TicketService();

        submitBtn.setOnAction(e -> {
            if (titleField.getText().isEmpty() || descField.getText().isEmpty()) {
                result.setText("Please fill in all fields.");
                return;
            }

            Ticket ticket = new Ticket(titleField.getText(), descField.getText());

            service.submitTicket(ticket, loggedInUser.getUserID());

            result.setText("Ticket submitted!\nCategory: " + ticket.getCategory() +
                    " | Priority: " + ticket.getPriority());
        });

        backBtn.setOnAction(e -> goBack.run());

        card.getChildren().addAll(title, titleField, descField, submitBtn, result, backBtn);

        this.getChildren().add(card);
    }
}