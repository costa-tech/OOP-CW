Real-Time Ticketing System

Overview

The Real-Time Ticketing System is a Java-based simulation designed to manage ticket distribution and customer retrieval in real time. It features configurable parameters for ticket release and retrieval rates, ticket pool capacity, and total tickets. The system provides a menu-driven interface for configuration, logging, and monitoring.

Features

Configuration Management: Save and load system configurations in JSON and text formats.

Real-Time Simulation: Simulate ticket release and retrieval with adjustable rates.

Logging: Log all significant events, including configuration changes, ticket transactions, and system status.

Status Monitoring: Display the current status of the ticket pool and processed tickets.

Graceful Shutdown: Automatically save the configuration and terminate processes when the system shuts down.

Components

Configuration: Manages system parameters, file storage, and logging.

TicketPool: Represents the shared pool of tickets, with synchronized methods for adding and retrieving tickets.

Vendor: Simulates the ticket release process.

Customer: Simulates ticket retrieval by customers.

RealTimeTicketingSystem: Main class that orchestrates the simulation and provides a user interface.

Prerequisites

Java Development Kit (JDK) 8 or later

Gson library for JSON handling (download from Maven)

Setup

Clone the repository or download the source code.

Add the Gson library to your classpath.

Compile the Java files:

javac -cp gson-<version>.jar *.java

Run the main program:

java -cp .:gson-<version>.jar RealTimeTicketingSystem

Usage

Main Menu

Configure System:

Enter parameters such as total tickets, pool capacity, ticket release rate, and customer retrieval rate.

Choose to load an existing configuration if available.

Starts the system simulation automatically after configuration.

Exit:

Terminates the program gracefully, saving the configuration and stopping all threads.

Simulation

Press Enter at any time during the simulation to stop the system.

The simulation threads manage ticket release and retrieval independently while displaying the current status.

Logs

All significant events are logged in system_logs.txt.

System logs are stored in system.log for debugging purposes.

Files

settings.txt: Stores the current configuration in a human-readable format.

Configuration.json: Stores the configuration in JSON format.

system_logs.txt: Contains event logs.

system.log: Detailed logs for debugging.

Extending the System

Adding Features

Implement new classes or modify existing ones to include additional functionality (e.g., dynamic rate adjustment).

Update the Configuration class to handle new parameters.

Improving Logging

Enhance log messages to include more detailed context or use structured formats (e.g., JSON).

Troubleshooting

Issue: Log files are not created.

Solution: Ensure the program has write permissions to the directory.

Issue: Simulation threads not terminating.

Solution: Press Enter to stop the simulation or ensure proper thread management in the code.

Issue: Missing Gson library.

Solution: Download the library and add it to the classpath.

License

This project is open-source and licensed under the MIT License.
