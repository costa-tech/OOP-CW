import React from 'react';
import { useState, useEffect } from 'react';
import { 
  getSystemStatus, 
  updateConfiguration, 
  startSystem,
  stopSystem,
  resetSystem
} from './services/api';
import './App.css';
import LogDisplay from './components/LogDisplay';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';

function App() {
  // Configuration state
  const [config, setConfig] = useState({
    totalTickets: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: ''
  });

  // System status state
  const [systemStatus, setSystemStatus] = useState({
    currentTickets: 0,
    maxCapacity: 0,
    isRunning: false
  });

  // Error/notification state
  const [notification, setNotification] = useState(null);
  
  // Logs state
  const [logs, setLogs] = useState([]);

  // Fetch system status periodically
  useEffect(() => {
    const fetchStatus = async () => {
      try {
        const status = await getSystemStatus();
        setSystemStatus(status);
      } catch (error) {
        showNotification(error.message, 'error');
      }
    };

    fetchStatus();
    const interval = setInterval(fetchStatus, 1000);
    return () => clearInterval(interval);
  }, []);

  // Show notification helper
  const showNotification = (message, type = 'info') => {
    setNotification({ message, type });
    setTimeout(() => setNotification(null), 5000);
  };

  // Handle configuration update
  const handleConfigSubmit = async (newConfig) => {
    try {
      // Validate configuration
      if (newConfig.totalTickets <= 0 || newConfig.maxTicketCapacity <= 0) {
        showNotification('Ticket counts must be positive numbers', 'error');
        return;
      }
      if (newConfig.ticketReleaseRate <= 0 || newConfig.customerRetrievalRate <= 0) {
        showNotification('Rates must be positive numbers', 'error');
        return;
      }
      if (newConfig.totalTickets > newConfig.maxTicketCapacity) {
        showNotification('Total tickets cannot exceed maximum capacity', 'error');
        return;
      }

      await updateConfiguration(newConfig);
      setConfig(newConfig);
      showNotification('Configuration updated successfully', 'success');
    } catch (error) {
      showNotification(error.message, 'error');
    }
  };

  // Handle system start
  const handleStart = async () => {
    try {
      await startSystem();
      showNotification('System started successfully', 'success');
    } catch (error) {
      showNotification(error.message, 'error');
    }
  };

  // Handle system stop
  const handleStop = async () => {
    try {
      await stopSystem();
      showNotification('System stopped successfully', 'success');
    } catch (error) {
      showNotification(error.message, 'error');
    }
  };

  // Handle system reset
  const handleReset = async () => {
    try {
      await resetSystem();
      showNotification('System reset successfully', 'success');
    } catch (error) {
      showNotification(error.message, 'error');
    }
  };

  return React.createElement('div', { className: 'App' },
    // Navigation Bar
    React.createElement('nav', { className: 'navbar' },
      React.createElement('div', { className: 'nav-brand' }, 'Ticket Management System')
    ),

    // Notification display
    notification && React.createElement('div', { className: `notification ${notification.type}` },
      notification.message
    ),

    // Configuration Form
    React.createElement('section', { className: 'config-section', id: 'config' },
      React.createElement(ConfigurationForm, { 
        onSaveConfig: handleConfigSubmit 
      })
    ),

    // Control Panel
    React.createElement('section', { className: 'control-section', id: 'system' },
      React.createElement(ControlPanel, {
        isRunning: systemStatus.isRunning,
        onStart: handleStart,
        onStop: handleStop,
        onReset: handleReset
      })
    ),

    // Log Display
    React.createElement('section', { className: 'logs-section', id: 'logs' },
      React.createElement(LogDisplay, {
        logs: logs,
        systemStatus: systemStatus
      })
    )
  );
}

export default App;
