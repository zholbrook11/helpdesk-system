package service;

import model.Ticket;
import storage.TicketDAO;


public class TicketService {

    private final TicketDAO ticketDAO = new TicketDAO();

    public void submitTicket(Ticket ticket, int userID) {
        // AI classification
        ticket.setCategory(ClassificationService.classify(ticket.getDescription()));
        ticket.setPriority(PriorityService.assignPriority(ticket.getDescription()));

        ticketDAO.addTicket(ticket, userID);
    }
}