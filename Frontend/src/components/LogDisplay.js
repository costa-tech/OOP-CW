import { createElement as e, useState, useEffect } from 'react';

// Function to display logs and system status
function LogDisplay({ logs, systemStatus }) {
  // State to store status summary and ticket pool status
  const [statusSummary, setStatusSummary] = useState(null);
  const [ticketPoolStatus, setTicketPoolStatus] = useState({
    availableTickets: 0,
    totalProcessed: 0
  });

  // Effect to update status summary and ticket pool status when system status changes
  useEffect(() => {
    if (systemStatus) {
      // Create a summary object with current system status
      const summary = {
        timestamp: new Date().toLocaleTimeString(),
        type: 'system_status',
        message: `System ${systemStatus.isRunning ? 'Running' : 'Stopped'}`,
        availableTickets: systemStatus.currentTickets,
        capacity: (systemStatus.currentTickets / systemStatus.maxCapacity) * 100
      };
      // Update status summary and ticket pool status
      setStatusSummary(summary);
      setTicketPoolStatus(prev => ({
        ...prev,
        availableTickets: systemStatus.currentTickets
      }));
    }
  }, [systemStatus]);

  // Effect to update ticket pool status when logs change
  useEffect(() => {
    if (logs && logs.length > 0) {
      // Get the last log
      const lastLog = logs[logs.length - 1];
      // Update ticket pool status if the last log is a ticket sold or added log
      if (lastLog.type === 'ticket_sold' || lastLog.type === 'ticket_added') {
        setTicketPoolStatus(prev => ({
          availableTickets: lastLog.availableTickets,
          totalProcessed: prev.totalProcessed + 1
        }));
      }
    }
  }, [logs]);

  // Function to get log type style
  const getLogTypeStyle = (type) => {
    // Return a CSS class based on the log type
    switch (type) {
      case 'error':
        return 'log-error';
      case 'warning':
        return 'log-warning';
      case 'ticket_sold':
        return 'log-success';
      case 'ticket_added':
        return 'log-info';
      case 'system_status':
        return 'log-status';
      default:
        return '';
    }
  };

  // Function to format log messages based on their type
  const formatLogMessage = (log) => {
    // Return a formatted log message based on the log type
    switch (log.type) {
      case 'ticket_sold':
        // Log message for ticket sold
        return `Customer bought ${log.ticketAmount} ticket(s)`;
      case 'ticket_added':
        // Log message for ticket added
        return `Vendor added ${log.ticketAmount} ticket(s) to the pool`;
      case 'system_status':
        // Log message for system status changes
        if (log.message.includes('started')) {
          return 'System simulation started';
        } else if (log.message.includes('stopped')) {
          return 'System simulation stopped';
        } else if (log.message.includes('reset')) {
          return 'System reset - all values cleared';
        }
        return log.message;
      default:
        return log.message;
    }
  };

  // Return the JSX for the log display component
  return e('div', { className: 'log-display' },
    // Ticket Pool Status Section
    e('div', { className: 'ticket-pool-status', style: {
      backgroundColor: '#f5f5f5',
      padding: '15px',
      borderRadius: '5px',
      marginBottom: '20px',
      textAlign: 'center'
    }},
      e('h2', null, 'Ticket System Simulation'),
      e('div', { style: { display: 'flex', justifyContent: 'space-around', marginTop: '10px' }},
        e('div', null,
          e('strong', null, 'Tickets in Pool: '),
          e('span', null, ticketPoolStatus.availableTickets)
        ),
        e('div', null,
          e('strong', null, 'Simulation Status: '),
          e('span', { style: { color: systemStatus?.isRunning ? 'green' : 'red' }},
            systemStatus?.isRunning ? 'Running' : 'Stopped'
          )
        )
      )
    ),
    // Logs Section
    e('div', { className: 'log-container', style: {
      maxHeight: '400px',
      overflowY: 'auto',
      padding: '10px',
      border: '1px solid #ddd',
      borderRadius: '5px',
      backgroundColor: '#fff'
    }},
      e('div', { style: { marginBottom: '10px', fontWeight: 'bold' }}, 'Simulation Log:'),
      logs.map((log, index) =>
        e('div', {
          key: index,
          className: `log-entry ${getLogTypeStyle(log.type)}`,
          style: { 
            marginBottom: '5px', 
            padding: '8px', 
            borderLeft: '4px solid',
            borderLeftColor: log.type === 'ticket_sold' ? '#4CAF50' : 
                           log.type === 'ticket_added' ? '#2196F3' : 
                           log.type === 'system_status' ? '#FF9800' : '#ccc',
            backgroundColor: '#f8f9fa'
          }
        },
          e('span', { className: 'log-message' }, formatLogMessage(log))
        )
      ).reverse()
    )
  );
}

// Export the LogDisplay component
export default LogDisplay;
