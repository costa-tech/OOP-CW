package com.example.ticketingsystem;

import com.example.ticketingsystem.model.TicketPool;
import com.example.ticketingsystem.thread.Vendor;
import com.example.ticketingsystem.thread.Customer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class TicketingSystemApplication {

    private static List<Vendor> vendors = new ArrayList<>();
    private static List<Customer> customers = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(TicketingSystemApplication.class, args);

        // Create a ticket pool with a maximum capacity of 10 tickets
        TicketPool ticketPool = new TicketPool(10);

        // Create and start Vendor threads
        Vendor vendor1 = new Vendor(ticketPool, "Ticket 001");
        Vendor vendor2 = new Vendor(ticketPool, "Ticket 002");
        vendors.add(vendor1);
        vendors.add(vendor2);
        vendor1.start();
        vendor2.start();

        // Create and start Customer threads
        Customer customer1 = new Customer(ticketPool);
        Customer customer2 = new Customer(ticketPool);
        customers.add(customer1);
        customers.add(customer2);
        customer1.start();
        customer2.start();

        // Add a shutdown hook to clean up threads on application shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Application is shutting down. Cleaning up resources...");

            // Stop all vendor threads
            for (Vendor vendor : vendors) {
                vendor.stopVendor();
            }

            // Stop all customer threads
            for (Customer customer : customers) {
                customer.stopCustomer();
            }

            System.out.println("Resources cleaned up successfully.");
        }));
    }
}
