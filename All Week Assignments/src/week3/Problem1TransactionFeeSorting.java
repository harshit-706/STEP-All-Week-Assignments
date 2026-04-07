package week3;

import java.util.*;

class Transaction {
    String id;
    double fee;
    String timestamp;

    public Transaction(String id, double fee, String timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    public String toString() {
        return id + ":" + fee + "@" + timestamp;
    }
}

class SortingService {

    public static void bubbleSortByFee(List<Transaction> list) {
        int n = list.size();
        int swaps = 0;
        int passes = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            passes++;

            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Collections.swap(list, j, j + 1);
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        System.out.println("Bubble Sort Result:");
        printFees(list);
        System.out.println("Passes: " + passes + ", Swaps: " + swaps);
    }

    public static void insertionSortByFeeAndTimestamp(List<Transaction> list) {
        int n = list.size();

        for (int i = 1; i < n; i++) {
            Transaction key = list.get(i);
            int j = i - 1;

            while (j >= 0 && compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }

        System.out.println("Insertion Sort Result:");
        printFull(list);
    }

    private static int compare(Transaction t1, Transaction t2) {
        if (t1.fee != t2.fee) {
            return Double.compare(t1.fee, t2.fee);
        }
        return t1.timestamp.compareTo(t2.timestamp);
    }

    private static void printFees(List<Transaction> list) {
        for (Transaction t : list) {
            System.out.print(t.id + ":" + t.fee + " ");
        }
        System.out.println();
    }

    private static void printFull(List<Transaction> list) {
        for (Transaction t : list) {
            System.out.print(t + " ");
        }
        System.out.println();
    }
}

class OutlierDetector {

    public static void detectHighFee(List<Transaction> list) {
        System.out.println("High Fee Outliers (>50):");
        boolean found = false;

        for (Transaction t : list) {
            if (t.fee > 50) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("None");
        }
    }
}

public class Problem1TransactionFeeSorting {
    public static void main(String[] args) {

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("id1", 10.5, "10:00"));
        transactions.add(new Transaction("id2", 25.0, "09:30"));
        transactions.add(new Transaction("id3", 5.0, "10:15"));

        List<Transaction> smallBatch = new ArrayList<>(transactions);
        List<Transaction> mediumBatch = new ArrayList<>(transactions);

        if (smallBatch.size() <= 100) {
            SortingService.bubbleSortByFee(smallBatch);
        }

        if (mediumBatch.size() > 100 && mediumBatch.size() <= 1000) {
            SortingService.insertionSortByFeeAndTimestamp(mediumBatch);
        } else {
            SortingService.insertionSortByFeeAndTimestamp(mediumBatch);
        }

        OutlierDetector.detectHighFee(transactions);
    }
}