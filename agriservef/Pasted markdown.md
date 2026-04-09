# AgriServe — Complete Backend System Walkthrough

> **Production-ready Spring Boot 3.2 + MySQL + JWT backend — 109 source files generated**

---

## 📁 Project Structure

```
agriserve/
├── pom.xml
├── AgriServe_Postman_Collection.json
└── src/main/
    ├── resources/
    │   └── application.properties
    └── java/com/agriserve/
        ├── AgriServeApplication.java
        ├── config/
        │   └── OpenApiConfig.java                  ← Swagger/OpenAPI
        ├── entity/
        │   ├── enums/                              ← 10 Enums
        │   │   ├── Role, Status, Gender
        │   │   ├── AdvisoryCategory, DocumentType
        │   │   ├── AttendanceStatus, ComplianceType
        │   │   ├── AuditScope, ReportScope, NotificationCategory
        │   ├── User.java, AuditLog.java
        │   ├── Farmer.java, FarmerDocument.java
        │   ├── AdvisoryContent.java, AdvisorySession.java
        │   ├── TrainingProgram.java, Workshop.java, Participation.java
        │   ├── Feedback.java, SatisfactionMetric.java
        │   ├── ComplianceRecord.java, Audit.java
        │   ├── Report.java, Notification.java
        ├── repository/                             ← 15 Repositories
        ├── dto/
        │   ├── request/                            ← 13 Request DTOs
        │   └── response/                           ← 15 Response DTOs + ApiResponse
        ├── service/                                ← 8 interfaces
        │   └── impl/                               ← 8 implementations
        ├── controller/                             ← 8 Controllers
        ├── security/
        │   ├── UserPrincipal.java
        │   ├── UserDetailsServiceImpl.java
        │   ├── JwtTokenProvider.java
        │   ├── JwtAuthenticationFilter.java
        │   ├── JwtAuthEntryPoint.java
        │   └── SecurityConfig.java
        ├── exception/
        │   ├── ResourceNotFoundException.java (404)
        │   ├── BusinessException.java (400)
        │   ├── DuplicateResourceException.java (409)
        │   └── GlobalExceptionHandler.java
        └── util/
            ├── FileStorageUtil.java
            └── SecurityUtils.java
```

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+

### Step 1 — Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/agriserve_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### Step 2 — Build & Run
```bash
cd agriserve
mvn clean install -DskipTests
mvn spring-boot:run
```

### Step 3 — Access Swagger UI
```
http://localhost:8080/api/swagger-ui.html
```

---

## 🔐 Security Design

| Layer | Technology |
|-------|-----------|
| Authentication | JWT (JJWT 0.12.5 / HS256) |
| Password | BCrypt (Spring Security) |
| Authorization | Method-level `@PreAuthorize` |
| Session | Stateless (no HTTP sessions) |
| 401 Handler | Custom JSON `JwtAuthEntryPoint` |

### Role-Based Access Matrix

| Endpoint | FARMER | OFFICER | MANAGER | ADMIN | AUDITOR |
|----------|--------|---------|---------|-------|---------|
| `POST /auth/register` | ✅ | ✅ | ✅ | ✅ | ✅ |
| `GET /farmers` | — | ✅ | ✅ | ✅ | — |
| `POST /advisory/content` | — | ✅ | — | ✅ | — |
| `POST /training/programs` | — | — | ✅ | ✅ | — |
| `POST /feedback` | ✅ | ✅ | — | ✅ | — |
| `GET /compliance/audits` | — | — | — | ✅ | ✅ |
| `GET /reports` | — | — | ✅ | ✅ | ✅ |
| `DELETE /**` | — | — | — | ✅ | — |

---

## 🗄️ Entity Relationship Summary

```
User ──────────────────────────────────────────────►  AuditLog (1:N)
                                                   ►  Notification (1:N)
Farmer ────────────────────────────────────────────►  FarmerDocument (1:N)
                                                   ►  AdvisorySession (1:N)
                                                   ►  Participation (1:N)
                                                   ►  Feedback (1:N)
AdvisoryContent ──────────────────────────────────►  AdvisorySession (1:N)
TrainingProgram ──────────────────────────────────►  Workshop (1:N)
                                                   ►  SatisfactionMetric (1:N)
Workshop ─────────────────────────────────────────►  Participation (1:N)
AdvisorySession ──────────────────────────────────►  Feedback (1:N)
```

**Unique Constraints:**
- `User.email` — no duplicate accounts
- `Feedback(farmer_id, session_id)` — one feedback per farmer per session
- `Participation(workshop_id, farmer_id)` — one registration per farmer per workshop

---

## ✅ Key Features Implemented

| Feature | Status |
|---------|--------|
| JWT Authentication (HS256) | ✅ |
| Role-Based Access Control | ✅ |
| BCrypt Password Encoding | ✅ |
| Farmer Registration + Docs Upload | ✅ |
| Advisory Content CRUD | ✅ |
| Advisory Session Booking | ✅ |
| Training Programs + Workshops | ✅ |
| Participation Tracking | ✅ |
| Feedback (rating 1–5 validated) | ✅ |
| Satisfaction Metrics Computation | ✅ |
| Compliance Record Tracking | ✅ |
| Formal Audit Management | ✅ |
| System AuditLog (action trail) | ✅ |
| Notification System | ✅ |
| Report Generation with Metrics | ✅ |
| Pagination + Sorting + Filtering | ✅ |
| Global Exception Handler | ✅ |
| DTO pattern (no entity exposure) | ✅ |
| Swagger/OpenAPI Documentation | ✅ |
| Multipart File Upload | ✅ |

---

## 📮 Postman Quick Start

Import `AgriServe_Postman_Collection.json` into Postman, then:

1. **Run request #1** → Register Admin
2. **Run request #2** → Login → Copy `accessToken`
3. Set the `token` collection variable to the copied token
4. Run any other request with `Bearer {{token}}`

> [!TIP]
> The collection includes a **validation test (#21)** that submits `rating: 6` — this should return a `400` with field-level error messages demonstrating the validation layer works.

---

## 🔧 Configuration Reference

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | `8080` | HTTP port |
| `app.jwt.expiration-ms` | `86400000` | 24 hours |
| `app.jwt.refresh-expiration-ms` | `604800000` | 7 days |
| `app.file.upload-dir` | `./uploads` | File storage path |
| `spring.jpa.hibernate.ddl-auto` | `update` | Auto-create/migrate schema |

> [!WARNING]
> Change `app.jwt.secret` to a strong random 256-bit key before going to production. Never commit secrets to version control.
