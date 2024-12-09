import { createElement as e, useState } from 'react';
import '../styles/ConfigurationForm.css';

function ConfigurationForm({ onSaveConfig }) {
  const [config, setConfig] = useState({
    totalTickets: 100,
    ticketReleaseRate: 5,
    customerRetrievalRate: 3,
    maxTicketCapacity: 150
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setConfig(prev => ({
      ...prev,
      [name]: parseInt(value, 10)
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSaveConfig(config);
  };

  return e('div', { className: 'config-form' },
    e('h2', null, 'System Configuration'),
    e('form', { onSubmit: handleSubmit },
      // Total Tickets Input
      e('div', { className: 'form-group' },
        e('label', { htmlFor: 'totalTickets' }, 'Total Tickets:'),
        e('input', {
          type: 'number',
          id: 'totalTickets',
          name: 'totalTickets',
          value: config.totalTickets,
          onChange: handleChange,
          min: '1',
          required: true
        }),
        e('small', null, 'Total tickets available in the system')
      ),

      // Ticket Release Rate Input
      e('div', { className: 'form-group' },
        e('label', { htmlFor: 'ticketReleaseRate' }, 'Ticket Release Rate (per minute):'),
        e('input', {
          type: 'number',
          id: 'ticketReleaseRate',
          name: 'ticketReleaseRate',
          value: config.ticketReleaseRate,
          onChange: handleChange,
          min: '1',
          required: true
        }),
        e('small', null, 'How frequently vendors add tickets')
      ),

      // Customer Retrieval Rate Input
      e('div', { className: 'form-group' },
        e('label', { htmlFor: 'customerRetrievalRate' }, 'Customer Retrieval Rate (per minute):'),
        e('input', {
          type: 'number',
          id: 'customerRetrievalRate',
          name: 'customerRetrievalRate',
          value: config.customerRetrievalRate,
          onChange: handleChange,
          min: '1',
          required: true
        }),
        e('small', null, 'How often customers purchase tickets')
      ),

      // Max Ticket Capacity Input
      e('div', { className: 'form-group' },
        e('label', { htmlFor: 'maxTicketCapacity' }, 'Max Ticket Capacity:'),
        e('input', {
          type: 'number',
          id: 'maxTicketCapacity',
          name: 'maxTicketCapacity',
          value: config.maxTicketCapacity,
          onChange: handleChange,
          min: '1',
          required: true
        }),
        e('small', null, 'Maximum capacity of tickets the system can hold')
      ),

      e('button', { type: 'submit', className: 'submit-btn' }, 'Save Configuration')
    )
  );
}

export default ConfigurationForm;
