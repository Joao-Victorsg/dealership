import { jwtDecode } from 'jwt-decode';
import { STORAGE_KEYS, TOKEN_REFRESH_THRESHOLD } from '../config/keycloak';
import { DecodedToken, UserInfo } from '../types/auth';

/**
 * Token Storage Utility
 * 
 * Strategy:
 * - Access Token: Stored in memory (secure, not persisted)
 * - Refresh Token: Stored in localStorage (allows session persistence)
 * - User Info: Stored in localStorage (for quick access)
 */

class TokenStorage {
  private accessToken: string | null = null;

  /**
   * Store tokens and user info
   */
  setTokens(accessToken: string, refreshToken: string): void {
    this.accessToken = accessToken;
    localStorage.setItem(STORAGE_KEYS.REFRESH_TOKEN, refreshToken);
    
    // Extract and store user info
    const userInfo = this.extractUserInfo(accessToken);
    if (userInfo) {
      localStorage.setItem(STORAGE_KEYS.USER_INFO, JSON.stringify(userInfo));
    }
  }

  /**
   * Get access token from memory
   */
  getAccessToken(): string | null {
    return this.accessToken;
  }

  /**
   * Get refresh token from localStorage
   */
  getRefreshToken(): string | null {
    return localStorage.getItem(STORAGE_KEYS.REFRESH_TOKEN);
  }

  /**
   * Get stored user info
   */
  getUserInfo(): UserInfo | null {
    const userInfoStr = localStorage.getItem(STORAGE_KEYS.USER_INFO);
    if (!userInfoStr) return null;
    
    try {
      return JSON.parse(userInfoStr);
    } catch {
      return null;
    }
  }

  /**
   * Update access token in memory
   */
  setAccessToken(token: string): void {
    this.accessToken = token;
    
    // Update user info
    const userInfo = this.extractUserInfo(token);
    if (userInfo) {
      localStorage.setItem(STORAGE_KEYS.USER_INFO, JSON.stringify(userInfo));
    }
  }

  /**
   * Clear all tokens and user info
   */
  clearTokens(): void {
    this.accessToken = null;
    localStorage.removeItem(STORAGE_KEYS.REFRESH_TOKEN);
    localStorage.removeItem(STORAGE_KEYS.USER_INFO);
  }

  /**
   * Check if access token exists
   */
  hasAccessToken(): boolean {
    return this.accessToken !== null;
  }

  /**
   * Check if refresh token exists
   */
  hasRefreshToken(): boolean {
    return this.getRefreshToken() !== null;
  }

  /**
   * Decode JWT token
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
   * Check if token is expired
   */
  isTokenExpired(token: string): boolean {
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return true;
    
    const currentTime = Math.floor(Date.now() / 1000);
    return decoded.exp < currentTime;
  }

  /**
   * Check if token needs refresh (expires soon)
   */
  shouldRefreshToken(token: string): boolean {
    const decoded = this.decodeToken(token);
    if (!decoded || !decoded.exp) return true;
    
    const currentTime = Math.floor(Date.now() / 1000);
    const timeUntilExpiry = decoded.exp - currentTime;
    
    return timeUntilExpiry < TOKEN_REFRESH_THRESHOLD;
  }

  /**
   * Extract user info from JWT token
   */
  private extractUserInfo(token: string): UserInfo | null {
    const decoded = this.decodeToken(token);
    if (!decoded) return null;

    return {
      id: decoded.sub,
      email: decoded.email || '',
      username: decoded.preferred_username || decoded.email || '',
      firstName: decoded.given_name,
      lastName: decoded.family_name,
      name: decoded.name,
      roles: decoded.realm_access?.roles || [],
    };
  }
}

// Export singleton instance
export const tokenStorage = new TokenStorage();
