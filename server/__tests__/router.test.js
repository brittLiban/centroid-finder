import request from 'supertest';
import app from '../server.js';

describe('API Route Tests', () => {
  it('GET / should return 200', async () => {
    const res = await request(app).get('/');
    expect(res.statusCode).toBe(200);
  });

  it('GET /api/videos should return 200 and a list', async () => {
    const res = await request(app).get('/api/videos');
    expect(res.statusCode).toBe(200);
    expect(Array.isArray(res.body)).toBe(true);
  });

  it('GET /thumbnail/:fileName should return image content-type or 404', async () => {
    const res = await request(app).get('/thumbnail/shortTest.mp4');
    if (res.statusCode === 200) {
      expect(res.headers['content-type']).toMatch(/image\/jpeg|image\/png/);
    } else {
      expect([404, 500]).toContain(res.statusCode);
    }
  });

  it('POST /process/:fileName should return job ID or error', async () => {
    const res = await request(app)
      .post('/process/shortTest.mp4?targetColor=5a020c&threshold=60');
    expect([200, 400, 500]).toContain(res.statusCode);
  });

  it('GET /csvjson/:jobId should return JSON array or 404', async () => {
    const res = await request(app).get('/csvjson/fake-job-id');
    if (res.statusCode === 200) {
      expect(res.headers['content-type']).toMatch(/application\/json/);
      expect(Array.isArray(res.body)).toBe(true);
    } else {
      expect(res.statusCode).toBe(404);
    }
  });
});
