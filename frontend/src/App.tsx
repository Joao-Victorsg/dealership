import { CssBaseline } from '@mui/material';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import { Navigate, Route, BrowserRouter as Router, Routes } from 'react-router-dom';

// Auth Components
import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import PasswordReset from './pages/auth/PasswordReset';

// Admin Components
import Layout from './components/Layout';
import CarDetail from './pages/CarDetail';
import CarForm from './pages/CarForm';
import CarList from './pages/CarList';
import Home from './pages/Home';

// Client Components
import ClientLayout from './components/ClientLayout';
import ClientCarDetail from './pages/client/ClientCarDetail';
import ClientCarList from './pages/client/ClientCarList';
import ClientHome from './pages/client/ClientHome';
import PurchaseFlow from './pages/client/PurchaseFlow';

// Public Components
import PublicLayout from './components/PublicLayout';
import PublicCarList from './pages/PublicCarList';

// Create a theme instance
const theme = createTheme({
  palette: {
    primary: {
      main: '#3b82f6',
      light: '#60a5fa',
      dark: '#1d4ed8',
    },
    secondary: {
      main: '#8b5cf6',
      light: '#a78bfa',
      dark: '#7c3aed',
    },
    background: {
      default: '#f8fafc',
      paper: 'rgba(255, 255, 255, 0.8)',
    },
  },
  typography: {
    fontFamily: '"Inter", "Roboto", "Helvetica", "Arial", sans-serif',
    h1: {
      fontWeight: 700,
    },
    h2: {
      fontWeight: 600,
    },
    h3: {
      fontWeight: 600,
    },
    h4: {
      fontWeight: 600,
    },
    h5: {
      fontWeight: 600,
    },
    h6: {
      fontWeight: 600,
    },
  },
  shape: {
    borderRadius: 12,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 600,
          borderRadius: 8,
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06)',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
          },
        },
      },
    },
  },
});

// Create a client
const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

const App: React.FC = () => {
  return (
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <Router>
            <Routes>
              {/* Public Auth Routes */}
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/password-reset" element={<PasswordReset />} />

              {/* Public Home Route - Car List without authentication */}
              <Route 
                path="/" 
                element={
                  <PublicLayout><PublicCarList /></PublicLayout>
                } 
              />

              {/* Protected Client Routes */}
              <Route 
                path="/client/home" 
                element={
                  <ProtectedRoute>
                    <ClientLayout><ClientHome /></ClientLayout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/client/cars" 
                element={
                  <ProtectedRoute>
                    <ClientLayout><ClientCarList /></ClientLayout>
                  </ProtectedRoute>
                } 
              />
              <Route path="/cars/new" element={<Navigate to="/admin/cars/new" replace />} />
              <Route 
                path="/cars/:vin" 
                element={
                  <ProtectedRoute>
                    <ClientLayout><ClientCarDetail /></ClientLayout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/purchase/:vin" 
                element={
                  <ProtectedRoute>
                    <ClientLayout><PurchaseFlow /></ClientLayout>
                  </ProtectedRoute>
                } 
              />
              
              {/* Protected Admin Routes (requires admin or staff role) */}
              <Route 
                path="/admin" 
                element={
                  <ProtectedRoute roles={['admin', 'staff']}>
                    <Layout><Home /></Layout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/admin/cars" 
                element={
                  <ProtectedRoute roles={['admin', 'staff']}>
                    <Layout><CarList /></Layout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/admin/cars/new" 
                element={
                  <ProtectedRoute roles={['admin', 'staff']}>
                    <Layout><CarForm /></Layout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/admin/cars/:vin" 
                element={
                  <ProtectedRoute roles={['admin', 'staff']}>
                    <Layout><CarDetail /></Layout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/admin/cars/:vin/edit" 
                element={
                  <ProtectedRoute roles={['admin', 'staff']}>
                    <Layout><CarForm /></Layout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/admin/clients" 
                element={
                  <ProtectedRoute roles={['admin', 'staff']}>
                    <Layout><div>Clients Page (Coming Soon)</div></Layout>
                  </ProtectedRoute>
                } 
              />
              <Route 
                path="/admin/sales" 
                element={
                  <ProtectedRoute roles={['admin', 'staff']}>
                    <Layout><div>Sales Page (Coming Soon)</div></Layout>
                  </ProtectedRoute>
                } 
              />
            </Routes>
          </Router>
        </AuthProvider>
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App; 