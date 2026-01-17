# DocLoc - Intelligent Document Summarization System

**DocLoc** is a full-stack, containerized application that allows users to upload documents (PDF, DOC, TXT) and automatically generate intelligent summaries using advanced AI models (Google Gemini).

## 1. Technology Stack

The project follows a **microservices-inspired architecture**, separating concerns between the user interface, business logic/storage, and the compute-heavy AI processing.

### **Frontend**
*   **Framework**: React.js (v19)
*   **Styling**: CSS Modules, Responsive Design
*   **Server**: Nginx (serves static assets and acts as a **Reverse Proxy** to the backend)

### **Backend**
*   **Framework**: Spring Boot 3+ (Java 17)
*   **Build Tool**: Maven
*   **Database Access**: Spring Data JPA (Hibernate)
*   **API Style**: RESTful API

### **Database**
*   **System**: PostgreSQL 13
*   **Persistence**: Docker Volumes (data survives container restarts)

### **AI Microservice**
*   **Runtime**: Node.js 18 (Alpine)
*   **Framework**: Express.js
*   **PDF Processing**: `pdf-parse` (stable v1.1.1)
*   **AI Integration**: Google Gemini API (Primary), OpenRouter (Fallback)

---

## 2. System Architecture & Data Flow

### **1. Upload Flow**
1.  **User** drags & drops a file in the React Frontend.
2.  **Frontend** sends a `POST /api/documents` request to Nginx.
3.  **Nginx** proxies the request to the **Spring Boot Backend**.
4.  **Backend** stores the raw file binary (BLOB) and metadata (filename, size) into **PostgreSQL**.
5.  **Backend** returns a unique `documentId` to the frontend.

### **2. Analysis Flow**
1.  **Frontend** automatically triggers a `POST /api/summaries` request with the `documentId`.
2.  **Backend** retrieves the file content from the database.
3.  **Backend** sends the file and authentication token to the **AI Service** (internal network call).
4.  **AI Service**:
    *   Parses the text content from the PDF.
    *   Constructs a prompt for the AI model.
    *   Calls the **Google Gemini API** (or OpenRouter) to generate the summary.
5.  **AI Service** returns the generated text to the Backend.
6.  **Backend** saves the summary text and updates the status in **PostgreSQL**.
7.  **Frontend** receives the summary and displays it to the user.

---

## 3. Key Features

### 📄 Document Upload
*   Drag-and-drop interface.
*   Supports PDF, Word (DOC/DOCX), and Text files.
*   Real-time upload progress indicators.

### 🧠 AI-Powered Summarization
*   Utilizes **Google Gemini 2.0 Flash** (or similar) for high-speed, high-quality summaries.
*   Context-aware processing: Extracts text, handles page counts, and generates coherent paragraphs.
*   **Smart Fallback**: The system detects available API keys and prioritizes Gemini but can switch to OpenRouter if configured.

### 📥 Results & Export
*   **Instant Preview**: Summaries are displayed directly in the UI with preserved formatting.
*   **Download**: Users can download the generated summary as a `.txt` file for offline use.

### 🛡️ Security & Architecture
*   **Containerized**: Fully isolated environment using Docker.
*   **Internal Auth**: Communication between the Backend and AI Service is secured via secret tokens (`AUTH_TOKEN`).
*   **Proxy**: The frontend is served via Nginx, which hides the backend topology from the public client.

---

## 4. Prerequisites

Before running the project, ensure you have the following installed:

1.  **Docker Desktop** (or Docker Engine + Docker Compose).
2.  **Google Gemini API Key**:
    *   Get one for free at [Google AI Studio](https://aistudio.google.com/).

---

## 5. Setup & Running

### **Step 1: Configuration**
Create a `.env` file in the root directory of the project. This file will hold your sensitive API keys.

**File:** `./.env`
```env
# Your Google Gemini API Key (Required)
GEMINI_API_KEY=AIzaSy...

# Optional: OpenRouter Key (if you prefer using other models)
# OPENROUTER=sk-or-v1...

# Internal Token (Shared between Backend and AI Service)
# You don't usually need to change this unless deploying to production.
AUTH_TOKEN=dev-secret-token
```

### **Step 2: Start the Application**
Open a terminal in the project root and run:

```powershell
docker-compose up --build
```

*This command will:*
1.  Spin up the **PostgreSQL** database.
2.  Build and start the **Spring Boot Backend**.
3.  Build and start the **AI Service**.
4.  Build the **React Frontend** and serve it via Nginx.

### **Step 3: Access the App**
Once the logs settle (look for "Started DocLocBackendApplication"), open your browser:

*   **Frontend**: [http://localhost:3000](http://localhost:3000)

---

## 6. Project Structure

```text
doc-loc/
├── .env                    # Environment variables (API Keys)
├── docker-compose.yml      # Orchestration for DB, Backend, Frontend, AI
├── ai-service/             # Node.js Microservice
│   ├── services/           # Gemini/OpenRouter integration logic
│   ├── server.js           # Express server entry point
│   └── Dockerfile
├── Client/
│   └── client/             # React Frontend
│       ├── src/components/ # UI Components (DocumentUpload, History)
│       ├── nginx.conf      # Nginx Proxy Configuration
│       └── Dockerfile
└── doc-loc-backend/        # Spring Boot Backend
    ├── src/main/java/      # Java Source Code
    │   ├── domain/         # Entities (Document, Summary)
    │   ├── web/            # REST Controllers
    │   └── service/        # Business Logic
    └── Dockerfile
```

---

## 7. Troubleshooting

*   **"Failed to call AI service"**:
    *   Check the Docker logs (`docker logs doc-loc-ai-service`).
    *   Ensure your `GEMINI_API_KEY` is correct in the `.env` file.
    *   Ensure the `.env` file is in the **root** folder, not inside a subfolder.

*   **Database Connection Refused**:
    *   Ensure port `5432` is not being used by a local PostgreSQL installation on your host machine. If it is, stop the local service or modify `docker-compose.yml` to map to a different host port (e.g., `"5433:5432"`).



[📄 Project Architecture Description and Diagram](cebp%20%284%29.pdf)
