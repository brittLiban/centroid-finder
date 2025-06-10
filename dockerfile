# Base image with Java 25
FROM openjdk:21-jdk-slim

# Install curl, git, Node.js 24 and cleanup
RUN apt-get update && \
    apt-get install -y curl gnupg git && \
    curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Set up working directory
WORKDIR /app

# Clone & build React/Next.js frontend
RUN rm -rf frontend \
 && git clone https://github.com/brittLiban/salamander-tracker-frontend frontend

WORKDIR /app/frontend
RUN npm ci && npm run build

# Prepare Express backend
WORKDIR /app
COPY server/package*.json ./server/
RUN cd server && npm ci --omit=dev

COPY server ./server

# Copy JAR into the container
COPY processor/target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar /app/processor/centroidfinder.jar

#Including videos for test
COPY processor/videos ./processor/videos

# Expose ports for backend (3000) & frontend (3001) ─
EXPOSE 3000 3001

# Install concurrently to run both servers  a
RUN npm install -g concurrently

# Default start command
WORKDIR /app
CMD ["concurrently", \
     "--names", "API,FRONTEND", \
     "--prefix", "[{name}]", \
     "node server/server.js", \
     "npm --prefix frontend run start -- -p 3002"]




