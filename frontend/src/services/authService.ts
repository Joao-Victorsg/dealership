import axios from 'axios';
import { KEYCLOAK_CONFIG, KEYCLOAK_ENDPOINTS } from '../config/keycloak';
import {
  AuthError,
  DecodedToken,
  KeycloakTokenResponse,
  LoginCredentials,
  RegisterUserData,
  UserInfo,
} from '../types/auth';
import { tokenStorage } from '../utils/tokenStorage';

/**
 * Authentication Service
 * Handles direct communication with Keycloak OpenID Connect endpoints
 */

// Create a separate axios instance for auth (no interceptors)
const authApi = axios.create({
  headers: {
    'Content-Type': 'application/x-www-form-urlencoded',
  },
});

export const authService = {
  /**
   * Login with username and password
   * Uses the Resource Owner Password Credentials Grant (Direct Access Grant)
   */
  async login(credentials: LoginCredentials): Promise<KeycloakTokenResponse> {
    try {
      const params = new URLSearchParams({
        grant_type: 'password',
        client_id: KEYCLOAK_CONFIG.clientId,
        username: credentials.username,
        password: credentials.password,
      });

      const response = await authApi.post<KeycloakTokenResponse>(
        KEYCLOAK_ENDPOINTS.token,
        params
      );

      // Store tokens
      tokenStorage.setAccessToken(response.data.access_token);
      tokenStorage.setRefreshToken(response.data.refresh_token);

      return response.data;
    } catch (error: any) {
      if (axios.isAxiosError(error) && error.response) {
        const authError = error.response.data as AuthError;
        throw new Error(
          authError.error_description || authError.message || 'Login failed'
        );
      }
      throw new Error('Network error during login');
    }
  },

  /**
   * Refresh access token using refresh token
   */
  async refreshToken(): Promise<KeycloakTokenResponse> {
    const refreshToken = tokenStorage.getRefreshToken();

    if (!refreshToken) {
      throw new Error('No refresh token available');
    }

    try {
      const params = new URLSearchParams({
        grant_type: 'refresh_token',
        client_id: KEYCLOAK_CONFIG.clientId,
        refresh_token: refreshToken,
      });

      const response = await authApi.post<KeycloakTokenResponse>(
        KEYCLOAK_ENDPOINTS.token,
        params
      );

      // Update tokens
      tokenStorage.setAccessToken(response.data.access_token);
      tokenStorage.setRefreshToken(response.data.refresh_token);

      return response.data;
    } catch (error: any) {
      // If refresh fails, clear all tokens
      tokenStorage.clearAllTokens();
      
      if (axios.isAxiosError(error) && error.response) {
        const authError = error.response.data as AuthError;
        throw new Error(
          authError.error_description || authError.message || 'Token refresh failed'
        );
      }
      throw new Error('Network error during token refresh');
    }
  },

  /**
   * Logout and revoke tokens
   */
  async logout(): Promise<void> {
    const refreshToken = tokenStorage.getRefreshToken();

    if (refreshToken) {
      try {
        const params = new URLSearchParams({
          client_id: KEYCLOAK_CONFIG.clientId,
          refresh_token: refreshToken,
        });

        await authApi.post(KEYCLOAK_ENDPOINTS.logout, params);
      } catch (error) {
        console.error('Error during logout:', error);
        // Continue with local logout even if server logout fails
      }
    }

    // Clear local tokens
    tokenStorage.clearAllTokens();
  },

  /**
   * Register a new user
   * Note: This may need backend support depending on Keycloak configuration
   */
  async register(userData: RegisterUserData): Promise<void> {
    try {
      // This is a placeholder - actual registration might need to go through a backend API
      // that interacts with Keycloak Admin API, as direct user registration may not be enabled
      const response = await authApi.post(KEYCLOAK_ENDPOINTS.register, userData);
      return response.data;
    } catch (error: any) {
      if (axios.isAxiosError(error) && error.response) {
        const authError = error.response.data as AuthError;
        throw new Error(
          authError.error_description || authError.message || 'Registration failed'
        );
      }
      throw new Error('Network error during registration');
    }
  },

  /**
   * Request password reset
   * This will call the API Gateway endpoint (Lambda to be implemented later)
   */
  async requestPasswordReset(email: string): Promise<void> {
    try {
      // This endpoint will be implemented with Lambda later
      await axios.post('/api/auth/password-reset', { email });
    } catch (error: any) {
      if (axios.isAxiosError(error) && error.response) {
        throw new Error(
          error.response.data.message || 'Password reset request failed'
        );
      }
      throw new Error('Network error during password reset request');
    }
  },

  /**
   * Get user info from access token
   */
  getUserInfo(): UserInfo | null {
    const accessToken = tokenStorage.getAccessToken();
    
    if (!accessToken) {
      return null;
    }

    const decoded = tokenStorage.decodeToken(accessToken);
    
    if (!decoded) {
      return null;
    }

    return {
      id: decoded.sub,
      email: decoded.email || '',
      username: decoded.preferred_username || '',
      firstName: decoded.given_name,
      lastName: decoded.family_name,
      roles: decoded.realm_access?.roles || [],
      emailVerified: decoded.email_verified,
    };
  },

  /**
   * Check if user has a specific role
   */
  hasRole(role: string): boolean {
    const userInfo = this.getUserInfo();
    return userInfo?.roles.includes(role) || false;
  },

  /**
   * Check if user has any of the specified roles
   */
  hasAnyRole(roles: string[]): boolean {
    const userInfo = this.getUserInfo();
    if (!userInfo) return false;
    return roles.some(role => userInfo.roles.includes(role));
  },

  /**
   * Decode JWT token without validation
   */
  decodeToken(token: string): DecodedToken | null {
    return tokenStorage.decodeToken(token);
  },

  /**
   * Check if token is expired
   */
  isTokenExpired(token: string): boolean {
    return tokenStorage.isTokenExpired(token);
  },
};
