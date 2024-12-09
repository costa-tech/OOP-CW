import { createElement as e, useState, useEffect } from 'react';
import '../styles/LogDisplay.css';

function LogDisplay({ logs, systemStatus }) {
  const [statusSummary, setStatusSummary] = useState(null);

  useEffect(() => {
    if (systemStatus) {
      const summary = {
        timestamp: new Date().toLocaleTimeString(),
        type: 'system_status',
        message: `System ${systemStatus.isRunning ? 'Running' : 'Stopped'}`,
        availableTickets: systemStatus.currentTickets,
        capacity: (systemStatus.currentTickets / systemStatus.maxCapacity) * 100
      };
      setStatusSummary(summary);
    }
  }, [systemStatus]);

  const getLogTypeStyle = (type) => {
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

  const formatLogMessage = (log) => {
    switch (log.type) {
      case 'ticket_sold':
        return `Ticket ${log.ticketId} sold to customer ${log.customerId} for $${log.price}`;
      case 'ticket_added':
        return `Ticket ${log.ticketId} added by vendor ${log.vendorId}`;
      case 'system_status':
        return `${log.message} (Available: ${log.availableTickets}, Capacity: ${log.capacity?.toFixed(1)}%)`;
      default:
        return log.message;
    }
  };

  return e('div', { className: 'log-display' },
    e('div', { className: 'log-header' },
      e('h2', null, 'System Logs'),
      statusSummary && e('div', { 
        className: `status-badge ${systemStatus?.isRunning ? 'status-running' : 'status-stopped'}` 
      }, 
        `System ${systemStatus?.isRunning ? 'Running' : 'Stopped'}`
      )
    ),
    statusSummary && e('div', { className: 'status-summary' },
      e('div', { className: 'status-metric' },
        e('span', { className: 'metric-label' }, 'Available Tickets:'),
        e('span', { className: 'metric-value' }, systemStatus.currentTickets)
      ),
      e('div', { className: 'status-metric' },
        e('span', { className: 'metric-label' }, 'Capacity:'),
        e('span', { className: 'metric-value' }, 
          `${((systemStatus.currentTickets / systemStatus.maxCapacity) * 100).toFixed(1)}%`
        )
      )
    ),
    e('div', { className: 'log-container' },
      logs.map((log, index) =>
        e('div', {
          key: index,
          className: `log-entry ${getLogTypeStyle(log.type)}`
        },
          e('span', { className: 'log-timestamp' }, log.timestamp),
          e('span', { className: 'log-message' }, formatLogMessage(log))
        )
      ),
      logs.length === 0 && e('div', { className: 'empty-logs' }, 'No logs available')
    )
  );
}

export default LogDisplay;
