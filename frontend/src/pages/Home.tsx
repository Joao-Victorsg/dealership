import {
    DirectionsCar as CarIcon,
    Person as PersonIcon,
    AttachMoney as SalesIcon,
    Search as SearchIcon,
    Security as SecurityIcon,
    Speed as SpeedIcon,
    Support as SupportIcon,
    TrendingUp as TrendingUpIcon,
} from '@mui/icons-material';
import {
    Avatar,
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Chip,
    Container,
    LinearProgress,
    Paper,
    Typography,
} from '@mui/material';
import React from 'react';
import { useNavigate } from 'react-router-dom';

const Home: React.FC = () => {
  const navigate = useNavigate();

  const stats = [
    { label: 'Total Cars', value: '1,234', icon: <CarIcon />, color: '#3b82f6', progress: 75 },
    { label: 'Active Clients', value: '856', icon: <PersonIcon />, color: '#ef4444', progress: 60 },
    { label: 'Monthly Sales', value: '$2.4M', icon: <SalesIcon />, color: '#10b981', progress: 85 },
    { label: 'Search Queries', value: '3,421', icon: <SearchIcon />, color: '#f59e0b', progress: 45 },
  ];

  const features = [
    {
      title: 'Smart Inventory',
      description: 'AI-powered car recommendations and intelligent stock management with real-time analytics.',
      icon: <CarIcon sx={{ fontSize: 40 }} />,
      action: 'Browse Cars',
      path: '/cars',
      color: '#3b82f6',
      gradient: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
    },
    {
      title: 'Client Hub',
      description: 'Comprehensive customer management with personalized experiences and automated follow-ups.',
      icon: <PersonIcon sx={{ fontSize: 40 }} />,
      action: 'Manage Clients',
      path: '/clients',
      color: '#ef4444',
      gradient: 'linear-gradient(135deg, #ef4444 0%, #dc2626 100%)',
    },
    {
      title: 'Sales Analytics',
      description: 'Advanced reporting and insights to optimize your sales strategy and boost revenue.',
      icon: <SalesIcon sx={{ fontSize: 40 }} />,
      action: 'View Sales',
      path: '/sales',
      color: '#10b981',
      gradient: 'linear-gradient(135deg, #10b981 0%, #059669 100%)',
    },
    {
      title: 'Advanced Search',
      description: 'Powerful filtering and search capabilities to find the perfect vehicle instantly.',
      icon: <SearchIcon sx={{ fontSize: 40 }} />,
      action: 'Search Cars',
      path: '/cars',
      color: '#f59e0b',
      gradient: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)',
    },
  ];

  const highlights = [
    {
      title: 'Performance Boost',
      description: '30% faster search results with our optimized engine',
      icon: <SpeedIcon />,
      color: '#8b5cf6',
    },
    {
      title: 'Secure Platform',
      description: 'Enterprise-grade security with end-to-end encryption',
      icon: <SecurityIcon />,
      color: '#06b6d4',
    },
    {
      title: '24/7 Support',
      description: 'Round-the-clock customer support and technical assistance',
      icon: <SupportIcon />,
      color: '#84cc16',
    },
  ];

  return (
    <Container maxWidth="xl">
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 6 }}>
        <Chip
          label="New Features Available"
          color="primary"
          icon={<TrendingUpIcon />}
          sx={{ mb: 2 }}
        />
        <Typography 
          variant="h2" 
          component="h1" 
          gutterBottom 
          sx={{ 
            fontWeight: 700,
            background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
            backgroundClip: 'text',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
            mb: 2,
          }}
        >
          Welcome to Dealership Pro
        </Typography>
        <Typography 
          variant="h5" 
          color="text.secondary" 
          paragraph
          sx={{ maxWidth: 600, mx: 'auto', mb: 4 }}
        >
          Your comprehensive solution for modern dealership management with AI-powered insights and seamless operations
        </Typography>
      </Box>

      {/* Stats Cards */}
      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3, mb: 6 }}>
        {stats.map((stat) => (
          <Card
            key={stat.label}
            sx={{
              flex: '1 1 250px',
              minWidth: 250,
              background: 'rgba(255, 255, 255, 0.8)',
              backdropFilter: 'blur(20px)',
              border: '1px solid rgba(255, 255, 255, 0.2)',
              borderRadius: 3,
              transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
              '&:hover': {
                transform: 'translateY(-4px)',
                boxShadow: '0 20px 40px rgba(0, 0, 0, 0.1)',
              },
            }}
          >
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar
                  sx={{
                    background: stat.color,
                    mr: 2,
                    width: 48,
                    height: 48,
                  }}
                >
                  {stat.icon}
                </Avatar>
                <Box sx={{ flexGrow: 1 }}>
                  <Typography variant="h4" component="div" sx={{ fontWeight: 700 }}>
                    {stat.value}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {stat.label}
                  </Typography>
                </Box>
              </Box>
              <LinearProgress
                variant="determinate"
                value={stat.progress}
                sx={{
                  height: 6,
                  borderRadius: 3,
                  backgroundColor: 'rgba(0, 0, 0, 0.1)',
                  '& .MuiLinearProgress-bar': {
                    background: stat.color,
                    borderRadius: 3,
                  },
                }}
              />
            </CardContent>
          </Card>
        ))}
      </Box>

      {/* Features Grid */}
      <Typography variant="h4" component="h2" sx={{ mb: 4, fontWeight: 600 }}>
        Core Features
      </Typography>
      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 4, mb: 8 }}>
        {features.map((feature) => (
          <Card
            key={feature.title}
            sx={{
              flex: '1 1 300px',
              minWidth: 300,
              height: '100%',
              display: 'flex',
              flexDirection: 'column',
              background: 'rgba(255, 255, 255, 0.8)',
              backdropFilter: 'blur(20px)',
              border: '1px solid rgba(255, 255, 255, 0.2)',
              borderRadius: 3,
              transition: 'all 0.3s ease-in-out',
              '&:hover': {
                transform: 'translateY(-8px)',
                boxShadow: '0 25px 50px rgba(0, 0, 0, 0.15)',
              },
            }}
          >
            <CardContent sx={{ flexGrow: 1, textAlign: 'center', p: 4 }}>
              <Avatar
                sx={{
                  width: 80,
                  height: 80,
                  mx: 'auto',
                  mb: 3,
                  background: feature.gradient,
                }}
              >
                {feature.icon}
              </Avatar>
              <Typography gutterBottom variant="h5" component="h3" sx={{ fontWeight: 600, mb: 2 }}>
                {feature.title}
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
                {feature.description}
              </Typography>
            </CardContent>
            <CardActions sx={{ justifyContent: 'center', pb: 3 }}>
              <Button
                variant="contained"
                onClick={() => navigate(feature.path)}
                sx={{
                  background: feature.gradient,
                  borderRadius: 2,
                  px: 4,
                  py: 1.5,
                  textTransform: 'none',
                  fontWeight: 600,
                  '&:hover': {
                    background: feature.gradient,
                    transform: 'scale(1.05)',
                  },
                }}
              >
                {feature.action}
              </Button>
            </CardActions>
          </Card>
        ))}
      </Box>

      {/* Highlights Section */}
      <Paper
        sx={{
          p: 4,
          background: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
          borderRadius: 3,
          color: 'white',
          mb: 6,
        }}
      >
        <Typography variant="h4" component="h2" sx={{ mb: 4, fontWeight: 600, textAlign: 'center' }}>
          Why Choose Dealership Pro?
        </Typography>
        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
          {highlights.map((highlight) => (
            <Box key={highlight.title} sx={{ flex: '1 1 300px', textAlign: 'center' }}>
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
                {highlight.icon}
              </Avatar>
              <Typography variant="h6" component="h3" sx={{ mb: 1, fontWeight: 600 }}>
                {highlight.title}
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.9 }}>
                {highlight.description}
              </Typography>
            </Box>
          ))}
        </Box>
      </Paper>

      {/* CTA Section */}
      <Box sx={{ textAlign: 'center', py: 6 }}>
        <Typography variant="h4" component="h2" sx={{ mb: 2, fontWeight: 600 }}>
          Ready to Get Started?
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 4, maxWidth: 500, mx: 'auto' }}>
          Join thousands of dealerships that trust our platform to manage their operations efficiently.
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/cars')}
            startIcon={<CarIcon />}
            sx={{
              background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
              borderRadius: 2,
              px: 4,
              py: 1.5,
              textTransform: 'none',
              fontWeight: 600,
            }}
          >
            Explore Cars
          </Button>
          <Button
            variant="outlined"
            size="large"
            onClick={() => navigate('/clients')}
            startIcon={<PersonIcon />}
            sx={{
              borderRadius: 2,
              px: 4,
              py: 1.5,
              textTransform: 'none',
              fontWeight: 600,
              borderColor: 'primary.main',
              color: 'primary.main',
            }}
          >
            Manage Clients
          </Button>
        </Box>
      </Box>
    </Container>
  );
};

export default Home; 