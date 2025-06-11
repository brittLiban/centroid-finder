FROM node:20

WORKDIR /app

# Clean stale content
RUN rm -rf *

# Clone latest frontend
RUN git clone --depth=1 https://github.com/brittLiban/salamander-tracker-frontend.git .

# Install dependencies
RUN npm install



# Start production server on port 3000
EXPOSE 3000
CMD ["npm", "run", "dev", "--", "-p", "3000"]

