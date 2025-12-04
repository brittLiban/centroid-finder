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

