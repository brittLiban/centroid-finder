#The Goal
-   To make a front end that can make verb calls to the backend and get this to serve properly

| Endpoint                 | Method | Description                                   |
| ------------------------ | ------ | --------------------------------------------- || `http://localhost:3000/api/videos`            | GET    | Lists all video files in `/videos`            |
| `http://localhost:3000/thumbnail/:filename`   | GET    | Returns first frame of video as JPEG          |
| `http://localhost:3000/process/:filename`     | POST   | Starts processing job using Java JAR          |
| `http://localhost:3000/process/:jobId/status` | GET    | Checks if job is running, finished, or failed |


Outline structure

-Server

server/
├── server.js             # 🌐 Main entry point — sets up the Express app and loads all routes
├── routes/               # 📁 Modular API endpoints
│   ├── videos.js         # GET    /api/videos          → Lists available video files
│   ├── thumbnail.js      # GET    /thumbnail/:filename → Returns first frame of video
│   ├── process.js        # POST   /process/:filename   → Starts processing job via Java JAR
│   └── status.js         # GET    /process/:jobId/status → Returns job status and result path
├── oldJobs/              # 📂 Tracks all job statuses as JSON files, keyed by jobId  
├── .env                  # 🔐 Environment variables (JAR path, input/output dirs)
├── package.json          # 📦 Project manifest with dependencies and scripts
├── nodemon.json          # 🌀 Optional: configures auto-reloading during development
