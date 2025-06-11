docker build -t ghcr.io/brittliban/salamander .


docker run -p 3000:3000 \
  -v "//c/Users/liban/Documents/videos:/videos" \
  -v "//c/Users/liban/Documents/results:/results" \
  centroid-backend
