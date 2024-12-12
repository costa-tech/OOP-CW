package com.example.ticketingsystem.websocket;

// Importing necessary Spring WebSocket and scheduling classes
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;
import com.example.ticketingsystem.service.TicketingService;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.stereotype.Component;

// Marks the class as a Spring-managed component (bean)
@Component
public class TicketingWebSocketHandler extends TextWebSocketHandler {
    // Extends TextWebSocketHandler to handle text-based WebSocket messages

    private final TicketingService ticketingService;
    // Injects the TicketingService to retrieve ticket-related data

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    // Maintains a thread-safe list of active WebSocket sessions

    // Constructor for dependency injection of the TicketingService
    public TicketingWebSocketHandler(TicketingService ticketingService) {
        this.ticketingService = ticketingService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Adds the new session to the active sessions list when a WebSocket connection is established
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Removes the session from the list when the WebSocket connection is closed
        sessions.remove(session);
    }

    @Scheduled(fixedRate = 1000)
    // Scheduled task that runs every 1 second to send updates to all connected clients
    public void sendUpdates() {
        // Generates a status update message with ticketing information
        String statusUpdate = String.format(
                "Current Status:\nTickets Available in Pool: %d\nTickets Processed: %d/%d\nRemaining Tickets: %d",
                ticketingService.getTicketCount(), // Number of tickets currently available
                ticketingService.getConfiguration().getTotalTickets() - ticketingService.getTicketCount(), // Tickets processed
                ticketingService.getConfiguration().getTotalTickets(), // Total tickets configured
                ticketingService.getTicketCount() // Remaining tickets
        );

        // Sends the status update to all active WebSocket sessions
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(statusUpdate));
                // Sends a text message to the client
            } catch (Exception e) {
                e.printStackTrace(); // Logs any exception that occurs while sending the message
            }
        }
    }
}
