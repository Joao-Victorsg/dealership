import { jwtDecode } from 'jwt-decode';
import { TOKEN_STORAGE_KEYS } from '../config/keycloak';
import { DecodedToken } from '../types/auth';

/**
 * Token Storage Utility
 * 
 * Security strategy:
 * - Access Token: Stored in memory (React state) for security against XSS
 * - Refresh Token: Stored in localStorage for session persistence
 * 
 * This allows users to remain logged in across page refreshes while
 * minimizing the exposure of the short-lived access token.
 */

class TokenStorage {
  private accessToken: string | null = null;

  /**
   * Set access token in memory
   */
  setAccessToken(token: string): void {
    this.accessToken = token;
  }

  /**
   * Get access token from memory
   */
  getAccessToken(): string | null {
    return this.accessToken;
  }

  /**
   * Clear access token from memory
   */
  clearAccessToken(): void {
    this.accessToken = null;
  }

  /**
   * Set refresh token in localStorage
   */
  setRefreshToken(token: string): void {
    try {
      localStorage.setItem(TOKEN_STORAGE_KEYS.REFRESH_TOKEN, token);
    } catch (error) {
      console.error('Failed to store refresh token:', error);
    }
  }

  /**
   * Get refresh token from localStorage
   */
  getRefreshToken(): string | null {
    try {
      return localStorage.getItem(TOKEN_STORAGE_KEYS.REFRESH_TOKEN);
    } catch (error) {
      console.error('Failed to retrieve refresh token:', error);
      return null;
    }
  }

  /**
   * Clear refresh token from localStorage
   */
  clearRefreshToken(): void {
    try {
      localStorage.removeItem(TOKEN_STORAGE_KEYS.REFRESH_TOKEN);
    } catch (error) {
      console.error('Failed to clear refresh token:', error);
    }
  }

  /**
   * Clear all tokens (both memory and localStorage)
   */
  clearAllTokens(): void {
    this.clearAccessToken();
    this.clearRefreshToken();
  }

  /**
   * Check if a token is expired
   */
  isTokenExpired(token: string | null): boolean {
    if (!token) return true;

    try {
      const decoded = jwtDecode<DecodedToken>(token);
      const currentTime = Date.now() / 1000;
      // Add 30 second buffer to refresh before actual expiration
      return decoded.exp < currentTime + 30;
    } catch (error) {
      console.error('Failed to decode token:', error);
      return true;
    }
  }

  /**
   * Decode a JWT token
   */
  decodeToken(token: string): DecodedToken | null {
    try {
      return jwtDecode<DecodedToken>(token);
    } catch (error) {
      console.error('Failed to decode token:', error);
      return null;
    }
  }

  /**
   * Check if user is authenticated (has valid tokens)
   */
  isAuthenticated(): boolean {
    const accessToken = this.getAccessToken();
    const refreshToken = this.getRefreshToken();

    // If we have a valid access token, user is authenticated
    if (accessToken && !this.isTokenExpired(accessToken)) {
      return true;
    }

    // If access token is expired/missing but we have a refresh token, we can refresh
    if (refreshToken && !this.isTokenExpired(refreshToken)) {
      return true;
    }

    return false;
  }
}

// Export singleton instance
export const tokenStorage = new TokenStorage();
