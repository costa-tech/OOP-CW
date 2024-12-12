// ConfigurationForm component handles user input for system configuration
// It validates input and submits configuration to the parent component

// Import necessary hooks and styles
import React, { useState } from 'react';

// Define the ConfigurationForm component
function ConfigurationForm({ onSaveConfig }) {
  // State to store form input values
  const [config, setConfig] = useState({
    totalTickets: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: ''
  });

  // Handle input changes and update state
  const handleChange = (e) => {
    const { name, value } = e.target;
    // Remove any non-numeric characters except backspace
    const numericValue = value.replace(/[^0-9]/g, '');
    
    // Only update if empty or a valid positive number
    if (numericValue === '' || parseInt(numericValue, 10) >= 0) {
      setConfig(prev => ({
        ...prev,
        [name]: numericValue
      }));
    }
  };

  // Validate and submit form values
  const handleSubmit = (e) => {
    e.preventDefault();
    // Convert all values to numbers before submitting
    const numericConfig = {
      totalTickets: parseInt(config.totalTickets) || 0,
      ticketReleaseRate: parseInt(config.ticketReleaseRate) || 0,
      customerRetrievalRate: parseInt(config.customerRetrievalRate) || 0,
      maxTicketCapacity: parseInt(config.maxTicketCapacity) || 0
    };
    // Call the onSaveConfig function with validated values
    onSaveConfig(numericConfig);
  };

  // Render the configuration form
  return React.createElement('div', { className: 'system-configuration' },
    React.createElement('h2', null, 'System Configuration'),
    React.createElement('form', { onSubmit: handleSubmit },
      // Total Tickets Input
      React.createElement('div', { className: 'form-group' },
        React.createElement('label', { htmlFor: 'totalTickets' }, 'Total Tickets:'),
        React.createElement('input', {
          type: 'text',
          inputMode: 'numeric',
          pattern: '[0-9]*',
          id: 'totalTickets',
          name: 'totalTickets',
          value: config.totalTickets,
          onChange: handleChange,
          required: true,
          placeholder: ''
        }),
        React.createElement('small', null, 'Total number of tickets to be processed by the system')
      ),

      // Ticket Release Rate Input
      React.createElement('div', { className: 'form-group' },
        React.createElement('label', { htmlFor: 'ticketReleaseRate' }, 'Ticket Release Rate (ticketReleaseRate):'),
        React.createElement('input', {
          type: 'text',
          inputMode: 'numeric',
          pattern: '[0-9]*',
          id: 'ticketReleaseRate',
          name: 'ticketReleaseRate',
          value: config.ticketReleaseRate,
          onChange: handleChange,
          required: true,
          placeholder: ''
        }),
        React.createElement('small', null, 'Time in seconds between each ticket release by vendors')
      ),

      // Customer Retrieval Rate Input
      React.createElement('div', { className: 'form-group' },
        React.createElement('label', { htmlFor: 'customerRetrievalRate' }, 'Customer Retrieval Rate (customerRetrievalRate):'),
        React.createElement('input', {
          type: 'text',
          inputMode: 'numeric',
          pattern: '[0-9]*',
          id: 'customerRetrievalRate',
          name: 'customerRetrievalRate',
          value: config.customerRetrievalRate,
          onChange: handleChange,
          required: true,
          placeholder: ''
        }),
        React.createElement('small', null, 'Time in seconds between each ticket collection by customers')
      ),

      // Max Ticket Capacity Input
      React.createElement('div', { className: 'form-group' },
        React.createElement('label', { htmlFor: 'maxTicketCapacity' }, 'Max Ticket Capacity:'),
        React.createElement('input', {
          type: 'text',
          inputMode: 'numeric',
          pattern: '[0-9]*',
          id: 'maxTicketCapacity',
          name: 'maxTicketCapacity',
          value: config.maxTicketCapacity,
          onChange: handleChange,
          required: true,
          placeholder: ''
        }),
        React.createElement('small', null, 'Maximum number of tickets that can be in the system at once')
      ),

      React.createElement('button', { 
        type: 'submit',
        className: 'submit-btn'
      }, 'Save Configuration')
    )
  );
}

// Export the ConfigurationForm component
export default ConfigurationForm;
