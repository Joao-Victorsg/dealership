import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { authService } from '../services/authService';
import { tokenStorage } from '../utils/tokenStorage';
import { LoginCredentials, RegisterUserData, UserInfo } from '../types/auth';

interface AuthContextType {
  user: UserInfo | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => Promise<void>;
  register: (userData: RegisterUserData) => Promise<void>;
  refreshUserInfo: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<UserInfo | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  /**
   * Refresh user info from token
   */
  const refreshUserInfo = () => {
    const userInfo = authService.getUserInfo();
    setUser(userInfo);
  };

  /**
   * Initialize auth state on mount
   * Try to restore session from refresh token
   */
  useEffect(() => {
    const initAuth = async () => {
      setIsLoading(true);
      
      try {
        const accessToken = tokenStorage.getAccessToken();
        const refreshToken = tokenStorage.getRefreshToken();

        // If we have a valid access token, restore user info
        if (accessToken && !tokenStorage.isTokenExpired(accessToken)) {
          refreshUserInfo();
        } 
        // If access token is expired/missing but we have refresh token, try to refresh
        else if (refreshToken && !tokenStorage.isTokenExpired(refreshToken)) {
          await authService.refreshToken();
          refreshUserInfo();
        }
        // No valid tokens, user is not authenticated
        else {
          tokenStorage.clearAllTokens();
          setUser(null);
        }
      } catch (error) {
        console.error('Failed to restore session:', error);
        tokenStorage.clearAllTokens();
        setUser(null);
      } finally {
        setIsLoading(false);
      }
    };

    initAuth();
  }, []);

  /**
   * Login user
   */
  const login = async (credentials: LoginCredentials) => {
    setIsLoading(true);
    try {
      await authService.login(credentials);
      refreshUserInfo();
    } finally {
      setIsLoading(false);
    }
  };

  /**
   * Logout user
   */
  const logout = async () => {
    setIsLoading(true);
    try {
      await authService.logout();
      setUser(null);
    } finally {
      setIsLoading(false);
    }
  };

  /**
   * Register new user
   */
  const register = async (userData: RegisterUserData) => {
    setIsLoading(true);
    try {
      await authService.register(userData);
      // Note: After registration, user will need to login
    } finally {
      setIsLoading(false);
    }
  };

  const value: AuthContextType = {
    user,
    isAuthenticated: !!user,
    isLoading,
    login,
    logout,
    register,
    refreshUserInfo,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

/**
 * Custom hook to use auth context
 */
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
