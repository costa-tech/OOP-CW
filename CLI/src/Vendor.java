public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int ticketReleaseRate;

    public Vendor(TicketPool ticketPool, int ticketReleaseRate) {
        this.ticketPool = ticketPool;
        this.ticketReleaseRate = ticketReleaseRate;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                if (RealTimeTicketingSystem.getProcessedTickets() >= RealTimeTicketingSystem.config.getTotalTickets()) {
                    break;  // Stop adding tickets when all have been processed
                }

                synchronized (ticketPool) {
                    for (int i = 0; i < ticketReleaseRate; i++) {
                        if (ticketPool.getTickets().size() < ticketPool.getCapacity()) {
                            ticketPool.addTicket((int) (Math.random() * 1000));
                        }
                    }
                    ticketPool.notifyAll();  // Notify waiting customers
                }

                Thread.sleep(1000);  // Delay for ticket release
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}