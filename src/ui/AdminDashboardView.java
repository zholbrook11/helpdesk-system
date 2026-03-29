package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import storage.TicketDAO;

public class AdminDashboardView extends VBox {

    private final TicketDAO ticketDAO = new TicketDAO();
    private final TableView<TicketDAO.TicketWithUser> table = new TableView<>();

    public AdminDashboardView(Runnable goBack) {
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);

        Label title = new Label("Admin Dashboard");
        title.getStyleClass().add("title");

        TextField searchField = new TextField();
        searchField.setPromptText("Search tickets...");
        searchField.setMaxWidth(400);

// Columns
        TableColumn<TicketDAO.TicketWithUser, String> userCol = new TableColumn<>("User");
        userCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getUsername()));

        TableColumn<TicketDAO.TicketWithUser, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTicket().getTitle()));

        TableColumn<TicketDAO.TicketWithUser, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTicket().getDescription()));
        descCol.setPrefWidth(250);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        descCol.setCellFactory(tc -> {
            TableCell<TicketDAO.TicketWithUser, String> cell = new TableCell<>();
            Label label = new Label();
            label.setWrapText(true);
            label.setMaxWidth(250);
            cell.setGraphic(label);
            cell.itemProperty().addListener((obs, oldText, newText) -> label.setText(newText));
            return cell;
        });

        TableColumn<TicketDAO.TicketWithUser, String> catCol = new TableColumn<>("Category");
        catCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTicket().getCategory()));

        TableColumn<TicketDAO.TicketWithUser, String> priCol = new TableColumn<>("Priority");
        priCol.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getTicket().getPriority()));

        table.getColumns().addAll(userCol, titleCol, catCol, priCol, descCol);

        // Initial load
        refreshTable("");

        // Search dynamically
        searchField.textProperty().addListener((obs, oldText, newText) -> refreshTable(newText));

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> goBack.run());

        this.getChildren().addAll(title, searchField, table, backBtn);
    }

    private void refreshTable(String keyword) {
        table.getItems().clear();
        if (keyword.isEmpty()) {
            table.getItems().addAll(ticketDAO.getAllTickets());
        } else {
            table.getItems().addAll(ticketDAO.searchTickets(keyword));
        }
    }
}