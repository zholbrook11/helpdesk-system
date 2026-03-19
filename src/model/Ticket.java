package model;

import java.time.LocalDateTime;

public class Ticket {
    private String title;
    private String description;
    private String category;
    private String priority;
    private LocalDateTime timestamp;

    public Ticket(String title, String description) {
        this.title = title;
        this.description = description;
        this.timestamp = LocalDateTime.now();
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }
}