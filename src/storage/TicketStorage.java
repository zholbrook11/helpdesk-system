package storage;

import model.Comment;
import model.Ticket;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TicketStorage {

    private static final String FILE = "tickets.txt";
    private static int nextId = 1;

    public static void saveTicket(Ticket ticket) {
        List<Ticket> tickets = loadTickets();
        tickets.add(ticket);
        saveAllTickets(tickets);
    }

    public static void updateTicket(Ticket ticket) {
        List<Ticket> tickets = loadTickets();
        for (int i = 0; i < tickets.size(); i++) {
            if (tickets.get(i).getId() == ticket.getId()) {
                tickets.set(i, ticket);
                break;
            }
        }
        saveAllTickets(tickets);
    }

    private static void saveAllTickets(List<Ticket> tickets) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE))) {
            for (Ticket ticket : tickets) {
                StringBuilder sb = new StringBuilder();
                sb.append(ticket.getId()).append("|")
                  .append(ticket.getTitle()).append("|")
                  .append(ticket.getDescription()).append("|")
                  .append(ticket.getCategory() != null ? ticket.getCategory() : "").append("|")
                  .append(ticket.getPriority() != null ? ticket.getPriority() : "").append("|")
                  .append(ticket.getStatus()).append("|")
                  .append(ticket.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("|");

                // comments
                for (Comment c : ticket.getComments()) {
                    sb.append(c.getText()).append("~")
                      .append(c.getAuthor()).append("~")
                      .append(c.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append(";");
                }

                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                Ticket t;
                if (parts.length >= 8) {
                    // New format: id|title|desc|cat|pri|status|timestamp|comments
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String desc = parts[2];
                    String cat = parts[3].isEmpty() ? null : parts[3];
                    String pri = parts[4].isEmpty() ? null : parts[4];
                    String status = parts[5];
                    LocalDateTime timestamp = LocalDateTime.parse(parts[6]);

                    t = new Ticket(id, title, desc);
                    t.setCategory(cat);
                    t.setPriority(pri);
                    t.setStatus(status);
                    // timestamp is set in constructor, but we can ignore for now

                    if (!parts[7].isEmpty()) {
                        String[] commentParts = parts[7].split(";");
                        for (String cp : commentParts) {
                            if (!cp.isEmpty()) {
                                String[] cparts = cp.split("~");
                                Comment c = new Comment(cparts[0], cparts[1]);
                                t.addComment(c);
                            }
                        }
                    }

                    if (id >= nextId) nextId = id + 1;
                } else if (parts.length >= 5) {
                    // Old format: title|desc|cat|pri|timestamp
                    String title = parts[0];
                    String desc = parts[1];
                    String cat = parts[2].isEmpty() ? null : parts[2];
                    String pri = parts[3].isEmpty() ? null : parts[3];
                    // timestamp not used

                    t = new Ticket(nextId++, title, desc);
                    t.setCategory(cat);
                    t.setPriority(pri);
                    t.setStatus("Open");
                    // comments empty
                } else {
                    continue; // invalid line
                }

                tickets.add(t);
            }

        } catch (IOException e) {
            // first run = no file yet
        }

        return tickets;
    }

    public static int getNextId() {
        return nextId++;
    }
}