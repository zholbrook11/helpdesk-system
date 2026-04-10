package storage;

import model.Ticket;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

    public void addTicket(Ticket ticket, int userID) {
        String sql = "INSERT INTO Tickets (title, description, category, priority, timestamp, userID) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticket.getTitle());
            stmt.setString(2, ticket.getDescription());
            stmt.setString(3, ticket.getCategory());
            stmt.setString(4, ticket.getPriority());
            stmt.setTimestamp(5, Timestamp.valueOf(ticket.getTimestamp()));
            stmt.setInt(6, userID);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<TicketWithUser> getAllTickets() {
        List<TicketWithUser> tickets = new ArrayList<>();
        String sql = "SELECT t.ticketID, t.title, t.description, t.category, t.priority, t.timestamp, u.username " +
                "FROM Tickets t JOIN Users u ON t.userID = u.userID ORDER BY t.timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ticket ticket = new Ticket(rs.getString("title"), rs.getString("description"));
                ticket.setCategory(rs.getString("category"));
                ticket.setPriority(rs.getString("priority"));
                tickets.add(new TicketWithUser(ticket, rs.getString("username")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tickets;
    }

    public List<TicketWithUser> searchTickets(String keyword) {
        List<TicketWithUser> tickets = new ArrayList<>();
        String sql = "SELECT t.ticketID, t.title, t.description, t.category, t.priority, t.timestamp, u.username " +
                "FROM Tickets t JOIN Users u ON t.userID = u.userID " +
                "WHERE t.title LIKE ? OR t.description LIKE ? OR t.category LIKE ? OR t.priority LIKE ? OR u.username LIKE ? " +
                "ORDER BY t.timestamp DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) stmt.setString(i, like);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Ticket ticket = new Ticket(rs.getString("title"), rs.getString("description"));
                ticket.setCategory(rs.getString("category"));
                ticket.setPriority(rs.getString("priority"));
                tickets.add(new TicketWithUser(ticket, rs.getString("username")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tickets;
    }

    public static class TicketWithUser {
        private final Ticket ticket;
        private final String username;

        public TicketWithUser(Ticket ticket, String username) {
            this.ticket = ticket;
            this.username = username;
        }

        public Ticket getTicket() { return ticket; }
        public String getUsername() { return username; }
    }
}