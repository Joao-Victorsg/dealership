import {
  Add as AddIcon,
  DirectionsCar as CarIcon,
  Clear as ClearIcon,
  Delete as DeleteIcon,
  Edit as EditIcon,
  FilterList as FilterIcon,
  Search as SearchIcon,
  Visibility as ViewIcon,
} from '@mui/icons-material';
import {
  Alert,
  Avatar,
  Box,
  Button,
  Card,
  CardContent,
  CardMedia,
  Chip,
  CircularProgress,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  FormControl,
  IconButton,
  InputAdornment,
  MenuItem,
  Paper,
  Skeleton,
  TextField,
  Tooltip,
  Typography,
} from '@mui/material';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import React, { useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { carService } from '../services/carService';
import { Car, CarSearchFilters } from '../types/car';

const CarList: React.FC = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();
  const [searchTerm, setSearchTerm] = useState('');
  const [showFilters, setShowFilters] = useState(false);
  const [filters, setFilters] = useState<CarSearchFilters>({});
  const [deleteDialog, setDeleteDialog] = useState<{ open: boolean; car: Car | null }>({
    open: false,
    car: null,
  });

  // Fetch cars with pagination and filters
  const { data: carsResponse, isLoading, error } = useQuery({
    queryKey: ['cars', filters, searchTerm],
    queryFn: () => {
      const apiFilters = {
        ...filters,
        initialValue: filters.initialValue && filters.initialValue > 0 ? filters.initialValue : undefined,
        finalValue: filters.finalValue && filters.finalValue > 0 ? filters.finalValue : undefined,
      };
      return carService.getCars(0, 100, apiFilters);
    },
  });

  // Fetch manufacturers for dropdown
  const { data: manufacturersResponse } = useQuery({
    queryKey: ['manufacturers'],
    queryFn: () => carService.getManufacturers(),
  });

  // Fetch models for dropdown
  const { data: modelsResponse } = useQuery({
    queryKey: ['models'],
    queryFn: () => carService.getModels(),
  });

  // Fetch models by manufacturer for cascading filter
  const { data: modelsByManufacturerResponse } = useQuery({
    queryKey: ['modelsByManufacturer', filters.manufacturer],
    queryFn: () => carService.getModelsByManufacturer(filters.manufacturer!),
    enabled: !!filters.manufacturer,
  });

  // Delete car mutation
  const deleteMutation = useMutation({
    mutationFn: (vin: string) => carService.deleteCar(vin),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['cars'] });
      setDeleteDialog({ open: false, car: null });
    },
  });

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
  };

  const handleFilterChange = (key: keyof CarSearchFilters, value: string | number) => {
    // Handle empty values for numeric filters
    if ((key === 'initialValue' || key === 'finalValue') && (value === '' || value === 0)) {
      setFilters(prev => {
        const newFilters = { ...prev };
        delete newFilters[key];
        return newFilters;
      });
    } else if (key === 'color' && value === '') {
      // Handle empty color filter
      setFilters(prev => {
        const newFilters = { ...prev };
        delete newFilters[key];
        return newFilters;
      });
    } else {
      setFilters(prev => ({
        ...prev,
        [key]: value,
      }));
    }
  };

  const clearFilters = () => {
    setFilters({});
    setSearchTerm('');
  };

  const handleDelete = (car: Car) => {
    setDeleteDialog({ open: true, car });
  };

  const confirmDelete = () => {
    if (deleteDialog.car) {
      deleteMutation.mutate(deleteDialog.car.vin);
    }
  };

  // Filter cars based on search term
  const filteredCars = useMemo(() => {
    if (!carsResponse?.data?.content) return [];
    
    let cars = carsResponse.data.content;
    
    if (searchTerm) {
      cars = cars.filter(car =>
        car.model.toLowerCase().includes(searchTerm.toLowerCase()) ||
        car.manufacturer.toLowerCase().includes(searchTerm.toLowerCase()) ||
        car.vin.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    
    return cars;
  }, [carsResponse?.data?.content, searchTerm]);

  // Placeholder car image logic (same as client page)
  const getCarImage = (car: Car) => {
    const images = [
      'https://images.unsplash.com/photo-1560958089-b8a1929cea89?w=400&h=300&fit=crop',
      'https://images.unsplash.com/photo-1555215695-3004980ad54e?w=400&h=300&fit=crop',
      'https://images.unsplash.com/photo-1618843479313-40f8afb4b4d8?w=400&h=300&fit=crop',
      'https://images.unsplash.com/photo-1549317661-bd32c8ce0db2?w=400&h=300&fit=crop',
      'https://images.unsplash.com/photo-1494976388531-d1058494cdd8?w=400&h=300&fit=crop',
    ];
    return images[Math.abs(car.vin.length) % images.length];
  };

  return (
    <Box>
      {/* Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 700, mb: 1 }}>
            Car Inventory
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Manage your vehicle inventory with ease
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => navigate('/admin/cars/new')}
          sx={{
            background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
            borderRadius: 2,
            px: 3,
            py: 1.5,
            textTransform: 'none',
            fontWeight: 600,
          }}
        >
          Add Car
        </Button>
      </Box>

      {/* Search and Filters */}
      <Card sx={{ mb: 4, background: 'rgba(255, 255, 255, 0.8)', backdropFilter: 'blur(20px)' }}>
        <CardContent>
          <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap', alignItems: 'center' }}>
            <TextField
              placeholder="Search cars..."
              value={searchTerm}
              onChange={handleSearch}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
              }}
              sx={{ flex: '1 1 300px', minWidth: 300 }}
            />
            <Button
              variant="outlined"
              startIcon={<FilterIcon />}
              onClick={() => setShowFilters(!showFilters)}
              sx={{ borderRadius: 2 }}
            >
              Filters
            </Button>
            {(Object.keys(filters).length > 0 || searchTerm) && (
              <Button
                variant="text"
                startIcon={<ClearIcon />}
                onClick={clearFilters}
                sx={{ borderRadius: 2 }}
              >
                Clear
              </Button>
            )}
          </Box>

          {showFilters && (
            <Box sx={{ mt: 3, display: 'flex', gap: 2, flexWrap: 'wrap' }}>
              <FormControl sx={{ minWidth: 150 }}>
                <TextField
                  select
                  label="Manufacturer"
                  value={filters.manufacturer || ''}
                  onChange={(e) => handleFilterChange('manufacturer', e.target.value)}
                >
                  <MenuItem value="">
                    <em>All Manufacturers</em>
                  </MenuItem>
                  {manufacturersResponse?.data?.map((manufacturer) => (
                    <MenuItem key={manufacturer} value={manufacturer}>
                      {manufacturer}
                    </MenuItem>
                  ))}
                </TextField>
              </FormControl>

              <FormControl sx={{ minWidth: 150 }}>
                <TextField
                  select
                  label="Model"
                  value={filters.model || ''}
                  onChange={(e) => handleFilterChange('model', e.target.value)}
                  disabled={!filters.manufacturer}
                >
                  <MenuItem value="">
                    <em>All Models</em>
                  </MenuItem>
                  {(filters.manufacturer ? modelsByManufacturerResponse?.data : modelsResponse?.data)?.map((model) => (
                    <MenuItem key={model} value={model}>
                      {model}
                    </MenuItem>
                  ))}
                </TextField>
              </FormControl>

              <TextField
                label="Model Year"
                value={filters.modelYear || ''}
                onChange={(e) => handleFilterChange('modelYear', e.target.value)}
                sx={{ minWidth: 150 }}
              />
              <TextField
                label="Min Value"
                type="number"
                value={filters.initialValue || ''}
                onChange={(e) => handleFilterChange('initialValue', Number(e.target.value))}
                sx={{ minWidth: 120 }}
              />
              <TextField
                label="Max Value"
                type="number"
                value={filters.finalValue || ''}
                onChange={(e) => handleFilterChange('finalValue', Number(e.target.value))}
                sx={{ minWidth: 120 }}
              />
              <FormControl sx={{ minWidth: 150 }}>
                <TextField
                  label="Color"
                  value={filters.color || ''}
                  onChange={(e) => handleFilterChange('color', e.target.value)}
                  placeholder="Enter color"
                  sx={{ minWidth: 150 }}
                />
              </FormControl>
            </Box>
          )}
        </CardContent>
      </Card>

      {/* Error Alert */}
      {error && (
        <Alert severity="error" sx={{ mb: 3 }}>
          Failed to load cars. Please try again.
        </Alert>
      )}

      {/* Cars Grid */}
      {isLoading ? (
        <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 3 }}>
          {[...Array(6)].map((_, index) => (
            <Card key={index} sx={{ flex: '1 1 350px', minWidth: 350 }}>
              <CardContent>
                <Skeleton variant="rectangular" height={200} sx={{ borderRadius: 2, mb: 2 }} />
                <Skeleton variant="text" height={32} sx={{ mb: 1 }} />
                <Skeleton variant="text" height={24} sx={{ mb: 2 }} />
                <Box sx={{ display: 'flex', gap: 1 }}>
                  <Skeleton variant="rectangular" width={80} height={32} sx={{ borderRadius: 1 }} />
                  <Skeleton variant="rectangular" width={80} height={32} sx={{ borderRadius: 1 }} />
                </Box>
              </CardContent>
            </Card>
          ))}
        </Box>
      ) : filteredCars.length === 0 ? (
        <Paper sx={{ p: 8, textAlign: 'center' }}>
          <CarIcon sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No cars found
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
            {searchTerm || Object.keys(filters).length > 0
              ? 'Try adjusting your search or filters'
              : 'Get started by adding your first car to the inventory'
            }
          </Typography>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => navigate('/admin/cars/new')}
            sx={{ borderRadius: 2 }}
          >
            Add First Car
          </Button>
        </Paper>
      ) : (
        <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', lg: 'repeat(3, 1fr)', xl: 'repeat(4, 1fr)' }, gap: 3, mb: 4 }}>
          {filteredCars.map((car) => (
            <Card
              key={car.vin}
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                transition: 'transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out',
                cursor: 'pointer',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: '0 12px 24px rgba(0, 0, 0, 0.15)',
                },
              }}
            >
              <CardMedia
                component="img"
                height="200"
                image={getCarImage(car)}
                alt={car.model}
                sx={{ objectFit: 'cover' }}
              />
              <CardContent sx={{ flexGrow: 1, p: 3 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
                  <Avatar
                    sx={{
                      width: 60,
                      height: 60,
                      mr: 2,
                      background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                    }}
                  >
                    <CarIcon />
                  </Avatar>
                  <Box sx={{ flexGrow: 1 }}>
                    <Typography variant="h6" component="h3" sx={{ fontWeight: 600 }}>
                      {car.model}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      {car.manufacturer}
                    </Typography>
                  </Box>
                </Box>

                <Box sx={{ mb: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2" color="text.secondary">
                      VIN
                    </Typography>
                    <Typography variant="body2" sx={{ fontWeight: 500 }}>
                      {car.vin}
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2" color="text.secondary">
                      Year
                    </Typography>
                    <Typography variant="body2" sx={{ fontWeight: 500 }}>
                      {car.modelYear}
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography variant="body2" color="text.secondary">
                      Color
                    </Typography>
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
                  <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="body2" color="text.secondary">
                      Value
                    </Typography>
                    <Typography variant="body2" sx={{ fontWeight: 600, color: '#10b981' }}>
                      ${car.value.toLocaleString()}
                    </Typography>
                  </Box>
                </Box>

                {/* Centered Action Buttons */}
                <Box sx={{ display: 'flex', justifyContent: 'center', gap: 1, mb: 2 }}>
                  <Tooltip title="View Details">
                    <IconButton
                      size="small"
                      onClick={() => navigate(`/admin/cars/${car.vin}`)}
                      sx={{ color: '#3b82f6' }}
                    >
                      <ViewIcon />
                    </IconButton>
                  </Tooltip>
                  <Tooltip title="Edit Car">
                    <IconButton
                      size="small"
                      onClick={() => navigate(`/admin/cars/${car.vin}/edit`)}
                      sx={{ color: '#f59e0b' }}
                    >
                      <EditIcon />
                    </IconButton>
                  </Tooltip>
                  <Tooltip title="Delete Car">
                    <IconButton
                      size="small"
                      onClick={() => handleDelete(car)}
                      sx={{ color: '#ef4444' }}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Tooltip>
                </Box>
              </CardContent>
            </Card>
          ))}
        </Box>
      )}

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={deleteDialog.open}
        onClose={() => setDeleteDialog({ open: false, car: null })}
      >
        <DialogTitle>Delete Car</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete the car "{deleteDialog.car?.model}" ({deleteDialog.car?.vin})? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDeleteDialog({ open: false, car: null })}>
            Cancel
          </Button>
          <Button
            onClick={confirmDelete}
            color="error"
            variant="contained"
            disabled={deleteMutation.isPending}
          >
            {deleteMutation.isPending ? <CircularProgress size={20} /> : 'Delete'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default CarList;