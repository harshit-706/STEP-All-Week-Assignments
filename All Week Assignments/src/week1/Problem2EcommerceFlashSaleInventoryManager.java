package week1;

        import java.util.concurrent.*;
        import java.util.concurrent.atomic.AtomicInteger;

public class Problem2EcommerceFlashSaleInventoryManager {
    private final ConcurrentHashMap<String, AtomicInteger> stock = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Integer>> waitingList = new ConcurrentHashMap<>();

    public Problem2EcommerceFlashSaleInventoryManager() {
        stock.put("IPHONE15_256GB", new AtomicInteger(100));
        waitingList.put("IPHONE15_256GB", new ConcurrentLinkedQueue<>());
    }

    public int checkStock(String productId) {
        return stock.getOrDefault(productId, new AtomicInteger(0)).get();
    }

    public String purchaseItem(String productId, int userId) {
        stock.putIfAbsent(productId, new AtomicInteger(0));
        waitingList.putIfAbsent(productId, new ConcurrentLinkedQueue<>());

        AtomicInteger currentStock = stock.get(productId);

        while (true) {
            int available = currentStock.get();
            if (available > 0) {
                if (currentStock.compareAndSet(available, available - 1)) {
                    return "Success, " + (available - 1) + " units remaining";
                }
            } else {
                ConcurrentLinkedQueue<Integer> queue = waitingList.get(productId);
                queue.add(userId);
                return "Added to waiting list, position #" + queue.size();
            }
        }
    }

    public static void main(String[] args) {
        Problem2EcommerceFlashSaleInventoryManager manager = new Problem2EcommerceFlashSaleInventoryManager();
        System.out.println(manager.checkStock("IPHONE15_256GB"));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 12345));
        System.out.println(manager.purchaseItem("IPHONE15_256GB", 67890));

        for (int i = 0; i < 100; i++) {
            manager.purchaseItem("IPHONE15_256GB", i);
        }

        System.out.println(manager.purchaseItem("IPHONE15_256GB", 99999));
    }
}