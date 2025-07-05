import {
    ArrowBack as BackIcon,
    Build as BuildIcon,
    CalendarToday as CalendarIcon,
    DirectionsCar as CarIcon,
    ShoppingCart as CartIcon,
    Palette as ColorIcon,
    Favorite as WishlistIcon
} from '@mui/icons-material';
import {
    Alert,
    Box,
    Button,
    Card,
    CardMedia,
    Chip,
    CircularProgress,
    Container,
    Divider,
    Paper,
    Typography
} from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { carService } from '../../services/carService';
import { Car } from '../../types/car';

const ClientCarDetail: React.FC = () => {
  const navigate = useNavigate();
  const { vin } = useParams<{ vin: string }>();

  const { data: carResponse, isLoading, error } = useQuery({
    queryKey: ['car', vin],
    queryFn: () => carService.getCarByVin(vin!),
    enabled: !!vin,
  });

  const car = carResponse?.data;

  const handlePurchase = () => {
    if (car) {
      navigate(`/purchase/${car.vin}`, { state: { car } });
    }
  };

  const handleAddToWishlist = () => {
    // TODO: Implement wishlist functionality
    console.log('Add to wishlist:', car);
  };

  const getCarImage = (car: Car) => {
    const images = [
      'https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=600&h=400&fit=crop',
      'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=600&h=400&fit=crop',
      'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=600&h=400&fit=crop',
      'https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?w=600&h=400&fit=crop',
      'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=600&h=400&fit=crop',
    ];
    return images[Math.abs(car.vin.length) % images.length];
  };

  if (isLoading) {
    return (
      <Container maxWidth="lg">
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
          <CircularProgress />
        </Box>
      </Container>
    );
  }

  if (error || !car) {
    return (
      <Container maxWidth="lg">
        <Alert severity="error" sx={{ mt: 4 }}>
          Car not found or error loading car details.
        </Alert>
        <Button
          variant="contained"
          onClick={() => navigate('/cars')}
          sx={{ mt: 2 }}
        >
          Back to Cars
        </Button>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg">
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Button
          variant="outlined"
          startIcon={<BackIcon />}
          onClick={() => navigate('/cars')}
          sx={{ mb: 2, textTransform: 'none' }}
        >
          Back to Cars
        </Button>
        <Typography variant="h3" component="h1" sx={{ fontWeight: 700, mb: 1 }}>
          {car.model}
        </Typography>
        <Typography variant="h6" color="text.secondary" sx={{ mb: 3 }}>
          {car.manufacturer} • {car.modelYear}
        </Typography>
      </Box>

      <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', lg: '2fr 1fr' }, gap: 4 }}>
        {/* Main Content */}
        <Box>
          {/* Car Image */}
          <Card sx={{ mb: 4, overflow: 'hidden' }}>
            <CardMedia
              component="img"
              height="400"
              image={getCarImage(car)}
              alt={car.model}
              sx={{ objectFit: 'cover' }}
            />
          </Card>

          {/* Car Details */}
          <Paper sx={{ p: 4, mb: 4 }}>
            <Typography variant="h5" component="h2" sx={{ fontWeight: 600, mb: 3 }}>
              Vehicle Details
            </Typography>
            
            <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)' }, gap: 3 }}>
              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <BuildIcon color="primary" />
                <Box>
                  <Typography variant="body2" color="text.secondary">
                    Manufacturer
                  </Typography>
                  <Typography variant="body1" sx={{ fontWeight: 500 }}>
                    {car.manufacturer}
                  </Typography>
                </Box>
              </Box>

              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <CarIcon color="primary" />
                <Box>
                  <Typography variant="body2" color="text.secondary">
                    Model
                  </Typography>
                  <Typography variant="body1" sx={{ fontWeight: 500 }}>
                    {car.model}
                  </Typography>
                </Box>
              </Box>

              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <CalendarIcon color="primary" />
                <Box>
                  <Typography variant="body2" color="text.secondary">
                    Year
                  </Typography>
                  <Typography variant="body1" sx={{ fontWeight: 500 }}>
                    {car.modelYear}
                  </Typography>
                </Box>
              </Box>

              <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                <ColorIcon color="primary" />
                <Box>
                  <Typography variant="body2" color="text.secondary">
                    Color
                  </Typography>
                  <Typography variant="body1" sx={{ fontWeight: 500 }}>
                    {car.color}
                  </Typography>
                </Box>
              </Box>
            </Box>

            <Divider sx={{ my: 3 }} />

            <Box>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                Vehicle Identification
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                <strong>VIN:</strong> {car.vin}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                <strong>Registration Date:</strong> {new Date(car.registrationDate).toLocaleDateString()}
              </Typography>
            </Box>
          </Paper>

          {/* Additional Information */}
          <Paper sx={{ p: 4 }}>
            <Typography variant="h5" component="h2" sx={{ fontWeight: 600, mb: 3 }}>
              About This Vehicle
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
              This {car.manufacturer} {car.model} from {car.modelYear} is a well-maintained vehicle 
              with excellent performance and reliability. The {car.color} exterior gives it a 
              modern and sophisticated look that will turn heads wherever you go.
            </Typography>
            <Typography variant="body1" color="text.secondary">
              All our vehicles undergo thorough inspections and come with complete documentation. 
              This car is ready for immediate purchase and delivery.
            </Typography>
          </Paper>
        </Box>

        {/* Sidebar */}
        <Box>
          {/* Purchase Card */}
          <Paper sx={{ p: 4, mb: 4, position: 'sticky', top: 24 }}>
            <Typography variant="h4" component="h2" sx={{ fontWeight: 700, mb: 3, color: '#10b981' }}>
              ${car.value.toLocaleString()}
            </Typography>

            <Box sx={{ mb: 3 }}>
              <Chip
                label={car.color}
                size="medium"
                sx={{
                  background: 'rgba(59, 130, 246, 0.1)',
                  color: '#3b82f6',
                  fontWeight: 500,
                  mb: 2,
                }}
              />
              <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                <strong>VIN:</strong> {car.vin}
              </Typography>
              <Typography variant="body2" color="text.secondary">
                <strong>Year:</strong> {car.modelYear}
              </Typography>
            </Box>

            <Divider sx={{ my: 3 }} />

            <Box sx={{ mb: 3 }}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                Purchase Summary
              </Typography>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography variant="body2">Car Price:</Typography>
                <Typography variant="body2" sx={{ fontWeight: 500 }}>
                  ${car.value.toLocaleString()}
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography variant="body2">Taxes & Fees:</Typography>
                <Typography variant="body2" sx={{ fontWeight: 500 }}>
                  ${(car.value * 0.08).toLocaleString()}
                </Typography>
              </Box>
              <Divider sx={{ my: 1 }} />
              <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                <Typography variant="body1" sx={{ fontWeight: 600 }}>
                  Total:
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 700, color: '#10b981' }}>
                  ${(car.value * 1.08).toLocaleString()}
                </Typography>
              </Box>
            </Box>

            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Button
                variant="contained"
                fullWidth
                size="large"
                onClick={handlePurchase}
                startIcon={<CartIcon />}
                sx={{
                  background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                  textTransform: 'none',
                  fontWeight: 600,
                  py: 1.5,
                }}
              >
                Purchase Now
              </Button>
              <Button
                variant="outlined"
                fullWidth
                onClick={handleAddToWishlist}
                startIcon={<WishlistIcon />}
                sx={{
                  textTransform: 'none',
                  borderColor: '#3b82f6',
                  color: '#3b82f6',
                  '&:hover': {
                    borderColor: '#1d4ed8',
                    backgroundColor: 'rgba(59, 130, 246, 0.04)',
                  },
                }}
              >
                Add to Wishlist
              </Button>
            </Box>

            <Alert severity="info" sx={{ mt: 3 }}>
              <Typography variant="body2">
                Free delivery within 50km • 30-day return policy • Full warranty included
              </Typography>
            </Alert>
          </Paper>

          {/* Contact Information */}
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
              Need Help?
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Our sales team is here to help you with any questions about this vehicle.
            </Typography>
            <Button
              variant="outlined"
              fullWidth
              onClick={() => navigate('/client/contact')}
              sx={{ textTransform: 'none' }}
            >
              Contact Sales Team
            </Button>
          </Paper>
        </Box>
      </Box>
    </Container>
  );
};

export default ClientCarDetail; 