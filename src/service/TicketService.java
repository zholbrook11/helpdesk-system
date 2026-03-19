package service;

import model.Ticket;
import storage.TicketStorage;

import java.util.List;

public class TicketService {

    public void submitTicket(Ticket ticket) {
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
}