# Authentication System Documentation

## Overview

The Dealership Frontend implements a secure authentication system using Keycloak as the identity provider. This document describes the authentication architecture, setup, and usage.

## Architecture

### Components

1. **Token Storage Strategy**
   - **Access Token**: Stored in memory (React state) for security against XSS
   - **Refresh Token**: Stored in localStorage for session persistence
   - This approach allows users to remain logged in across page refreshes while minimizing the exposure of the short-lived access token

2. **Authentication Service** (`services/authService.ts`)
   - Handles direct communication with Keycloak OpenID Connect endpoints
   - Manages login, logout, token refresh, and registration
   - Provides user information extraction from JWT tokens

3. **API Client** (`services/apiClient.ts`)
   - Centralized Axios instance with authentication interceptors
   - Automatically adds Authorization header to requests
   - Handles 401 errors by refreshing tokens and retrying requests
   - Prevents multiple simultaneous token refresh attempts

4. **Auth Context** (`contexts/AuthContext.tsx`)
   - React Context for global authentication state management
   - Provides authentication status, user info, and auth methods
   - Automatically restores sessions on page load
   - Custom `useAuth()` hook for easy access to auth state

5. **Protected Route Component** (`components/ProtectedRoute.tsx`)
   - Wraps routes that require authentication
   - Redirects unauthenticated users to login with return URL
   - Supports role-based access control
   - Shows loading spinner during authentication check

## Setup Instructions

### 1. Environment Configuration

Create a `.env` file in the frontend directory:

```env
# API Endpoints
REACT_APP_CAR_API_URL=http://localhost:8087/v1/dealership
REACT_APP_SALES_API_URL=http://localhost:8086/v1/dealership
REACT_APP_CLIENT_API_URL=http://localhost:8085/v1/dealership

# Keycloak Configuration
REACT_APP_KEYCLOAK_URL=http://localhost:8084
REACT_APP_KEYCLOAK_REALM=dealership-realm
REACT_APP_KEYCLOAK_CLIENT_ID=dealership-frontend
```

### 2. Install Dependencies

```bash
npm install --legacy-peer-deps
```

Required packages:
- `axios` - HTTP client
- `jwt-decode` - JWT token decoding
- `react-router-dom` - Routing
- `@mui/material` - UI components

### 3. Start the Application

```bash
npm start
```

The application will run on `http://localhost:3000`

## Keycloak Configuration

### Test Users

The system comes pre-configured with test users:

| Username/Email | Password | Role | Description |
|----------------|----------|------|-------------|
| admin@dealership.com | admin123 | admin | Administrator with full access |
| jane.smith@dealership.com | staff123 | staff | Staff member with admin access |
| john.doe@example.com | password123 | client | Regular client user |

### Roles

- **admin**: Full system access (admin panel + client features)
- **staff**: Full system access (admin panel + client features)
- **client**: Access to client-facing features only

## User Flows

### 1. Login Flow

1. User navigates to `/login` or is redirected when accessing a protected route
2. User enters username/email and password
3. Frontend calls Keycloak token endpoint via `authService.login()`
4. Keycloak validates credentials and returns tokens
5. Tokens are stored (access token in memory, refresh token in localStorage)
6. User info is extracted from access token
7. User is redirected to intended page or home

### 2. Registration Flow

1. User navigates to `/register`
2. User fills out registration form (username, email, first name, last name, password)
3. Frontend calls `authService.register()`
4. On success, user is redirected to login page
5. User can now log in with their credentials

**Note**: Direct registration through Keycloak API may require additional backend configuration. The current implementation is prepared for future integration.

### 3. Logout Flow

1. User clicks logout from the user menu
2. Frontend calls `authService.logout()`
3. Refresh token is sent to Keycloak logout endpoint
4. Keycloak revokes the tokens
5. All tokens are cleared from storage
6. User is redirected to login page

### 4. Session Persistence

1. User logs in successfully
2. Refresh token is saved to localStorage
3. User closes/refreshes the browser
4. On app load, AuthContext checks for tokens:
   - If access token exists and is valid: restore user session
   - If access token is expired but refresh token is valid: refresh tokens and restore session
   - If no valid tokens: user must log in again

### 5. Token Refresh Flow

**Automatic Token Refresh** (handled by API interceptor):

1. User makes an API request
2. API interceptor adds access token to request
3. If API returns 401 (unauthorized):
   - Interceptor calls `authService.refreshToken()`
   - New tokens are obtained using refresh token
   - Original request is retried with new access token
   - User continues working without interruption
4. If refresh fails:
   - All tokens are cleared
   - User is redirected to login page

### 6. Password Reset Flow

1. User clicks "Forgot password?" on login page
2. User navigates to `/password-reset`
3. User enters email address
4. Frontend calls `authService.requestPasswordReset()`
5. Email with reset instructions is sent (Lambda implementation pending)

**Note**: The password reset endpoint `/api/auth/password-reset` will be implemented with AWS Lambda in a future phase.

## Protected Routes

### Client Routes (Requires Authentication)

- `/` - Client home page
- `/cars` - Browse cars
- `/cars/:vin` - Car details
- `/purchase/:vin` - Purchase flow

### Admin Routes (Requires `admin` or `staff` role)

- `/admin` - Admin dashboard
- `/admin/cars` - Car management
- `/admin/cars/new` - Add new car
- `/admin/cars/:vin` - Car details (admin view)
- `/admin/cars/:vin/edit` - Edit car
- `/admin/clients` - Client management
- `/admin/sales` - Sales management

### Public Routes (No Authentication Required)

- `/login` - Login page
- `/register` - Registration page
- `/password-reset` - Password reset request

## Security Considerations

### Token Storage

- **Access Token**: Stored in memory (React state)
  - ✅ Protected from XSS attacks
  - ✅ Automatically cleared on page close
  - ❌ Lost on page refresh (by design)

- **Refresh Token**: Stored in localStorage
  - ✅ Persists across page refreshes
  - ⚠️ Vulnerable to XSS if application has XSS vulnerabilities
  - ⚠️ Available to JavaScript

**Future Improvement**: Consider using httpOnly cookies for refresh tokens once backend support is added. This would require:
- Backend API Gateway to handle token cookies
- SameSite cookie configuration
- CSRF protection

### Token Expiration

- Access tokens have a short lifespan (typically 5-15 minutes)
- Refresh tokens have a longer lifespan (typically hours to days)
- Tokens are automatically refreshed before expiration (30-second buffer)
- Users are logged out if refresh token expires

### API Security

- All API requests include the access token in the Authorization header
- Expired tokens automatically trigger refresh and retry
- 401 responses are handled gracefully with automatic logout if refresh fails

### XSS Protection

- Access tokens in memory reduce XSS attack surface
- Refresh tokens in localStorage require XSS vulnerability to be exploited
- Always keep dependencies updated
- Use Content Security Policy (CSP) headers
- Validate and sanitize all user inputs

## Development

### Adding New Protected Routes

```typescript
<Route 
  path="/new-feature" 
  element={
    <ProtectedRoute>
      <YourComponent />
    </ProtectedRoute>
  } 
/>
```

### Adding Role-Based Routes

```typescript
<Route 
  path="/admin-feature" 
  element={
    <ProtectedRoute roles={['admin', 'staff']}>
      <YourComponent />
    </ProtectedRoute>
  } 
/>
```

### Using Auth in Components

```typescript
import { useAuth } from '../contexts/AuthContext';

function MyComponent() {
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <div>
      <p>Welcome, {user?.firstName}!</p>
      <p>Email: {user?.email}</p>
      <p>Roles: {user?.roles.join(', ')}</p>
      <button onClick={logout}>Logout</button>
    </div>
  );
}
```

### Making Authenticated API Calls

All services should use the centralized API clients:

```typescript
import { carApiClient } from './apiClient';

// API calls automatically include auth token
const response = await carApiClient.get('/cars');
```

## Troubleshooting

### "Network error during login"

- Ensure Keycloak is running on the configured port (default: 8084)
- Check REACT_APP_KEYCLOAK_URL in .env file
- Verify LocalStack and Keycloak ECS container are running

### "Token refresh failed"

- Check if refresh token is expired
- Verify Keycloak is accessible
- Check browser console for detailed error messages

### "Unauthorized" errors on API calls

- Ensure backend APIs are configured to validate Keycloak tokens
- Check if API Gateway has the Lambda Authorizer configured
- Verify the client ID matches between frontend and backend

### Session not persisting across refreshes

- Check browser localStorage for 'dealership_refresh_token'
- Verify refresh token hasn't expired
- Check browser console for token refresh errors

## Testing

### Manual Testing Checklist

- [ ] Login with valid credentials
- [ ] Login with invalid credentials (should show error)
- [ ] Logout (should clear session and redirect)
- [ ] Page refresh while logged in (should restore session)
- [ ] Access protected route while not logged in (should redirect to login)
- [ ] Access admin route as client user (should redirect to home)
- [ ] Access admin route as admin user (should allow access)
- [ ] Register new account
- [ ] Token auto-refresh (wait for token to expire, make API call)
- [ ] Password reset request

### Test Users for Different Scenarios

```typescript
// Test admin access
Email: admin@dealership.com
Password: admin123

// Test staff access
Email: jane.smith@dealership.com
Password: staff123

// Test client access (limited permissions)
Email: john.doe@example.com
Password: password123
```

## Future Enhancements

1. **HttpOnly Cookie Storage**
   - Move refresh token to httpOnly cookies
   - Implement backend cookie handling
   - Add CSRF protection

2. **Multi-Factor Authentication**
   - Enable MFA in Keycloak
   - Add MFA UI flows

3. **Social Login**
   - Configure Google/Facebook identity providers in Keycloak
   - Add social login buttons to login page

4. **Remember Me**
   - Implement longer-lived refresh tokens for "remember me" option
   - Add checkbox to login form

5. **Account Management**
   - Profile editing
   - Password change
   - Email verification
   - Account deletion

## References

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [OpenID Connect Specification](https://openid.net/connect/)
- [JWT.io](https://jwt.io/) - JWT token decoder
- [OWASP Authentication Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Authentication_Cheat_Sheet.html)
