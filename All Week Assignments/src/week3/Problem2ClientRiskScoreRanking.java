package week3;

import java.util.*;

class Client {
    String name;
    int riskScore;
    double accountBalance;

    public Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String toString() {
        return name + "(" + riskScore + ")";
    }
}

class RiskSortingService {

    public static void bubbleSortAscending(Client[] arr) {
        int n = arr.length;
        int swaps = 0;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j].riskScore > arr[j + 1].riskScore) {
                    Client temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                }
            }

            if (!swapped) break;
        }

        System.out.println("Bubble Sort (Ascending Risk):");
        printArray(arr);
        System.out.println("Total Swaps: " + swaps);
    }

    public static void insertionSortDescending(Client[] arr) {
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            Client key = arr[i];
            int j = i - 1;

            while (j >= 0 && compare(arr[j], key) < 0) {
                arr[j + 1] = arr[j];
                j--;
            }

            arr[j + 1] = key;
        }

        System.out.println("Insertion Sort (Descending Risk + Balance):");
        printArray(arr);
    }

    private static int compare(Client c1, Client c2) {
        if (c1.riskScore != c2.riskScore) {
            return Integer.compare(c1.riskScore, c2.riskScore);
        }
        return Double.compare(c1.accountBalance, c2.accountBalance);
    }

    private static void printArray(Client[] arr) {
        for (Client c : arr) {
            System.out.print(c + " ");
        }
        System.out.println();
    }
}

class RiskAnalyzer {

    public static void topHighRiskClients(Client[] arr, int topN) {
        System.out.println("Top " + topN + " High Risk Clients:");
        for (int i = 0; i < Math.min(topN, arr.length); i++) {
            System.out.println(arr[i].name + " (" + arr[i].riskScore + ")");
        }
    }
}

public class Problem2ClientRiskScoreRanking {

    public static void main(String[] args) {

        Client[] clients = {
                new Client("clientC", 80, 2000),
                new Client("clientA", 20, 5000),
                new Client("clientB", 50, 3000)
        };

        Client[] bubbleArray = clients.clone();
        Client[] insertionArray = clients.clone();

        RiskSortingService.bubbleSortAscending(bubbleArray);

        RiskSortingService.insertionSortDescending(insertionArray);

        RiskAnalyzer.topHighRiskClients(insertionArray, 10);
    }
}