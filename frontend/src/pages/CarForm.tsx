import {
    ArrowBack as ArrowBackIcon,
    DirectionsCar as CarIcon,
    Save as SaveIcon,
} from '@mui/icons-material';
import {
    Alert,
    Avatar,
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    TextField,
    Typography
} from '@mui/material';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { carService } from '../services/carService';
import { CreateCarRequest, UpdateCarRequest } from '../types/car';

const CarForm: React.FC = () => {
  const navigate = useNavigate();
  const { vin } = useParams<{ vin: string }>();
  const queryClient = useQueryClient();
  const isEditing = Boolean(vin);

  const [formData, setFormData] = useState<CreateCarRequest>({
    model: '',
    modelYear: '',
    manufacturer: '',
    color: '',
    vin: '',
    value: 0,
  });

  const [errors, setErrors] = useState<Partial<Record<keyof CreateCarRequest, string>>>({});

  // Fetch car data if editing
  const { data: carResponse, isLoading: isLoadingCar } = useQuery({
    queryKey: ['car', vin],
    queryFn: () => carService.getCarByVin(vin!),
    enabled: isEditing && Boolean(vin),
  });

  // Create car mutation
  const createMutation = useMutation({
    mutationFn: (data: CreateCarRequest) => carService.createCar(data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cars'] });
      navigate('/admin/cars');
    },
  });

  // Update car mutation
  const updateMutation = useMutation({
    mutationFn: ({ vin, data }: { vin: string; data: UpdateCarRequest }) =>
      carService.updateCar(vin, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cars'] });
      queryClient.invalidateQueries({ queryKey: ['car', vin] });
      navigate('/admin/cars');
    },
  });

  // Load car data when editing
  useEffect(() => {
    if (carResponse?.data) {
      const car = carResponse.data;
      setFormData({
        model: car.model,
        modelYear: car.modelYear,
        manufacturer: car.manufacturer,
        color: car.color,
        vin: car.vin,
        value: car.value,
      });
    }
  }, [carResponse]);

  const handleInputChange = (field: keyof CreateCarRequest, value: string | number) => {
    setFormData(prev => ({
      ...prev,
      [field]: value as any,
    }));
    
    // Clear error when user starts typing
    if (errors[field]) {
      setErrors(prev => ({
        ...prev,
        [field]: undefined,
      }));
    }
  };

  const validateForm = (): boolean => {
    const newErrors: Partial<Record<keyof CreateCarRequest, string>> = {};

    if (!formData.model.trim()) newErrors.model = 'Model is required';
    if (!formData.modelYear.trim()) newErrors.modelYear = 'Model year is required';
    if (!formData.manufacturer.trim()) newErrors.manufacturer = 'Manufacturer is required';
    if (!formData.color.trim()) newErrors.color = 'Color is required';
    if (!formData.vin.trim()) newErrors.vin = 'VIN is required';
    if (formData.value <= 0) newErrors.value = 'Value must be greater than 0';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!validateForm()) return;

    if (isEditing) {
      const updateData: UpdateCarRequest = {
        color: formData.color,
        value: formData.value,
        modelYear: formData.modelYear,
      };
      updateMutation.mutate({ vin: vin!, data: updateData });
    } else {
      createMutation.mutate(formData);
    }
  };

  const isLoading = isLoadingCar || createMutation.isPending || updateMutation.isPending;

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
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 600 }}>
            {isEditing ? 'Edit Car' : 'Add New Car'}
          </Typography>
          <Typography variant="body1" color="text.secondary">
            {isEditing ? 'Update car information' : 'Add a new vehicle to your inventory'}
          </Typography>
        </Box>
      </Box>

      {/* Error Alerts */}
      {(createMutation.error || updateMutation.error) && (
        <Alert severity="error" sx={{ mb: 3 }}>
          {createMutation.error?.message || updateMutation.error?.message || 'An error occurred'}
        </Alert>
      )}

      {/* Form */}
      <Card sx={{ background: 'rgba(255, 255, 255, 0.8)', backdropFilter: 'blur(20px)' }}>
        <CardContent sx={{ p: 4 }}>
          <form onSubmit={handleSubmit}>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
              <Box sx={{ flex: '1 1 300px', minWidth: 300 }}>
                <TextField
                  fullWidth
                  label="Manufacturer"
                  value={formData.manufacturer}
                  onChange={(e) => handleInputChange('manufacturer', e.target.value)}
                  error={Boolean(errors.manufacturer)}
                  helperText={errors.manufacturer}
                  disabled={isLoading || isEditing}
                  required
                />
              </Box>

              <Box sx={{ flex: '1 1 300px', minWidth: 300 }}>
                <TextField
                  fullWidth
                  label="Model"
                  value={formData.model}
                  onChange={(e) => handleInputChange('model', e.target.value)}
                  error={Boolean(errors.model)}
                  helperText={errors.model}
                  disabled={isLoading || isEditing}
                  required
                />
              </Box>

              <Box sx={{ flex: '1 1 300px', minWidth: 300 }}>
                <TextField
                  fullWidth
                  label="Model Year"
                  value={formData.modelYear}
                  onChange={(e) => handleInputChange('modelYear', e.target.value)}
                  error={Boolean(errors.modelYear)}
                  helperText={errors.modelYear}
                  disabled={isLoading}
                  required
                />
              </Box>

              <Box sx={{ flex: '1 1 300px', minWidth: 300 }}>
                <TextField
                  fullWidth
                  label="Color"
                  value={formData.color}
                  onChange={(e) => handleInputChange('color', e.target.value)}
                  error={Boolean(errors.color)}
                  helperText={errors.color}
                  disabled={isLoading}
                  required
                />
              </Box>

              <Box sx={{ flex: '1 1 300px', minWidth: 300 }}>
                <TextField
                  fullWidth
                  label="VIN"
                  value={formData.vin}
                  onChange={(e) => handleInputChange('vin', e.target.value)}
                  error={Boolean(errors.vin)}
                  helperText={errors.vin}
                  disabled={isLoading || isEditing}
                  required
                />
              </Box>

              <Box sx={{ flex: '1 1 300px', minWidth: 300 }}>
                <TextField
                  fullWidth
                  label="Value ($)"
                  type="number"
                  value={formData.value}
                  onChange={(e) => handleInputChange('value', e.target.value ? Number(e.target.value) : 0)}
                  error={Boolean(errors.value)}
                  helperText={errors.value}
                  disabled={isLoading}
                  required
                  InputProps={{
                    startAdornment: '$',
                  }}
                />
              </Box>
            </Box>

            <Box sx={{ display: 'flex', gap: 2, mt: 4 }}>
              <Button
                type="submit"
                variant="contained"
                startIcon={isLoading ? <CircularProgress size={20} /> : <SaveIcon />}
                disabled={isLoading}
                sx={{
                  background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                  px: 4,
                  py: 1.5,
                }}
              >
                {isLoading ? 'Saving...' : (isEditing ? 'Update Car' : 'Add Car')}
              </Button>
              <Button
                variant="outlined"
                onClick={() => navigate('/admin/cars')}
                disabled={isLoading}
              >
                Cancel
              </Button>
            </Box>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
};

export default CarForm; 