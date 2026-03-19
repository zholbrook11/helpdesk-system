package service;

public class PriorityService {

    public static String assignPriority(String text) {
        text = text.toLowerCase();

        if (text.contains("urgent") || text.contains("immediately") || text.contains("down")) {
            return "High";
        }
        if (text.contains("slow") || text.contains("issue")) {
            return "Medium";
        }

        return "Low";
    }
}
