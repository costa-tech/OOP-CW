package com.example.ticketingsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import com.example.ticketingsystem.model.TicketPool;
import com.example.ticketingsystem.thread.Customer;
import com.example.ticketingsystem.thread.Vendor;
import com.example.ticketingsystem.model.Configuration;

@Service
@EnableScheduling
public class TicketingService {

    private TicketPool ticketPool;
    private final List<Vendor> vendors;
    private final List<Customer> customers;
    private boolean systemRunning = false;
    private Configuration configuration;
    private final List<Map<String, Object>> systemLogs;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public TicketingService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
        this.vendors = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.configuration = new Configuration();
        this.systemLogs = new ArrayList<>();
        addLog("System initialized", "system_status");
    }

    private void addLog(String message, String type) {
        Map<String, Object> log = new HashMap<>();
        log.put("timestamp", LocalDateTime.now().format(formatter));
        log.put("type", type);
        log.put("message", message);
        systemLogs.add(0, log); // Add to the beginning of the list
        
        // Keep only the last 100 logs
        if (systemLogs.size() > 100) {
            systemLogs.remove(systemLogs.size() - 1);
        }
    }

    private void addTicketLog(int ticketAmount, String actorId, String type) {
        Map<String, Object> log = new HashMap<>();
        log.put("timestamp", LocalDateTime.now().format(formatter));
        log.put("type", type);
        log.put("ticketAmount", ticketAmount);
        log.put("actorId", actorId);
        log.put("message", type.equals("ticket_added") ? 
                String.format("Vendor added %d ticket(s) to the pool", ticketAmount) :
                String.format("Customer bought %d ticket(s)", ticketAmount));
        systemLogs.add(0, log);
        
        if (systemLogs.size() > 100) {
            systemLogs.remove(systemLogs.size() - 1);
        }
    }

    public List<Map<String, Object>> getLogs() {
        return new ArrayList<>(systemLogs);
    }

    // Start a new vendor thread
    public void startVendor() {
        String vendorId = "V-" + UUID.randomUUID().toString().substring(0, 8);
        Vendor vendor = new Vendor(ticketPool, vendorId);
        vendors.add(vendor);
        vendor.start();
        // Wait for the vendor to add tickets
        try {
            Thread.sleep(2000); // Ensure some tickets are added before logging
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int ticketsAdded = vendor.getTicketsAdded();
        addTicketLog(ticketsAdded, vendorId, "ticket_added");
        addLog("Started new vendor: " + vendorId, "system_status");
    }

    // Start a new customer thread
    public void startCustomer() {
        String customerId = "C-" + UUID.randomUUID().toString().substring(0, 8);
        Customer customer = new Customer(ticketPool);
        customers.add(customer);
        customer.start();
        // Wait for the customer to purchase tickets
        try {
            Thread.sleep(1000); // Ensure some tickets are purchased before logging
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        int ticketsBought = customer.getTicketsPurchased();
        addTicketLog(ticketsBought, customerId, "ticket_sold");
        addLog("Started new customer: " + customerId, "system_status");
    }

    // Stop all vendor threads
    public void stopVendors() {
        vendors.forEach(Vendor::stopVendor);
        vendors.clear();
        addLog("All vendors stopped", "system_status");
    }

    // Stop all customer threads
    public void stopCustomers() {
        customers.forEach(Customer::stopCustomer);
        customers.clear();
        addLog("All customers stopped", "system_status");
    }

    // Get current ticket count
    public int getTicketCount() {
        return ticketPool.getTicketCount();
    }

    // Get maximum ticket capacity
    public int getMaxCapacity() {
        return ticketPool.getMaxCapacity();
    }

    // Get number of active vendors
    public int getActiveVendorCount() {
        return vendors.size();
    }

    // Get number of active customers
    public int getActiveCustomerCount() {
        return customers.size();
    }
    

    // Get detailed system statistics
    public Map<String, Object> getDetailedStatus() {
        Map<String, Object> status = new HashMap<>();
        
        // Pool statistics
        status.put("poolStats", ticketPool.getStatistics());
        
        // Vendor statistics
        Map<String, Integer> vendorStats = new HashMap<>();
        vendors.forEach(v -> vendorStats.put(v.getVendorId(), v.getTicketsAdded()));
        status.put("vendorStats", vendorStats);
        
        // Customer statistics
        List<Integer> customerStats = customers.stream()
            .map(Customer::getTicketsPurchased)
            .collect(Collectors.toList());
        status.put("customerStats", customerStats);
        
        return status;
    }

    // Update system configuration
    public synchronized void updateConfiguration(Integer totalTickets, Integer ticketReleaseRate, 
                                  Integer customerRetrievalRate, Integer maxTicketCapacity) {
        // Stop the system before updating configuration
        if (systemRunning) {
            stopSystem();
        }

        // Validate input parameters
        if (totalTickets != null) {
            if (totalTickets < 0) {
                throw new IllegalArgumentException("Total tickets cannot be negative");
            }
            // Check against the new maxTicketCapacity if provided
            if (maxTicketCapacity != null && totalTickets <= maxTicketCapacity) {
                throw new IllegalArgumentException("Total tickets must exceed maximum capacity");
            }
            // Check against current maxTicketCapacity if new one not provided
            if (maxTicketCapacity == null && totalTickets <= configuration.getMaxTicketCapacity()) {
                throw new IllegalArgumentException("Total tickets must exceed current maximum capacity");
            }
        }

        if (maxTicketCapacity != null) {
            if (maxTicketCapacity < 0) {
                throw new IllegalArgumentException("Maximum ticket capacity cannot be negative");
            }
            // Check against the new totalTickets if provided
            if (totalTickets != null && totalTickets <= maxTicketCapacity) {
                throw new IllegalArgumentException("Total tickets must exceed maximum capacity");
            }
            // Check against current totalTickets if new one not provided
            if (totalTickets == null && configuration.getTotalTickets() <= maxTicketCapacity) {
                throw new IllegalArgumentException("Current total tickets must exceed new maximum capacity");
            }
        }

        if (ticketReleaseRate != null && ticketReleaseRate < 0) {
            throw new IllegalArgumentException("Ticket release rate cannot be negative");
        }
        if (customerRetrievalRate != null && customerRetrievalRate < 0) {
            throw new IllegalArgumentException("Customer retrieval rate cannot be negative");
        }

        // Update configuration
        if (maxTicketCapacity != null) {
            configuration.setMaxTicketCapacity(maxTicketCapacity);
            this.ticketPool = new TicketPool(maxTicketCapacity);
        }
        if (totalTickets != null) {
            configuration.setTotalTickets(totalTickets);
        }
        if (ticketReleaseRate != null) {
            configuration.setTicketReleaseRate(ticketReleaseRate);
        }
        if (customerRetrievalRate != null) {
            configuration.setCustomerRetrievalRate(customerRetrievalRate);
        }
        
        addLog("Configuration updated", "system_status");
    }

    // Start the ticketing system
    public synchronized void startSystem() {
        if (!systemRunning) {
            // Validate configuration before starting
            if (configuration.getMaxTicketCapacity() <= 0) {
                throw new IllegalStateException("Invalid ticket capacity. Please configure the system first.");
            }
            if (configuration.getTicketReleaseRate() <= 0) {
                throw new IllegalStateException("Invalid ticket release rate. Please configure the system first.");
            }
            if (configuration.getCustomerRetrievalRate() <= 0) {
                throw new IllegalStateException("Invalid customer retrieval rate. Please configure the system first.");
            }

            systemRunning = true;
            // Start initial vendors and customers based on configuration
            for (int i = 0; i < configuration.getTicketReleaseRate(); i++) {
                startVendor();
            }
            for (int i = 0; i < configuration.getCustomerRetrievalRate(); i++) {
                startCustomer();
            }
            addLog("System started", "system_status");
        }
    }

    // Stop the ticketing system
    public synchronized void stopSystem() {
        if (systemRunning) {
            systemRunning = false;
            stopVendors();
            stopCustomers();
            addLog("System stopped", "system_status");
        }
    }

    // Reset the system to initial state
    public void resetSystem() {
        stopSystem();
        this.ticketPool = new TicketPool(configuration.getMaxTicketCapacity());
        systemRunning = false;
        addLog("System reset", "system_status");
    }

    // Check if the system is running
    public boolean isSystemRunning() {
        return systemRunning;
    }

    // Get current configuration
    public Configuration getConfiguration() {
        return configuration;
    }

    // Get available tickets
    public int getAvailableTickets() {
        return ticketPool.getTicketCount();
    }

    // Get total tickets processed
    public int getTotalTickets() {
        return ticketPool.getTotalTicketsProcessed();
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Shutting down ticketing service...");
        stopVendors();
        stopCustomers();
        addLog("System shutdown", "system_status");
    }
}