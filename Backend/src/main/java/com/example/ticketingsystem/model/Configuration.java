package com.example.ticketingsystem.model;

// Importing JPA annotations for entity mapping
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

// Importing classes for file handling
import java.io.FileWriter;
import java.io.IOException;

@Entity // Marks this class as a JPA entity mapped to a database table
public class Configuration {

    @Id // Marks this field as the primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Auto-generates unique IDs for each record in the database
    private Long id;

    // Fields for configuration settings
    private int totalTickets; // Total number of tickets in the system
    private int ticketReleaseRate; // Rate at which tickets are released
    private int customerRetrievalRate; // Rate at which customers retrieve tickets
    private int maxTicketCapacity; // Maximum number of tickets the system can handle

    // Default constructor required for JPA
    public Configuration() {}

    // Parameterized constructor for initializing the configuration
    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        this.totalTickets = totalTickets;
        this.ticketReleaseRate = ticketReleaseRate;
        this.customerRetrievalRate = customerRetrievalRate;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    // Getters and Setters for encapsulating fields

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        // Validation to ensure total tickets are non-negative
        if (totalTickets < 0) {
            throw new IllegalArgumentException("Total tickets cannot be negative");
        }
        // Validation to ensure total tickets are logically correct relative to capacity
        if (totalTickets > 0 && totalTickets <= maxTicketCapacity) {
            throw new IllegalArgumentException("Total tickets (" + totalTickets + ") must be greater than maximum capacity (" + maxTicketCapacity + ")");
        }
        this.totalTickets = totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Saves the configuration settings to a text file for persistence.
     */
    public void saveToFile() {
        try (FileWriter writer = new FileWriter("configuration.txt")) {
            writer.write("Configuration Settings:\n");
            writer.write("-----------------------\n");
            writer.write("Total Tickets: " + totalTickets + "\n"); // Writes total tickets to the file
            writer.write("Ticket Release Rate: " + ticketReleaseRate + "\n"); // Writes ticket release rate
            writer.write("Customer Retrieval Rate: " + customerRetrievalRate + "\n"); // Writes retrieval rate
            writer.write("Max Ticket Capacity: " + maxTicketCapacity + "\n"); // Writes max capacity
            writer.write("-----------------------\n");
        } catch (IOException e) { // Handles exceptions during file writing
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        // Provides a string representation of the configuration object
        return "Configuration{" +
                "totalTickets=" + totalTickets +
                ", ticketReleaseRate=" + ticketReleaseRate +
                ", customerRetrievalRate=" + customerRetrievalRate +
                ", maxTicketCapacity=" + maxTicketCapacity +
                '}';
    }
}
