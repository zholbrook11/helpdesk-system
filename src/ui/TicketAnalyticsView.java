package ui;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import storage.TicketDAO;

import java.util.Map;

public class TicketAnalyticsView extends VBox {

    private final TicketDAO ticketDAO = new TicketDAO();
    private final Runnable onBack;

    public TicketAnalyticsView(Runnable onBack) {
        this.onBack = onBack;
        this.setSpacing(20);
        this.setPadding(new javafx.geometry.Insets(20));

        // Title
        Label title = new Label("Ticket Analytics");
        title.getStyleClass().add("title");

        // Header with back button
        HBox headerBox = new HBox();
        headerBox.setSpacing(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> onBack.run());

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshAnalytics());

        headerBox.getChildren().addAll(backBtn, refreshBtn);

        // Scroll pane for analytics content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        VBox analyticsContent = createAnalyticsContent();
        scrollPane.setContent(analyticsContent);

        this.getChildren().addAll(title, headerBox, scrollPane);
    }

    private VBox createAnalyticsContent() {
        VBox content = new VBox();
        content.setSpacing(20);
        content.setPadding(new javafx.geometry.Insets(10));

        // Total tickets section
        VBox totalSection = createTotalTicketsSection();
        content.getChildren().add(totalSection);

        // Priority section
        VBox prioritySection = createPrioritySection();
        content.getChildren().add(prioritySection);

        // Category section
        VBox categorySection = createCategorySection();
        content.getChildren().add(categorySection);

        return content;
    }

    private VBox createTotalTicketsSection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 15; -fx-border-radius: 5;");

        Label sectionTitle = new Label("Overview");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        int totalTickets = ticketDAO.getTotalTicketCount();

        HBox statsBox = new HBox();
        statsBox.setSpacing(30);
        statsBox.setAlignment(Pos.CENTER_LEFT);

        // Total tickets stat
        VBox totalBox = createStatBox("Total Tickets", String.valueOf(totalTickets));
        statsBox.getChildren().add(totalBox);

        section.getChildren().addAll(sectionTitle, statsBox);
        return section;
    }

    private VBox createPrioritySection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 15; -fx-border-radius: 5;");

        Label sectionTitle = new Label("Tickets by Priority");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Map<String, Integer> priorityData = ticketDAO.getTicketsByPriority();

        VBox dataBox = new VBox();
        dataBox.setSpacing(8);

        if (priorityData.isEmpty()) {
            Label noData = new Label("No data available");
            dataBox.getChildren().add(noData);
        } else {
            int maxCount = priorityData.values().stream().max(Integer::compare).orElse(1);

            for (Map.Entry<String, Integer> entry : priorityData.entrySet()) {
                VBox entryBox = createChartEntryBox(entry.getKey(), entry.getValue(), maxCount);
                dataBox.getChildren().add(entryBox);
            }
        }

        section.getChildren().addAll(sectionTitle, dataBox);
        return section;
    }

    private VBox createCategorySection() {
        VBox section = new VBox();
        section.setSpacing(10);
        section.setStyle("-fx-border-color: #ddd; -fx-border-width: 1; -fx-padding: 15; -fx-border-radius: 5;");

        Label sectionTitle = new Label("Tickets by Category");
        sectionTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");

        Map<String, Integer> categoryData = ticketDAO.getTicketsByCategory();

        VBox dataBox = new VBox();
        dataBox.setSpacing(8);

        if (categoryData.isEmpty()) {
            Label noData = new Label("No data available");
            dataBox.getChildren().add(noData);
        } else {
            int maxCount = categoryData.values().stream().max(Integer::compare).orElse(1);

            for (Map.Entry<String, Integer> entry : categoryData.entrySet()) {
                VBox entryBox = createChartEntryBox(entry.getKey(), entry.getValue(), maxCount);
                dataBox.getChildren().add(entryBox);
            }
        }

        section.getChildren().addAll(sectionTitle, dataBox);
        return section;
    }

    private VBox createStatBox(String label, String value) {
        VBox box = new VBox();
        box.setSpacing(5);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: #0078d4; -fx-border-width: 2; -fx-padding: 15; -fx-border-radius: 5;");

        Label labelItem = new Label(label);
        labelItem.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Label valueItem = new Label(value);
        valueItem.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: #0078d4;");

        box.getChildren().addAll(labelItem, valueItem);
        return box;
    }

    private VBox createChartEntryBox(String label, int value, int maxCount) {
        VBox entryBox = new VBox();
        entryBox.setSpacing(5);

        HBox labelBox = new HBox();
        labelBox.setSpacing(10);
        labelBox.setAlignment(Pos.CENTER_LEFT);

        Label labelItem = new Label(label);
        labelItem.setPrefWidth(100);
        labelItem.setStyle("-fx-font-weight: bold;");

        Label valueItem = new Label(String.valueOf(value));
        valueItem.setStyle("-fx-font-weight: bold;");

        labelBox.getChildren().addAll(labelItem, valueItem);

        // Create a simple bar chart representation
        int barWidth = (int) ((value / (double) maxCount) * 400);
        ProgressBar progressBar = new ProgressBar(value / (double) maxCount);
        progressBar.setPrefWidth(400);
        progressBar.setStyle("-fx-control-inner-background: #0078d4;");

        entryBox.getChildren().addAll(labelBox, progressBar);
        return entryBox;
    }

    private void refreshAnalytics() {
        this.getChildren().clear();

        Label title = new Label("Ticket Analytics");
        title.getStyleClass().add("title");

        HBox headerBox = new HBox();
        headerBox.setSpacing(15);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> onBack.run());

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> refreshAnalytics());

        headerBox.getChildren().addAll(backBtn, refreshBtn);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        VBox analyticsContent = createAnalyticsContent();
        scrollPane.setContent(analyticsContent);

        this.getChildren().addAll(title, headerBox, scrollPane);
    }
}
