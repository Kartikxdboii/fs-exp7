# Experiment 7 – Spring Security with JWT Authentication

## Objective
Implement secure authentication using Spring Security with JWT token generation ands validation.

## What This Project Covers

### Part (a) – Login Endpoint with JWT Token
- POST `/login` authenticates user with hardcoded credentials
- Returns a signed JWT token on successful login
- Hardcoded users: `admin/admin123` (ADMIN role), `user/user123` (USER role)

### Part (b) – JWT Filter & Security Configuration
- `JwtFilter` intercepts every request and validates Bearer tokens
- Public routes (`/login`, `/register`) accessible without JWT
- All other routes require a valid JWT token in the `Authorization` header
- Stateless session management (no server-side sessions)

### Part (c) – Role-Based Access Control (RBAC)
- `@PreAuthorize("hasRole('ADMIN')")` restricts `/admin/data` to admins only
- `@PreAuthorize("hasRole('USER')")` restricts `/user/profile` to users only
- `/dashboard` accessible by any authenticated user (any role)
- Unauthorized access returns 403 Forbidden

## Project Structure
```
src/main/java/com/example/exp7/
├── Exp7Application.java               ← Main Spring Boot class
├── config/
│   └── SecurityConfig.java            ← Spring Security configuration
├── controller/
│   ├── AuthController.java            ← Login & Register endpoints
│   └── ProtectedController.java       ← Role-protected endpoints
├── filter/
│   └── JwtFilter.java                 ← JWT validation filter
└── util/
    └── JwtUtil.java                   ← JWT token create/validate helper
```

## How to Run

### Prerequisites
- Java 17+
- Maven (or use the included Maven wrapper)

### Steps
```bash
# Clone the repository
git clone https://github.com/Kartikxdboii/fs-exp7.git
cd fs-exp7

# Run the application
./mvnw spring-boot:run
```

## Testing with curl

### 1. Login (get a JWT token)
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```
Response: `{"token":"eyJhbGci...","message":"Login successful! Role: ADMIN"}`

### 2. Access protected endpoint (with token)
```bash
curl http://localhost:8080/dashboard \
  -H "Authorization: Bearer <your-token-here>"
```

### 3. Access admin-only endpoint
```bash
curl http://localhost:8080/admin/data \
  -H "Authorization: Bearer <admin-token>"
```

### 4. Test role restriction (user trying admin endpoint → 403)
```bash
# Login as user first
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}'

# Try accessing admin endpoint with user token → 403 Forbidden
curl http://localhost:8080/admin/data \
  -H "Authorization: Bearer <user-token>"
```

## API Endpoints

| Endpoint | Method | Access | Description |
|----------|--------|--------|-------------|
| `/login` | POST | Public | Authenticate and get JWT token |
| `/register` | POST | Public | Simulated user registration |
| `/dashboard` | GET | Authenticated | Any logged-in user |
| `/admin/data` | GET | ADMIN only | Admin-restricted data |
| `/user/profile` | GET | USER only | User-restricted profile |

## Technologies Used
- Java 17
- Spring Boot 3.2.5
- Spring Security
- JJWT 0.12.5 (JSON Web Token)
- Maven
