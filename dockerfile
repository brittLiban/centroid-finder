# Base image with Java 25
FROM openjdk:21-slim

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
