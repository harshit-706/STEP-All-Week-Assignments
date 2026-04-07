package week4;

import java.util.*;

class TransactionLog {
    String accountId;

    public TransactionLog(String accountId) {
        this.accountId = accountId;
    }

    public String toString() {
        return accountId;
    }
}

class SearchService {

    public static void linearSearchFirstLast(TransactionLog[] arr, String target) {
        int first = -1, last = -1;
        int comparisons = 0;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i].accountId.equals(target)) {
                if (first == -1) first = i;
                last = i;
            }
        }

        System.out.println("Linear Search:");
        System.out.println("First Index: " + first + ", Last Index: " + last);
        System.out.println("Comparisons: " + comparisons);
    }

    public static int binarySearch(TransactionLog[] arr, String target) {
        int low = 0, high = arr.length - 1;
        int comparisons = 0;

        while (low <= high) {
            int mid = (low + high) / 2;
            comparisons++;

            int cmp = arr[mid].accountId.compareTo(target);

            if (cmp == 0) {
                System.out.println("Binary Search Comparisons: " + comparisons);
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println("Binary Search Comparisons: " + comparisons);
        return -1;
    }

    public static int countOccurrences(TransactionLog[] arr, String target) {
        int first = firstOccurrence(arr, target);
        int last = lastOccurrence(arr, target);

        if (first == -1) return 0;
        return last - first + 1;
    }

    private static int firstOccurrence(TransactionLog[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = arr[mid].accountId.compareTo(target);

            if (cmp == 0) {
                result = mid;
                high = mid - 1;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return result;
    }

    private static int lastOccurrence(TransactionLog[] arr, String target) {
        int low = 0, high = arr.length - 1, result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;
            int cmp = arr[mid].accountId.compareTo(target);

            if (cmp == 0) {
                result = mid;
                low = mid + 1;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return result;
    }

    public static void sortLogs(TransactionLog[] arr) {
        Arrays.sort(arr, Comparator.comparing(log -> log.accountId));
    }

    public static void printArray(TransactionLog[] arr) {
        for (TransactionLog log : arr)
            System.out.print(log + " ");
        System.out.println();
    }
}

public class Problem5AccountIDLookupInTransactionLogs {

    public static void main(String[] args) {

        TransactionLog[] logs = {
                new TransactionLog("accB"),
                new TransactionLog("accA"),
                new TransactionLog("accB"),
                new TransactionLog("accC")
        };

        SearchService.linearSearchFirstLast(logs, "accB");

        SearchService.sortLogs(logs);
        System.out.println("Sorted Logs:");
        SearchService.printArray(logs);

        int index = SearchService.binarySearch(logs, "accB");
        System.out.println("Binary Search Index: " + index);

        int count = SearchService.countOccurrences(logs, "accB");
        System.out.println("Total Occurrences: " + count);
    }
}