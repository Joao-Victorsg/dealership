import {
    DirectionsCar as CarIcon,
    Home as HomeIcon,
    Menu as MenuIcon,
    Login as LoginIcon,
    PersonAdd as SignUpIcon,
} from '@mui/icons-material';
import {
    AppBar,
    Avatar,
    Box,
    Button,
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

interface PublicLayoutProps {
  children: React.ReactNode;
}

const PublicLayout: React.FC<PublicLayoutProps> = ({ children }) => {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const [mobileOpen, setMobileOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    { text: 'Home', path: '/', icon: <HomeIcon /> },
    { text: 'Browse Cars', path: '/', icon: <CarIcon /> },
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
    <Box sx={{ width: 280, background: 'linear-gradient(135deg, #1e293b 0%, #334155 100%)' }}>
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
          AutoDealer
        </Typography>
        <Typography variant="body2" sx={{ color: 'rgba(255, 255, 255, 0.7)', mt: 1 }}>
          Find Your Perfect Car
        </Typography>
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
          background: 'rgba(255, 255, 255, 0.95)',
          backdropFilter: 'blur(20px)',
          borderBottom: '1px solid rgba(0, 0, 0, 0.1)',
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
                background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                cursor: 'pointer',
              }}
              onClick={() => navigate('/')}
            >
              <CarIcon />
            </Avatar>
            <Typography 
              variant="h6" 
              component="div" 
              sx={{ 
                fontWeight: 700,
                background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                backgroundClip: 'text',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
                cursor: 'pointer',
              }}
              onClick={() => navigate('/')}
            >
              AutoDealer
            </Typography>
          </Box>

          {/* Navigation Buttons - Desktop */}
          {!isMobile && (
            <Box sx={{ display: 'flex', gap: 1, mr: 2 }}>
              {menuItems.map((item) => (
                <Button
                  key={item.text}
                  variant={location.pathname === item.path ? 'contained' : 'text'}
                  onClick={() => handleNavigation(item.path)}
                  startIcon={item.icon}
                  sx={{
                    borderRadius: 2,
                    px: 2,
                    py: 1,
                    textTransform: 'none',
                    fontWeight: 600,
                    background: location.pathname === item.path 
                      ? 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)'
                      : 'transparent',
                    color: location.pathname === item.path ? 'white' : 'text.primary',
                    '&:hover': {
                      background: location.pathname === item.path 
                        ? 'linear-gradient(135deg, #1d4ed8 0%, #3b82f6 100%)'
                        : 'rgba(0, 0, 0, 0.04)',
                    },
                  }}
                >
                  {item.text}
                </Button>
              ))}
            </Box>
          )}

          {/* Auth Buttons */}
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Button
              variant="outlined"
              onClick={() => navigate('/login')}
              startIcon={!isMobile && <LoginIcon />}
              sx={{
                borderRadius: 2,
                px: { xs: 1.5, sm: 2.5 },
                py: 1,
                textTransform: 'none',
                fontWeight: 600,
                borderColor: '#3b82f6',
                color: '#3b82f6',
                '&:hover': {
                  borderColor: '#1d4ed8',
                  backgroundColor: 'rgba(59, 130, 246, 0.04)',
                },
              }}
            >
              {isMobile ? <LoginIcon /> : 'Sign In'}
            </Button>
            <Button
              variant="contained"
              onClick={() => navigate('/register')}
              startIcon={!isMobile && <SignUpIcon />}
              sx={{
                borderRadius: 2,
                px: { xs: 1.5, sm: 2.5 },
                py: 1,
                textTransform: 'none',
                fontWeight: 600,
                background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                '&:hover': {
                  background: 'linear-gradient(135deg, #1d4ed8 0%, #3b82f6 100%)',
                },
              }}
            >
              {isMobile ? <SignUpIcon /> : 'Sign Up'}
            </Button>
          </Box>
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
          borderTop: '1px solid rgba(0, 0, 0, 0.1)',
        }}
      >
        <Container maxWidth="lg">
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap' }}>
            <Typography variant="body2" color="text.secondary">
              © 2024 AutoDealer. Find your perfect car with us.
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, mt: { xs: 2, sm: 0 } }}>
              <Button size="small" color="inherit">Privacy Policy</Button>
              <Button size="small" color="inherit">Terms of Service</Button>
              <Button size="small" color="inherit">Contact Us</Button>
            </Box>
          </Box>
        </Container>
      </Box>
    </Box>
  );
};

export default PublicLayout;
