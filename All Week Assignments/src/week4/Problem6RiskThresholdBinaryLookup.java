package week4;

import java.util.*;

class RiskSearchService {

    public static void linearSearch(int[] arr, int target) {
        int comparisons = 0;
        boolean found = false;

        for (int i = 0; i < arr.length; i++) {
            comparisons++;
            if (arr[i] == target) {
                found = true;
                break;
            }
        }

        System.out.println("Linear Search Found: " + found);
        System.out.println("Comparisons: " + comparisons);
    }

    public static int binaryInsertionPoint(int[] arr, int target) {
        int low = 0, high = arr.length;
        int comparisons = 0;

        while (low < high) {
            int mid = (low + high) / 2;
            comparisons++;

            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        System.out.println("Binary Comparisons: " + comparisons);
        return low;
    }

    public static int floor(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] <= target) {
                result = arr[mid];
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return result;
    }

    public static int ceiling(int[] arr, int target) {
        int low = 0, high = arr.length - 1;
        int result = -1;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (arr[mid] >= target) {
                result = arr[mid];
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return result;
    }

    public static void printArray(int[] arr) {
        for (int x : arr)
            System.out.print(x + " ");
        System.out.println();
    }
}

public class Problem6RiskThresholdBinaryLookup {

    public static void main(String[] args) {

        int[] risks = {10, 25, 50, 100};

        System.out.println("Risk Bands:");
        RiskSearchService.printArray(risks);

        int target = 30;

        RiskSearchService.linearSearch(risks, target);

        int index = RiskSearchService.binaryInsertionPoint(risks, target);
        System.out.println("Insertion Index: " + index);

        int floor = RiskSearchService.floor(risks, target);
        int ceiling = RiskSearchService.ceiling(risks, target);

        System.out.println("Floor: " + floor);
        System.out.println("Ceiling: " + ceiling);
    }
}