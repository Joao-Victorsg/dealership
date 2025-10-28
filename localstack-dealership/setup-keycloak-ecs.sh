#!/bin/bash

# Keycloak ECS Setup Script
# This script configures Keycloak realm, clients, roles, and users after ECS deployment

set -e

echo "🔐 Starting Keycloak ECS configuration..."

# Configuration
KEYCLOAK_URL="http://localhost:8084"
KEYCLOAK_URL_HEALTH="http://localhost:9000"
KEYCLOAK_ADMIN_USERNAME="admin"
KEYCLOAK_ADMIN_PASSWORD="admin123"
REALM_NAME="dealership-realm"
FRONTEND_CLIENT_ID="dealership-frontend"
ACCOUNT_API_CLIENT_ID="account-api"
CLIENT_API_CLIENT_ID="client-api"
CAR_API_CLIENT_ID="car-api"
SALES_API_CLIENT_ID="sales-api"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to log messages
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1"
}

log_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

log_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

log_error() {
    echo -e "${RED}❌ $1${NC}"
}

# Function to wait for Keycloak to be ready
wait_for_keycloak() {
    log "Waiting for Keycloak to be ready..."
    local max_attempts=30
    local attempt=1
    
    while [ $attempt -le $max_attempts ]; do
        if curl -f -s "${KEYCLOAK_URL_HEALTH}/health" > /dev/null 2>&1; then
            log_success "Keycloak is ready!"
            return 0
        fi
        
        log "Attempt $attempt/$max_attempts: Keycloak not ready yet, waiting 10 seconds..."
        sleep 10
        ((attempt++))
    done
    
    log_error "Keycloak failed to start within expected time"
    return 1
}

# Function to get admin token
get_admin_token() {
    # Only log errors, not info, to avoid polluting stdout
    local token_response=$(curl -s -X POST "${KEYCLOAK_URL}/realms/master/protocol/openid-connect/token" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        --data-urlencode "username=${KEYCLOAK_ADMIN_USERNAME}" \
        --data-urlencode "password=${KEYCLOAK_ADMIN_PASSWORD}" \
        --data-urlencode "grant_type=password" \
        --data-urlencode "client_id=admin-cli")
    
    if echo "$token_response" | jq -e '.access_token' > /dev/null 2>&1; then
        local token=$(echo "$token_response" | jq -r '.access_token')
        echo "$token"
    else
        log_error "Failed to get admin token: $token_response" >&2
        return 1
    fi
}

# Function to set email for admin user in master realm
set_admin_email() {
    local token=$1

    log "Setting email for admin user in master realm..."

    # Get admin user ID
    local admin_user_response=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/master/users?username=${KEYCLOAK_ADMIN_USERNAME}" \
        -H "Authorization: Bearer $token")

    local admin_user_id=$(echo "$admin_user_response" | jq -r '.[0].id')

    if [ "$admin_user_id" != "null" ] && [ -n "$admin_user_id" ]; then
        local user_update=$(cat <<EOF
{
    "email": "admin@dealership.com",
    "emailVerified": true
}
EOF
)

        local response=$(curl -s -w "\n%{http_code}" -X PUT "${KEYCLOAK_URL}/admin/realms/master/users/${admin_user_id}" \
            -H "Authorization: Bearer $token" \
            -H "Content-Type: application/json" \
            -d "$user_update")

        local http_code=$(echo "$response" | tail -n1)

        if [ "$http_code" = "204" ]; then
            log_success "Admin user email set to admin@dealership.com"
        else
            log_warning "Failed to set admin email. HTTP Code: $http_code"
        fi
    else
        log_warning "Could not find admin user in master realm"
    fi
}

# Function to create realm
create_realm() {
    local token=$1
    
    log "Creating realm: $REALM_NAME"
    
    local realm_data=$(cat <<EOF
{
    "realm": "$REALM_NAME",
    "enabled": true,
    "smtpServer": {
        "host": "host.docker.internal",
        "port": "25",
        "from": "noreply@example.com",
        "fromDisplayName": "Dealership Platform",
        "auth": false,
        "ssl": false,
        "starttls": false
    }
}
EOF
)
    
    log "Realm data: $realm_data"
    log "Token (first 50 chars): ${token:0:50}..."
    
    # Create realm and capture response (no verbose)
    local response=$(curl -s -w "\n%{http_code}" -X POST "${KEYCLOAK_URL}/admin/realms" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d "$realm_data")
    
    # Extract HTTP code (last line) and response body (all but last line)
    local http_code=$(echo "$response" | tail -n1)
    local response_body=$(echo "$response" | head -n -1)
    
    log "HTTP Code: $http_code"
    log "Full Response: $response_body"
    
    if [ "$http_code" = "201" ] || [ "$http_code" = "409" ]; then
        if [ "$http_code" = "201" ]; then
            log_success "Realm created successfully"
        else
            log_success "Realm already exists, updating SMTP configuration..."
            update_smtp_config "$token"
        fi
    else
        log_error "Failed to create realm. HTTP Code: $http_code, Response: $response_body"
        return 1
    fi
}

# Function to update SMTP configuration for existing realm
update_smtp_config() {
    local token=$1

    log "Updating SMTP configuration for realm: $REALM_NAME"

    local smtp_config=$(cat <<EOF
{
    "smtpServer": {
        "host": "host.docker.internal",
        "port": "25",
        "from": "noreply@example.com",
        "fromDisplayName": "Dealership Platform",
        "auth": "false",
        "ssl": "false",
        "starttls": "false"
    }
}
EOF
)

    local response=$(curl -s -w "\n%{http_code}" -X PUT "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d "$smtp_config")

    local http_code=$(echo "$response" | tail -n1)
    local response_body=$(echo "$response" | head -n -1)

    if [ "$http_code" = "204" ]; then
        log_success "SMTP configuration updated successfully"
    else
        log_error "Failed to update SMTP configuration. HTTP Code: $http_code, Response: $response_body"
        return 1
    fi
}

# Function to create client
create_client() {
    local token=$1
    local client_id=$2
    local client_secret=$3
    local redirect_uris=$4
    local public_client=$5
    
    log "Creating client: $client_id"

    # Build client data with optional secret field
    local secret_field=""
    if [ "$client_secret" != "" ]; then
        secret_field=",\"secret\": \"$client_secret\""
    fi

    local client_data=$(cat <<EOF
{
    "clientId": "$client_id"${secret_field},
    "enabled": true,
    "publicClient": $public_client,
    "redirectUris": $redirect_uris,
    "webOrigins": ["*"],
    "standardFlowEnabled": true,
    "directAccessGrantsEnabled": true,
    "serviceAccountsEnabled": true
}
EOF
)
    
    local response=$(curl -s -w "\n%{http_code}" -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d "$client_data")
    
    # Extract HTTP code (last line) and response body (all but last line)
    local http_code=$(echo "$response" | tail -n1)
    local response_body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" = "201" ]; then
        log_success "Client $client_id created successfully"
    elif [ "$http_code" = "409" ]; then
        log_success "Client $client_id already exists"

        # Update secret for existing client
        if [ "$client_secret" != "" ]; then
            log "Updating client secret for existing client: $client_id"
            local client_response=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients?clientId=${client_id}" \
                -H "Authorization: Bearer $token")

            local internal_client_id=$(echo "$client_response" | jq -r '.[0].id')

            if [ "$internal_client_id" != "null" ]; then
                curl -s -X PUT "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients/${internal_client_id}/client-secret" \
                    -H "Authorization: Bearer $token" \
                    -H "Content-Type: application/json" \
                    -d "{\"type\":\"secret\",\"value\":\"$client_secret\"}" > /dev/null
                log_success "Client secret updated for $client_id"
            fi
        fi
    else
        log_error "Failed to create client $client_id. HTTP Code: $http_code, Response: $response_body"
        return 1
    fi
}

# Function to create role
create_role() {
    local token=$1
    local role_name=$2
    local role_description=$3
    
    log "Creating role: $role_name"
    
    local role_data=$(cat <<EOF
{
    "name": "$role_name",
    "description": "$role_description",
    "composite": false,
    "clientRole": false
}
EOF
)
    
    local response=$(curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d "$role_data")
    
    if [ $? -eq 0 ]; then
        log_success "Role $role_name created successfully"
    else
        log_warning "Role $role_name might already exist or failed to create: $response"
    fi
}

# Function to create user
create_user() {
    local token=$1
    local username=$2
    local email=$3
    local password=$4
    local roles=$5
    
    log "Creating user: $username"
    
    local user_data=$(cat <<EOF
{
    "username": "$username",
    "email": "$email",
    "enabled": true,
    "emailVerified": true,
    "credentials": [
        {
            "type": "password",
            "value": "$password",
            "temporary": false
        }
    ]
}
EOF
)
    
    local response=$(curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d "$user_data")
    
    if [ $? -eq 0 ]; then
        log_success "User $username created successfully"
        
        # Get user ID for role assignment
        local user_response=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users?username=${username}" \
            -H "Authorization: Bearer $token")
        
        local user_id=$(echo "$user_response" | jq -r '.[0].id')
        
        if [ "$roles" != "" ] && [ "$user_id" != "null" ]; then
            log "Assigning roles to user $username"
            for role in $roles; do
                local role_response=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/roles?search=${role}" \
                    -H "Authorization: Bearer $token")
                
                local role_id=$(echo "$role_response" | jq -r '.[0].id')
                
                if [ "$role_id" != "null" ]; then
                    curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users/${user_id}/role-mappings/realm" \
                        -H "Authorization: Bearer $token" \
                        -H "Content-Type: application/json" \
                        -d "[{\"id\":\"$role_id\",\"name\":\"$role\"}]" > /dev/null
                    log_success "Role $role assigned to user $username"
                fi
            done
        fi
    else
        log_warning "User $username might already exist or failed to create: $response"
    fi
}

# Function to assign manage-users role to a client's service account
assign_manage_users_role() {
    local token=$1
    local client_id=$2

    log "Assigning manage-users role to $client_id service account..."

    # Get client UUID
    local client_uuid=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients?clientId=${client_id}" \
        -H "Authorization: Bearer $token" | jq -r '.[0].id')

    # Get service account user ID
    local service_account_id=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients/${client_uuid}/service-account-user" \
        -H "Authorization: Bearer $token" | jq -r '.id')

    # Get realm-management client UUID
    local realm_mgmt_uuid=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients?clientId=realm-management" \
        -H "Authorization: Bearer $token" | jq -r '.[0].id')

    # Get manage-users role
    local manage_users_role=$(curl -s -X GET "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/clients/${realm_mgmt_uuid}/roles/manage-users" \
        -H "Authorization: Bearer $token" | jq -r '{id: .id, name: .name}')

    # Assign role to service account
    curl -s -X POST "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}/users/${service_account_id}/role-mappings/clients/${realm_mgmt_uuid}" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d "[$manage_users_role]" > /dev/null

    log_success "manage-users role assigned to $client_id"
}

# Function to enable user registration
enable_registration() {
    local token=$1

    log "Enabling user registration for realm: $REALM_NAME"
    
    local realm_settings=$(cat <<EOF
{
    "enabled": true,
    "registrationAllowed": true,
    "registrationEmailAsUsername": false,
    "editUsernameAllowed": true,
    "resetPasswordAllowed": true,
    "rememberMe": true,
    "verifyEmail": true,
    "loginWithEmailAllowed": true,
    "duplicateEmailsAllowed": false
}
EOF
)
    
    local response=$(curl -s -w "\n%{http_code}" -X PUT "${KEYCLOAK_URL}/admin/realms/${REALM_NAME}" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d "$realm_settings")
    
    local http_code=$(echo "$response" | tail -n1)
    local response_body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" = "204" ]; then
        log_success "User registration enabled successfully"
    else
        log_warning "Failed to enable registration. HTTP Code: $http_code, Response: $response_body"
    fi
}

# Main execution
main() {
    log "Starting Keycloak ECS configuration..."
    
    # Wait for Keycloak to be ready
    if ! wait_for_keycloak; then
        log_error "Keycloak is not available. Exiting."
        exit 1
    fi
    
    # Get admin token
    log "Getting admin token..."
    local admin_token=$(get_admin_token)
    if [ $? -ne 0 ] || [ -z "$admin_token" ]; then
        log_error "Failed to get admin token. Exiting."
        exit 1
    fi
    log_success "Admin token obtained successfully"

    # Set email for admin user in master realm
    set_admin_email "$admin_token"

    # Create realm
    create_realm "$admin_token"
    
    # Create clients
    create_client "$admin_token" "$FRONTEND_CLIENT_ID" "" '["http://localhost:3000/*", "http://localhost:3000"]' "true"
    create_client "$admin_token" "$ACCOUNT_API_CLIENT_ID" "account-api-secret" '["http://localhost:8083/*", "http://localhost:8083/callback"]' "false"
    create_client "$admin_token" "$CLIENT_API_CLIENT_ID" "client-secret" '["http://localhost:8085/*"]' "false"
    create_client "$admin_token" "$CAR_API_CLIENT_ID" "car-api-secret" '["http://localhost:8087/*"]' "false"
    create_client "$admin_token" "$SALES_API_CLIENT_ID" "sales-api-secret" '["http://localhost:8086/*"]' "false"

    # Assign manage-users role to client-api service account
    log "Assigning manage-users role to client-api service account..."
    assign_manage_users_role "$admin_token" "$CLIENT_API_CLIENT_ID"
    
    # Create roles
    create_role "$admin_token" "client" "Regular client user"
    create_role "$admin_token" "admin" "Administrator user"
    create_role "$admin_token" "staff" "Staff member"
    
    # Create users
    create_user "$admin_token" "admin@dealership.com" "admin@dealership.com" "admin123" "admin"
    create_user "$admin_token" "john.doe@example.com" "john.doe@example.com" "password123" "client"
    create_user "$admin_token" "jane.smith@dealership.com" "jane.smith@dealership.com" "staff123" "staff"
    
    # Enable user registration
    enable_registration "$admin_token"
    
    log_success "Keycloak ECS configuration completed successfully!"
    
    echo ""
    echo "📋 Configuration Summary:"
    echo "  ✅ Realm: $REALM_NAME"
    echo "  ✅ Frontend Client: $FRONTEND_CLIENT_ID"
    echo "  ✅ Account API Client: $ACCOUNT_API_CLIENT_ID"
    echo "  ✅ Client API Client: $CLIENT_API_CLIENT_ID"
    echo "  ✅ Car API Client: $CAR_API_CLIENT_ID"
    echo "  ✅ Sales API Client: $SALES_API_CLIENT_ID"
    echo "  ✅ Roles: client, admin, staff"
    echo "  ✅ Users: admin@dealership.com, john.doe@example.com, jane.smith@dealership.com"
    echo ""
    echo "🌐 Access Points:"
    echo "  • Keycloak Admin Console: $KEYCLOAK_URL"
    echo "  • Admin Credentials: $KEYCLOAK_ADMIN_USERNAME / $KEYCLOAK_ADMIN_PASSWORD"
    echo ""
    echo "👤 Sample Users:"
    echo "  • Admin: admin@dealership.com / admin123"
    echo "  • Client: john.doe@example.com / password123"
    echo "  • Staff: jane.smith@dealership.com / staff123"
}

# Run main function
main "$@" 