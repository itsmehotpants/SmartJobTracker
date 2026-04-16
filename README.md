# 🌐 Smart Job Tracker Platform

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-Auth-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)](https://jwt.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

> A **production-ready Full-Stack SaaS platform** for job tracking with enterprise-level features — React Glassmorphic Frontend, JWT authentication, role-based access control, application pipeline tracking, AI Resume Scanning, and advanced search.

![Smart Job Tracker Live Action](assets/demo.webp)

---

## ✨ Features

| Category | Feature | Description |
|----------|---------|-------------|
| 🔐 **Auth** | JWT + Refresh Tokens | Stateless auth with token rotation |
| 🔐 **Auth** | BCrypt Hashing | Industry-standard password security |
| 👥 **Roles** | RBAC | USER and ADMIN role-based access |
| 💼 **Jobs** | CRUD + Search | Full job management with advanced filtering |
| 💼 **Jobs** | Pagination & Sorting | `?page=0&size=10&sort=title,asc` |
| 💼 **Jobs** | Auto-Expiry | Scheduled task deactivates past-deadline jobs |
| 🔖 **Bookmarks** | Toggle API | Idempotent bookmark add/remove |
| 🔖 **Bookmarks** | CSV Export | Download bookmarks as spreadsheet |
| 📋 **Applications** | Pipeline Tracker | Wishlist → Applied → Interview → Offered |
| 📋 **Applications** | Status Validation | Enforced logical transitions |
| 📋 **Applications** | Analytics | Interview rate, offer rate, status breakdown |
| 👤 **Profile** | Management | Update profile, change password |
| 📊 **Dashboard** | Aggregated Stats | Bookmarks, applications, active jobs |
| 📖 **Docs** | Swagger UI | Interactive API documentation |
| 🏥 **Monitoring** | Spring Actuator | Health checks and metrics |
| 📝 **Logging** | AOP Audit | Every API call logged with execution time |
| 🚫 **Errors** | Global Handler | Consistent JSON error responses |
| 🐳 **DevOps** | Docker + Compose | One-command deployment |
| 🔄 **CI/CD** | GitHub Actions | Automated build, test, and Docker |

---

## 🏗️ Architecture

```
Client (Postman / Frontend)
        ↓
   [CORS Filter]
        ↓
   [JWT Auth Filter]     →  JwtService (validate token)
        ↓
   [Rate Limiter]
        ↓
   Controller (REST API)  →  Swagger/OpenAPI Docs
        ↓
   Service (Business Logic)
        ↓
   Repository (Spring Data JPA)
        ↓
   Database (H2 / MySQL)
```

### Package Structure
```
com.jobtracker.demo
├── aspect/          → AOP logging
├── auth/            → Auth controller & service
├── config/          → Security, CORS, OpenAPI
├── controller/      → REST controllers
├── dto/
│   ├── request/     → Validated request DTOs
│   └── response/    → Response DTOs
├── entity/          → JPA entities
│   └── enums/       → Role, JobType, Status enums
├── exception/       → Global handler + custom exceptions
├── mapper/          → Entity ↔ DTO mappers
├── repository/      → JPA repositories
├── scheduler/       → Scheduled tasks
├── security/        → JWT filter, entry point
├── service/         → Business logic
└── DemoApplication.java
```

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.8+

### Run Locally (H2 Database)
```bash
# Clone the repository
git clone https://github.com/yourusername/smart-job-tracker.git
cd smart-job-tracker

# Build and run
mvn clean install
mvn spring-boot:run
```

The app starts at **http://localhost:8080**

### Run with Docker
```bash
# Build and start all services
docker-compose up --build

# Stop
docker-compose down
```

---

## 📖 API Documentation

Once running, access the interactive Swagger UI:

🔗 **Swagger UI:** http://localhost:8080/swagger-ui.html
🔗 **API Docs:** http://localhost:8080/v3/api-docs
🔗 **H2 Console:** http://localhost:8080/h2-console
🔗 **Health Check:** http://localhost:8080/actuator/health

---

## 🔌 API Endpoints

### 🔐 Authentication
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/auth/register` | Register new user | Public |
| POST | `/auth/login` | Login & get tokens | Public |
| POST | `/auth/refresh` | Refresh access token | Public |
| POST | `/auth/logout` | Invalidate refresh token | Public |

### 💼 Jobs
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/v1/jobs` | List all active jobs (paginated) | Auth |
| GET | `/api/v1/jobs/{id}` | Get job by ID | Auth |
| GET | `/api/v1/jobs/search?keyword=` | Search jobs | Auth |
| GET | `/api/v1/jobs/filter?jobType=&experienceLevel=` | Advanced filter | Auth |
| GET | `/api/v1/jobs/stats` | Job statistics | Auth |
| POST | `/api/v1/jobs` | Create job | ADMIN |
| PUT | `/api/v1/jobs/{id}` | Update job | ADMIN |
| DELETE | `/api/v1/jobs/{id}` | Delete job | ADMIN |

### 🔖 Bookmarks
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/v1/bookmarks/toggle` | Toggle bookmark | Auth |
| GET | `/api/v1/bookmarks` | My bookmarks | Auth |
| DELETE | `/api/v1/bookmarks/{id}` | Remove bookmark | Auth |
| GET | `/api/v1/bookmarks/export` | Export as CSV | Auth |

### 📋 Applications
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/v1/applications` | Apply to job | Auth |
| GET | `/api/v1/applications` | My applications | Auth |
| GET | `/api/v1/applications/{id}` | Application details | Auth |
| PATCH | `/api/v1/applications/{id}/status` | Update status | Auth |
| GET | `/api/v1/applications/stats` | Application analytics | Auth |
| GET | `/api/v1/applications/export` | Export as CSV | Auth |

### 👤 Users
| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/v1/users/me` | My profile | Auth |
| PUT | `/api/v1/users/me` | Update profile | Auth |
| PATCH | `/api/v1/users/me/password` | Change password | Auth |
| GET | `/api/v1/users/dashboard` | Dashboard stats | Auth |

---

## 🧪 Testing with cURL

### Register
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","password":"password123"}'
```

### Login
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@jobtracker.com","password":"admin123"}'
```

### Get Jobs (with token)
```bash
curl http://localhost:8080/api/v1/jobs \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

### Search Jobs
```bash
curl "http://localhost:8080/api/v1/jobs/search?keyword=java&page=0&size=5" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 🛠️ Tech Stack

| Technology | Purpose |
|-----------|---------|
| Java 17 | Core language |
| Spring Boot 3.2.5 | Application framework |
| Spring Security 6 | Authentication & authorization |
| Spring Data JPA | Data layer |
| Hibernate | ORM |
| JWT (JJWT 0.12.6) | Token-based auth |
| BCrypt | Password hashing |
| H2 Database | Development |
| MySQL 8.0 | Production |
| Springdoc OpenAPI | API documentation |
| Spring Actuator | Monitoring |
| Spring AOP | Cross-cutting logging |
| OpenCSV | CSV export |
| Lombok | Boilerplate reduction |
| Docker | Containerization |
| GitHub Actions | CI/CD |
| Maven | Build tool |

---

## 📊 Database Schema

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│    users     │     │     jobs     │     │  bookmarks   │
├──────────────┤     ├──────────────┤     ├──────────────┤
│ id (PK)      │     │ id (PK)      │     │ id (PK)      │
│ name         │     │ title        │     │ user_id (FK) │──→ users
│ email (UQ)   │     │ company      │     │ job_id (FK)  │──→ jobs
│ password     │     │ location     │     │ notes        │
│ role         │     │ description  │     │ created_at   │
│ phone        │     │ link         │     └──────────────┘
│ skills       │     │ salary       │
│ bio          │     │ job_type     │     ┌──────────────┐
│ refresh_token│     │ exp_level    │     │ applications │
│ created_at   │     │ deadline     │     ├──────────────┤
│ updated_at   │     │ is_active    │     │ id (PK)      │
└──────────────┘     │ created_by   │     │ user_id (FK) │──→ users
                     │ created_at   │     │ job_id (FK)  │──→ jobs
                     │ updated_at   │     │ status       │
                     └──────────────┘     │ notes        │
                                          │ resume_link  │
                                          │ applied_date │
                                          │ last_updated │
                                          └──────────────┘
```

---

## 🔐 Security Flow

```
1. User registers/logs in → Server returns Access Token + Refresh Token
2. Client stores tokens
3. API Request → Authorization: Bearer <access_token>
4. JwtAuthFilter → Validates token → Sets SecurityContext
5. Controller processes request based on role
6. Access Token expires (15 min) → Client uses Refresh Token
7. Server rotates Refresh Token (invalidates old one)
8. Logout → Refresh Token invalidated in database
```

---

## 📝 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

---

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

<p align="center">
  Built with ❤️ using Spring Boot | © 2024 Smart Job Tracker
</p>
