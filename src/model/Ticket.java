package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private int id;
    private String title;
    private String description;
    private String category;
    private String priority;
    private String status;
    private LocalDateTime timestamp;
    private List<Comment> comments;

    public Ticket(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = "Open";
        this.timestamp = LocalDateTime.now();
        this.comments = new ArrayList<>();
    }

    // For backward compatibility, but we'll update callers
    public Ticket(String title, String description) {
        this(0, title, description);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public List<Comment> getComments() { return comments; }

    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStatus(String status) { this.status = status; }
    public void addComment(Comment comment) { this.comments.add(comment); }
}