package week2;

import java.util.*;
import java.util.concurrent.*;

public class Problem7AutocompleteSystemforSearchEngine {

    private static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> counts = new HashMap<>();
    }

    private final TrieNode root = new TrieNode();
    private final ConcurrentHashMap<String, Integer> frequencyMap = new ConcurrentHashMap<>();

    public void addQuery(String query) {
        frequencyMap.merge(query, 1, Integer::sum);
        int freq = frequencyMap.get(query);

        TrieNode node = root;
        for (char c : query.toCharArray()) {
            node = node.children.computeIfAbsent(c, k -> new TrieNode());
            node.counts.put(query, freq);
        }
    }

    public List<String> search(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            node = node.children.get(c);
            if (node == null) return Collections.emptyList();
        }

        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        for (Map.Entry<String, Integer> e : node.counts.entrySet()) {
            pq.offer(e);
            if (pq.size() > 10) pq.poll();
        }

        List<String> result = new ArrayList<>();
        while (!pq.isEmpty()) result.add(pq.poll().getKey());
        Collections.reverse(result);
        return result;
    }

    public void updateFrequency(String query) {
        addQuery(query);
    }

    public String correctTypo(String input) {
        String bestMatch = "";
        int minDist = Integer.MAX_VALUE;

        for (String q : frequencyMap.keySet()) {
            int dist = editDistance(input, q);
            if (dist < minDist) {
                minDist = dist;
                bestMatch = q;
            }
        }
        return bestMatch;
    }

    private int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1]));
            }
        }
        return dp[a.length()][b.length()];
    }

    public static void main(String[] args) {
        Problem7AutocompleteSystemforSearchEngine system = new Problem7AutocompleteSystemforSearchEngine();

        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java tutorial");

        System.out.println(system.search("jav"));
        system.updateFrequency("java 21 features");
        System.out.println(system.correctTypo("jvaa tutorial"));
    }
}