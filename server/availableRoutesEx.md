## 📡 API Endpoints Overview

### 1. `GET /`
- **URL:** `http://localhost:3000/`
- **Description:** Returns a generic welcome message.

---

### 2. `GET /api/videos`
- **URL:** `http://localhost:3000/api/videos`
- **Description:** Returns a JSON array of available video files.

---

### 3. `GET /thumbnail/:fileName`
- **Example:** `http://localhost:3000/thumbnail/shortTest.mp4`
- **Description:** Returns a JPEG thumbnail of the first frame of the specified video.

---

### 4. `POST /process/:fileName`
- **Example:**  
  `http://localhost:3000/process/ensantina.mp4?targetColor=5a020c&threshold=60`
- **Description:** Starts a background processing job on the specified video.  
  Returns a `jobId` immediately.

---

### 5. `GET /process/:jobId/status`
- **Example:**  
  `http://localhost:3000/process/241a05ea-80f0-4bf6-ad4b-e4730b7c1463/status`
- **Description:** Returns the status of the background job: `processing`, `done`, or `error`.

---

### 6. `GET /csvJson/:jobId`
- **Example:**  
  `http://localhost:3000/csvJson/241a05ea-80f0-4bf6-ad4b-e4730b7c1463`
- **Description:** Returns the final processed CSV data as JSON — one entry per frame.

---
