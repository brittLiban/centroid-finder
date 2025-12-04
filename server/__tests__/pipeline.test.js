import { jest } from "@jest/globals";
import request from "supertest";
import fs from "fs/promises";
import path from "path";

let app;

// Test #1: /results serves a generated CSV and blocks path traversal
const tmpDir = path.join(process.cwd(), "__tests__", "tmp-results");
const fileName = "demo.csv";

beforeAll(async () => {
  // Run server in test mode and tell it where results live
  process.env.NODE_ENV = "test";
  process.env.OUTPUT_DIR = tmpDir;

  // Create fake output CSV simulates processor output
  await fs.mkdir(tmpDir, { recursive: true });
  await fs.writeFile(path.join(tmpDir, fileName), "x,y\n1,2\n", "utf8");

  // Reload server after env vars set
  jest.resetModules();
  const mod = await import("../server.js");
  app = mod.default;
});

afterAll(async () => {
  // Cleanup
  await fs.rm(tmpDir, { recursive: true, force: true });
});

test("GET /results/demo.csv serves a generated CSV", async () => {
  const res = await request(app).get(`/results/${fileName}`);
  expect(res.status).toBe(200);
  expect(res.text).toContain("x,y");
  expect(res.text).toContain("1,2");
});

test("GET /results/../server.js is blocked (path traversal)", async () => {
  const res = await request(app).get("/results/../server.js");
  expect([403, 404]).toContain(res.status);
});


// Test #2: /api/videos returns safe file names (no path leakage)
describe("Critical: /api/videos safety", () => {
  let videosApp;
  const tmpVideosDir = path.join(process.cwd(), "__tests__", "tmp-videos");

  beforeAll(async () => {
    process.env.NODE_ENV = "test";
    process.env.VIDEO_INPUT_DIR = tmpVideosDir;

    await fs.mkdir(tmpVideosDir, { recursive: true });

    // create a mix of files
    await fs.writeFile(path.join(tmpVideosDir, "a.mp4"), "fake", "utf8");
    await fs.writeFile(path.join(tmpVideosDir, "b.MP4"), "fake", "utf8");
    await fs.writeFile(path.join(tmpVideosDir, "notes.txt"), "nope", "utf8");

    // Reload server so it reads VIDEO_INPUT_DIR
    jest.resetModules();
    const mod = await import("../server.js");
    videosApp = mod.default;
  });
});
