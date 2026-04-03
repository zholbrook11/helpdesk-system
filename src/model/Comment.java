package model;

import java.time.LocalDateTime;

public class Comment {
    private String text;
    private LocalDateTime timestamp;
    private String author;

    public Comment(String text, String author) {
        this.text = text;
        this.author = author;
        this.timestamp = LocalDateTime.now();
    }

    public String getText() { return text; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getAuthor() { return author; }
}