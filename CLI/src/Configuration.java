import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;
    private static final String SETTINGS_FILE = "settings.txt";
    private static final String LOG_FILE = "system_logs.txt";

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() { return totalTickets; }
    public int getTicketReleaseRate() { return ticketReleaseRate; }
    public int getCustomerRetrievalRate() { return customerRetrievalRate; }
    public int getMaxTicketCapacity() { return maxTicketCapacity; }

    // Save configuration to both JSON and text files
    public void saveToFile() {
        saveToJsonFile();
        saveSettingsToFile();
    }

    // Save configuration to a JSON file
    private void saveToJsonFile() {
        Gson gson = new Gson();
        String filePath = "Configuration.json";
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(this, writer);
            System.out.println("Configuration saved to " + filePath);
            logEvent("Configuration saved to " + filePath);
        } catch (IOException e) {
            System.out.println("Failed to save configuration to JSON file: " + e.getMessage());
            logEvent("Failed to save configuration to JSON file: " + e.getMessage());
        }
    }

    // Save settings to a text file
    private void saveSettingsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SETTINGS_FILE))) {
            writer.write("Settings Last Updated: " + getCurrentTimestamp() + "\n");
            writer.write("Total Tickets: " + totalTickets + "\n");
            writer.write("Ticket Release Rate: " + ticketReleaseRate + " tickets/second\n");
            writer.write("Customer Retrieval Rate: " + customerRetrievalRate + " tickets/second\n");
            writer.write("Max Ticket Pool Capacity: " + maxTicketCapacity + "\n");
            System.out.println("Settings saved to " + SETTINGS_FILE);
            logEvent("Settings saved to " + SETTINGS_FILE);
        } catch (IOException e) {
            System.err.println("Failed to save settings to text file: " + e.getMessage());
            logEvent("Failed to save settings to text file: " + e.getMessage());
        }
    }

    // Log an event to the log file
    public static void logEvent(String event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String logEntry = String.format("[%s] %s", getCurrentTimestamp(), event);
            // Add separator line for better readability if it's a ticket-related event
            if (event.contains("Ticket")) {
                logEntry += "\n----------------------------------------";
            }
            writer.write(logEntry + "\n");
        } catch (IOException e) {
            System.err.println("Failed to write to log file: " + e.getMessage());
        }
    }

    // Get current timestamp for logging
    private static String getCurrentTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        return now.format(formatter);
    }

    // Load configuration from a JSON file
    public static Configuration loadFromFile() {
        Gson gson = new Gson();
        String filePath = "Configuration.json";
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filePath))) {
            Configuration config = gson.fromJson(reader, Configuration.class);
            System.out.println("Configuration loaded from " + filePath);
            logEvent("Configuration loaded from " + filePath);
            return config;
        } catch (IOException e) {
            System.out.println("No existing configuration found. Creating new configuration.");
            logEvent("No existing configuration found. Creating new configuration.");
            return null;
        }
    }

    // Clear log file
    public static void clearLogs() {
        try {
            new FileWriter(LOG_FILE).close();
            logEvent("Log file cleared");
        } catch (IOException e) {
            System.err.println("Failed to clear log file: " + e.getMessage());
            logEvent("Failed to clear log file: " + e.getMessage());
        }
    }

    // Read all logs
    public static String readLogs() {
        try {
            return new String(Files.readAllBytes(Paths.get(LOG_FILE)));
        } catch (IOException e) {
            return "Failed to read logs: " + e.getMessage();
        }
    }
}