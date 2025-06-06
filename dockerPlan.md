# Docker Plan for CentroidFinder Video Suite

> **Goal**  
> Package the Java centroid-tracker **and** the Node/Express API in a single container.  
> The container must expose REST endpoints on **port 3000** and read/write videos & CSVs via host-mounted volumes.

---

## 1 Base Image & Multi-Stage Strategy

| Stage            | Base image                                   | Purpose                                        | Key commands                                   |
| ---------------- | -------------------------------------------- | ---------------------------------------------- | ---------------------------------------------- |
| **builder-java** | `maven:3.9-eclipse-temurin-21-slim`          | Compile Java service and create a fat JAR                | `mvn clean compile assembly:single`          |
| **builder-node** | `node:20-slim`                               | Install API deps, prune devDependencies    | `npm ci --omit=dev`                          |
| **runtime**      | `node:20-slim` + `openjdk-21-jre-headless`   | Final image: Node runtime + JRE + compiled JAR | copy artifacts, clean caches                       |

**Why this combo?**  
* Slim variants keep image size low.  
* Multi-stage means code edits only rebuild final layers.  
* One container is enough because Node shells out to `java -jar`.

---


## 2 Ports & Volumes

| Item            | Path / Port | Purpose            |
| --------------- | ----------- | ------------------ |
| **EXPOSE 3000** | 3000        | All REST endpoints |
| **Volume**      | `/videos`   | Input MP4 files    |
| **Volume**      | `/results`  | Output CSV files   |


---

## 3 Local Build & Smoke Test

```bash
# Build the dev image
docker build -t salamander .

# Run the container in background
docker run -d --name cf-test \
           -p 3000:3000 \
           -v "$(pwd)/processor/videos:/videos" \
           -v "$(pwd)/outputCsv:/results" \
           salamander

# Quick API check
curl http://localhost:3000/api/videos
```

---

## 4 Image Size & Cache Notes

* **`.dockerignore`** – exclude `node_modules/`, `target/`, `*.mp4`, `*.csv`, logs.  
* Use **`npm ci --omit=dev`** to install only production packages.  
* Copy only `target/*-jar-with-dependencies.jar` into the final stage.  
* Clean APT cache to shrink layers:

```dockerfile
# 1️⃣ Refresh the package list
RUN apt-get update

# 2️⃣ Install headless Java 21 runtime
RUN apt-get install -y openjdk-21-jre-headless

# 3️⃣ Remove cached package lists
RUN rm -rf /var/lib/apt/lists/*
  
