import { createElement as e } from 'react';
import '../styles/TicketStatus.css';

function TicketStatus({ availableTickets, soldTickets, capacity }) {
  const getCapacityColor = (cap) => {
    if (cap < 30) return '#2ecc71'; // Green
    if (cap < 70) return '#f1c40f'; // Yellow
    return '#e74c3c'; // Red
  };

  return e('div', { className: 'ticket-status' },
    e('h2', null, 'System Status'),
    e('div', { className: 'status-info' },
      e('div', { className: 'status-card' },
        e('h3', null, 'Available Tickets'),
        e('div', { className: 'status-value' }, availableTickets)
      ),
      e('div', { className: 'status-card' },
        e('h3', null, 'Sold Tickets'),
        e('div', { className: 'status-value' }, soldTickets)
      )
    ),
    
    e('div', { className: 'capacity-section' },
      e('h3', null, 'System Capacity'),
      e('div', { className: 'capacity-bar' },
        e('div', {
          className: 'capacity-fill',
          style: {
            width: `${Math.min(100, capacity)}%`,
            backgroundColor: getCapacityColor(capacity)
          }
        })
      ),
      e('div', { className: 'capacity-text' },
        `${capacity.toFixed(1)}% Used`
      )
    )
  );
}

export default TicketStatus;
