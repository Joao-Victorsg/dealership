#!/bin/bash

# Keycloak Database Initialization Script
# This script creates the Keycloak database in LocalStack RDS

set -e

# Load environment variables if .env file exists
if [ -f .env ]; then
  echo "📄 Loading environment variables from .env file..."
  export $(cat .env | grep -v '^#' | xargs)
fi

# Set default values if not provided
KEYCLOAK_DB_HOST=${KEYCLOAK_DB_HOST:-localhost}
KEYCLOAK_DB_PORT=${KEYCLOAK_DB_PORT:-4513}
KEYCLOAK_DB_NAME=${KEYCLOAK_DB_NAME:-keycloak-dealership}
KEYCLOAK_DB_USERNAME=${KEYCLOAK_DB_USERNAME:-postgres}
KEYCLOAK_DB_PASSWORD=${KEYCLOAK_DB_PASSWORD:-password}

echo "🚀 Initializing Keycloak database in LocalStack RDS..."
echo "📊 Database Configuration:"
echo "  • Host: $KEYCLOAK_DB_HOST"
echo "  • Port: $KEYCLOAK_DB_PORT"
echo "  • Database: $KEYCLOAK_DB_NAME"
echo "  • Username: $KEYCLOAK_DB_USERNAME"

# Check if psql is available
if ! command -v psql &> /dev/null; then
  echo "❌ psql is not installed. Please install PostgreSQL client tools."
  echo "   On Ubuntu/Debian: sudo apt-get install postgresql-client"
  echo "   On macOS: brew install postgresql"
  echo "   On Windows: Download from https://www.postgresql.org/download/windows/"
  exit 1
fi

# Wait for LocalStack RDS to be ready
echo "⏳ Waiting for LocalStack RDS to be ready..."
until PGPASSWORD=$KEYCLOAK_DB_PASSWORD psql -h $KEYCLOAK_DB_HOST -p $KEYCLOAK_DB_PORT -U $KEYCLOAK_DB_USERNAME -d postgres -c "SELECT 1;" > /dev/null 2>&1; do
  echo "LocalStack RDS is not ready yet, waiting..."
  sleep 5
done

echo "✅ LocalStack RDS is ready!"

# Check if database already exists
echo "🔍 Checking if database '$KEYCLOAK_DB_NAME' exists..."
DB_EXISTS=$(PGPASSWORD=$KEYCLOAK_DB_PASSWORD psql -h $KEYCLOAK_DB_HOST -p $KEYCLOAK_DB_PORT -U $KEYCLOAK_DB_USERNAME -d postgres -tAc "SELECT 1 FROM pg_database WHERE datname='$KEYCLOAK_DB_NAME'")

if [ "$DB_EXISTS" = "1" ]; then
  echo "✅ Database '$KEYCLOAK_DB_NAME' already exists"
else
  echo "🏗️ Creating database '$KEYCLOAK_DB_NAME'..."
  PGPASSWORD=$KEYCLOAK_DB_PASSWORD psql -h $KEYCLOAK_DB_HOST -p $KEYCLOAK_DB_PORT -U $KEYCLOAK_DB_USERNAME -d postgres -c "CREATE DATABASE \"$KEYCLOAK_DB_NAME\";"
  echo "✅ Database '$KEYCLOAK_DB_NAME' created successfully"
fi

# Test connection to the new database
echo "🔍 Testing connection to '$KEYCLOAK_DB_NAME'..."
PGPASSWORD=$KEYCLOAK_DB_PASSWORD psql -h $KEYCLOAK_DB_HOST -p $KEYCLOAK_DB_PORT -U $KEYCLOAK_DB_USERNAME -d $KEYCLOAK_DB_NAME -c "SELECT version();" > /dev/null 2>&1

echo "✅ Database connection test successful"
echo ""
echo "🎉 Keycloak database initialization completed!"
echo ""
echo "📋 Database Details:"
echo "  • Database Name: $KEYCLOAK_DB_NAME"
echo "  • Host: $KEYCLOAK_DB_HOST"
echo "  • Port: $KEYCLOAK_DB_PORT"
echo "  • Username: $KEYCLOAK_DB_USERNAME"
echo ""
echo "🔧 Next Steps:"
echo "  1. Start Keycloak: docker-compose up keycloak"
echo "  2. Run setup script: ./setup-keycloak.sh"
echo "  3. Test the authentication flow" 