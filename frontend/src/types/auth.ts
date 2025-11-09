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

export interface DecodedToken {
  exp: number;
  iat: number;
  sub: string;
  email?: string;
  preferred_username?: string;
  given_name?: string;
  family_name?: string;
  name?: string;
  realm_access?: {
    roles: string[];
  };
}

export interface UserInfo {
  id: string;
  email: string;
  username: string;
  firstName?: string;
  lastName?: string;
  name?: string;
  roles: string[];
}

export interface RegisterUserData {
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  password: string;
}

export interface LoginCredentials {
  username: string;
  password: string;
}

export interface PasswordResetRequest {
  email: string;
}
