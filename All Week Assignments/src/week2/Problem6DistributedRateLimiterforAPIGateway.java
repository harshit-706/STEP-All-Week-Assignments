package week2;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Problem6DistributedRateLimiterforAPIGateway {

    private static class TokenBucket {
        private final int maxTokens;
        private final int refillRate;
        private AtomicInteger tokens;
        private volatile long lastRefillTime;

        public TokenBucket(int maxTokens, int refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = new AtomicInteger(maxTokens);
            this.lastRefillTime = System.currentTimeMillis();
        }

        public synchronized boolean allowRequest() {
            refill();
            if (tokens.get() > 0) {
                tokens.decrementAndGet();
                return true;
            }
            return false;
        }

        private void refill() {
            long now = System.currentTimeMillis();
            long elapsed = now - lastRefillTime;
            long tokensToAdd = (elapsed / 3600000.0) * refillRate > 0 ? (long)((elapsed / 3600000.0) * refillRate) : 0;
            if (tokensToAdd > 0) {
                int newCount = Math.min(maxTokens, tokens.get() + (int) tokensToAdd);
                tokens.set(newCount);
                lastRefillTime = now;
            }
        }

        public int getTokens() {
            refill();
            return tokens.get();
        }

        public long getResetTime() {
            return lastRefillTime + 3600000;
        }
    }

    private final ConcurrentHashMap<String, TokenBucket> clients = new ConcurrentHashMap<>();
    private final int LIMIT = 1000;

    public String checkRateLimit(String clientId) {
        TokenBucket bucket = clients.computeIfAbsent(clientId, k -> new TokenBucket(LIMIT, LIMIT));
        boolean allowed = bucket.allowRequest();
        if (allowed) {
            return "Allowed (" + bucket.getTokens() + " requests remaining)";
        } else {
            long retryAfter = (bucket.getResetTime() - System.currentTimeMillis()) / 1000;
            return "Denied (0 requests remaining, retry after " + retryAfter + "s)";
        }
    }

    public String getRateLimitStatus(String clientId) {
        TokenBucket bucket = clients.get(clientId);
        if (bucket == null) return "No data";
        int remaining = bucket.getTokens();
        int used = LIMIT - remaining;
        return "{used: " + used + ", limit: " + LIMIT + ", reset: " + bucket.getResetTime() + "}";
    }

    public static void main(String[] args) {
        Problem6DistributedRateLimiterforAPIGateway limiter = new Problem6DistributedRateLimiterforAPIGateway();

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));

        for (int i = 0; i < 1000; i++) {
            limiter.checkRateLimit("abc123");
        }

        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.getRateLimitStatus("abc123"));
    }
}
