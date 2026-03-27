package week2;

import java.util.*;
import java.util.concurrent.*;

public class Problem10MultiLevelCacheSystemwithHashTables {

    static class VideoData {
        String id;
        String content;
        VideoData(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    private final int L1_CAP = 10000;
    private final int L2_CAP = 100000;

    private final Map<String, VideoData> l1 = new LinkedHashMap<>(16, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, VideoData> e) {
            return size() > L1_CAP;
        }
    };

    private final Map<String, VideoData> l2 = new LinkedHashMap<>(16, 0.75f, true) {
        protected boolean removeEldestEntry(Map.Entry<String, VideoData> e) {
            return size() > L2_CAP;
        }
    };

    private final ConcurrentHashMap<String, VideoData> l3 = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Integer> accessCount = new ConcurrentHashMap<>();

    private long l1Hits = 0, l2Hits = 0, l3Hits = 0, total = 0;

    public Problem10MultiLevelCacheSystemwithHashTables() {
        for (int i = 0; i < 200000; i++) {
            l3.put("video_" + i, new VideoData("video_" + i, "data_" + i));
        }
    }

    public synchronized VideoData getVideo(String id) {
        total++;

        if (l1.containsKey(id)) {
            l1Hits++;
            return l1.get(id);
        }

        if (l2.containsKey(id)) {
            l2Hits++;
            VideoData v = l2.get(id);
            promoteToL1(id, v);
            return v;
        }

        VideoData v = l3.get(id);
        if (v != null) {
            l3Hits++;
            l2.put(id, v);
            accessCount.put(id, 1);
        }
        return v;
    }

    private void promoteToL1(String id, VideoData v) {
        int count = accessCount.getOrDefault(id, 0) + 1;
        accessCount.put(id, count);
        if (count > 2) {
            l1.put(id, v);
        }
    }

    public void invalidate(String id) {
        l1.remove(id);
        l2.remove(id);
        l3.remove(id);
        accessCount.remove(id);
    }

    public String getStatistics() {
        double l1Rate = total == 0 ? 0 : (l1Hits * 100.0 / total);
        double l2Rate = total == 0 ? 0 : (l2Hits * 100.0 / total);
        double l3Rate = total == 0 ? 0 : (l3Hits * 100.0 / total);
        double overall = total == 0 ? 0 : ((l1Hits + l2Hits) * 100.0 / total);

        return "L1: Hit Rate " + String.format("%.1f", l1Rate) + "%\n" +
                "L2: Hit Rate " + String.format("%.1f", l2Rate) + "%\n" +
                "L3: Hit Rate " + String.format("%.1f", l3Rate) + "%\n" +
                "Overall: Hit Rate " + String.format("%.1f", overall) + "%";
    }

    public static void main(String[] args) {
        Problem10MultiLevelCacheSystemwithHashTables cache =
                new Problem10MultiLevelCacheSystemwithHashTables();

        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_999999"));

        System.out.println(cache.getStatistics());
    }
}