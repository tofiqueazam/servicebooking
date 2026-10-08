# ServiceHub

ServiceHub is a full-stack local home-services booking application. Customers can discover services and providers, make bookings, and leave reviews. Providers can manage their availability, while administrators can manage providers and bookings.

## Tech stack

- Frontend: React, Vite
- Backend: Java 21, Spring Boot, Spring Security, Spring Data JPA
- Database: MySQL
- Authentication: JWT
- API documentation: Swagger UI / OpenAPI

## Features

- Customer registration and login with JWT authentication
- Service categories and service catalogue
- Provider profiles, search, approval, and availability management
- Booking creation, cancellation, and status updates
- Reviews for completed bookings
- Role-based access for customers, providers, and admins
- Swagger UI for API testing

## Project structure

```text
servicebooking/
|- servicebooking/            # Spring Boot backend
|- service-booking-frontend/  # React frontend
```

## Prerequisites

- Java 21+
- Maven (or use the included Maven wrapper)
- Node.js 18+
- MySQL 8+

## Backend setup

1. Create a MySQL database named `servicebooking`.
2. Set your database password as an environment variable.

   **PowerShell:**

   ```powershell
   $env:DB_PASSWORD="your_mysql_password"
   ```

3. Run the backend:

   ```powershell
   cd servicebooking
   .\mvnw.cmd spring-boot:run
   ```

The backend starts at `http://localhost:8080`.

### Swagger UI

Open [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

For protected endpoints:

1. Register with `POST /api/auth/register` or log in with `POST /api/auth/login`.
2. Copy the `token` from the login response.
3. Click **Authorize** in Swagger UI and paste only the token. Swagger adds `Bearer` automatically.

## Frontend setup

In a second terminal:

```powershell
cd service-booking-frontend
npm install
npm run dev
```

Open the URL shown by Vite, usually `http://localhost:5173`.

## API overview

| Area | Base endpoint |
| --- | --- |
| Authentication | `/api/auth` |
| Users | `/api/users` |
| Categories | `/api/categories` |
| Services | `/api/services` |
| Providers | `/api/providers` |
| Bookings | `/api/bookings` |
| Reviews | `/api/reviews` |
| Admin provider management | `/api/admin/providers` |

## Environment variables

| Variable | Purpose |
| --- | --- |
| `DB_PASSWORD` | MySQL database password |
| `JWT_SECRET` | JWT signing secret; a development default exists, but use a strong secret in production |

## Security note

Never commit database passwords, JWT secrets, `.env` files, `node_modules`, or backend build files to GitHub.

## License

This project is for educational purposes.
