# Base image with Java 21 (Temurin) on Ubuntu Jammy
FROM eclipse-temurin:21-jdk-jammy

# Install Node.js 24, ffmpeg, curl, and git
RUN apt-get update && \
    apt-get install -y curl gnupg git ffmpeg && \
    curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Create expected folders to prevent ENOENT
RUN mkdir -p /videos /results

# Set working directory
WORKDIR /app

# Copy backend dependencies
COPY server/package*.json ./server/
RUN cd server && npm install

# Copy backend and Java processor
COPY server ./server
COPY processor/target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar ./processor/target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar

# Set environment variables
ENV VIDEO_INPUT_DIR=/videos
ENV OUTPUT_DIR=/results

# Expose backend port
EXPOSE 3000

# Start the backend server
CMD ["node", "server/server.js"]

#started working on tests