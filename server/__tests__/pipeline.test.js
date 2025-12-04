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

  afterAll(async () => {
    await fs.rm(tmpVideosDir, { recursive: true, force: true });
  });

  test("GET /api/videos returns safe file names (no path leakage)", async () => {
    const res = await request(videosApp).get("/api/videos");
    expect(res.status).toBe(200);
    expect(Array.isArray(res.body)).toBe(true);

    const names = res.body.map((item) =>
      typeof item === "string" ? item : item.fileName
    );

    // Should include the files created
    expect(names).toEqual(expect.arrayContaining(["a.mp4", "b.MP4", "notes.txt"]));

    // API must return plain names, not paths or traversal tokens
    for (const n of names) {
      expect(typeof n).toBe("string");
      expect(n).not.toContain("..");
      expect(n).not.toContain("/");
      expect(n).not.toContain("\\");
      expect(n).not.toContain(":");
    }
  });
  
});


// Test #3: CORS preflight works (frontend can call backend)
describe("Critical: CORS preflight", () => {
  let corsApp;

  beforeAll(async () => {
    process.env.NODE_ENV = "test";

    // Reload server for a clean instance
    jest.resetModules();
    const mod = await import("../server.js");
    corsApp = mod.default;
  });

  test("OPTIONS /api/videos returns CORS headers", async () => {
    const res = await request(corsApp)
      .options("/api/videos")
      .set("Origin", "http://example.com")
      .set("Access-Control-Request-Method", "GET");

    // cors middleware often responds 204 on preflight
    expect([200, 204]).toContain(res.status);

    // Should include allow-origin (either * or echo of origin)
    expect(res.headers["access-control-allow-origin"]).toBeDefined();
  });
});
