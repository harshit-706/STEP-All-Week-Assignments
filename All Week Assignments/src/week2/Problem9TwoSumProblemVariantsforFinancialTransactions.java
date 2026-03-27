package week2;

import java.util.*;

public class Problem9TwoSumProblemVariantsforFinancialTransactions {

    static class Transaction {
        int id;
        int amount;
        String merchant;
        String account;
        long time;

        Transaction(int id, int amount, String merchant, String account, long time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.account = account;
            this.time = time;
        }
    }

    public List<List<Transaction>> findTwoSum(List<Transaction> txs, int target) {
        Map<Integer, Transaction> map = new HashMap<>();
        List<List<Transaction>> result = new ArrayList<>();

        for (Transaction t : txs) {
            int complement = target - t.amount;
            if (map.containsKey(complement)) {
                result.add(Arrays.asList(map.get(complement), t));
            }
            map.put(t.amount, t);
        }
        return result;
    }

    public List<List<Transaction>> findTwoSumWithTimeWindow(List<Transaction> txs, int target, long windowMs) {
        List<List<Transaction>> result = new ArrayList<>();
        txs.sort(Comparator.comparingLong(t -> t.time));

        for (int i = 0; i < txs.size(); i++) {
            Map<Integer, Transaction> map = new HashMap<>();
            for (int j = i; j < txs.size(); j++) {
                if (txs.get(j).time - txs.get(i).time > windowMs) break;
                int complement = target - txs.get(j).amount;
                if (map.containsKey(complement)) {
                    result.add(Arrays.asList(map.get(complement), txs.get(j)));
                }
                map.put(txs.get(j).amount, txs.get(j));
            }
        }
        return result;
    }

    public List<List<Transaction>> findKSum(List<Transaction> txs, int k, int target) {
        List<List<Transaction>> result = new ArrayList<>();
        backtrack(txs, k, target, 0, new ArrayList<>(), result);
        return result;
    }

    private void backtrack(List<Transaction> txs, int k, int target, int start,
                           List<Transaction> current, List<List<Transaction>> result) {
        if (k == 0 && target == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        if (k == 0 || target < 0) return;

        for (int i = start; i < txs.size(); i++) {
            current.add(txs.get(i));
            backtrack(txs, k - 1, target - txs.get(i).amount, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    public List<String> detectDuplicates(List<Transaction> txs) {
        Map<String, Set<String>> map = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (Transaction t : txs) {
            String key = t.amount + "_" + t.merchant;
            map.computeIfAbsent(key, k -> new HashSet<>()).add(t.account);
        }

        for (Map.Entry<String, Set<String>> e : map.entrySet()) {
            if (e.getValue().size() > 1) {
                result.add("{amount:" + e.getKey().split("_")[0] +
                        ", merchant:" + e.getKey().split("_")[1] +
                        ", accounts:" + e.getValue() + "}");
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Problem9TwoSumProblemVariantsforFinancialTransactions system =
                new Problem9TwoSumProblemVariantsforFinancialTransactions();

        List<Transaction> txs = Arrays.asList(
                new Transaction(1, 500, "StoreA", "acc1", System.currentTimeMillis()),
                new Transaction(2, 300, "StoreB", "acc2", System.currentTimeMillis() + 1000),
                new Transaction(3, 200, "StoreC", "acc3", System.currentTimeMillis() + 2000),
                new Transaction(4, 500, "StoreA", "acc4", System.currentTimeMillis() + 3000)
        );

        System.out.println(system.findTwoSum(txs, 500));
        System.out.println(system.findTwoSumWithTimeWindow(txs, 500, 3600000));
        System.out.println(system.findKSum(txs, 3, 1000));
        System.out.println(system.detectDuplicates(txs));
    }
}