package service;

public class ClassificationService {

    public static String classify(String text) {
        text = text.toLowerCase();

        if (text.contains("network") || text.contains("wifi") || text.contains("connection")) {
            return "Networking";
        }
        if (text.contains("password") || text.contains("login") || text.contains("account")) {
            return "Authentication";
        }
        if (text.contains("error") || text.contains("crash") || text.contains("bug")) {
            return "Software";
        }
        if (text.contains("slow") || text.contains("performance")) {
            return "Performance";
        }

        return "General";
    }
}
