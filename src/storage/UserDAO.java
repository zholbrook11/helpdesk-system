package storage;

import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
import model.User;
import util.DBConnection;


public class UserDAO {

    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hash = rs.getString("userPassword");

                if (BCrypt.checkpw(password, hash)) {
                    return new User(
                            rs.getInt("userID"),
                            rs.getString("username"),
                            rs.getString("role")
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}