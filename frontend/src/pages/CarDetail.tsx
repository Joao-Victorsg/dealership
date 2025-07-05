import {
    ArrowBack as ArrowBackIcon,
    DirectionsCar as CarIcon,
    Edit as EditIcon,
} from '@mui/icons-material';
import {
    Alert,
    Avatar,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    Paper,
    Skeleton,
    Typography,
} from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import React from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { carService } from '../services/carService';

const CarDetail: React.FC = () => {
  const navigate = useNavigate();
  const { vin } = useParams<{ vin: string }>();

  const { data: carResponse, isLoading, error } = useQuery({
    queryKey: ['car', vin],
    queryFn: () => carService.getCarByVin(vin!),
    enabled: Boolean(vin),
  });

  const car = carResponse?.data;

  if (isLoading) {
    return (
      <Box>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
          <Button
            startIcon={<ArrowBackIcon />}
            onClick={() => navigate('/admin/cars')}
            sx={{ mr: 2 }}
          >
            Back to Cars
          </Button>
          <Skeleton variant="rectangular" width={200} height={40} />
        </Box>
        <Card sx={{ background: 'rgba(255, 255, 255, 0.8)', backdropFilter: 'blur(20px)' }}>
          <CardContent sx={{ p: 4 }}>
            <Skeleton variant="rectangular" height={200} sx={{ borderRadius: 2, mb: 3 }} />
            <Skeleton variant="text" height={32} sx={{ mb: 2 }} />
            <Skeleton variant="text" height={24} sx={{ mb: 1 }} />
            <Skeleton variant="text" height={24} sx={{ mb: 1 }} />
            <Skeleton variant="text" height={24} sx={{ mb: 3 }} />
            <Skeleton variant="rectangular" width={120} height={40} />
          </CardContent>
        </Card>
      </Box>
    );
  }

  if (error || !car) {
    return (
      <Box>
        <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
          <Button
            startIcon={<ArrowBackIcon />}
            onClick={() => navigate('/admin/cars')}
            sx={{ mr: 2 }}
          >
            Back to Cars
          </Button>
        </Box>
        <Alert severity="error">
          Failed to load car details. Please try again.
        </Alert>
      </Box>
    );
  }

  return (
    <Box>
      {/* Header */}
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
        <Button
          startIcon={<ArrowBackIcon />}
          onClick={() => navigate('/admin/cars')}
          sx={{ mr: 2 }}
        >
          Back to Cars
        </Button>
        <Avatar
          sx={{
            width: 48,
            height: 48,
            mr: 2,
            background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
          }}
        >
          <CarIcon />
        </Avatar>
        <Box sx={{ flexGrow: 1 }}>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 600 }}>
            {car.model}
          </Typography>
          <Typography variant="body1" color="text.secondary">
            {car.manufacturer} • {car.modelYear}
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<EditIcon />}
          onClick={() => navigate(`/admin/cars/${car.vin}/edit`)}
          sx={{
            background: 'linear-gradient(135deg, #f59e0b 0%, #d97706 100%)',
            borderRadius: 2,
            px: 3,
            py: 1.5,
          }}
        >
          Edit Car
        </Button>
      </Box>

      {/* Car Details */}
      <Card sx={{ background: 'rgba(255, 255, 255, 0.8)', backdropFilter: 'blur(20px)' }}>
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 4 }}>
            <Avatar
              sx={{
                width: 80,
                height: 80,
                mr: 3,
                background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
              }}
            >
              <CarIcon sx={{ fontSize: 40 }} />
            </Avatar>
            <Box>
              <Typography variant="h5" component="h2" sx={{ fontWeight: 600, mb: 1 }}>
                {car.model}
              </Typography>
              <Typography variant="body1" color="text.secondary" sx={{ mb: 2 }}>
                {car.manufacturer} • {car.modelYear}
              </Typography>
              <Chip
                label={car.color}
                sx={{
                  background: 'rgba(59, 130, 246, 0.1)',
                  color: '#3b82f6',
                  fontWeight: 600,
                }}
              />
            </Box>
          </Box>

          <Paper sx={{ p: 3, background: 'rgba(0, 0, 0, 0.02)', borderRadius: 2 }}>
            <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
              Vehicle Information
            </Typography>
            
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
              <Box sx={{ flex: '1 1 200px' }}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  VIN Number
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 500, fontFamily: 'monospace' }}>
                  {car.vin}
                </Typography>
              </Box>
              
              <Box sx={{ flex: '1 1 200px' }}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Model Year
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>
                  {car.modelYear}
                </Typography>
              </Box>
              
              <Box sx={{ flex: '1 1 200px' }}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Manufacturer
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>
                  {car.manufacturer}
                </Typography>
              </Box>
              
              <Box sx={{ flex: '1 1 200px' }}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Color
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>
                  {car.color}
                </Typography>
              </Box>
              
              <Box sx={{ flex: '1 1 200px' }}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Value
                </Typography>
                <Typography variant="h6" sx={{ fontWeight: 600, color: '#10b981' }}>
                  ${car.value.toLocaleString()}
                </Typography>
              </Box>
              
              <Box sx={{ flex: '1 1 200px' }}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Registration Date
                </Typography>
                <Typography variant="body1" sx={{ fontWeight: 500 }}>
                  {new Date(car.registrationDate).toLocaleDateString()}
                </Typography>
              </Box>
            </Box>
          </Paper>
        </CardContent>
      </Card>
    </Box>
  );
};

export default CarDetail; 