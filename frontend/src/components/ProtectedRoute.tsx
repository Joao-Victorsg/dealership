import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { Box, CircularProgress } from '@mui/material';
import { useAuth } from '../contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
  roles?: string[]; // Optional: specific roles required to access this route
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, roles }) => {
  const { isAuthenticated, isLoading, user } = useAuth();
  const location = useLocation();

  // Show loading spinner while checking authentication
  if (isLoading) {
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
        }}
      >
        <CircularProgress />
      </Box>
    );
  }

  // If not authenticated, redirect to login with return URL
  if (!isAuthenticated) {
    return <Navigate to={`/login?redirect=${encodeURIComponent(location.pathname)}`} replace />;
  }

  // If roles are specified, check if user has any of the required roles
  if (roles && roles.length > 0) {
    const hasRequiredRole = user?.roles.some(role => roles.includes(role));
    
    if (!hasRequiredRole) {
      // User doesn't have required role - redirect to home or show unauthorized
      return <Navigate to="/" replace />;
    }
  }

  // User is authenticated (and has required role if specified)
  return <>{children}</>;
};

export default ProtectedRoute;
