package com.example.ticketingsystem.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ticketingsystem.model.TicketPool;
import com.example.ticketingsystem.thread.Customer;
import com.example.ticketingsystem.thread.Vendor;
import com.example.ticketingsystem.model.Configuration;

@Service
public class TicketingService {

    private TicketPool ticketPool;
    private final List<Vendor> vendors;
    private final List<Customer> customers;
    private boolean systemRunning = false;
    private Configuration configuration;

    @Autowired
    public TicketingService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
        this.vendors = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.configuration = new Configuration();
    }

    // Start a new vendor thread
    public void startVendor() {
        String vendorId = "V-" + UUID.randomUUID().toString().substring(0, 8);
        Vendor vendor = new Vendor(ticketPool, vendorId);
        vendors.add(vendor);
        vendor.start();
    }

    // Start a new customer thread
    public void startCustomer() {
        Customer customer = new Customer(ticketPool);
        customers.add(customer);
        customer.start();
    }

    // Stop all vendor threads
    public void stopVendors() {
        vendors.forEach(Vendor::stopVendor);
        vendors.clear();
    }

    // Stop all customer threads
    public void stopCustomers() {
        customers.forEach(Customer::stopCustomer);
        customers.clear();
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
        if (totalTickets != null && totalTickets < 0) {
            throw new IllegalArgumentException("Total tickets cannot be negative");
        }
        if (ticketReleaseRate != null && ticketReleaseRate < 0) {
            throw new IllegalArgumentException("Ticket release rate cannot be negative");
        }
        if (customerRetrievalRate != null && customerRetrievalRate < 0) {
            throw new IllegalArgumentException("Customer retrieval rate cannot be negative");
        }
        if (maxTicketCapacity != null && maxTicketCapacity < 0) {
            throw new IllegalArgumentException("Maximum ticket capacity cannot be negative");
        }

        // Update configuration
        if (totalTickets != null) {
            configuration.setTotalTickets(totalTickets);
        }
        if (ticketReleaseRate != null) {
            configuration.setTicketReleaseRate(ticketReleaseRate);
        }
        if (customerRetrievalRate != null) {
            configuration.setCustomerRetrievalRate(customerRetrievalRate);
        }
        if (maxTicketCapacity != null) {
            configuration.setMaxTicketCapacity(maxTicketCapacity);
            this.ticketPool = new TicketPool(maxTicketCapacity);
        }
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
        }
    }

    // Stop the ticketing system
    public synchronized void stopSystem() {
        if (systemRunning) {
            systemRunning = false;
            stopVendors();
            stopCustomers();
        }
    }

    // Reset the system to initial state
    public void resetSystem() {
        stopSystem();
        this.ticketPool = new TicketPool(configuration.getMaxTicketCapacity());
        systemRunning = false;
    }

    // Check if the system is running
    public boolean isSystemRunning() {
        return systemRunning;
    }

    // Get current configuration
    public Configuration getConfiguration() {
        return configuration;
    }

    @PreDestroy
    public void cleanup() {
        System.out.println("Shutting down ticketing service...");
        stopVendors();
        stopCustomers();
    }
}
