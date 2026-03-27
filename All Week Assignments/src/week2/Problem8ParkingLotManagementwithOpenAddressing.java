package week2;

import java.util.*;

public class Problem8ParkingLotManagementwithOpenAddressing {

    private static class Spot {
        String plate;
        long entryTime;
        boolean deleted;
    }

    private final Spot[] table;
    private final int capacity = 500;
    private int size = 0;
    private int totalProbes = 0;
    private int operations = 0;
    private final Map<Integer, Integer> hourlyUsage = new HashMap<>();

    public Problem8ParkingLotManagementwithOpenAddressing() {
        table = new Spot[capacity];
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % capacity;
    }

    public String parkVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index] != null && !table[index].deleted) {
            index = (index + 1) % capacity;
            probes++;
        }

        Spot s = new Spot();
        s.plate = plate;
        s.entryTime = System.currentTimeMillis();
        s.deleted = false;

        table[index] = s;
        size++;
        totalProbes += probes;
        operations++;

        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    public String exitVehicle(String plate) {
        int index = hash(plate);
        int probes = 0;

        while (table[index] != null) {
            if (!table[index].deleted && table[index].plate.equals(plate)) {
                long durationMs = System.currentTimeMillis() - table[index].entryTime;
                table[index].deleted = true;
                size--;

                long hours = durationMs / (1000 * 60 * 60);
                long minutes = (durationMs / (1000 * 60)) % 60;
                double fee = hours * 5 + minutes * 0.1;

                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                hourlyUsage.put(hour, hourlyUsage.getOrDefault(hour, 0) + 1);

                return "Spot #" + index + " freed, Duration: " + hours + "h " + minutes + "m, Fee: $" + String.format("%.2f", fee);
            }
            index = (index + 1) % capacity;
            probes++;
        }
        return "Vehicle not found";
    }

    public String getStatistics() {
        double occupancy = (size * 100.0) / capacity;
        double avgProbes = operations == 0 ? 0 : (totalProbes * 1.0 / operations);

        int peakHour = -1;
        int max = 0;
        for (Map.Entry<Integer, Integer> e : hourlyUsage.entrySet()) {
            if (e.getValue() > max) {
                max = e.getValue();
                peakHour = e.getKey();
            }
        }

        return "Occupancy: " + String.format("%.1f", occupancy) + "%, Avg Probes: " +
                String.format("%.2f", avgProbes) + ", Peak Hour: " +
                (peakHour == -1 ? "N/A" : peakHour + "-" + (peakHour + 1));
    }

    public static void main(String[] args) throws Exception {
        Problem8ParkingLotManagementwithOpenAddressing system = new Problem8ParkingLotManagementwithOpenAddressing();

        System.out.println(system.parkVehicle("ABC-1234"));
        System.out.println(system.parkVehicle("ABC-1235"));
        System.out.println(system.parkVehicle("XYZ-9999"));

        Thread.sleep(2000);

        System.out.println(system.exitVehicle("ABC-1234"));
        System.out.println(system.getStatistics());
    }
}