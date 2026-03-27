package week1;

import java.util.*;
import java.util.concurrent.*;

public class Problem3DNSCacheWithTTL {
    private static class DNSEntry {
        String domain;
        String ip;
        long expiryTime;
        DNSEntry(String d, String i, long ttl) {
            domain = d;
            ip = i;
            expiryTime = System.currentTimeMillis() + ttl;
        }
    }

    private final int capacity = 100;
    private final Map<String, DNSEntry> cache = new LinkedHashMap<>(16, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> e) {
            return size() > capacity;
        }
    };

    private long hits = 0;
    private long misses = 0;

    public Problem3DNSCacheWithTTL() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            synchronized (cache) {
                Iterator<Map.Entry<String, DNSEntry>> it = cache.entrySet().iterator();
                while (it.hasNext()) {
                    if (it.next().getValue().expiryTime < now) it.remove();
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public String resolve(String domain) {
        long start = System.nanoTime();
        synchronized (cache) {
            DNSEntry entry = cache.get(domain);
            if (entry != null && entry.expiryTime > System.currentTimeMillis()) {
                hits++;
                return "Cache HIT → " + entry.ip;
            }
        }
        misses++;
        String ip = queryUpstream(domain);
        synchronized (cache) {
            cache.put(domain, new DNSEntry(domain, ip, 300000));
        }
        return "Cache MISS → " + ip;
    }

    private String queryUpstream(String domain) {
        return "172.217.14." + new Random().nextInt(255);
    }

    public String getCacheStats() {
        long total = hits + misses;
        double rate = total == 0 ? 0 : (hits * 100.0 / total);
        return "Hit Rate: " + String.format("%.2f", rate) + "%";
    }

    public static void main(String[] args) throws Exception {
        Problem3DNSCacheWithTTL dns = new Problem3DNSCacheWithTTL();
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        Thread.sleep(2000);
        System.out.println(dns.getCacheStats());
    }
}
