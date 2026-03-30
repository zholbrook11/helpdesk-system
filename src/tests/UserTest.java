package tests;

import org.junit.Test;
import static org.junit.Assert.*;
import model.User;

public class UserTest {

    @Test
    public void testGetRole_Admin() {
        User user = new User(1, "adminUser", "ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    public void testGetRole_User() {
        User user = new User(2, "normalUser", "USER");
        assertEquals("USER", user.getRole());
    }

    @Test
    public void testGetRole_CaseSensitivity() {
        User user = new User(3, "testUser", "admin");
        assertNotEquals("ADMIN", user.getRole()); // ensures exact match behavior
    }

    @Test
    public void testGetRole_NullRole() {
        User user = new User(4, "nullUser", null);
        assertNull(user.getRole());
    }
}