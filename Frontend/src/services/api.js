import axios from 'axios';

// Create axios instance with default config
const api = axios.create({
  baseURL: 'http://localhost:8080/api/tickets', // Explicitly set the full URL
  headers: {
    'Content-Type': 'application/json',
  },
  // Add timeout to prevent infinite waiting
  timeout: 5000
});

// Add response interceptor for error handling
api.interceptors.response.use(
  response => response,
  error => {
    console.error('API Error:', error);
    const errorMessage = error.response?.data?.message || error.message || 'An error occurred';
    throw new Error(errorMessage);
  }
);

export const getSystemStatus = async () => {
  try {
    const response = await api.get('/status');
    console.log('Status response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Status error:', error);
    throw error;
  }
};

export const getDetailedStatistics = async () => {
  try {
    const response = await api.get('/statistics');
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const updateConfiguration = async (config) => {
  try {
    const response = await api.post('/config', config);
    console.log('Config update response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Config update error:', error);
    throw error;
  }
};

export const startSystem = async () => {
  try {
    const response = await api.post('/start');
    console.log('Start system response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Start system error:', error);
    throw error;
  }
};

export const stopSystem = async () => {
  try {
    const response = await api.post('/stop');
    console.log('Stop system response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Stop system error:', error);
    throw error;
  }
};

export const resetSystem = async () => {
  try {
    const response = await api.post('/reset');
    console.log('Reset system response:', response.data);
    return response.data;
  } catch (error) {
    console.error('Reset system error:', error);
    throw error;
  }
};
