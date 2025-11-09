import {
    DirectionsCar as CarIcon,
    Search as SearchIcon,
    Security as SecurityIcon,
    Support as SupportIcon,
    TrendingUp as TrendingIcon,
    Favorite as WishlistIcon
} from '@mui/icons-material';
import {
    Avatar,
    Box,
    Button,
    Card,
    CardContent,
    CardMedia,
    Chip,
    Container,
    Paper,
    Typography
} from '@mui/material';
import React from 'react';
import { useNavigate } from 'react-router-dom';

const ClientHome: React.FC = () => {
  const navigate = useNavigate();

  const featuredCars = [
    {
      id: '1',
      model: 'Tesla Model 3',
      manufacturer: 'Tesla',
      year: '2023',
      price: 45000,
      color: 'White',
      image: 'https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=400&h=300&fit=crop',
    },
    {
      id: '2',
      model: 'BMW X5',
      manufacturer: 'BMW',
      year: '2023',
      price: 65000,
      color: 'Black',
      image: 'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=400&h=300&fit=crop',
    },
    {
      id: '3',
      model: 'Mercedes C-Class',
      manufacturer: 'Mercedes-Benz',
      year: '2023',
      price: 55000,
      color: 'Silver',
      image: 'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=400&h=300&fit=crop',
    },
  ];

  const features = [
    {
      icon: <SearchIcon sx={{ fontSize: 40 }} />,
      title: 'Easy Search',
      description: 'Find your perfect car with our advanced search and filter options.',
      color: '#3b82f6',
    },
    {
      icon: <SecurityIcon sx={{ fontSize: 40 }} />,
      title: 'Secure Purchase',
      description: 'Safe and secure online car purchasing with full documentation.',
      color: '#10b981',
    },
    {
      icon: <SupportIcon sx={{ fontSize: 40 }} />,
      title: '24/7 Support',
      description: 'Get help anytime with our dedicated customer support team.',
      color: '#f59e0b',
    },
  ];

  return (
    <Container maxWidth="xl">
      {/* Hero Section */}
      <Box sx={{ textAlign: 'center', mb: 8 }}>
        <Typography 
          variant="h2" 
          component="h1" 
          sx={{ 
            fontWeight: 700,
            mb: 3,
            background: 'linear-gradient(135deg, #1e293b 0%, #3b82f6 100%)',
            backgroundClip: 'text',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
          }}
        >
          Find Your Perfect Car
        </Typography>
        <Typography 
          variant="h5" 
          color="text.secondary" 
          sx={{ mb: 4, maxWidth: 600, mx: 'auto' }}
        >
          Browse our extensive collection of quality vehicles and drive home in your dream car today
        </Typography>
        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/client/cars')}
            startIcon={<SearchIcon />}
            sx={{
              background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
              borderRadius: 3,
              px: 4,
              py: 1.5,
              textTransform: 'none',
              fontWeight: 600,
            }}
          >
            Browse Cars
          </Button>
          <Button
            variant="outlined"
            size="large"
            onClick={() => navigate('/account')}
            startIcon={<WishlistIcon />}
            sx={{
              borderRadius: 3,
              px: 4,
              py: 1.5,
              textTransform: 'none',
              fontWeight: 600,
              borderColor: '#3b82f6',
              color: '#3b82f6',
            }}
          >
            My Wishlist
          </Button>
        </Box>
      </Box>

      {/* Featured Cars */}
      <Box sx={{ mb: 8 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
          <TrendingIcon sx={{ mr: 2, color: '#3b82f6' }} />
          <Typography variant="h4" component="h2" sx={{ fontWeight: 600 }}>
            Featured Cars
          </Typography>
        </Box>
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(3, 1fr)' }, gap: 4 }}>
          {featuredCars.map((car) => (
            <Box key={car.id}>
              <Card
                sx={{
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
                  cursor: 'pointer',
                  '&:hover': {
                    transform: 'translateY(-8px)',
                    boxShadow: '0 20px 40px rgba(0, 0, 0, 0.15)',
                  },
                }}
                onClick={() => navigate(`/cars/${car.id}`)}
              >
                <CardMedia
                  component="img"
                  height="200"
                  image={car.image}
                  alt={car.model}
                  sx={{ objectFit: 'cover' }}
                />
                <CardContent sx={{ flexGrow: 1, p: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                    <Box>
                      <Typography variant="h6" component="h3" sx={{ fontWeight: 600, mb: 1 }}>
                        {car.model}
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {car.manufacturer} • {car.year}
                      </Typography>
                    </Box>
                    <Chip
                      label={car.color}
                      size="small"
                      sx={{
                        background: 'rgba(59, 130, 246, 0.1)',
                        color: '#3b82f6',
                        fontWeight: 500,
                      }}
                    />
                  </Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Typography variant="h5" sx={{ fontWeight: 700, color: '#10b981' }}>
                      ${car.price.toLocaleString()}
                    </Typography>
                    <Button
                      variant="contained"
                      size="small"
                      onClick={(e) => {
                        e.stopPropagation();
                        navigate(`/cars/${car.id}`);
                      }}
                      sx={{
                        background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                        borderRadius: 2,
                        textTransform: 'none',
                        fontWeight: 600,
                      }}
                    >
                      View Details
                    </Button>
                  </Box>
                </CardContent>
              </Card>
            </Box>
          ))}
        </Box>
      </Box>

      {/* Features Section */}
      <Paper
        sx={{
          p: 6,
          background: 'linear-gradient(135deg, #1e293b 0%, #334155 100%)',
          borderRadius: 4,
          color: 'white',
          mb: 8,
        }}
      >
        <Typography variant="h4" component="h2" sx={{ mb: 6, fontWeight: 600, textAlign: 'center' }}>
          Why Choose AutoDealer?
        </Typography>
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: 'repeat(3, 1fr)' }, gap: 4 }}>
          {features.map((feature, index) => (
            <Box key={index}>
              <Box sx={{ textAlign: 'center' }}>
                <Avatar
                  sx={{
                    width: 80,
                    height: 80,
                    mx: 'auto',
                    mb: 3,
                    background: 'rgba(255, 255, 255, 0.2)',
                    backdropFilter: 'blur(10px)',
                    color: feature.color,
                  }}
                >
                  {feature.icon}
                </Avatar>
                <Typography variant="h6" component="h3" sx={{ mb: 2, fontWeight: 600 }}>
                  {feature.title}
                </Typography>
                <Typography variant="body2" sx={{ opacity: 0.9 }}>
                  {feature.description}
                </Typography>
              </Box>
            </Box>
          ))}
        </Box>
      </Paper>

      {/* CTA Section */}
      <Box sx={{ textAlign: 'center', py: 6 }}>
        <Typography variant="h4" component="h2" sx={{ mb: 2, fontWeight: 600 }}>
          Ready to Find Your Dream Car?
        </Typography>
        <Typography variant="body1" color="text.secondary" sx={{ mb: 4, maxWidth: 500, mx: 'auto' }}>
          Join thousands of satisfied customers who found their perfect vehicle with us.
        </Typography>
        <Button
          variant="contained"
          size="large"
          onClick={() => navigate('/client/cars')}
          startIcon={<CarIcon />}
          sx={{
            background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
            borderRadius: 3,
            px: 6,
            py: 2,
            textTransform: 'none',
            fontWeight: 600,
            fontSize: '1.1rem',
          }}
        >
          Start Browsing Now
        </Button>
      </Box>
    </Container>
  );
};

export default ClientHome; 