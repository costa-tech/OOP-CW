public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int retrievalRate;

    public Customer(TicketPool ticketPool, int retrievalRate) {
        this.ticketPool = ticketPool;
        this.retrievalRate = retrievalRate;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (RealTimeTicketingSystem.getProcessedTickets() >= RealTimeTicketingSystem.config.getTotalTickets()) {
                    break;  // Stop if all tickets are processed
                }

                int ticket = ticketPool.retrieveTicket();
                if (ticket != -1) {  // If a ticket was retrieved
                    RealTimeTicketingSystem.incrementProcessedTickets();
                }

                Thread.sleep(1000 / retrievalRate);  // Delay between ticket retrievals
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}