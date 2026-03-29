package model;

public class User {
    private int userID;
    private String username;
    private String role;

    public User(int userID, String username, String role) {
        this.userID = userID;
        this.username = username;
        this.role = role;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }
    public String getRole() { return role; }
}
