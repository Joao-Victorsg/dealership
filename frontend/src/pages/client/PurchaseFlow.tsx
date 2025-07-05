import {
    ArrowBack as BackIcon,
    CheckCircle as CheckIcon
} from '@mui/icons-material';
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    CardMedia,
    CircularProgress,
    Container,
    Divider,
    Paper,
    Step,
    StepLabel,
    Stepper,
    TextField,
    Typography
} from '@mui/material';
import { useMutation } from '@tanstack/react-query';
import React, { useState } from 'react';
import { useLocation, useNavigate, useParams } from 'react-router-dom';
import { salesService } from '../../services/salesService';
import { Car } from '../../types/car';
import { SalesRequest } from '../../types/sales';

const steps = ['Car Details', 'Customer Information', 'Purchase Confirmation'];

const PurchaseFlow: React.FC = () => {
  const navigate = useNavigate();
  const { vin } = useParams<{ vin: string }>();
  const location = useLocation();
  const car = location.state?.car as Car;

  const [activeStep, setActiveStep] = useState(0);
  const [customerInfo, setCustomerInfo] = useState({
    cpf: '',
    name: '',
    email: '',
    phone: '',
  });

  // Create sale mutation
  const createSaleMutation = useMutation({
    mutationFn: (saleData: SalesRequest) => salesService.createSale(saleData),
    onSuccess: () => {
      setActiveStep(2);
    },
    onError: (error) => {
      console.error('Error creating sale:', error);
    },
  });

  const handleNext = () => {
    if (activeStep === 0) {
      setActiveStep(1);
    } else if (activeStep === 1) {
      // Validate customer info
      if (!customerInfo.cpf || !customerInfo.name || !customerInfo.email) {
        return;
      }
      
      // Create the sale
      const saleData: SalesRequest = {
        cpf: customerInfo.cpf,
        vin: vin!,
      };
      
      createSaleMutation.mutate(saleData);
    }
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleCustomerInfoChange = (field: string, value: string) => {
    setCustomerInfo(prev => ({
      ...prev,
      [field]: value,
    }));
  };

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

  if (!car) {
    return (
      <Container maxWidth="md">
        <Alert severity="error" sx={{ mt: 4 }}>
          Car information not found. Please go back and select a car.
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
    <Container maxWidth="md">
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
        <Typography variant="h4" component="h1" sx={{ fontWeight: 700, mb: 1 }}>
          Purchase Car
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Complete your purchase in just a few steps
        </Typography>
      </Box>

      {/* Stepper */}
      <Paper sx={{ p: 3, mb: 4 }}>
        <Stepper activeStep={activeStep} alternativeLabel>
          {steps.map((label) => (
            <Step key={label}>
              <StepLabel>{label}</StepLabel>
            </Step>
          ))}
        </Stepper>
      </Paper>

      {/* Step Content */}
      <Box sx={{ mb: 4 }}>
        {activeStep === 0 && (
          <Paper sx={{ p: 4 }}>
            <Typography variant="h5" component="h2" sx={{ mb: 3, fontWeight: 600 }}>
              Car Details
            </Typography>
            
            <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', md: '1fr 1fr' }, gap: 4 }}>
              <Card>
                <CardMedia
                  component="img"
                  height="250"
                  image={getCarImage(car)}
                  alt={car.model}
                  sx={{ objectFit: 'cover' }}
                />
                <CardContent>
                  <Typography variant="h6" component="h3" sx={{ fontWeight: 600, mb: 2 }}>
                    {car.model}
                  </Typography>
                  <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                    <Typography variant="body2" color="text.secondary">
                      <strong>Manufacturer:</strong> {car.manufacturer}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      <strong>Year:</strong> {car.modelYear}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      <strong>Color:</strong> {car.color}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      <strong>VIN:</strong> {car.vin}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      <strong>Registration:</strong> {new Date(car.registrationDate).toLocaleDateString()}
                    </Typography>
                  </Box>
                </CardContent>
              </Card>

              <Box>
                <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
                  Purchase Summary
                </Typography>
                
                <Box sx={{ background: '#f8fafc', p: 3, borderRadius: 2, mb: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                    <Typography variant="body1">Car Price:</Typography>
                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                      ${car.value.toLocaleString()}
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
                    <Typography variant="body1">Taxes & Fees:</Typography>
                    <Typography variant="body1" sx={{ fontWeight: 600 }}>
                      ${(car.value * 0.08).toLocaleString()}
                    </Typography>
                  </Box>
                  <Divider sx={{ my: 2 }} />
                  <Box sx={{ display: 'flex', justifyContent: 'space-between' }}>
                    <Typography variant="h6" sx={{ fontWeight: 700 }}>
                      Total:
                    </Typography>
                    <Typography variant="h6" sx={{ fontWeight: 700, color: '#10b981' }}>
                      ${(car.value * 1.08).toLocaleString()}
                    </Typography>
                  </Box>
                </Box>

                <Alert severity="info" sx={{ mb: 3 }}>
                  <Typography variant="body2">
                    This purchase will be processed through our secure system. 
                    You'll receive a confirmation email once the transaction is complete.
                  </Typography>
                </Alert>

                <Button
                  variant="contained"
                  fullWidth
                  onClick={handleNext}
                  sx={{
                    background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                    textTransform: 'none',
                    fontWeight: 600,
                    py: 1.5,
                  }}
                >
                  Continue to Customer Information
                </Button>
              </Box>
            </Box>
          </Paper>
        )}

        {activeStep === 1 && (
          <Paper sx={{ p: 4 }}>
            <Typography variant="h5" component="h2" sx={{ mb: 3, fontWeight: 600 }}>
              Customer Information
            </Typography>
            
            <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: 'repeat(2, 1fr)' }, gap: 3, mb: 4 }}>
              <TextField
                label="CPF (Brazilian ID)"
                value={customerInfo.cpf}
                onChange={(e) => handleCustomerInfoChange('cpf', e.target.value)}
                fullWidth
                required
                placeholder="000.000.000-00"
                helperText="Enter your CPF number"
              />
              <TextField
                label="Full Name"
                value={customerInfo.name}
                onChange={(e) => handleCustomerInfoChange('name', e.target.value)}
                fullWidth
                required
                placeholder="John Doe"
              />
              <TextField
                label="Email"
                type="email"
                value={customerInfo.email}
                onChange={(e) => handleCustomerInfoChange('email', e.target.value)}
                fullWidth
                required
                placeholder="john.doe@example.com"
              />
              <TextField
                label="Phone"
                value={customerInfo.phone}
                onChange={(e) => handleCustomerInfoChange('phone', e.target.value)}
                fullWidth
                placeholder="+55 (11) 99999-9999"
              />
            </Box>

            <Alert severity="info" sx={{ mb: 3 }}>
              <Typography variant="body2">
                Your information will be used to complete the purchase and send you confirmation details.
                We respect your privacy and will not share your information with third parties.
              </Typography>
            </Alert>

            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'space-between' }}>
              <Button
                variant="outlined"
                onClick={handleBack}
                sx={{ textTransform: 'none' }}
              >
                Back
              </Button>
              <Button
                variant="contained"
                onClick={handleNext}
                disabled={!customerInfo.cpf || !customerInfo.name || !customerInfo.email || createSaleMutation.isPending}
                sx={{
                  background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                  textTransform: 'none',
                  fontWeight: 600,
                  minWidth: 200,
                }}
              >
                {createSaleMutation.isPending ? (
                  <>
                    <CircularProgress size={20} sx={{ mr: 1, color: 'white' }} />
                    Processing...
                  </>
                ) : (
                  'Complete Purchase'
                )}
              </Button>
            </Box>
          </Paper>
        )}

        {activeStep === 2 && (
          <Paper sx={{ p: 4, textAlign: 'center' }}>
            <CheckIcon sx={{ fontSize: 64, color: '#10b981', mb: 3 }} />
            <Typography variant="h4" component="h2" sx={{ fontWeight: 700, mb: 2 }}>
              Purchase Successful!
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 4, maxWidth: 500, mx: 'auto' }}>
              Congratulations! Your purchase has been completed successfully. 
              You will receive a confirmation email with all the details shortly.
            </Typography>

            <Box sx={{ background: '#f8fafc', p: 3, borderRadius: 2, mb: 4, textAlign: 'left' }}>
              <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
                Purchase Details
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
                <Typography variant="body2">
                  <strong>Car:</strong> {car.manufacturer} {car.model} ({car.modelYear})
                </Typography>
                <Typography variant="body2">
                  <strong>VIN:</strong> {car.vin}
                </Typography>
                <Typography variant="body2">
                  <strong>Customer:</strong> {customerInfo.name}
                </Typography>
                <Typography variant="body2">
                  <strong>CPF:</strong> {customerInfo.cpf}
                </Typography>
                <Typography variant="body2">
                  <strong>Total Amount:</strong> ${(car.value * 1.08).toLocaleString()}
                </Typography>
              </Box>
            </Box>

            <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
              <Button
                variant="contained"
                onClick={() => navigate('/')}
                sx={{
                  background: 'linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%)',
                  textTransform: 'none',
                  fontWeight: 600,
                }}
              >
                Back to Home
              </Button>
              <Button
                variant="outlined"
                onClick={() => navigate('/cars')}
                sx={{ textTransform: 'none' }}
              >
                Browse More Cars
              </Button>
            </Box>
          </Paper>
        )}
      </Box>
    </Container>
  );
};

export default PurchaseFlow; 