#Workflow Overview:
┌───────────────────────┐
│     1. BUILD          │
│  (Compile & Test)     │
└───────────┬───────────┘
│
┌──────────────┴──────────────┐
▼                             ▼
┌───────────────────────┐     ┌───────────────────────┐
│     2. PUBLISH        │     │    3. SONAR_SCAN      │
│   (Uploads JAR)       │     │  (Code Quality Scan)  │
└───────────┬───────────┘     └───────────┬───────────┘
│                             │
└──────────────┬──────────────┘
▼
┌───────────────────────┐
│ 4. DOWNLOAD_ARTIFACT  │
│   (Procures Package)  │
└───────────┬───────────┘
│
▼
┌───────────────────────┐
│   5. DEPLOY_AND_RUN   │
│ (Tomcat Boot & Tests) │
└───────────────────────┘

#Detailed Step-by-Step Data Flow

[JOB 1: BUILD] ──────────────────► Generates 'compiled-workspace' artifact
│
├──► [JOB 2: PUBLISH] ────► Downloads workspace ──► Extracts and uploads 'springboot-app-jar'
│
└──► [JOB 3: SONAR_SCAN] ─► Downloads workspace ──► Runs SonarCloud Analysis
│
┌──────────────────────────────────────────┘
▼
[JOB 4: DOWNLOAD_ARTIFACT] ──────► Downloads 'springboot-app-jar' ──► Uploads verified 'staged-deployment-package'
│
▼
[JOB 5: DEPLOY_AND_RUN] ─────────► Downloads 'staged-deployment-package' ──► Kills port 8081 ──► Launches JAR ──► Runs Curl Checks