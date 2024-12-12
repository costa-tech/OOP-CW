package com.example.ticketingsystem.thread;

// Importing the TicketPool class to interact with the shared ticket pool
import com.example.ticketingsystem.model.TicketPool;

public class Customer extends Thread {
    // Represents a customer thread responsible for purchasing tickets from the ticket pool

    private final TicketPool ticketPool;
    // Shared resource where tickets are consumed
    private volatile boolean running = true;
    // A flag to control the thread's running state; volatile ensures visibility across threads
    private int ticketsPurchased = 0;
    // Counter to track the number of tickets purchased by this customer

    // Constructor to initialize the ticket pool
    public Customer(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    // Method to stop the customer thread gracefully
    public void stopCustomer() {
        running = false; // Sets the running flag to false to exit the loop
        interrupt(); // Interrupts the thread if it's waiting or sleeping
    }

    // Getter for the number of tickets purchased
    public int getTicketsPurchased() {
        return ticketsPurchased;
    }

    @Override
    public void run() {
        // The main logic of the customer thread
        while (running) {
            // Keeps running until the `running` flag is set to false
            try {
                Thread.sleep(1000);
                // Simulates a delay of 1 second for purchasing a ticket
                String ticket = ticketPool.removeTicket();
                // Attempts to remove a ticket from the ticket pool
                if (ticket != null) {
                    // If a ticket is successfully removed (not null), increment the counter
                    ticketsPurchased++;
                }
            } catch (InterruptedException e) {
                // Handles interruption (e.g., during thread stopping)
                System.out.println("Customer thread interrupted");
                Thread.currentThread().interrupt();
                // Restores the interrupt status
                break;
                // Exits the loop when interrupted
            }
        }
        // Logs the total tickets purchased after the thread stops
        System.out.println("Customer thread stopped. Total tickets purchased: " + ticketsPurchased);
    }
}
