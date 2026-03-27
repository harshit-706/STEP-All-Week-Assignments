package week1;

import java.util.*;
import java.util.concurrent.*;

public class Problem1SocialMediaUsernameAvailabilityChecker {
    private final ConcurrentHashMap<String, Integer> userMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> attempts = new ConcurrentHashMap<>();

    public Problem1SocialMediaUsernameAvailabilityChecker() {
        userMap.put("john_doe", 1);
        userMap.put("admin", 2);
    }

    public boolean checkAvailability(String username) {
        attempts.merge(username, 1, Integer::sum);
        return !userMap.containsKey(username);
    }

    public List<String> suggestAlternatives(String username) {
        List<String> suggestions = new ArrayList<>();
        for (int i = 1; suggestions.size() < 3; i++) {
            String s = username + i;
            if (!userMap.containsKey(s)) suggestions.add(s);
        }
        String alt = username.replace('_', '.');
        if (!userMap.containsKey(alt)) suggestions.add(alt);
        return suggestions;
    }

    public String getMostAttempted() {
        return attempts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public static void main(String[] args) {
        Problem1SocialMediaUsernameAvailabilityChecker system = new Problem1SocialMediaUsernameAvailabilityChecker();
        System.out.println(system.checkAvailability("john_doe"));
        System.out.println(system.checkAvailability("jane_smith"));
        System.out.println(system.suggestAlternatives("john_doe"));
        System.out.println(system.getMostAttempted());
    }
}