import axios, { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios';
import { authService } from './authService';
import { tokenStorage } from '../utils/tokenStorage';

/**
 * Centralized API Client with authentication interceptors
 * 
 * Features:
 * - Automatically adds Authorization header to requests
 * - Handles 401 errors by refreshing token and retrying request
 * - Centralized error handling
 */

// Flag to prevent multiple simultaneous refresh attempts
let isRefreshing = false;
let failedQueue: Array<{
  resolve: (value?: any) => void;
  reject: (error?: any) => void;
}> = [];

const processQueue = (error: any = null) => {
  failedQueue.forEach(promise => {
    if (error) {
      promise.reject(error);
    } else {
      promise.resolve();
    }
  });

  failedQueue = [];
};

/**
 * Create API client instance
 */
const createApiClient = (baseURL?: string): AxiosInstance => {
  const client = axios.create({
    baseURL,
    headers: {
      'Content-Type': 'application/json',
    },
  });

  // Request interceptor - Add auth token
  client.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
      const token = tokenStorage.getAccessToken();
      
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }

      return config;
    },
    (error: AxiosError) => {
      return Promise.reject(error);
    }
  );

  // Response interceptor - Handle 401 and token refresh
  client.interceptors.response.use(
    (response: AxiosResponse) => {
      return response;
    },
    async (error: AxiosError) => {
      const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

      // If error is 401 and we haven't retried yet
      if (error.response?.status === 401 && !originalRequest._retry) {
        if (isRefreshing) {
          // If already refreshing, queue this request
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject });
          })
            .then(() => {
              const token = tokenStorage.getAccessToken();
              if (token) {
                originalRequest.headers.Authorization = `Bearer ${token}`;
              }
              return client(originalRequest);
            })
            .catch(err => {
              return Promise.reject(err);
            });
        }

        originalRequest._retry = true;
        isRefreshing = true;

        try {
          // Try to refresh the token
          await authService.refreshToken();
          
          // Process queued requests
          processQueue(null);
          
          // Retry original request with new token
          const token = tokenStorage.getAccessToken();
          if (token) {
            originalRequest.headers.Authorization = `Bearer ${token}`;
          }
          
          return client(originalRequest);
        } catch (refreshError) {
          // Refresh failed - clear tokens and redirect to login
          processQueue(refreshError);
          tokenStorage.clearAllTokens();
          
          // Redirect to login page
          if (window.location.pathname !== '/login') {
            window.location.href = '/login?redirect=' + encodeURIComponent(window.location.pathname);
          }
          
          return Promise.reject(refreshError);
        } finally {
          isRefreshing = false;
        }
      }

      return Promise.reject(error);
    }
  );

  return client;
};

// Create default API clients for each service
export const carApiClient = createApiClient(
  process.env.REACT_APP_CAR_API_URL || 'http://localhost:8087/v1/dealership'
);

export const salesApiClient = createApiClient(
  process.env.REACT_APP_SALES_API_URL || 'http://localhost:8086/v1/dealership'
);

export const clientApiClient = createApiClient(
  process.env.REACT_APP_CLIENT_API_URL || 'http://localhost:8085/v1/dealership'
);

// Export factory function for custom API clients
export { createApiClient };
