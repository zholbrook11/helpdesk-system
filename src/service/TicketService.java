package service;

import model.Comment;
import model.Ticket;
import storage.TicketStorage;

import java.util.List;

public class TicketService {

    public void submitTicket(Ticket ticket) {
        // Assign id if not set
        if (ticket.getId() == 0) {
            ticket.setId(TicketStorage.getNextId());
        }
        // AI classification
        String category = ClassificationService.classify(ticket.getDescription());
        String priority = PriorityService.assignPriority(ticket.getDescription());

        ticket.setCategory(category);
        ticket.setPriority(priority);

        TicketStorage.saveTicket(ticket);
    }

    public List<Ticket> getAllTickets() {
        return TicketStorage.loadTickets();
    }

    public void updateTicket(Ticket ticket) {
        TicketStorage.updateTicket(ticket);
    }

    public void addComment(int ticketId, String commentText, String author) {
        List<Ticket> tickets = getAllTickets();
        for (Ticket t : tickets) {
            if (t.getId() == ticketId) {
                t.addComment(new Comment(commentText, author));
                updateTicket(t);
                break;
            }
        }
    }

    public Ticket getTicketById(int id) {
        List<Ticket> tickets = getAllTickets();
        for (Ticket t : tickets) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }
}