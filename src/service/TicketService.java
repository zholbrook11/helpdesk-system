package service;

import model.Ticket;
import storage.TicketDAO;


public class TicketService {

    private final TicketDAO ticketDAO = new TicketDAO();

    public void submitTicket(Ticket ticket, int userID) {
        // AI classification
        ticket.setCategory(ClassificationService.classify(ticket.getDescription()));
        ticket.setPriority(PriorityService.assignPriority(ticket.getDescription()));
        ticket.setAssignedTeam(assignTeam(ticket.getCategory()));

        ticketDAO.addTicket(ticket, userID);
    }

    public static String assignTeam(String category) {
        if (category == null) return "General Support";
        return switch (category) {
            case "Networking"      -> "Network Team";
            case "Authentication"  -> "Security Team";
            case "Software"        -> "Software Team";
            case "Performance"     -> "Infrastructure Team";
            default                -> "General Support";
        };
    }
}