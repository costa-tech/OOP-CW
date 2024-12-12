package com.example.ticketingsystem.model;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

/**
 * Represents a pool of tickets that can be added and removed by vendors and customers.
 * Uses ReentrantLock and Conditions for better thread synchronization.
 */
@Component
public class TicketPool {

    private final ConcurrentLinkedQueue<String> ticketQueue;
    private final int maxTicketCapacity;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    private int totalTicketsProcessed = 0;
    private int peakPoolSize = 0;

    /**
     * Constructs a new TicketPool with the specified maximum capacity.
     * 
     * @param maxTicketCapacity the maximum number of tickets that can be stored in the pool
     */
    public TicketPool(@Value("${ticket.pool.capacity:10}") int maxTicketCapacity) {
        this.ticketQueue = new ConcurrentLinkedQueue<>();
        this.maxTicketCapacity = maxTicketCapacity;
        this.lock = new ReentrantLock(true); // fair lock
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }

    /**
     * Adds a ticket to the pool if there is available capacity.
     * If the pool is full, waits until space becomes available.
     * 
     * @param ticket the ticket to add
     * @return true if the ticket was added successfully
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public boolean addTickets(String ticket) throws InterruptedException {
        lock.lock();
        try {
            while (ticketQueue.size() >= maxTicketCapacity) {
                notFull.await();
            }
            boolean added = ticketQueue.offer(ticket);
            if (added) {
                notEmpty.signal();
                totalTicketsProcessed++;
                peakPoolSize = Math.max(peakPoolSize, ticketQueue.size());
            }
            return added;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Removes and returns a ticket from the pool.
     * If the pool is empty, waits until a ticket becomes available.
     * 
     * @return the removed ticket
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public String removeTicket() throws InterruptedException {
        lock.lock();
        try {
            while (ticketQueue.isEmpty()) {
                notEmpty.await();
            }
            String ticket = ticketQueue.poll();
            if (ticket != null) {
                notFull.signal();
                totalTicketsProcessed++;
            }
            return ticket;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current number of tickets in the pool.
     * 
     * @return the number of tickets currently in the pool
     */
    public int getTicketCount() {
        return ticketQueue.size();
    }

    /**
     * Returns the maximum capacity of the ticket pool.
     * 
     * @return the maximum number of tickets that can be stored in the pool
     */
    public int getMaxCapacity() {
        return maxTicketCapacity;
    }

    /**
     * Gets statistics about the ticket pool operations.
     * @return Map containing various statistics
     */
    public Map<String, Integer> getStatistics() {
        lock.lock();
        try {
            Map<String, Integer> stats = new HashMap<>();
            stats.put("currentSize", ticketQueue.size());
            stats.put("maxCapacity", maxTicketCapacity);
            stats.put("totalProcessed", totalTicketsProcessed);
            stats.put("peakPoolSize", peakPoolSize);
            return stats;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the total number of tickets processed (both added and removed).
     * 
     * @return the total number of tickets processed
     */
    public int getTotalTicketsProcessed() {
        return totalTicketsProcessed;
    }

    @Override
    public String toString() {
        return String.format("TicketPool[size=%d, capacity=%d, processed=%d, peak=%d]",
            ticketQueue.size(), maxTicketCapacity, totalTicketsProcessed, peakPoolSize);
    }
}
