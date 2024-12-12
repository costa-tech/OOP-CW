# README

# Ticket Management System - CLI

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


# Ticket Management System - FrontEnd

## Introduction
The Ticket Management System is a real-time ticket processing application that simulates ticket sales and distribution. It features a multi-threaded backend for handling concurrent ticket operations and a reactive frontend interface for monitoring and controlling the system.

## Prerequisites
- Backend:
  - Java JDK 17 or higher
  - Maven 3.8.x or higher
  - Spring Boot 3.x

- Frontend:
  - Node.js 16.x or higher
  - npm 8.x or higher

## Setup Instructions

### Backend Setup
1. Navigate to the backend directory:
   ```bash
   cd ../backend
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   The backend server will start on http://localhost:8080

### Frontend Setup
1. Navigate to the frontend directory:
   ```bash
   cd Frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```
   The frontend application will be available at http://localhost:5173

## Usage Instructions

### System Configuration
1. **Initial Setup**
   - Set Total Tickets: Define the total number of tickets in the system
   - Set Ticket Release Rate: How frequently new tickets are released
   - Set Customer Retrieval Rate: How often customers attempt to purchase tickets
   - Set Maximum Ticket Capacity: The maximum number of tickets that can be in the system

2. **Save Configuration**
   - Click "Save Configuration" to store your settings
   - The system will validate your inputs before saving

### System Controls
1. **Start System**
   - Click the "Start" button to begin ticket processing
   - The system will initialize with your saved configuration

2. **Stop System**
   - Click the "Stop" button to pause all ticket operations
   - The system will complete any in-progress transactions before stopping

3. **Reset System**
   - Click the "Reset" button to clear all current operations
   - This will reset all counters and clear the logs

### Monitoring Interface
1. **System Status**
   - Running Status: Shows if the system is currently active
   - Available Tickets: Current number of tickets in the pool
   - Total Tickets: Maximum ticket capacity

2. **Ticket Pool Status**
   - Current Tickets: Number of tickets currently available
   - Maximum Capacity: Total ticket capacity of the system

3. **System Logs**
   - Real-time updates of system operations
   - Timestamp for each operation
   - Details of ticket transactions
   - System state changes

## Troubleshooting
- If the system doesn't respond, check if both backend and frontend servers are running
- Ensure all configuration values are within valid ranges
- Check the browser console for any error messages
- Verify network connectivity to both frontend and backend servers

## Additional Notes
- The system uses a multi-threaded approach for concurrent ticket processing
- All operations are logged and displayed in real-time
- The interface updates automatically to reflect the current system state
- Configuration changes require a system reset to take effect


# Ticket Management System - BackEnd

A modern, robust ticketing system built with Spring Boot, providing efficient ticket management capabilities with real-time updates through WebSocket integration.

## Introduction

This ticketing system is a comprehensive solution designed to manage and track tickets efficiently. It features:
- RESTful API endpoints for ticket management
- Real-time updates using WebSocket
- MySQL database integration
- Thymeleaf templating for frontend views
- Secure and scalable architecture

## Prerequisites

Before you begin, ensure you have the following installed:
- Java Development Kit (JDK) 22
- MySQL Server 8.0 or higher
- Maven 3.6 or higher
- Your preferred IDE (IntelliJ IDEA, Eclipse, or VS Code recommended)

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone [your-repository-url]
   cd ticketing-system
   ```

2. **Database Configuration**
   - Create a MySQL database
   - Update `src/main/resources/application.properties` with your database credentials

3. **Build the Application**
   ```bash
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   Or run using the provided wrapper:
   ```bash
   ./mvnw spring-boot:run   # For Unix-based systems
   mvnw.cmd spring-boot:run # For Windows
   ```

## Usage Instructions

### Starting the System
1. Ensure MySQL server is running
2. Start the application using one of the methods mentioned above
3. Access the application at `http://localhost:8080`

### System Configuration
- The application uses the following default ports:
  - Web Server: 8080
  - WebSocket: 8080/ws
- Configuration can be modified in `application.properties`

### UI Controls and Features
1. **Ticket Management**
   - Create new tickets
   - View existing tickets
   - Update ticket status
   - Assign tickets to users

2. **Real-time Updates**
   - Automatic updates for ticket status changes
   - Live notifications for new tickets

3. **User Interface**
   - Intuitive dashboard for ticket overview
   - Search and filter functionality
   - Responsive design for various screen sizes

## Additional Information

- The system uses Spring Boot 3.2.0
- WebSocket integration for real-time updates
- JPA/Hibernate for database operations
- Thymeleaf for server-side templating

For more detailed information about specific features or technical documentation, please refer to the project's Wiki or contact the development team.
