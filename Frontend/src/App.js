// App component is the main entry point of the application
// It manages the overall state and handles system operations

// Import necessary hooks, components, and services
import React, { useState, useEffect } from 'react';
import { 
  getSystemStatus, 
  updateConfiguration, 
  startSystem,
  stopSystem,
  resetSystem,
  getLogs,
  saveConfigurationToDb,
  getLatestConfiguration
} from './services/api';
import './App.css';
import LogDisplay from './components/LogDisplay';
import ConfigurationForm from './components/ConfigurationForm';
import ControlPanel from './components/ControlPanel';
import LogViewer from './components/LogViewer';

// Define the App component
function App() {
  // State to store configuration settings
  const [config, setConfig] = useState({
    totalTickets: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: ''
  });

  // State to store system status
  const [systemStatus, setSystemStatus] = useState({
    availableTickets: 0,
    totalTickets: 0,
    isRunning: false
  });

  // State to store notifications
  const [notification, setNotification] = useState(null);
  
  // State to store logs
  const [logs, setLogs] = useState([]);

  // Fetch system status and logs periodically
  useEffect(() => {
    const fetchStatusAndLogs = async () => {
      try {
        console.log('Fetching system status and logs...');
        const [status, logsData] = await Promise.all([
          getSystemStatus(),
          getLogs()
        ]);
        console.log('Received system status:', status);
        console.log('Received logs:', logsData);
        setSystemStatus(status);
        setLogs(logsData);
      } catch (error) {
        console.error('Error fetching status and logs:', error);
        showNotification(error.message, 'error');
      }
    };

    fetchStatusAndLogs();
    const interval = setInterval(fetchStatusAndLogs, 1000);
    return () => clearInterval(interval);
  }, []);

  // Helper function to show notifications
  const showNotification = (message, type = 'info') => {
    console.log(`Showing notification: ${message} (${type})`);
    setNotification({ message, type });
    setTimeout(() => setNotification(null), 5000);
  };

  // Handle configuration submission
  const handleConfigSubmit = async (newConfig) => {
    console.log('Processing configuration submission:', newConfig);
    try {
      // Convert values to numbers and validate
      const numericConfig = {
        totalTickets: parseInt(newConfig.totalTickets),
        ticketReleaseRate: parseInt(newConfig.ticketReleaseRate),
        customerRetrievalRate: parseInt(newConfig.customerRetrievalRate),
        maxTicketCapacity: parseInt(newConfig.maxTicketCapacity)
      };

      // Validate all fields are valid numbers
      if (Object.values(numericConfig).some(isNaN)) {
        const message = 'All configuration values must be valid numbers';
        console.error('Validation error:', message, numericConfig);
        showNotification(message, 'error');
        return;
      }

      // Validate positive numbers
      if (numericConfig.totalTickets <= 0 || numericConfig.maxTicketCapacity <= 0) {
        const message = 'Ticket counts must be positive numbers';
        console.error('Validation error:', message, numericConfig);
        showNotification(message, 'error');
        return;
      }

      if (numericConfig.ticketReleaseRate <= 0 || numericConfig.customerRetrievalRate <= 0) {
        const message = 'Rates must be positive numbers';
        console.error('Validation error:', message, numericConfig);
        showNotification(message, 'error');
        return;
      }

      if (numericConfig.maxTicketCapacity > numericConfig.totalTickets) {
        const message = 'Maximum tickets cannot exceed Total capacity';
        console.error('Validation error:', message, numericConfig);
        showNotification(message, 'error');
        return;
      }

      // Save to database first
      await saveConfigurationToDb(numericConfig);
      
      // Then update the system configuration
      await updateConfiguration(numericConfig);
      setConfig(numericConfig);
      showNotification('Configuration saved and updated successfully', 'success');

      // Fetch updated status immediately after configuration update
      const updatedStatus = await getSystemStatus();
      console.log('Updated system status after configuration:', updatedStatus);
      setSystemStatus(updatedStatus);
    } catch (error) {
      console.error('Configuration update failed:', error);
      showNotification(error.message, 'error');
    }
  };

  // Load the latest configuration when the app starts
  useEffect(() => {
    const loadLatestConfig = async () => {
      try {
        const latestConfig = await getLatestConfiguration();
        if (latestConfig) {
          setConfig(latestConfig);
          showNotification('Loaded latest configuration from database', 'success');
        }
      } catch (error) {
        console.error('Failed to load latest configuration:', error);
        showNotification('Failed to load latest configuration', 'error');
      }
    };

    loadLatestConfig();
  }, []);

  // Handle system start
  const handleStart = async () => {
    console.log('Start button clicked');
    try {
      console.log('Entering handleStart function');
      // Validate configuration before starting
      console.log('Validating configuration:', config);
      if (!config.totalTickets || !config.ticketReleaseRate || !config.customerRetrievalRate || !config.maxTicketCapacity) {
        console.log('Configuration is invalid');
        const message = 'Please fill in all configuration fields';
        console.error(message);
        showNotification(message, 'error');
        return;
      }

      const numericConfig = {
        totalTickets: Number(config.totalTickets),
        ticketReleaseRate: Number(config.ticketReleaseRate),
        customerRetrievalRate: Number(config.customerRetrievalRate),
        maxTicketCapacity: Number(config.maxTicketCapacity)
      };

      console.log('Converted config to numbers:', numericConfig);

      if (isNaN(numericConfig.totalTickets) || isNaN(numericConfig.ticketReleaseRate) || 
          isNaN(numericConfig.customerRetrievalRate) || isNaN(numericConfig.maxTicketCapacity)) {
        console.log('Configuration values are not valid numbers');
        const message = 'All configuration values must be valid numbers';
        console.error(message);
        showNotification(message, 'error');
        return;
      }

      if (numericConfig.totalTickets <= 0 || numericConfig.maxTicketCapacity <= 0) {
        console.log('Ticket counts are not positive numbers');
        const message = 'Ticket counts must be positive numbers';
        console.error(message);
        showNotification(message, 'error');
        return;
      }

      if (numericConfig.ticketReleaseRate <= 0 || numericConfig.customerRetrievalRate <= 0) {
        console.log('Rates are not positive numbers');
        const message = 'Rates must be positive numbers';
        console.error(message);
        showNotification(message, 'error');
        return;
      }

      if (numericConfig.totalTickets <= numericConfig.maxTicketCapacity) {
        console.log('Total tickets must be greater than maximum capacity');
        const message = 'Total tickets must be greater than maximum capacity';
        console.error(message);
        showNotification(message, 'error');
        return;
      }

      console.log('Starting system with config:', numericConfig);
      await startSystem();
      console.log('System started successfully');
      showNotification('System started successfully', 'success');
    } catch (error) {
      console.error('Error starting system:', error);
      showNotification(error.message, 'error');
    }
  };

  // Handle system stop
  const handleStop = async () => {
    console.log('Stop button clicked');
    try {
      showNotification('Stopping system...', 'info');
      await stopSystem();
      showNotification('System stopped successfully', 'success');
    } catch (error) {
      console.error('Error stopping system:', error);
      const errorMessage = error.code === 'ECONNABORTED' 
        ? 'System stop operation is taking longer than expected. Please check system status.'
        : error.message || 'Failed to stop system';
      showNotification(errorMessage, 'error');
    }
  };

  // Handle system reset
  const handleReset = async () => {
    try {
      await resetSystem();
      // Reset all states to default
      setConfig({
        totalTickets: '',
        ticketReleaseRate: '',
        customerRetrievalRate: '',
        maxTicketCapacity: ''
      });
      setSystemStatus({
        availableTickets: 0,
        totalTickets: 0,
        isRunning: false
      });
      setLogs([]);
      setNotification({
        type: 'success',
        message: 'System reset successful'
      });
    } catch (error) {
      setNotification({
        type: 'error',
        message: `Reset failed: ${error.message}`
      });
    }
  };

  // Render the main application layout
  return React.createElement('div', { className: 'App' },
    // Navigation Bar
    React.createElement('nav', { className: 'navbar' },
      React.createElement('div', { className: 'nav-brand' }, 'Ticket Management System')
    ),

    // Notification display
    notification && React.createElement('div', {
      className: `notification ${notification.type}`,
      style: { marginBottom: '20px', textAlign: 'center' }
    }, notification.message),

    // Main Container
    React.createElement('div', { className: 'main-container' },
      // Configuration Form
      React.createElement('section', { className: 'config-section' },
        React.createElement(ConfigurationForm, {
          onSaveConfig: handleConfigSubmit
        })
      ),

      // System Control
      React.createElement('section', { className: 'control-section' },
        React.createElement(ControlPanel, {
          isRunning: systemStatus.isRunning,
          onStart: handleStart,
          onStop: handleStop,
          onReset: handleReset
        }),
        React.createElement('div', { className: 'ticket-stats-section' },
          React.createElement('h2', null, 'Ticket Pool Status'),
          React.createElement('div', { className: 'ticket-pool-status' },
            React.createElement('p', null, `Current Tickets: ${systemStatus.availableTickets || 0}`),
            React.createElement('p', null, `Maximum Capacity: ${systemStatus.totalTickets || 0}`)
          )
        )
      )
    ),

    // Log Display
    React.createElement('section', { className: 'logs-section' },
      React.createElement(LogDisplay, {
        logs: logs,
        systemStatus: systemStatus
      })
    ),

    // Log Viewer
    React.createElement(LogViewer, {
      logs: logs,
      systemStatus: systemStatus
    })
  );
}

// Export the App component
export default App;
