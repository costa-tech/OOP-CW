package com.example.ticketingsystem.thread;

import com.example.ticketingsystem.model.TicketPool;

public class Vendor extends Thread {

    private final TicketPool ticketPool;
    private final String vendorId;
    private volatile boolean running = true;
    private int ticketsAdded = 0;

    public Vendor(TicketPool ticketPool, String vendorId) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
    }

    public void stopVendor() {
        running = false;
        interrupt();
    }

    public int getTicketsAdded() {
        return ticketsAdded;
    }

    public String getVendorId() {
        return vendorId;
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(2000); // Simulate time delay for adding a ticket
                String ticket = vendorId + "-T" + System.currentTimeMillis();
                if (ticketPool.addTickets(ticket)) {
                    ticketsAdded++;
                }
            } catch (InterruptedException e) {
                System.out.println("Vendor thread " + vendorId + " interrupted");
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("Vendor " + vendorId + " stopped. Total tickets added: " + ticketsAdded);
    }
}
