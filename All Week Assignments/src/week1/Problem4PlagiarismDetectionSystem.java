package week1;

import java.util.*;
import java.util.concurrent.*;

public class Problem4PlagiarismDetectionSystem {
    private final int N = 5;
    private final Map<String, Set<String>> index = new ConcurrentHashMap<>();
    private final Map<String, List<String>> documents = new ConcurrentHashMap<>();

    public void addDocument(String docId, String content) {
        List<String> ngrams = generateNGrams(content);
        documents.put(docId, ngrams);
        for (String gram : ngrams) {
            index.computeIfAbsent(gram, k -> ConcurrentHashMap.newKeySet()).add(docId);
        }
    }

    private List<String> generateNGrams(String content) {
        String[] words = content.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();
        for (int i = 0; i <= words.length - N; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < N; j++) {
                sb.append(words[i + j]).append(" ");
            }
            grams.add(sb.toString().trim());
        }
        return grams;
    }

    public void analyzeDocument(String docId) {
        List<String> grams = documents.get(docId);
        Map<String, Integer> matchCount = new HashMap<>();

        for (String gram : grams) {
            Set<String> docs = index.getOrDefault(gram, Collections.emptySet());
            for (String d : docs) {
                if (!d.equals(docId)) {
                    matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                }
            }
        }

        System.out.println("Extracted " + grams.size() + " n-grams");

        for (Map.Entry<String, Integer> e : matchCount.entrySet()) {
            double similarity = (e.getValue() * 100.0) / grams.size();
            System.out.println("Found " + e.getValue() + " matching n-grams with \"" + e.getKey() + "\"");
            System.out.println("Similarity: " + String.format("%.1f", similarity) + "% " +
                    (similarity > 60 ? "(PLAGIARISM DETECTED)" : "(suspicious)"));
        }
    }

    public static void main(String[] args) {
        Problem4PlagiarismDetectionSystem system = new Problem4PlagiarismDetectionSystem();

        system.addDocument("essay_089.txt", "this is a sample essay with some common phrases used in many essays");
        system.addDocument("essay_092.txt", "this is a sample essay with some common phrases used in many essays and more content added here");
        system.addDocument("essay_123.txt", "this is a sample essay with some common phrases used in many essays and additional text");

        system.analyzeDocument("essay_123.txt");
    }
}