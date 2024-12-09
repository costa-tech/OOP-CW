package com.example.ticketingsystem.controller;

import com.example.ticketingsystem.service.TicketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
public class TicketingController {

    private final TicketingService ticketingService;

    @Autowired
    public TicketingController(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @PostMapping("/config")
    public ResponseEntity<String> updateConfiguration(
            @RequestBody Map<String, Integer> config) {
        ticketingService.updateConfiguration(
            config.get("totalTickets"),
            config.get("ticketReleaseRate"),
            config.get("customerRetrievalRate"),
            config.get("maxTicketCapacity")
        );
        return ResponseEntity.ok("Configuration updated successfully");
    }

    @PostMapping("/system/start")
    public ResponseEntity<String> startSystem() {
        ticketingService.startSystem();
        return ResponseEntity.ok("System started successfully");
    }

    @PostMapping("/system/stop")
    public ResponseEntity<String> stopSystem() {
        ticketingService.stopSystem();
        return ResponseEntity.ok("System stopped successfully");
    }

    @PostMapping("/system/reset")
    public ResponseEntity<String> resetSystem() {
        ticketingService.resetSystem();
        return ResponseEntity.ok("System reset successfully");
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSystemStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("currentTickets", ticketingService.getTicketCount());
        status.put("maxCapacity", ticketingService.getMaxCapacity());
        status.put("activeVendors", ticketingService.getActiveVendorCount());
        status.put("activeCustomers", ticketingService.getActiveCustomerCount());
        status.put("isRunning", ticketingService.isSystemRunning());
        status.put("configuration", ticketingService.getConfiguration());
        return ResponseEntity.ok(status);
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDetailedStatistics() {
        return ResponseEntity.ok(ticketingService.getDetailedStatus());
    }

    @PostMapping("/vendors/start")
    public ResponseEntity<String> startVendor() {
        ticketingService.startVendor();
        return ResponseEntity.ok("New vendor thread started");
    }

    @PostMapping("/customers/start")
    public ResponseEntity<String> startCustomer() {
        ticketingService.startCustomer();
        return ResponseEntity.ok("New customer thread started");
    }

    @PostMapping("/vendors/stop")
    public ResponseEntity<String> stopVendors() {
        ticketingService.stopVendors();
        return ResponseEntity.ok("All vendor threads stopped");
    }

    @PostMapping("/customers/stop")
    public ResponseEntity<String> stopCustomers() {
        ticketingService.stopCustomers();
        return ResponseEntity.ok("All customer threads stopped");
    }
}
