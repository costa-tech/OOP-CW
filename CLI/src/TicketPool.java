import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<Integer> tickets = new LinkedList<>(); // Queue to store tickets
    private final int capacity; // Maximum capacity of the ticket pool
    private int totalTicketsSold = 0; // Tracks total tickets sold

    public TicketPool(int capacity) {
        this.capacity = capacity;
        Configuration.logEvent("Ticket Pool initialized with capacity: " + capacity);
    }

    public synchronized void addTicket(int ticket) {
        // Add a ticket if there's room, otherwise log failure
        if (tickets.size() < capacity) {
            tickets.add(ticket);
            Configuration.logEvent("New ticket added to pool: Ticket #" + ticket +
                    " | Pool size: " + tickets.size() + "/" + capacity);
            notifyAll(); // Notify waiting threads
        } else {
            Configuration.logEvent("Failed to add ticket: Pool is at capacity (" + capacity + ")");
        }
    }

    public synchronized int retrieveTicket() {
        // Wait if no tickets are available
        while (tickets.isEmpty()) {
            try {
                Configuration.logEvent("Customer waiting for ticket - Pool empty");
                wait(); // Wait until notified
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Handle interruption
                Configuration.logEvent("Ticket retrieval interrupted");
                return -1; // Indicate failure
            }
        }
        // Retrieve a ticket and update ticket count
        int ticket = tickets.poll();
        totalTicketsSold++;
        Configuration.logEvent("Ticket #" + ticket + " sold | Total tickets sold: " + totalTicketsSold +
                " | Remaining in pool: " + tickets.size());
        return ticket;
    }

    public synchronized Queue<Integer> getTickets() {
        return new LinkedList<>(tickets); // Return a copy of the ticket queue
    }

    public int getCapacity() {
        return capacity; // Return the pool's capacity
    }

    public int getTotalTicketsSold() {
        return totalTicketsSold; // Return the total tickets sold
    }
}
