package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.Ticket;
import service.TicketService;

public class AdminDashboardView extends VBox {

    public AdminDashboardView(Runnable goBack) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        Label title = new Label("Admin Dashboard");
        title.getStyleClass().add("title");

        TableView<Ticket> table = new TableView<>();
        table.setMaxWidth(500);

        TableColumn<Ticket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));

        TableColumn<Ticket, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));

        descCol.setPrefWidth(250);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        descCol.setCellFactory(tc -> {
            TableCell<Ticket, String> cell = new TableCell<>();
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(250);

            cell.setGraphic(label);

            cell.itemProperty().addListener((obs, oldText, newText) -> {
                label.setText(newText);
            });

            return cell;
        });

        TableColumn<Ticket, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        TableColumn<Ticket, String> priCol = new TableColumn<>("Priority");
        priCol.setCellValueFactory(data ->
            new javafx.beans.property.SimpleStringProperty(data.getValue().getPriority()));

        table.getColumns().addAll(titleCol, catCol, priCol, descCol);

        TicketService service = new TicketService();
        table.getItems().addAll(service.getAllTickets());

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> goBack.run());

        this.getChildren().addAll(title, table, backBtn);
    }
}