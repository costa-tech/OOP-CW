import React from 'react';
import './LogViewer.css';

const LogViewer = ({ logs, systemStatus }) => {
  const createLogEntry = (log, index) => {
    return React.createElement('div', { key: index, className: 'log-entry' },
      React.createElement('span', { className: 'timestamp' }, 
        new Date(log.timestamp).toLocaleTimeString()
      ),
      React.createElement('span', { className: 'message' }, 
        log.message
      ),
      log.ticketAmount && React.createElement('span', { className: 'ticket-info' },
        `Tickets: ${log.ticketAmount} (Available: ${log.availableTickets})`
      )
    );
  };

  const statusInfo = React.createElement('div', { className: 'status-info' },
    React.createElement('p', null,
      React.createElement('span', { className: 'status-label' }, 'Running: '),
      React.createElement('span', { 
        className: `status-value ${systemStatus?.isRunning ? 'running' : 'stopped'}` 
      }, systemStatus?.isRunning ? 'Yes' : 'No')
    ),
    React.createElement('p', null,
      React.createElement('span', { className: 'status-label' }, 'Available Tickets: '),
      React.createElement('span', { className: 'status-value' }, 
        systemStatus?.availableTickets
      )
    ),
    React.createElement('p', null,
      React.createElement('span', { className: 'status-label' }, 'Total Tickets: '),
      React.createElement('span', { className: 'status-value' }, 
        systemStatus?.totalTickets
      )
    )
  );

  return React.createElement('div', { className: 'log-viewer' },
    React.createElement('div', { className: 'status-section' },
      React.createElement('h3', null, 'System Status'),
      statusInfo
    ),
    React.createElement('div', { className: 'logs-section' },
      React.createElement('h3', null, 'System Logs'),
      React.createElement('div', { className: 'logs-container' },
        logs.map(createLogEntry)
      )
    )
  );
};

export default LogViewer;
