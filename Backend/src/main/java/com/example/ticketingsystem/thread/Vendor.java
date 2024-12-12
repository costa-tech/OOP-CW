package com.example.ticketingsystem.thread;

// Importing the TicketPool class to interact with the shared ticket pool
import com.example.ticketingsystem.model.TicketPool;

public class Vendor extends Thread {
    // Represents a vendor thread responsible for adding tickets to the ticket pool

    private final TicketPool ticketPool;
    // Shared resource where tickets are added
    private final String vendorId;
    // Unique identifier for the vendor
    private volatile boolean running = true;
    // A flag to control the thread's running state; volatile ensures visibility across threads
    private int ticketsAdded = 0;
    // Counter to track the number of tickets added by this vendor

    // Constructor to initialize the ticket pool and vendor ID
    public Vendor(TicketPool ticketPool, String vendorId) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
    }

    // Method to stop the vendor thread gracefully
    public void stopVendor() {
        running = false; // Sets the running flag to false to exit the loop
        interrupt(); // Interrupts the thread if it's waiting or sleeping
    }

    // Getter for the number of tickets added
    public int getTicketsAdded() {
        return ticketsAdded;
    }

    // Getter for the vendor's unique ID
    public String getVendorId() {
        return vendorId;
    }

    @Override
    public void run() {
        // The main logic of the vendor thread
        while (running) {
            // Keeps running until the `running` flag is set to false
            try {
                Thread.sleep(2000);
                // Simulates a delay of 2 seconds for adding a ticket
                String ticket = vendorId + "-T" + System.currentTimeMillis();
                // Generates a unique ticket using the vendor ID and the current timestamp
                if (ticketPool.addTickets(ticket)) {
                    // Attempts to add the ticket to the ticket pool
                    ticketsAdded++;
                    // Increments the counter if the ticket is successfully added
                }
            } catch (InterruptedException e) {
                // Handles interruption (e.g., during thread stopping)
                System.out.println("Vendor thread " + vendorId + " interrupted");
                Thread.currentThread().interrupt();
                // Restores the interrupt status
                break;
                // Exits the loop when interrupted
            }
        }
        // Logs the total tickets added after the thread stops
        System.out.println("Vendor " + vendorId + " stopped. Total tickets added: " + ticketsAdded);
    }
}
