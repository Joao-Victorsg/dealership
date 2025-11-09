// Keycloak Configuration Constants
export const KEYCLOAK_CONFIG = {
  url: process.env.REACT_APP_KEYCLOAK_URL || 'http://localhost:8084',
  realm: process.env.REACT_APP_KEYCLOAK_REALM || 'dealership-realm',
  clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || 'dealership-frontend',
};

// Token refresh threshold (refresh when token expires in less than 5 minutes)
export const TOKEN_REFRESH_THRESHOLD = 5 * 60; // 5 minutes in seconds

// LocalStorage keys
export const STORAGE_KEYS = {
  REFRESH_TOKEN: 'dealership_refresh_token',
  USER_INFO: 'dealership_user_info',
} as const;
