# Dealership Frontend

A modern React TypeScript frontend for the Dealership Management System. This application provides a user-friendly interface for managing cars, clients, and sales operations.

## Features

### 🚗 Car Management
- **Browse Cars**: View all vehicles in the inventory with search and filter capabilities
- **Add Cars**: Create new car entries with detailed information
- **Edit Cars**: Update existing car information
- **Delete Cars**: Remove cars from the inventory
- **Car Details**: View comprehensive information about each vehicle

### 🎨 Modern UI/UX
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile devices
- **Material-UI**: Professional and modern interface using Material Design principles
- **Intuitive Navigation**: Easy-to-use navigation with breadcrumbs and clear actions
- **Search & Filter**: Advanced search and filtering capabilities for finding specific vehicles

### 🔧 Technical Features
- **TypeScript**: Full type safety for better development experience
- **React Query**: Efficient data fetching and caching
- **React Router**: Client-side routing for smooth navigation
- **Axios**: HTTP client for API communication
- **Form Validation**: Comprehensive form validation with error handling

## Getting Started

### Prerequisites
- Node.js (v18 or higher)
- npm or yarn
- Backend APIs running (car-api, client-api, sales-api)

### Installation

1. **Install dependencies**:
   ```bash
   npm install
   ```

2. **Configure API endpoints**:
   Create a `.env` file in the frontend directory:
   ```env
   REACT_APP_API_URL=http://localhost:8080/v1/dealership
   ```

3. **Start the development server**:
   ```bash
   npm start
   ```

4. **Open your browser**:
   Navigate to `http://localhost:3000`

## Project Structure

```
frontend/
├── src/
│   ├── components/          # Reusable UI components
│   │   └── Layout.tsx       # Main layout with navigation
│   │   └── CarList.tsx     # Car inventory list
│   │   └── CarForm.tsx     # Add/Edit car form
│   │   └── CarDetail.tsx   # Car details view
│   ├── services/           # API services
│   │   └── carService.ts   # Car API integration
│   ├── types/              # TypeScript type definitions
│   │   └── car.ts          # Car-related types
│   ├── utils/              # Utility functions
│   ├── App.tsx             # Main application component
│   └── index.tsx           # Application entry point
├── public/                 # Static assets
└── package.json            # Dependencies and scripts
```

## Available Scripts

- `npm start` - Start the development server
- `npm build` - Build the application for production
- `npm test` - Run tests
- `npm eject` - Eject from Create React App (not recommended)

## API Integration

The frontend integrates with the following backend APIs:

### Car API (`/cars`)
- `GET /cars` - Get all cars with pagination and filters
- `GET /cars/{vin}` - Get car by VIN
- `POST /cars` - Create new car
- `PUT /cars/{vin}` - Update car
- `DELETE /cars/{vin}` - Delete car

### Future Integrations
- **Client API**: Customer management functionality
- **Sales API**: Sales transaction management

## Usage

### For Customers
1. **Browse Cars**: Visit the Cars page to view available vehicles
2. **Search & Filter**: Use the search bar and filters to find specific cars
3. **View Details**: Click on any car to see detailed information
4. **Contact**: Use the provided contact information to inquire about vehicles

### For Dealership Employees
1. **Manage Inventory**: Add, edit, and delete cars from the inventory
2. **Update Information**: Modify car details, prices, and specifications
3. **Track Sales**: Monitor sales activities and customer interactions
4. **Generate Reports**: Access comprehensive reports and analytics

## Development

### Adding New Features
1. Create new components in the `components/` directory
2. Add new pages in the `pages/` directory
3. Update routing in `App.tsx`
4. Add TypeScript types in the `types/` directory
5. Create API services in the `services/` directory

### Styling
The application uses Material-UI (MUI) for styling. Custom styles can be added using:
- MUI's `sx` prop for component-specific styles
- CSS modules for complex styling
- Theme customization in `App.tsx`

### State Management
- **React Query**: For server state management
- **React State**: For local component state
- **URL State**: For navigation and filters

## Deployment

### Production Build
```bash
npm run build
```

### Environment Variables
- `REACT_APP_API_URL`: Backend API base URL
- `REACT_APP_ENVIRONMENT`: Environment (development/production)

## Contributing

1. Follow the existing code structure and patterns
2. Use TypeScript for all new code
3. Add proper error handling and loading states
4. Test your changes thoroughly
5. Update documentation as needed

## Troubleshooting

### Common Issues
1. **API Connection Errors**: Ensure the backend services are running
2. **CORS Issues**: Check backend CORS configuration
3. **TypeScript Errors**: Verify type definitions match API responses
4. **Build Errors**: Clear node_modules and reinstall dependencies

### Getting Help
- Check the browser console for error messages
- Verify API endpoints are accessible
- Ensure all dependencies are installed correctly

## License

This project is part of the Dealership Management System and follows the same licensing terms.
