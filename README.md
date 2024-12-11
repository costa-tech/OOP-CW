# README

## Introduction
This Real-Time Ticketing System simulates a ticket distribution mechanism where tickets are added to a pool by a vendor and retrieved by customers. It is designed with the following features:
- Configurable parameters such as the total number of tickets, ticket release rate, and customer retrieval rate.
- Logging system to record activities.
- Persistent storage of configurations in both JSON and text formats.

The system provides a real-time view of ticket availability, processed tickets, and system status.

## Setup Instructions

### Prerequisites
To run this application, ensure the following requirements are met:
1. **Java Development Kit (JDK)**
   - Version: 8 or higher
2. **External Libraries**
   - Gson library (to handle JSON operations). Download and include the Gson library in your project.

### Steps to Build and Run the Application
1. **Clone or Download the Project**
   - Clone the repository or download the source code as a ZIP file and extract it.

2. **Set Up the Environment**
   - Ensure that the Gson library is included in the classpath. For example:
     - Add the Gson JAR file to your IDE's project library.
     - Use the command: `javac -cp .:gson-<version>.jar RealTimeTicketingSystem.java` (on Linux/Mac) or `javac -cp .;gson-<version>.jar RealTimeTicketingSystem.java` (on Windows).

3. **Compile the Application**
   - Open a terminal or command prompt.
   - Navigate to the directory containing the source files.
   - Run the command:
     ```
     javac -cp gson-<version>.jar *.java
     ```

4. **Run the Application**
   - Execute the program with:
     ```
     java -cp gson-<version>.jar;. RealTimeTicketingSystem
     ```

## Usage Instructions

### Configuring and Starting the System
1. **Main Menu**
   - Upon running the application, the main menu is displayed with the following options:
     - `1. Configure System`: Configure and start the ticketing system.
     - `2. Exit`: Exit the application.

2. **Configuration Steps**
   - If a saved configuration is found, the program will ask if you want to use it. Otherwise, input the following parameters:
     - Total number of tickets.
     - Maximum ticket pool capacity (must not exceed the total number of tickets).
     - Ticket release rate (tickets per second).
     - Customer retrieval rate (tickets per second).

3. **Automatic Start**
   - Once configured, the system starts automatically, with a vendor thread adding tickets to the pool and a customer thread retrieving tickets.

4. **Stop the System**
   - Press `Enter` at any time to stop the simulation.

### Logs and Configuration Files
- **Log File** (`system_logs.txt`): Contains event logs, such as tickets being added, retrieved, and system status updates.
- **Configuration Files**:
  - `Configuration.json`: Stores the system configuration in JSON format.
  - `settings.txt`: Stores a readable version of the system configuration.

### Clearing Logs
- Logs can be cleared by using the `clearLogs()` method in the `Configuration` class or manually deleting the `system_logs.txt` file.

### Reading Logs
- Use the `readLogs()` method to retrieve and print log content in the console.

### Extending the System
- Developers can add additional features or modify the existing system by extending classes such as `Customer`, `Vendor`, or adding new functionality to `RealTimeTicketingSystem`.

