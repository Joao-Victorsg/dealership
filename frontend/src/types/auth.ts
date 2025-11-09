// Keycloak token response from the OpenID Connect token endpoint
export interface KeycloakTokenResponse {
  access_token: string;
  expires_in: number;
  refresh_expires_in: number;
  refresh_token: string;
  token_type: string;
  'not-before-policy'?: number;
  session_state?: string;
  scope: string;
}

// Decoded JWT token structure
export interface DecodedToken {
  exp: number;
  iat: number;
  sub: string;
  email?: string;
  preferred_username?: string;
  given_name?: string;
  family_name?: string;
  realm_access?: {
    roles: string[];
  };
  email_verified?: boolean;
}

// User information extracted from token
export interface UserInfo {
  id: string;
  email: string;
  username: string;
  firstName?: string;
  lastName?: string;
  roles: string[];
  emailVerified?: boolean;
}

// Registration data
export interface RegisterUserData {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  password: string;
}

// Login credentials
export interface LoginCredentials {
  username: string;
  password: string;
}

// Auth error response
export interface AuthError {
  error: string;
  error_description?: string;
  message?: string;
}
