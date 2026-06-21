## 🚀 CI/CD Automated Containerization Architecture

This repository uses a multi-job GitHub Actions pipeline to automatically compile, analyze, containerize, and verify our Spring Boot application whenever code is pushed to the `main` branch.

### 📌 Architecture Diagram

```text
       [ Developer pushes code to GitHub ]
                       │
                       ▼
 ┌───────────────────────────────────────────────────────────┐
 │               JOB 1: BUILD & COMPILE                      │
 │  • Clones repo  • Sets up JDK 21  • Compiles Executable    │
 │  • Runs JUnit Tests & Generates JaCoCo Coverage Artifact │
 └─────────────────────┬───────────────────┬─────────────────┘
                       │                   │
                       ▼                   ▼
 ┌───────────────────────────┐       ┌───────────────────────┐
 │   JOB 2: SONARCLOUD SCAN  │       │    JOB 3: PACKAGING   │
 │                           │       │                       │
 │  • Pulls compiled classes │       │  • Logs into GHCR.io  │
 │  • Analyzes code quality  │       │  • Bakes Docker Image │
 │  • Transmits scan data    │       │  • Pushes to Cloud    │
 └───────────────────────────┘       └───────────┬───────────┘
                                                 │
                                                 ▼
                             ┌───────────────────────────────────┐
                             │    JOB 4: RUNTIME VERIFICATION     │
                             │                                   │
                             │  • Pulls image from GHCR Registry │
                             │  • Spins up isolated Container    │
                             │  • Tests POST & GET Endpoints     │
                             └───────────────────────────────────┘