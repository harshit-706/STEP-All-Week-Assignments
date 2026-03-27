package week1;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Problem5RealTimeAnalyticsDashboardforWebsiteTraffic {
    private final ConcurrentHashMap<String, AtomicInteger> pageViews = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Set<String>> uniqueVisitors = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, AtomicInteger> trafficSources = new ConcurrentHashMap<>();

    public void processEvent(String url, String userId, String source) {
        pageViews.computeIfAbsent(url, k -> new AtomicInteger(0)).incrementAndGet();
        uniqueVisitors.computeIfAbsent(url, k -> ConcurrentHashMap.newKeySet()).add(userId);
        trafficSources.computeIfAbsent(source, k -> new AtomicInteger(0)).incrementAndGet();
    }

    public void getDashboard() {
        List<Map.Entry<String, AtomicInteger>> pages = new ArrayList<>(pageViews.entrySet());
        pages.sort((a, b) -> b.getValue().get() - a.getValue().get());

        System.out.println("Top Pages:");
        int count = 0;
        for (Map.Entry<String, AtomicInteger> e : pages) {
            if (count++ == 10) break;
            String url = e.getKey();
            int views = e.getValue().get();
            int unique = uniqueVisitors.getOrDefault(url, Collections.emptySet()).size();
            System.out.println(count + ". " + url + " - " + views + " views (" + unique + " unique)");
        }

        int total = trafficSources.values().stream().mapToInt(AtomicInteger::get).sum();
        System.out.println("\nTraffic Sources:");
        for (Map.Entry<String, AtomicInteger> e : trafficSources.entrySet()) {
            double percent = total == 0 ? 0 : (e.getValue().get() * 100.0 / total);
            System.out.println(e.getKey() + ": " + String.format("%.1f", percent) + "%");
        }
    }

    public static void main(String[] args) {
        Problem5RealTimeAnalyticsDashboardforWebsiteTraffic dashboard = new Problem5RealTimeAnalyticsDashboardforWebsiteTraffic();

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(dashboard::getDashboard, 5, 5, TimeUnit.SECONDS);

        dashboard.processEvent("/article/breaking-news", "user_123", "google");
        dashboard.processEvent("/article/breaking-news", "user_456", "facebook");
        dashboard.processEvent("/sports/championship", "user_789", "direct");
        dashboard.processEvent("/sports/championship", "user_123", "google");
    }
}
