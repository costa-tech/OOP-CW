package com.example.ticketingsystem.controller;

import com.example.ticketingsystem.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing the ticketing system.
 * Provides endpoints for configuration, system control, status retrieval, and logging.
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3002", "http://localhost:5173"}, allowCredentials = "true")
public class TicketingController {

    private final TicketingService ticketingService;

    /**
     * Constructor to initialize the controller with the {@link TicketingService}.
     *
     * @param ticketingService the service layer for managing ticketing operations
     */
    @Autowired
    public TicketingController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    /**
     * Updates the system configuration.
     *
     * @param config a map containing configuration parameters
     * @return a success message
     */
    @PostMapping("/config")
    public ResponseEntity<String> updateConfiguration(@RequestBody Map<String, Integer> config) {
        ticketingService.updateConfiguration(
                config.get("totalTickets"),
                config.get("ticketReleaseRate"),
                config.get("customerRetrievalRate"),
                config.get("maxTicketCapacity")
        );
        return ResponseEntity.ok("Configuration updated successfully");
    }

    /**
     * Starts the ticketing system.
     *
     * @return a success message or error details in case of failure
     */
    @PostMapping("/system/start")
    public ResponseEntity<?> startSystem() {
        try {
            ticketingService.startSystem();
            return ResponseEntity.ok("System started successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to start system: " + e.getMessage());
        }
    }

    /**
     * Stops the ticketing system.
     *
     * @return a success message or error details in case of failure
     */
    @PostMapping("/system/stop")
    public ResponseEntity<?> stopSystem() {
        try {
            ticketingService.stopSystem();
            return ResponseEntity.ok("System stopped successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to stop system: " + e.getMessage());
        }
    }

    /**
     * Resets the ticketing system.
     *
     * @return a success message or error details in case of failure
     */
    @PostMapping("/system/reset")
    public ResponseEntity<?> resetSystem() {
        try {
            ticketingService.resetSystem();
            return ResponseEntity.ok("System reset successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to reset system: " + e.getMessage());
        }
    }

    /**
     * Retrieves the current status of the system.
     *
     * @return the system status as a map
     */
    @GetMapping("/system/status")
    public ResponseEntity<?> getSystemStatus() {
        try {
            Map<String, Object> status = new HashMap<>();
            status.put("isRunning", ticketingService.isSystemRunning());
            status.put("availableTickets", ticketingService.getAvailableTickets());
            status.put("totalTickets", ticketingService.getTotalTickets());
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to get system status: " + e.getMessage());
        }
    }

    /**
     * Retrieves the system logs.
     *
     * @return the system logs as a list
     */
    @GetMapping("/system/logs")
    public ResponseEntity<?> getSystemLogs() {
        try {
            return ResponseEntity.ok(ticketingService.getLogs());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to get system logs: " + e.getMessage());
        }
    }

    /**
     * Starts a new vendor thread.
     *
     * @return a success message
     */
    @PostMapping("/vendors/start")
    public ResponseEntity<String> startVendor() {
        ticketingService.startVendor();
        return ResponseEntity.ok("New vendor thread started");
    }

    /**
     * Starts a new customer thread.
     *
     * @return a success message
     */
    @PostMapping("/customers/start")
    public ResponseEntity<String> startCustomer() {
        ticketingService.startCustomer();
        return ResponseEntity.ok("New customer thread started");
    }

    /**
     * Stops all vendor threads.
     *
     * @return a success message
     */
    @PostMapping("/vendors/stop")
    public ResponseEntity<String> stopVendors() {
        ticketingService.stopVendors();
        return ResponseEntity.ok("All vendor threads stopped");
    }

    /**
     * Stops all customer threads.
     *
     * @return a success message
     */
    @PostMapping("/customers/stop")
    public ResponseEntity<String> stopCustomers() {
        ticketingService.stopCustomers();
        return ResponseEntity.ok("All customer threads stopped");
    }
}
