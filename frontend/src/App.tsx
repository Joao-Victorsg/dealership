import { CssBaseline } from '@mui/material';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import React from 'react';
import { Navigate, Route, BrowserRouter as Router, Routes } from 'react-router-dom';

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
        <Router>
          <Routes>
            {/* Client Routes (Default) */}
            <Route path="/" element={<ClientLayout><ClientHome /></ClientLayout>} />
            <Route path="/cars" element={<ClientLayout><ClientCarList /></ClientLayout>} />
            <Route path="/cars/new" element={<Navigate to="/admin/cars/new" replace />} />
            <Route path="/cars/:vin" element={<ClientLayout><ClientCarDetail /></ClientLayout>} />
            <Route path="/purchase/:vin" element={<ClientLayout><PurchaseFlow /></ClientLayout>} />
            
            {/* Admin Routes */}
            <Route path="/admin" element={<Layout><Home /></Layout>} />
            <Route path="/admin/cars" element={<Layout><CarList /></Layout>} />
            <Route path="/admin/cars/new" element={<Layout><CarForm /></Layout>} />
            <Route path="/admin/cars/:vin" element={<Layout><CarDetail /></Layout>} />
            <Route path="/admin/cars/:vin/edit" element={<Layout><CarForm /></Layout>} />
            <Route path="/admin/clients" element={<Layout><div>Clients Page (Coming Soon)</div></Layout>} />
            <Route path="/admin/sales" element={<Layout><div>Sales Page (Coming Soon)</div></Layout>} />
          </Routes>
        </Router>
      </ThemeProvider>
    </QueryClientProvider>
  );
};

export default App; 