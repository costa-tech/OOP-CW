import java.util.LinkedList;
import java.util.Queue;

public class TicketPool {
    private final Queue<Integer> tickets = new LinkedList<>();
    private final int capacity;
    private int totalTicketsSold = 0;

    public TicketPool(int capacity) {
        this.capacity = capacity;
        Configuration.logEvent("Ticket Pool initialized with capacity: " + capacity);
    }

    public synchronized void addTicket(int ticket) {
        if (tickets.size() < capacity) {
            tickets.add(ticket);
            Configuration.logEvent("New ticket added to pool: Ticket #" + ticket + " | Pool size: " + tickets.size() + "/" + capacity);
            notifyAll();
        } else {
            Configuration.logEvent("Failed to add ticket: Pool is at capacity (" + capacity + ")");
        }
    }

    public synchronized int retrieveTicket() {
        while (tickets.isEmpty()) {
            try {
                Configuration.logEvent("Customer waiting for ticket - Pool empty");
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Configuration.logEvent("Ticket retrieval interrupted");
                return -1;
            }
        }
        int ticket = tickets.poll();
        totalTicketsSold++;
        Configuration.logEvent("Ticket #" + ticket + " sold | Total tickets sold: " + totalTicketsSold + 
                             " | Remaining in pool: " + tickets.size());
        return ticket;
    }

    public synchronized Queue<Integer> getTickets() {
        return new LinkedList<>(tickets);
    }

    public int getCapacity() {
        return capacity;
    }

    public int getTotalTicketsSold() {
        return totalTicketsSold;
    }
}