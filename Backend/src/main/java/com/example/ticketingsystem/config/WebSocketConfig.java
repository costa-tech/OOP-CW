package com.example.ticketingsystem.config;

// Importing Spring annotations and WebSocket configuration classes
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.ticketingsystem.websocket.TicketingWebSocketHandler;

// Marks this class as a Spring configuration component
@Configuration
// Enables WebSocket support in the application
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    // Injects a custom WebSocket handler to handle WebSocket connections
    private final TicketingWebSocketHandler ticketingWebSocketHandler;

    // Constructor for dependency injection of the WebSocket handler
    public WebSocketConfig(TicketingWebSocketHandler ticketingWebSocketHandler) {
        this.ticketingWebSocketHandler = ticketingWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Registers WebSocket handler and maps it to a specific endpoint
        registry.addHandler(ticketingWebSocketHandler, "/ws/ticketing")
                .setAllowedOrigins("*");
        // Allows WebSocket connections from all origins. Consider restricting this in production for security.
    }
}
