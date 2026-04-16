package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import storage.TicketDAO;
import java.util.function.Consumer;

public class AdminDashboardView extends VBox {

    private final TicketDAO ticketDAO = new TicketDAO();
    private final TableView<TicketDAO.TicketWithUser> table = new TableView<>();
    @SuppressWarnings("unused")
    private final Consumer<Integer> onTicketSelected;

    public AdminDashboardView(Runnable goBack, Consumer<Integer> onTicketSelected, Runnable onAnalytics) {
        this.onTicketSelected = onTicketSelected;
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(20);
        this.setPadding(new javafx.geometry.Insets(20));

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
        @SuppressWarnings("deprecation")
        var constrained = TableView.CONSTRAINED_RESIZE_POLICY;
        table.setColumnResizePolicy(constrained);

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

        table.getColumns().add(userCol);
        table.getColumns().add(titleCol);
        table.getColumns().add(catCol);
        table.getColumns().add(priCol);
        table.getColumns().add(descCol);

        // Add row click handler
        table.setRowFactory(tv -> {
            TableRow<TicketDAO.TicketWithUser> row = new TableRow<TicketDAO.TicketWithUser>() {
                @Override
                protected void updateItem(TicketDAO.TicketWithUser item, boolean empty) {
                    super.updateItem(item, empty);
                }
            };
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    TicketDAO.TicketWithUser selectedTicket = row.getItem();
                    onTicketSelected.accept(selectedTicket.getTicket().getTicketID());
                }
            });
            return row;
        });

        // Initial load
        refreshTable("");

        // Search dynamically
        searchField.textProperty().addListener((obs, oldText, newText) -> refreshTable(newText));

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER);

        Button analyticsBtn = new Button("📊 View Analytics");
        analyticsBtn.setStyle("-fx-padding: 8 20;");
        analyticsBtn.setOnAction(e -> onAnalytics.run());

        Button backBtn = new Button("Logout");
        backBtn.setStyle("-fx-padding: 8 20;");
        backBtn.setOnAction(e -> goBack.run());

        buttonBox.getChildren().addAll(analyticsBtn, backBtn);

        this.getChildren().addAll(title, searchField, table, buttonBox);
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