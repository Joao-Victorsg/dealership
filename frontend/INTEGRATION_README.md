# Backend Integration Guide

This document explains how the frontend integrates with the backend APIs.

## API Configuration

### Car API
- **Port**: 8087
- **Context Path**: `/v1/dealership`
- **Base URL**: `http://localhost:8087/v1/dealership`
- **Endpoints**:
  - `GET /cars` - List cars with pagination and filters
  - `GET /cars/{vin}` - Get car by VIN
  - `POST /cars` - Create new car
  - `PUT /cars/{vin}` - Update car
  - `DELETE /cars/{vin}` - Delete car

### Sales API
- **Port**: 8086
- **Context Path**: `/v1/dealership`
- **Base URL**: `http://localhost:8086/v1/dealership`
- **Endpoints**:
  - `POST /sales` - Create new sale
  - `GET /sales` - List sales with pagination and filters
  - `GET /sales/{id}` - Get sale by ID
  - `DELETE /sales/{id}` - Cancel sale

### Client API
- **Port**: 8085 (estimated)
- **Context Path**: `/v1/dealership`
- **Base URL**: `http://localhost:8085/v1/dealership`

## Environment Variables

Create a `.env` file in the frontend directory:

```env
# API Configuration
REACT_APP_CAR_API_URL=http://localhost:8087/v1/dealership
REACT_APP_SALES_API_URL=http://localhost:8086/v1/dealership
REACT_APP_CLIENT_API_URL=http://localhost:8085/v1/dealership

# Development Settings
REACT_APP_ENV=development
```

## Data Flow

### Car Browsing
1. **Client Home** (`/`) - Shows featured cars
2. **Car List** (`/cars`) - Browse all cars with search/filters
3. **Car Detail** (`/cars/:vin`) - View specific car details
4. **Purchase Flow** (`/purchase/:vin`) - Complete purchase

### API Integration Points

#### Car Service (`carService.ts`)
- Fetches car data from Car API
- Handles search, filtering, and pagination
- Manages CRUD operations for cars

#### Sales Service (`salesService.ts`)
- Creates sales records via Sales API
- Handles purchase confirmations
- Manages sale history and cancellations

## Route Structure

### Client Routes (Default)
- `/` - Client home page
- `/cars` - Car browsing
- `/cars/:vin` - Car details
- `/purchase/:vin` - Purchase flow

### Admin Routes
- `/admin` - Admin dashboard
- `/admin/cars` - Car management
- `/admin/cars/new` - Add new car
- `/admin/cars/:vin` - Edit car
- `/admin/cars/:vin/edit` - Edit car form

## Integration Checklist

### ✅ Completed
- [x] Car API integration with correct ports
- [x] Sales API integration for purchase flow
- [x] TypeScript types matching API responses
- [x] Route restructuring (client as default)
- [x] Navigation updates

### 🔄 In Progress
- [ ] Client API integration (when available)
- [ ] Error handling improvements
- [ ] Loading states optimization

### 📋 TODO
- [ ] Authentication integration
- [ ] Real car images integration
- [ ] Payment gateway integration
- [ ] Email notifications
- [ ] Wishlist functionality

## Testing the Integration

1. **Start Backend APIs**:
   ```bash
   # Start Car API (port 8087)
   cd car-api && mvn spring-boot:run
   
   # Start Sales API (port 8086)
   cd sales-api && mvn spring-boot:run
   
   # Start Client API (port 8085)
   cd client-api && mvn spring-boot:run
   ```

2. **Start Frontend**:
   ```bash
   cd frontend && npm start
   ```

3. **Test URLs**:
   - Client Interface: `http://localhost:3000/`
   - Admin Interface: `http://localhost:3000/admin`
   - Car Browsing: `http://localhost:3000/cars`
   - Purchase Flow: `http://localhost:3000/purchase/{vin}`

## Troubleshooting

### Common Issues

1. **CORS Errors**: Ensure backend APIs allow requests from `http://localhost:3000`
2. **Port Conflicts**: Verify APIs are running on correct ports (8085, 8086, 8087)
3. **API Timeouts**: Check if LocalStack and databases are running
4. **Data Not Loading**: Verify API endpoints are accessible and returning data

### Debug Steps

1. Check browser console for errors
2. Verify API endpoints in Network tab
3. Test API endpoints directly (e.g., `http://localhost:8087/v1/dealership/cars`)
4. Check backend logs for errors

## API Response Format

### Car API Response
```json
{
  "data": {
    "content": [
      {
        "id": "uuid",
        "model": "Model 3",
        "modelYear": "2023",
        "manufacturer": "Tesla",
        "color": "White",
        "vin": "VIN123456789",
        "value": 45000.00,
        "registrationDate": "2023-01-01T00:00:00"
      }
    ],
    "totalElements": 10,
    "totalPages": 1,
    "size": 10,
    "number": 0
  },
  "message": "Success",
  "timestamp": "2024-01-01T00:00:00"
}
```

### Sales API Response
```json
{
  "data": {
    "id": "uuid",
    "cpf": "123.456.789-00",
    "vin": "VIN123456789",
    "registrationDate": "2024-01-01T00:00:00"
  },
  "message": "Sale created successfully",
  "timestamp": "2024-01-01T00:00:00"
}
``` 