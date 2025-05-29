// tests/routes.test.js
import request from 'supertest';
import app from '../server.js'; // Adjust path if needed

describe('GET /', () => {
  it('should respond with 200 OK', async () => {
    const response = await request(app).get('/');
    expect(response.statusCode).toBe(200);
  });
});
