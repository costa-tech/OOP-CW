import React from 'react';

function TicketStatus({ availableTickets, soldTickets, capacity }) {
  const getCapacityColor = (cap) => {
    if (cap < 30) return '#2ecc71'; // Green
    if (cap < 70) return '#f1c40f'; // Yellow
    return '#e74c3c'; // Red
  };

  return React.createElement('div', { className: 'ticket-status' },
    React.createElement('h2', null, 'System Status'),
    React.createElement('div', { className: 'status-info' },
      React.createElement('div', { className: 'status-card' },
        React.createElement('h3', null, 'Available Tickets'),
        React.createElement('div', { className: 'status-value' }, availableTickets)
      ),
      React.createElement('div', { className: 'status-card' },
        React.createElement('h3', null, 'Sold Tickets'),
        React.createElement('div', { className: 'status-value' }, soldTickets)
      )
    ),
    
    React.createElement('div', { className: 'capacity-section' },
      React.createElement('h3', null, 'System Capacity'),
      React.createElement('div', { className: 'capacity-bar' },
        React.createElement('div', {
          className: 'capacity-fill',
          style: {
            width: `${Math.min(100, capacity)}%`,
            backgroundColor: getCapacityColor(capacity)
          }
        })
      ),
      React.createElement('div', { className: 'capacity-text' },
        `${capacity.toFixed(1)}% Used`
      )
    )
  );
}

export default TicketStatus;
