package tests;

import org.junit.Test;
import static org.junit.Assert.*;
import model.Ticket;

public class TicketTest {

    @Test
    public void testConstructorAndGetters() {
        Ticket ticket = new Ticket("Login Issue", "Cannot log in");

        assertEquals("Login Issue", ticket.getTitle());
        assertEquals("Cannot log in", ticket.getDescription());
        assertNotNull(ticket.getTimestamp()); // timestamp should be set
    }

    @Test
    public void testSetAndGetCategory() {
        Ticket ticket = new Ticket("Issue", "Desc");
        ticket.setCategory("Network");

        assertEquals("Network", ticket.getCategory());
    }

    @Test
    public void testSetAndGetPriority() {
        Ticket ticket = new Ticket("Issue", "Desc");
        ticket.setPriority("High");

        assertEquals("High", ticket.getPriority());
    }

    @Test
    public void testDefaultValues() {
        Ticket ticket = new Ticket("Issue", "Desc");

        assertNull(ticket.getCategory());
        assertNull(ticket.getPriority());
    }
}