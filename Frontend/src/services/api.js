import axios from 'axios';

// Create axios instance with default config
const api = axios.create({
  baseURL: 'http://localhost:8080/api/tickets',
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 60000, // Increase timeout to 60 seconds for all requests
  retry: 2,       // Add retry configuration
  retryDelay: 1000
});

// Add response interceptor for error handling and retry logic
api.interceptors.response.use(
  response => {
    // Process log entries
    if (response.config.url.includes('/logs')) {
      response.data = response.data.filter(log => {
        // Only show thread-related operations
        return (
          log.type === 'ticket_sold' ||
          log.type === 'ticket_added' ||
          (log.type === 'system_status' && 
           (log.message.includes('started') || 
            log.message.includes('stopped') || 
            log.message.includes('reset')))
        );
      }).map(log => ({
        ...log,
        ticketAmount: log.ticketAmount || 1,
        availableTickets: log.currentTickets || log.availableTickets || 0
      }));
    }
    return response;
  },
  async error => {
    const { config, response } = error;
    
    if (!response) {
      console.error('Network Error:', error);
      throw new Error('Network error: Please check your connection');
    }

    if (config && config.retry > 0) {
      config.retry -= 1;
      await new Promise(resolve => setTimeout(resolve, config.retryDelay));
      return api(config);
    }

    throw new Error(response?.data?.message || error.message || 'An error occurred');
  }
);

// System control endpoints
export const startSystem = async () => {
  try {
    console.log('Sending start system request...');
    const response = await api.post('/system/start');
    console.log('Start system response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Start system error:', error);
    throw error;
  }
};

export const stopSystem = async () => {
  try {
    console.log('Sending stop system request...');
    const response = await api.post('/system/stop', null, {
      timeout: 90000, // Longer timeout specifically for stop operation
    });
    console.log('Stop system response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Stop system error:', error);
    if (error.code === 'ECONNABORTED') {
      throw new Error('System stop operation timed out. The system might still be processing the stop request. Please check system status.');
    }
    throw error;
  }
};

export const resetSystem = async () => {
  try {
    console.log('Resetting system...');
    // Stop the system first if it's running
    await stopSystem();
    // Reset all system configurations and states
    const response = await api.post('/system/reset');
    console.log('Reset system response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Reset system error:', error);
    throw error;
  }
};

// Configuration endpoints
export const getSystemStatus = async () => {
  try {
    console.log('Fetching system status...');
    const response = await api.get('/system/status');
    console.log('Current system status:', response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching system status:', error);
    throw error;
  }
};

export const updateConfiguration = async (config) => {
  try {
    console.log('Sending configuration update request:', config);
    const response = await api.post('/config', {
      totalTickets: parseInt(config.totalTickets),
      ticketReleaseRate: parseInt(config.ticketReleaseRate),
      customerRetrievalRate: parseInt(config.customerRetrievalRate),
      maxTicketCapacity: parseInt(config.maxTicketCapacity)
    });
    console.log('Configuration update response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Configuration update error:', error);
    throw error;
  }
};

// Save configuration to database
export const saveConfigurationToDb = async (config) => {
  try {
    const response = await axios.post('http://localhost:8080/api/configuration/save', config);
    return response.data;
  } catch (error) {
    throw new Error(`Failed to save configuration: ${error.message}`);
  }
};

// Get latest configuration from database
export const getLatestConfiguration = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/configuration/latest');
    return response.data;
  } catch (error) {
    throw new Error(`Failed to get latest configuration: ${error.message}`);
  }
};

// Statistics endpoints
export const getDetailedStatistics = async () => {
  try {
    console.log('Sending get detailed statistics request...');
    const response = await api.get('/statistics');
    console.log('Get detailed statistics response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Get detailed statistics error:', error);
    throw error;
  }
};

// Vendor and Customer control endpoints
export const startVendor = async () => {
  try {
    console.log('Sending start vendor request...');
    const response = await api.post('/vendors/start');
    console.log('Start vendor response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Start vendor error:', error);
    throw error;
  }
};

export const startCustomer = async () => {
  try {
    console.log('Sending start customer request...');
    const response = await api.post('/customers/start');
    console.log('Start customer response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Start customer error:', error);
    throw error;
  }
};

export const stopVendors = async () => {
  try {
    console.log('Sending stop vendors request...');
    const response = await api.post('/vendors/stop');
    console.log('Stop vendors response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Stop vendors error:', error);
    throw error;
  }
};

export const stopCustomers = async () => {
  try {
    console.log('Sending stop customers request...');
    const response = await api.post('/customers/stop');
    console.log('Stop customers response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Stop customers error:', error);
    throw error;
  }
};

export const getLogs = async () => {
  try {
    console.log('Fetching system logs...');
    const response = await api.get('/system/logs');
    console.log('System logs:', response.data);
    return response.data;
  } catch (error) {
    console.error('Error fetching logs:', error);
    throw error;
  }
};
