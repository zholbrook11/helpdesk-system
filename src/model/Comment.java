package model;

import java.time.LocalDateTime;

public class Comment {
    private int commentID;
    private int ticketID;
    private int userID;
    private String username;
    private String content;
    private LocalDateTime timestamp;

    public Comment(int ticketID, int userID, String content) {
        this.ticketID = ticketID;
        this.userID = userID;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public Comment(int commentID, int ticketID, int userID, String username, String content, LocalDateTime timestamp) {
        this.commentID = commentID;
        this.ticketID = ticketID;
        this.userID = userID;
        this.username = username;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getCommentID() { return commentID; }
    public int getTicketID() { return ticketID; }
    public int getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public void setCommentID(int commentID) { this.commentID = commentID; }
    public void setUsername(String username) { this.username = username; }
}
