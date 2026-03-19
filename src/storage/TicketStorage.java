package storage;

import model.Ticket;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TicketStorage {

    private static final String FILE = "tickets.txt";

    public static void saveTicket(Ticket ticket) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE, true))) {
            writer.write(ticket.getTitle() + "|" +
                         ticket.getDescription() + "|" +
                         ticket.getCategory() + "|" +
                         ticket.getPriority() + "|" +
                         ticket.getTimestamp());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Ticket> loadTickets() {
        List<Ticket> tickets = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                Ticket t = new Ticket(parts[0], parts[1]);
                t.setCategory(parts[2]);
                t.setPriority(parts[3]);

                tickets.add(t);
            }

        } catch (IOException e) {
            // first run = no file yet
        }

        return tickets;
    }
}