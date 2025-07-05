import {
    DirectionsCar as CarIcon,
    Dashboard as DashboardIcon,
    Menu as MenuIcon,
    Person as PersonIcon,
    AttachMoney as SalesIcon
} from '@mui/icons-material';
import {
    AppBar,
    Avatar,
    Box,
    Button,
    Chip,
    Container,
    Drawer,
    IconButton,
    List,
    ListItemButton,
    ListItemIcon,
    ListItemText,
    Toolbar,
    Typography,
    useMediaQuery,
    useTheme,
} from '@mui/material';
import React, { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [mobileOpen, setMobileOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    { text: 'Dashboard', path: '/admin', icon: <DashboardIcon />, color: '#6366f1' },
    { text: 'Cars', path: '/admin/cars', icon: <CarIcon />, color: '#3b82f6' },
    { text: 'Clients', path: '/admin/clients', icon: <PersonIcon />, color: '#ef4444' },
    { text: 'Sales', path: '/admin/sales', icon: <SalesIcon />, color: '#10b981' },
  ];

  const handleDrawerToggle = () => {
    setMobileOpen(!mobileOpen);
  };

  const handleNavigation = (path: string) => {
    navigate(path);
    if (isMobile) {
      setMobileOpen(false);
    }
  };

  const drawer = (
    <Box sx={{ width: 280, background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' }}>
      <Box sx={{ p: 3, textAlign: 'center' }}>
        <Avatar
          sx={{
            width: 60,
            height: 60,
            mx: 'auto',
            mb: 2,
            background: 'rgba(255, 255, 255, 0.2)',
            backdropFilter: 'blur(10px)',
          }}
        >
          <CarIcon sx={{ fontSize: 30 }} />
        </Avatar>
        <Typography variant="h6" sx={{ color: 'white', fontWeight: 600 }}>
          Dealership Pro
        </Typography>
        <Chip
          label="v2.0"
          size="small"
          sx={{
            mt: 1,
            background: 'rgba(255, 255, 255, 0.2)',
            color: 'white',
            backdropFilter: 'blur(10px)',
          }}
        />
      </Box>
      <List sx={{ px: 2 }}>
        {menuItems.map((item) => (
          <ListItemButton
            key={item.text}
            onClick={() => handleNavigation(item.path)}
            selected={location.pathname === item.path}
            sx={{
              mb: 1,
              borderRadius: 2,
              background: location.pathname === item.path 
                ? 'rgba(255, 255, 255, 0.2)' 
                : 'transparent',
              backdropFilter: 'blur(10px)',
              '&:hover': {
                background: 'rgba(255, 255, 255, 0.1)',
              },
              '&.Mui-selected': {
                background: 'rgba(255, 255, 255, 0.2)',
                '&:hover': {
                  background: 'rgba(255, 255, 255, 0.25)',
                },
              },
            }}
          >
            <ListItemIcon sx={{ color: 'white', minWidth: 40 }}>
              {item.icon}
            </ListItemIcon>
            <ListItemText 
              primary={item.text} 
              sx={{ 
                color: 'white',
                '& .MuiTypography-root': {
                  fontWeight: location.pathname === item.path ? 600 : 400,
                },
              }}
            />
          </ListItemButton>
        ))}
      </List>
    </Box>
  );

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', background: '#f8fafc' }}>
      <AppBar 
        position="static" 
        elevation={0}
        sx={{
          background: 'rgba(255, 255, 255, 0.8)',
          backdropFilter: 'blur(20px)',
          borderBottom: '1px solid rgba(255, 255, 255, 0.2)',
        }}
      >
        <Toolbar>
          {isMobile && (
            <IconButton
              color="primary"
              aria-label="open drawer"
              edge="start"
              onClick={handleDrawerToggle}
              sx={{ mr: 2 }}
            >
              <MenuIcon />
            </IconButton>
          )}
          
          <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1 }}>
            <Avatar
              sx={{
                width: 40,
                height: 40,
                mr: 2,
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
              }}
            >
              <CarIcon />
            </Avatar>
            <Typography 
              variant="h6" 
              component="div" 
              sx={{ 
                fontWeight: 600,
                background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
                backgroundClip: 'text',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            >
              Dealership Management
            </Typography>
          </Box>

          {!isMobile && (
            <Box sx={{ display: 'flex', gap: 1, alignItems: 'center' }}>
              {menuItems.map((item) => (
                <Button
                  key={item.text}
                  variant={location.pathname === item.path ? 'contained' : 'text'}
                  onClick={() => handleNavigation(item.path)}
                  startIcon={item.icon}
                  sx={{
                    borderRadius: 2,
                    px: 3,
                    py: 1,
                    textTransform: 'none',
                    fontWeight: 600,
                    background: location.pathname === item.path 
                      ? `linear-gradient(135deg, ${item.color} 0%, ${item.color}dd 100%)`
                      : 'transparent',
                    color: location.pathname === item.path ? 'white' : 'text.primary',
                    '&:hover': {
                      background: location.pathname === item.path 
                        ? `linear-gradient(135deg, ${item.color}dd 0%, ${item.color} 100%)`
                        : 'rgba(0, 0, 0, 0.04)',
                    },
                  }}
                >
                  {item.text}
                </Button>
              ))}
              
              <Button
                variant="outlined"
                onClick={() => navigate('/')}
                sx={{
                  borderRadius: 2,
                  px: 3,
                  py: 1,
                  textTransform: 'none',
                  fontWeight: 600,
                  borderColor: '#3b82f6',
                  color: '#3b82f6',
                  ml: 2,
                  '&:hover': {
                    borderColor: '#1d4ed8',
                    backgroundColor: 'rgba(59, 130, 246, 0.04)',
                  },
                }}
              >
                View Client Site
              </Button>
            </Box>
          )}
        </Toolbar>
      </AppBar>

      {isMobile && (
        <Drawer
          variant="temporary"
          open={mobileOpen}
          onClose={handleDrawerToggle}
          ModalProps={{
            keepMounted: true,
          }}
          sx={{
            display: { xs: 'block', md: 'none' },
            '& .MuiDrawer-paper': { 
              boxSizing: 'border-box', 
              width: 280,
              border: 'none',
            },
          }}
        >
          {drawer}
        </Drawer>
      )}

      <Container 
        component="main" 
        maxWidth="xl"
        sx={{ 
          flexGrow: 1, 
          py: 4,
          px: { xs: 2, sm: 3, md: 4 },
        }}
      >
        {children}
      </Container>

      <Box
        component="footer"
        sx={{
          py: 4,
          px: 2,
          mt: 'auto',
          background: 'rgba(255, 255, 255, 0.8)',
          backdropFilter: 'blur(20px)',
          borderTop: '1px solid rgba(255, 255, 255, 0.2)',
        }}
      >
        <Container maxWidth="lg">
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap' }}>
            <Typography variant="body2" color="text.secondary">
              © 2024 Dealership Management System. All rights reserved.
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, mt: { xs: 2, sm: 0 } }}>
              <Chip label="v2.0" size="small" variant="outlined" />
              <Chip label="Production" size="small" color="success" />
            </Box>
          </Box>
        </Container>
      </Box>
    </Box>
  );
};

export default Layout; 