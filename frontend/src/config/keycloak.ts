// Keycloak configuration constants
export const KEYCLOAK_CONFIG = {
  url: process.env.REACT_APP_KEYCLOAK_URL || 'http://localhost:8084',
  realm: process.env.REACT_APP_KEYCLOAK_REALM || 'dealership-realm',
  clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID || 'dealership-frontend',
};

// Keycloak endpoints
export const KEYCLOAK_ENDPOINTS = {
  token: `${KEYCLOAK_CONFIG.url}/realms/${KEYCLOAK_CONFIG.realm}/protocol/openid-connect/token`,
  logout: `${KEYCLOAK_CONFIG.url}/realms/${KEYCLOAK_CONFIG.realm}/protocol/openid-connect/logout`,
  userInfo: `${KEYCLOAK_CONFIG.url}/realms/${KEYCLOAK_CONFIG.realm}/protocol/openid-connect/userinfo`,
  register: `${KEYCLOAK_CONFIG.url}/realms/${KEYCLOAK_CONFIG.realm}/protocol/openid-connect/registrations`,
};

// Token storage keys
export const TOKEN_STORAGE_KEYS = {
  REFRESH_TOKEN: 'dealership_refresh_token',
};
