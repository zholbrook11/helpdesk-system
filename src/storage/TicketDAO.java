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
                Ticket ticket = new Ticket(rs.getInt("ticketID"), rs.getString("title"), rs.getString("description"));
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
                Ticket ticket = new Ticket(rs.getInt("ticketID"), rs.getString("title"), rs.getString("description"));
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

    public Ticket getTicketById(int ticketID) {
        String sql = "SELECT t.ticketID, t.title, t.description, t.category, t.priority, t.timestamp " +
                "FROM Tickets t WHERE t.ticketID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Ticket ticket = new Ticket(rs.getInt("ticketID"), rs.getString("title"), rs.getString("description"));
                ticket.setCategory(rs.getString("category"));
                ticket.setPriority(rs.getString("priority"));
                return ticket;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void updateTicket(Ticket ticket) throws Exception {
        String sql = "UPDATE Tickets SET category = ?, priority = ?, status = ? WHERE ticketID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ticket.getCategory());
            stmt.setString(2, ticket.getPriority());
            stmt.setString(3, ticket.getStatus());
            stmt.setInt(4, ticket.getTicketID());

            stmt.executeUpdate();
        }
    }

    public void deleteTicket(int ticketID) throws Exception {
        String sql = "DELETE FROM Tickets WHERE ticketID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, ticketID);
            stmt.executeUpdate();
        }
    }

    public int getTotalTicketCount() {
        String sql = "SELECT COUNT(*) as count FROM Tickets";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public java.util.Map<String, Integer> getTicketsByPriority() {
        java.util.Map<String, Integer> priorityMap = new java.util.LinkedHashMap<>();
        String sql = "SELECT priority, COUNT(*) as count FROM Tickets GROUP BY priority ORDER BY count DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                priorityMap.put(rs.getString("priority"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return priorityMap;
    }

    public java.util.Map<String, Integer> getTicketsByCategory() {
        java.util.Map<String, Integer> categoryMap = new java.util.LinkedHashMap<>();
        String sql = "SELECT category, COUNT(*) as count FROM Tickets GROUP BY category ORDER BY count DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                categoryMap.put(rs.getString("category"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return categoryMap;
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