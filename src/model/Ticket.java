package model;

import java.time.LocalDateTime;

public class Ticket {
    private int ticketID;
    private String title;
    private String description;
    private String category;
    private String priority;
    private String status;
    private String assigned_team;
    private LocalDateTime timestamp;

    public Ticket(String title, String description) {
        this.title = title;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.status = "OPEN";
    }

    public Ticket(int ticketID, String title, String description) {
        this.ticketID = ticketID;
        this.title = title;
        this.description = description;
        this.timestamp = LocalDateTime.now();
        this.status = "OPEN";
    }

    public int getTicketID() { return ticketID; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public String getAssignedTeam() { return assigned_team; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setTicketID(int ticketID) { this.ticketID = ticketID; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setStatus(String status) { this.status = status; }
    public void setAssignedTeam(String assignedTeam) { this.assigned_team = assignedTeam; }
}