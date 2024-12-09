package com.example.ticketingsystem.thread;

import com.example.ticketingsystem.model.TicketPool;

public class Customer extends Thread {

    private final TicketPool ticketPool;
    private volatile boolean running = true;
    private int ticketsPurchased = 0;

    public Customer(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    public void stopCustomer() {
        running = false;
        interrupt();
    }

    public int getTicketsPurchased() {
        return ticketsPurchased;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // Simulate time delay for purchasing a ticket
                String ticket = ticketPool.removeTicket();
                if (ticket != null) {
                    ticketsPurchased++;
                }
            } catch (InterruptedException e) {
                System.out.println("Customer thread interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Customer thread stopped. Total tickets purchased: " + ticketsPurchased);
    }
}
