package storage;

import model.Comment;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentDAO {

    public void addComment(Comment comment) throws Exception {
        String sql = "INSERT INTO Comments (ticketID, userID, content, timestamp) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, comment.getTicketID());
            stmt.setInt(2, comment.getUserID());
            stmt.setString(3, comment.getContent());
            stmt.setTimestamp(4, Timestamp.valueOf(comment.getTimestamp()));

            stmt.executeUpdate();
        }
    }

    public List<Comment> getCommentsByTicketID(int ticketID) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT c.commentID, c.ticketID, c.userID, u.username, c.content, c.timestamp " +
                "FROM Comments c JOIN Users u ON c.userID = u.userID " +
                "WHERE c.ticketID = ? ORDER BY c.timestamp ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment(
                        rs.getInt("commentID"),
                        rs.getInt("ticketID"),
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("content"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                );
                comments.add(comment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return comments;
    }
}
