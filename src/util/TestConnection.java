package util;

public class TestConnection {
    public static void main(String[] args) {
        try {
            System.out.println(DBConnection.getConnection());
            System.out.println("Connected!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}