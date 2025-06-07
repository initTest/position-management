# Position Management System


## Project Structure
```
position-management-system/
├── backend/                 # Spring Boot application
│   └── src/main/resources/
│       ├── application.yml         # Common configuration
│       ├── application-dev.yml     # Development profile
│       └── application-prod.yml    # Production profile
└── frontend/               # React application
```

## Technology Stack

### Backend
- Java 21
- Spring Boot 3+
- H2 Database (Development)
- MySQL (Optional for Production)

### Frontend
- React 18
- TypeScript
- Material-UI

## Prerequisites
- Java 21
- Node.js 18+
- Maven

### Setting Up the Backend

1. Open a terminal and go to the backend folder:
   ```bash
   cd backend
   ```

2. Run the application:

   For development (uses H2 database):
   ```bash
   mvn spring-boot:run
   ```

   For production:
   ```bash
   mvn spring-boot:run \
   -Dspring.profiles.active=prod \
   -DDB_URL=jdbc:mysql://localhost:3306/database \
   -DDB_USERNAME=username \
   -DDB_PASSWORD=password \
   -DAPI_KEY=your-api-key \
   -DCORS_ALLOWED_ORIGINS=https://your-frontend-domain.com
   ```

   The backend will be available at `http://localhost:8080`

   Access Swagger documentation (dev profile only):
   - UI: `http://localhost:8080/swagger-ui.html`
   - JSON: `http://localhost:8080/v3/api-docs`

### Setting Up the Frontend

1. Open a new terminal and go to the frontend folder:
   ```bash
   cd frontend
   ```

2. Configure environment variables:
   Create a `.env` file with:
   ```
   REACT_APP_API_URL=http://localhost:8080/api
   REACT_APP_API_KEY=api-key
   ```

3. Install the required packages:
   ```bash
   npm install
   ```

4. Start the development server:
   ```bash
   npm start
   ```

   The frontend will open automatically at `http://localhost:3000`

## Development

### Profiles

#### Development Profile (default)
- H2 in-memory database
- Swagger UI enabled
- Debug logging
- Sample data loaded automatically
- H2 Console available at `/h2-console`

#### Production Profile
- MySQL database
- Swagger UI disabled
- Production-level logging
- No sample data loading
- Enhanced security settings

### API Security
All API endpoints require an API key for authentication. Add this header to your requests:
```
X-API-KEY: api-key
```

### Available API Endpoints

- Get all positions: `GET /api/positions`
  - Supports pagination: `?page=0&size=10`
  - Supports sorting: `?sortBy=title&direction=asc`
- Get a specific position: `GET /api/positions/{id}`
- Create a position: `POST /api/positions`
- Update a position: `PUT /api/positions/{id}`
- Delete a position: `DELETE /api/positions/{id}`

### API Documentation
Swagger UI is available in development environment:
- Access the documentation at `http://localhost:8080/swagger-ui.html`
- Download OpenAPI spec at `http://localhost:8080/v3/api-docs`
- Test API endpoints directly from the Swagger UI interface

### Running Tests

For backend tests:
```bash
cd backend
mvn test
```

### Environment Variables

#### Backend
Development profile (default) uses H2 database with default settings.

Production profile requires:
```bash
# Required
DB_URL              # MySQL database URL (e.g., jdbc:mysql://localhost:3306/database)
DB_USERNAME         # Database username
DB_PASSWORD         # Database password
API_KEY             # API key for authentication
CORS_ALLOWED_ORIGINS # Allowed frontend origins

# Optional
SERVER_PORT         # Default: 8080
SPRING_PROFILES_ACTIVE # Default: dev
```

#### Frontend (.env)
```
REACT_APP_API_URL=http://localhost:8080/api
REACT_APP_API_KEY=api-key
```

