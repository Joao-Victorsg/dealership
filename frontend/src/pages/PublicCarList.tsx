import {
    DirectionsCar as CarIcon,
    Clear as ClearIcon,
    FilterList as FilterIcon,
    Search as SearchIcon,
} from '@mui/icons-material';
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    CardMedia,
    Chip,
    CircularProgress,
    Container,
    FormControl,
    InputAdornment,
    InputLabel,
    MenuItem,
    Pagination,
    Paper,
    Select,
    Slider,
    TextField,
    Typography,
} from '@mui/material';
import { useQuery } from '@tanstack/react-query';
import React, { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { publicCarService } from '../services/carService';
import { Car } from '../types/car';

const PublicCarList: React.FC = () => {
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  
  // State for filters
  const [searchTerm, setSearchTerm] = useState(searchParams.get('search') || '');
  const [manufacturer, setManufacturer] = useState(searchParams.get('manufacturer') || '');
  const [model, setModel] = useState(searchParams.get('model') || '');
  const [year, setYear] = useState(searchParams.get('year') || '');
  const [color, setColor] = useState(searchParams.get('color') || '');
  const [priceRange, setPriceRange] = useState<number[]>([
    parseInt(searchParams.get('minPrice') || '0'),
    parseInt(searchParams.get('maxPrice') || '100000')
  ]);
  const [showFilters, setShowFilters] = useState(false);
  const [page, setPage] = useState(parseInt(searchParams.get('page') || '0'));

  // Fetch cars
  const { data: carsResponse, isLoading, error } = useQuery({
    queryKey: ['public-cars', page, searchTerm, manufacturer, model, year, color, priceRange],
    queryFn: () => publicCarService.getCars(page, 12, {
      manufacturer: manufacturer || undefined,
      model: model || undefined,
      modelYear: year || undefined,
      initialValue: priceRange[0] > 0 ? priceRange[0] : undefined,
      finalValue: priceRange[1] < 100000 ? priceRange[1] : undefined,
    }),
  });

  // Fetch manufacturers for dropdown
  const { data: manufacturersResponse } = useQuery({
    queryKey: ['public-manufacturers'],
    queryFn: () => publicCarService.getManufacturers(),
  });

  // Fetch models for dropdown
  const { data: modelsResponse } = useQuery({
    queryKey: ['public-models'],
    queryFn: () => publicCarService.getModels(),
  });

  // Fetch models by manufacturer for cascading dropdown
  const { data: modelsByManufacturerResponse } = useQuery({
    queryKey: ['public-modelsByManufacturer', manufacturer],
    queryFn: () => publicCarService.getModelsByManufacturer(manufacturer),
    enabled: !!manufacturer,
  });

  const cars = carsResponse?.data?.content || [];
  const totalPages = carsResponse?.data?.totalPages || 0;

  // Update URL params when filters change
  useEffect(() => {
    const params = new URLSearchParams();
    if (searchTerm) params.set('search', searchTerm);
    if (manufacturer) params.set('manufacturer', manufacturer);
    if (model) params.set('model', model);
    if (year) params.set('year', year);
    if (color) params.set('color', color);
    if (priceRange[0] > 0) params.set('minPrice', priceRange[0].toString());
    if (priceRange[1] < 100000) params.set('maxPrice', priceRange[1].toString());
    if (page > 0) params.set('page', page.toString());
    
    setSearchParams(params);
  }, [searchTerm, manufacturer, model, year, color, priceRange, page, setSearchParams]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    setPage(0);
  };

  const handleClearFilters = () => {
    setSearchTerm('');
    setManufacturer('');
    setModel('');
    setYear('');
    setColor('');
    setPriceRange([0, 100000]);
    setPage(0);
  };

  const handleManufacturerChange = (newManufacturer: string) => {
    setManufacturer(newManufacturer);
    setModel(''); // Clear model when manufacturer changes
    setPage(0);
  };

  const handleViewDetails = (car: Car) => {
    // Prompt user to sign in to view details
    navigate('/login', { state: { redirect: `/cars/${car.vin}` } });
  };

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value - 1);
  };

  // Placeholder car image
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

  if (error) {
    return (
      <Container maxWidth="xl">
        <Alert severity="error" sx={{ mt: 4 }}>
          Error loading cars. Please try again later.
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="xl">
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" sx={{ fontWeight: 700, mb: 2 }}>
          Browse Cars
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Find your perfect vehicle from our extensive collection
        </Typography>
      </Box>

      {/* Search and Filters */}
      <Paper sx={{ p: 3, mb: 4 }}>
        <Box component="form" onSubmit={handleSearch} sx={{ mb: 3 }}>
          <Box sx={{ display: 'flex', gap: 2, alignItems: 'center', flexWrap: 'wrap' }}>
            <TextField
              placeholder="Search cars..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              sx={{ flexGrow: 1, minWidth: 200 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon />
                  </InputAdornment>
                ),
              }}
            />
            <Button
              variant="outlined"
              onClick={() => setShowFilters(!showFilters)}
              startIcon={<FilterIcon />}
              sx={{ textTransform: 'none' }}
            >
              Filters
            </Button>
            <Button
              variant="contained"
              type="submit"
              sx={{
                background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                textTransform: 'none',
              }}
            >
              Search
            </Button>
          </Box>
        </Box>

        {/* Advanced Filters */}
        {showFilters && (
          <Box sx={{ pt: 3, borderTop: '1px solid rgba(0, 0, 0, 0.1)' }}>
            <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', md: 'repeat(4, 1fr)' }, gap: 2, mb: 3 }}>
              <FormControl fullWidth size="small">
                <InputLabel>Manufacturer</InputLabel>
                <Select
                  value={manufacturer}
                  onChange={(e) => handleManufacturerChange(e.target.value)}
                  label="Manufacturer"
                >
                  <MenuItem value="">All</MenuItem>
                  {manufacturersResponse?.data?.map((manufacturerName) => (
                    <MenuItem key={manufacturerName} value={manufacturerName}>
                      {manufacturerName}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <FormControl fullWidth size="small">
                <InputLabel>Model</InputLabel>
                <Select
                  value={model}
                  onChange={(e) => {
                    setModel(e.target.value);
                    setPage(0);
                  }}
                  label="Model"
                  disabled={!manufacturer}
                >
                  <MenuItem value="">All</MenuItem>
                  {(manufacturer ? modelsByManufacturerResponse?.data : modelsResponse?.data)?.map((modelName) => (
                    <MenuItem key={modelName} value={modelName}>
                      {modelName}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>

              <FormControl fullWidth size="small">
                <InputLabel>Year</InputLabel>
                <Select
                  value={year}
                  onChange={(e) => setYear(e.target.value)}
                  label="Year"
                >
                  <MenuItem value="">All</MenuItem>
                  <MenuItem value="2024">2024</MenuItem>
                  <MenuItem value="2023">2023</MenuItem>
                  <MenuItem value="2022">2022</MenuItem>
                  <MenuItem value="2021">2021</MenuItem>
                </Select>
              </FormControl>

              <FormControl fullWidth size="small">
                <InputLabel>Color</InputLabel>
                <Select
                  value={color}
                  onChange={(e) => setColor(e.target.value)}
                  label="Color"
                >
                  <MenuItem value="">All</MenuItem>
                  <MenuItem value="White">White</MenuItem>
                  <MenuItem value="Black">Black</MenuItem>
                  <MenuItem value="Silver">Silver</MenuItem>
                  <MenuItem value="Red">Red</MenuItem>
                  <MenuItem value="Blue">Blue</MenuItem>
                </Select>
              </FormControl>
            </Box>

            <Box sx={{ mb: 3 }}>
              <Typography variant="subtitle2" sx={{ mb: 2 }}>
                Price Range: ${priceRange[0].toLocaleString()} - ${priceRange[1].toLocaleString()}
              </Typography>
              <Slider
                value={priceRange}
                onChange={(_, newValue) => setPriceRange(newValue as number[])}
                valueLabelDisplay="auto"
                min={0}
                max={100000}
                step={1000}
                sx={{ px: 2 }}
              />
            </Box>

            <Button
              variant="outlined"
              onClick={handleClearFilters}
              startIcon={<ClearIcon />}
              sx={{ textTransform: 'none' }}
            >
              Clear All Filters
            </Button>
          </Box>
        )}
      </Paper>

      {/* Results Count */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="body2" color="text.secondary">
          {carsResponse?.data?.totalElements || 0} cars found
        </Typography>
      </Box>

      {/* Cars Grid */}
      {isLoading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 8 }}>
          <CircularProgress />
        </Box>
      ) : cars.length === 0 ? (
        <Box sx={{ textAlign: 'center', py: 8 }}>
          <CarIcon sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" color="text.secondary" sx={{ mb: 1 }}>
            No cars found
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Try adjusting your search criteria or filters
          </Typography>
        </Box>
      ) : (
        <>
          <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)', lg: 'repeat(3, 1fr)', xl: 'repeat(4, 1fr)' }, gap: 3, mb: 4 }}>
            {cars.map((car) => (
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
                onClick={() => handleViewDetails(car)}
              >
                <CardMedia
                  component="img"
                  height="200"
                  image={getCarImage(car)}
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
                        {car.manufacturer} • {car.modelYear}
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

                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                      VIN: {car.vin}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Registration: {new Date(car.registrationDate).toLocaleDateString()}
                    </Typography>
                  </Box>

                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                    <Typography variant="h5" sx={{ fontWeight: 700, color: '#10b981' }}>
                      ${car.value?.toLocaleString() || 'N/A'}
                    </Typography>
                  </Box>

                  <Button
                    variant="contained"
                    fullWidth
                    onClick={(e) => {
                      e.stopPropagation();
                      handleViewDetails(car);
                    }}
                    sx={{
                      background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                      textTransform: 'none',
                      fontWeight: 600,
                    }}
                  >
                    View Details
                  </Button>
                </CardContent>
              </Card>
            ))}
          </Box>

          {/* Pagination */}
          {totalPages > 1 && (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
              <Pagination
                count={totalPages}
                page={page + 1}
                onChange={handlePageChange}
                color="primary"
                size="large"
              />
            </Box>
          )}
        </>
      )}
    </Container>
  );
};

export default PublicCarList;
