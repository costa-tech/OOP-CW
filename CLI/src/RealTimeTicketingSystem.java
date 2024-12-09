import java.io.IOException;
import java.util.Scanner;
import java.util.logging.*;

public class RealTimeTicketingSystem {
    private static final Logger logger = Logger.getLogger(RealTimeTicketingSystem.class.getName());

    public static TicketPool ticketPool;
    public static Configuration config;
    private static boolean running = false;
    private static Thread vendorThread;
    private static Thread customerThread;
    private static Thread simulationThread;
    private static int processedTickets = 0;

    public static void main(String[] args) {
        setupLogger();  // Set up the logger
        Scanner scanner = new Scanner(System.in);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (config != null) {
                config.saveToFile();
                logger.info("Configuration saved on shutdown.");
            }
        }));

        while (true) {  // Loop to display the menu after the system stops
            System.out.println("\nMain Menu:");
            System.out.println("1. Configure System");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");

            int choice = getPositiveIntInput(scanner, "");  // Allow the user to input a choice

            switch (choice) {
                case 1:
                    configureSystem(scanner);  // Configure the system and start automatically
                    break;
                case 2:
                    System.out.println("Exiting the program...");
                    logger.info("Program exited by the user.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
            }
        }
    }

    private static void setupLogger() {
        try {
            // Set up the file handler
            FileHandler fileHandler = new FileHandler("system.log", true);  // Append to existing log
            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String FORMAT = "[%1$tF %1$tT] %2$s%n";

                @Override
                public synchronized String format(LogRecord record) {
                    return String.format(FORMAT,
                            record.getMillis(),
                            record.getMessage());
                }
            });

            // Set up the console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter() {
                private static final String FORMAT = "[%1$tT] %2$s%n";

                @Override
                public synchronized String format(LogRecord record) {
                    return String.format(FORMAT,
                            record.getMillis(),
                            record.getMessage());
                }
            });

            // Add handlers to logger
            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);
            logger.setUseParentHandlers(false);  // Disable default console logging

            logger.info("Logger setup complete.");
        } catch (IOException e) {
            System.err.println("Failed to set up file logger: " + e.getMessage());
        }
    }

    private static void configureSystem(Scanner scanner) {
        Configuration savedConfig = Configuration.loadFromFile();

        if (savedConfig != null) {
            System.out.println("Found saved configuration:");
            logger.info("Found saved configuration:");
            logger.info("Total Tickets: " + savedConfig.getTotalTickets());
            logger.info("Max Pool Capacity: " + savedConfig.getMaxTicketCapacity());
            logger.info("Release Rate: " + savedConfig.getTicketReleaseRate());
            logger.info("Retrieval Rate: " + savedConfig.getCustomerRetrievalRate());
            System.out.print("Would you like to use this configuration? (yes/no): ");

            String response = scanner.nextLine().toLowerCase();
            if (response.equals("yes")) {
                config = savedConfig;
                ticketPool = new TicketPool(config.getMaxTicketCapacity());
                startSystem();  // Automatically start the system after loading config
                return;
            }
        }

        System.out.println("Configure New System Parameters:");
        int totalTickets = getPositiveIntInput(scanner, "Enter total number of tickets to sell: ");
        int maxTicketCapacity;

        while (true) {
            maxTicketCapacity = getPositiveIntInput(scanner, "Enter maximum ticket pool capacity: ");
            if (maxTicketCapacity <= totalTickets) {
                break;
            } else {
                System.out.println("Maximum ticket pool capacity cannot exceed total number of tickets.");
            }
        }

        int ticketReleaseRate = getPositiveIntInput(scanner, "Enter ticket release rate (tickets per second): ");
        int customerRetrievalRate = getPositiveIntInput(scanner, "Enter customer retrieval rate (tickets per second): ");

        config = new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
        config.saveToFile();  // Log configuration save

        ticketPool = new TicketPool(config.getMaxTicketCapacity());

        startSystem();  // Automatically start the system after configuration
    }

    private static int getPositiveIntInput(Scanner scanner, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input > 0) {
                    return input;
                } else {
                    System.out.println("Please enter a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static void startSystem() {
        running = true;
        System.out.println("Starting the ticketing system...");
        logger.info("Ticketing system started.");

        simulationThread = new Thread(() -> {
            vendorThread = new Thread(new Vendor(ticketPool, config.getTicketReleaseRate()));
            customerThread = new Thread(new Customer(ticketPool, config.getCustomerRetrievalRate()));
            vendorThread.start();
            customerThread.start();

            while (running) {
                displayStatus();

                if (processedTickets >= config.getTotalTickets()) {
                    System.out.println("All tickets have been processed. Shutting down the system...");
                    logger.info("All tickets processed. Shutting down.");
                    stopSystem();
                    break;
                }

                try {
                    Thread.sleep(1000);  // Refresh status every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    logger.severe("Simulation interrupted: " + e.getMessage());
                    break;
                }
            }
        });
        simulationThread.start();

        new Thread(() -> {
            System.out.println("Press Enter at any time to stop the simulation...");
            new Scanner(System.in).nextLine();  // Wait for Enter key
            stopSystem();  // Stop the system when Enter is pressed
        }).start();

        try {
            simulationThread.join();  // Wait for the simulation thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.severe("Main thread interrupted: " + e.getMessage());
        }
    }

    private static void stopSystem() {
        running = false;
        System.out.println("Stopping the ticketing system...");
        logger.info("Stopping ticketing system...");
        if (vendorThread != null) {
            vendorThread.interrupt();
        }
        if (customerThread != null) {
            customerThread.interrupt();
        }
        if (simulationThread != null) {
            simulationThread.interrupt();
        }
        logger.info("System stopped.");
    }

    private static void displayStatus() {
        int remainingTickets = Math.max(0, config.getTotalTickets() - processedTickets);
        System.out.println("\nCurrent Status:");
        System.out.println("Tickets Available in Pool: " + ticketPool.getTickets().size());
        System.out.println("Tickets Processed: " + processedTickets + "/" + config.getTotalTickets());
        System.out.println("Remaining Tickets: " + remainingTickets);

        // Log ticket transaction status
        logger.info("Tickets Available in Pool: " + ticketPool.getTickets().size());
        logger.info("Tickets Processed: " + processedTickets + "/" + config.getTotalTickets());
        logger.info("Remaining Tickets: " + remainingTickets);
    }

    public static synchronized void incrementProcessedTickets() {
        if (processedTickets < config.getTotalTickets()) {
            processedTickets++;
            // Log the ticket transaction
            logger.info("Processed ticket " + processedTickets);
        }
    }

    public static synchronized int getProcessedTickets() {
        return processedTickets;
    }
}
