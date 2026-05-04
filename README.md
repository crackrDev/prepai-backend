# PrepAI Backend 🚀

> AI-Powered Mock Interview Platform — Complete Backend REST API
> Built with Java Spring Boot + MySQL + GPT-4 | 10-Day Sprint

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)
![GPT-4](https://img.shields.io/badge/OpenAI-GPT--4-purple)
![Railway](https://img.shields.io/badge/Deployed-Railway-red)

---

## 🔗 Live Links

| | URL |
|---|---|
| **Production API** | https://prepai-backend-production.up.railway.app/api |
| **Swagger Docs** | https://prepai-backend-production.up.railway.app/swagger-ui.html |
| **Frontend (Ashwin)** | https://prepai.vercel.app |
| **GitHub Org** | https://github.com/PrepAI-Dev |

---

## 📖 What is PrepAI?

PrepAI is an AI-powered mock interview platform built specifically for Indian fresher
developers (0–2 years experience). The platform provides:

- 🎙️ Push-to-talk voice interview experience
- 📄 Resume-based personalised question generation
- 💻 Live code editor for technical rounds
- 🤖 GPT-4 powered automated scoring across 5 categories
- 📊 Progress tracking and trend analysis

---

## 🏗️ System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                     FRONTEND (Ashwin)                    │
│              Next.js 14 + Tailwind + Supabase            │
│                   Deployed on Vercel                     │
└─────────────────┬───────────────────────────────────────┘
                  │ HTTP REST API calls
                  │ Authorization: Bearer <JWT>
┌─────────────────▼───────────────────────────────────────┐
│                  BACKEND (This Repo)                     │
│           Java Spring Boot 3.2 + Spring Security         │
│                  Deployed on Railway                     │
│                                                          │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐   │
│  │Questions │ │Sessions  │ │ Resume   │ │ Scoring  │   │
│  │Controller│ │Controller│ │Controller│ │Controller│   │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘   │
│                                                          │
│  ┌────────────────────────────────────────────────────┐ │
│  │              Service Layer (Business Logic)         │ │
│  │  QuestionService │ SessionService │ ScoringService  │ │
│  │  MessageService  │ ResumeParser   │ ProgressService │ │
│  └────────────────────────────────────────────────────┘ │
│                                                          │
│  ┌────────────────────────────────────────────────────┐ │
│  │           Repository Layer (JPA + MySQL)            │ │
│  └────────────────────────────────────────────────────┘ │
└─────────────────┬───────────────────────────────────────┘
                  │
┌─────────────────▼──────────┐   ┌─────────────────────┐
│      MySQL Database         │   │   OpenAI GPT-4 API  │
│   (Railway Cloud MySQL)     │   │  (Scoring Engine)   │
│  5 Tables, Indexed          │   │  Async Processing   │
└────────────────────────────┘   └─────────────────────┘
                  │
┌─────────────────▼──────────┐
│       Supabase              │
│  Auth (JWT) + File Storage  │
│  (Resume PDF uploads)       │
└────────────────────────────┘
```

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Core language |
| Spring Boot | 3.2.x | REST API framework |
| Spring Security | 6.x | JWT authentication |
| Spring Data JPA | 3.2.x | ORM / database layer |
| MySQL | 8.0 | Primary database |
| OpenAI GPT-4 | API | AI scoring engine |
| Apache PDFBox | 3.0.1 | Resume PDF parsing |
| JJWT | 0.12.3 | JWT token validation |
| Springdoc OpenAPI | 2.3.0 | Swagger UI docs |
| Lombok | latest | Boilerplate reduction |
| Railway | — | Cloud deployment |
| Maven | 3.x | Build tool |

---

## 📋 API Endpoints (12 Total)

Base URL: `https://prepai-backend-production.up.railway.app/api`

### 🟢 Questions
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/questions` | Public | Get filtered questions by role, difficulty, type |
| GET | `/questions/{id}` | Public | Get single question by UUID |

**Example:**
```
GET /api/questions?role=backend&difficulty=medium&type=technical&limit=5
```

---

### 🟡 Resume
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/resume/parse` | Public | Parse PDF resume from Supabase Storage URL |

**Request:**
```json
{
  "resumeUrl": "https://supabase.co/storage/resume.pdf"
}
```
**Response:**
```json
{
  "skills": ["java", "spring boot", "mysql"],
  "yoe": "0-1",
  "projects": ["PrepAI Platform", "E-Commerce App"],
  "rawText": "..."
}
```

---

### 🔵 Sessions
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/sessions/start` | Public | Create new interview session |
| POST | `/sessions/{id}/message` | Public | Save AI or user message |
| POST | `/sessions/{id}/end` | Public | End session + trigger GPT-4 scoring |
| GET | `/sessions/{id}` | JWT | Get session with all messages |
| GET | `/sessions/user/{userId}` | JWT | Get paginated session history |

**Start Session Request:**
```json
{
  "userId": "uuid-from-supabase",
  "role": "backend",
  "difficulty": "medium",
  "roundType": "technical",
  "resumeUrl": "https://..."
}
```
**Start Session Response:**
```json
{
  "sessionId": "uuid",
  "startedAt": "2024-01-15T10:30:00",
  "questionIds": ["id1", "id2", "id3", "id4", "id5"]
}
```

---

### 🔴 Scoring & Results
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| POST | `/score/{sessionId}` | Public | Manually trigger GPT-4 scoring |
| GET | `/results/{sessionId}` | Public | Get 5-category scores + feedback |

**Results Response:**
```json
{
  "scores": {
    "overall": 78,
    "categories": {
      "communication": 75,
      "technical": 82,
      "problemSolving": 80,
      "codeQuality": 85,
      "confidence": 70
    }
  },
  "feedback": [
    {"type": "positive", "text": "Strong understanding of HashMap internals"},
    {"type": "positive", "text": "Clean and efficient code solution"},
    {"type": "improvement", "text": "Work on confidence while explaining"},
    {"type": "improvement", "text": "Elaborate more on edge cases"}
  ],
  "improvedAreas": ["Confidence"],
  "sessionSummary": "Good attempt. Focus on weak areas and practice more."
}
```

---

### 🟣 Progress
| Method | Endpoint | Auth | Description |
|---|---|---|---|
| GET | `/progress/{userId}` | JWT | Get trends, weakest area, avg score |

**Response:**
```json
{
  "sessions": [...],
  "trends": {
    "communication": [70, 75, 80],
    "technical": [65, 72, 82],
    "overall": [68, 74, 78]
  },
  "weakestArea": "Confidence",
  "avgScore": 73.5
}
```

---

## 🗄️ Database Schema

### users
| Column | Type | Note |
|---|---|---|
| id | VARCHAR(36) PK | Supabase Auth UUID |
| email | VARCHAR(255) UNIQUE | User email |
| name | VARCHAR(100) | Display name |
| created_at | TIMESTAMP | Auto |

### questions
| Column | Type | Note |
|---|---|---|
| id | VARCHAR(36) PK | UUID |
| content | TEXT | Question text |
| type | VARCHAR(20) | technical / hr |
| role | VARCHAR(50) | frontend / backend / fullstack / devops / data |
| difficulty | VARCHAR(10) | easy / medium / hard |
| hint | TEXT | Optional hint |
| tags | TEXT | Comma separated tags |

### sessions
| Column | Type | Note |
|---|---|---|
| id | VARCHAR(36) PK | UUID |
| user_id | VARCHAR(36) | FK → users (indexed) |
| role | VARCHAR(50) | Target role |
| difficulty | VARCHAR(10) | easy / medium / hard |
| round_type | VARCHAR(20) | technical / hr |
| resume_url | TEXT | Supabase storage URL |
| started_at | TIMESTAMP | Auto |
| ended_at | TIMESTAMP | Null if active |
| status | VARCHAR(20) | active / completed / scored |

### messages
| Column | Type | Note |
|---|---|---|
| id | VARCHAR(36) PK | UUID |
| session_id | VARCHAR(36) | FK → sessions (indexed) |
| from_role | VARCHAR(10) | ai / user |
| content | TEXT | Message text |
| code_submission | TEXT | Optional code |
| created_at | TIMESTAMP | Auto |

### results
| Column | Type | Note |
|---|---|---|
| id | VARCHAR(36) PK | UUID |
| session_id | VARCHAR(36) UNIQUE | FK → sessions |
| overall_score | INT | 0-100 |
| communication | INT | 0-100 |
| technical | INT | 0-100 |
| problem_solving | INT | 0-100 |
| code_quality | INT | 0-100 (null for HR) |
| confidence | INT | 0-100 |
| feedback | TEXT | JSON array string |
| created_at | TIMESTAMP | Auto |

---

## 📁 Project Structure

```
src/main/java/com/prepai/
├── config/
│   ├── DataSeeder.java          # Seeds 30 questions on startup
│   ├── SecurityConfig.java      # Spring Security + CORS config
│   └── SwaggerConfig.java       # OpenAPI / Swagger UI config
│
├── controller/
│   ├── QuestionController.java  # GET /questions, GET /questions/{id}
│   ├── SessionController.java   # Session CRUD endpoints
│   ├── ResumeController.java    # POST /resume/parse
│   ├── ScoreController.java     # POST /score, GET /results
│   └── ProgressController.java  # GET /progress/{userId}
│
├── dto/
│   ├── SessionStartRequest.java
│   ├── SessionStartResponse.java
│   ├── ResumeParseRequest.java
│   ├── ResumeParseResponse.java
│   ├── MessageRequest.java
│   ├── MessageResponse.java
│   ├── SessionEndRequest.java
│   ├── SessionEndResponse.java
│   ├── SessionDetailResponse.java
│   ├── UserSessionResponse.java
│   ├── ScoreResponse.java
│   ├── ResultResponse.java
│   ├── ProgressResponse.java
│   └── PageResponse.java
│
├── exception/
│   ├── GlobalExceptionHandler.java   # @RestControllerAdvice
│   ├── ResourceNotFoundException.java
│   └── BadRequestException.java
│
├── model/
│   ├── User.java
│   ├── Question.java
│   ├── Session.java
│   └── Message.java
│   └── Result.java
│
├── repository/
│   ├── QuestionRepository.java
│   ├── SessionRepository.java
│   ├── MessageRepository.java
│   └── ResultRepository.java
│
├── security/
│   ├── JwtAuthFilter.java    # OncePerRequestFilter
│   └── JwtUtil.java          # Token validation utility
│
└── service/
    ├── QuestionService.java      # Question filtering logic
    ├── SessionService.java       # Session + pagination logic
    ├── MessageService.java       # Message save logic
    ├── ResumeParserService.java  # PDFBox text extraction
    ├── ScoringService.java       # GPT-4 async scoring engine
    └── ProgressService.java      # Trend + weakest area logic

src/main/resources/
├── application.properties       # Local dev config
└── application-prod.properties  # Production (Railway) config
```

---

## ⚙️ Local Setup

### Prerequisites
- Java 17+
- MySQL 8.0+
- Maven 3.x
- IntelliJ IDEA (recommended)

### Step 1 — Clone the repo
```bash
git clone https://github.com/PrepAI-Dev/prepai-backend.git
cd prepai-backend
```

### Step 2 — Create MySQL database
```sql
CREATE DATABASE prepai_db;
```

### Step 3 — Configure application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/prepai_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

openai.api.key=sk-YOUR_OPENAI_KEY
supabase.jwt.secret=YOUR_SUPABASE_JWT_SECRET
```

### Step 4 — Run
```bash
mvn spring-boot:run
```

### Step 5 — Verify
```
✅ App running: http://localhost:8080
✅ Swagger UI: http://localhost:8080/swagger-ui.html
✅ Console: "30 questions seeded successfully!"
```

---

## 🚀 Production Deployment (Railway)

### Environment Variables (set in Railway dashboard)
```
DATABASE_URL=jdbc:mysql://HOST:3306/railway?useSSL=false&serverTimezone=UTC
DATABASE_USERNAME=root
DATABASE_PASSWORD=your-railway-mysql-password
OPENAI_API_KEY=sk-your-key
SUPABASE_JWT_SECRET=your-secret
SPRING_PROFILES_ACTIVE=prod
PORT=8080
```

### Deploy
```bash
# Railway auto-deploys on every push to main
git push origin main
```

---

## 🔐 Authentication

Protected endpoints require:
```
Authorization: Bearer <supabase_jwt_token>
```

Flow:
```
User logs in via Supabase (Frontend)
→ Supabase returns JWT token
→ Frontend sends token in every API request header
→ JwtAuthFilter validates token using Supabase JWT secret
→ Valid token → request proceeds
→ Invalid token → 401 Unauthorized
```

---

## 🤖 GPT-4 Scoring Engine

How async scoring works:
```
POST /sessions/{id}/end
    ↓
SessionService.endSession()
    ↓
status = "completed" saved to MySQL
    ↓
scoreSessionAsync() → @Async background thread
    ↓                       ↓
Return "processing"   Fetch all messages
immediately ✅        Build transcript string
                            ↓
                      Call GPT-4 API with prompt
                            ↓
                      Parse JSON scores response
                            ↓
                      Save to results table
                            ↓
                      status = "scored" ✅
                            ↓
                      GET /results/{id} ready ✅
```

### GPT-4 Scoring Categories
| Category | Description |
|---|---|
| Communication | Clarity and articulation of answers |
| Technical | Depth of technical knowledge |
| Problem Solving | Approach and logical thinking |
| Code Quality | Code correctness and efficiency (technical rounds) |
| Confidence | Tone and conviction in responses |

---

## 📊 Error Response Format

All errors return consistent JSON:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Session not found with id: xyz",
  "timestamp": "2024-01-15T10:30:00"
}
```

```json
{
  "status": 400,
  "error": "Validation Failed",
  "messages": [
    "userId is required",
    "role must be one of: frontend, backend, fullstack, devops, data"
  ],
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 📅 10-Day Build Sprint

| Day | What was built |
|---|---|
| Day 1 | Spring Boot setup, MySQL config, 5 JPA entities, 30 questions seeded |
| Day 2 | JWT auth, Spring Security, JwtAuthFilter, GET /questions endpoint |
| Day 3 | POST /sessions/start, POST /resume/parse, PDFBox integration |
| Day 4 | POST /sessions/{id}/message, POST /sessions/{id}/end, session history |
| Day 5 | GPT-4 async scoring engine, POST /score, GET /results |
| Day 6 | GET /progress/{userId}, pagination, DB index optimization |
| Day 7 | GlobalExceptionHandler, custom exceptions, logging, validation |
| Day 8 | Swagger UI, @Operation annotations, prod/dev config profiles |
| Day 9 | Railway deployment, Railway MySQL, production environment variables |
| Day 10 | README, resume description, LinkedIn post, demo video |

---

## 🧪 Testing

### Run tests
```bash
mvn test
```

### Postman Collection
Import `docs/PrepAI-API-Collection.json` for all 12 endpoints pre-configured.

### Quick smoke test (after deploy)
```bash
# Questions
curl https://prepai-backend-production.up.railway.app/api/questions?role=backend

# Health check
curl https://prepai-backend-production.up.railway.app/swagger-ui.html
```

---

## 👥 Team

| Role | Developer | Responsibility |
|---|---|---|
| Backend | [உன் பேர்] | Spring Boot, MySQL, GPT-4, Railway |
| Frontend | Ashwin | Next.js 14, Supabase, OpenAI Realtime, Vercel |

---

## 📄 License

MIT License — Free to use and modify.

---

## 🙏 Acknowledgements

- [OpenAI](https://openai.com) — GPT-4 API for interview scoring
- [Supabase](https://supabase.com) — Auth and file storage
- [Apache PDFBox](https://pdfbox.apache.org) — PDF text extraction
- [Railway](https://railway.app) — Backend hosting
- [Springdoc OpenAPI](https://springdoc.org) — Swagger documentation
