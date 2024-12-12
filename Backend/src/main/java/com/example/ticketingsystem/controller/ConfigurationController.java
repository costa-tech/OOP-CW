package com.example.ticketingsystem.controller;

import com.example.ticketingsystem.model.Configuration;
import com.example.ticketingsystem.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing system configurations.
 * Provides endpoints to save and retrieve configuration data.
 */
@RestController
@RequestMapping("/api/configuration")
@CrossOrigin(origins = "http://localhost:3000")
public class ConfigurationController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    /**
     * Saves the provided configuration to the database.
     *
     * @param configuration the configuration object to save
     * @return the saved configuration object, or an error message in case of failure
     */
    @PostMapping("/save")
    public ResponseEntity<?> saveConfiguration(@RequestBody Configuration configuration) {
        try {
            Configuration savedConfig = configurationRepository.save(configuration);
            return ResponseEntity.ok(savedConfig);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving configuration: " + e.getMessage());
        }
    }

    /**
     * Retrieves the latest configuration.
     * This method currently returns an empty configuration object with default values.
     *
     * @return a default configuration object
     */
    @GetMapping("/latest")
    public ResponseEntity<?> getLatestConfiguration() {
        Configuration emptyConfig = new Configuration();
        emptyConfig.setTotalTickets(0);
        emptyConfig.setTicketReleaseRate(0);
        emptyConfig.setCustomerRetrievalRate(0);
        emptyConfig.setMaxTicketCapacity(0);
        return ResponseEntity.ok(emptyConfig);
    }
}
