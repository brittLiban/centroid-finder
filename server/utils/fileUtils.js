import fs from 'fs';
import path from 'path';

console.log("🛠️ process.env.VIDEO_INPUT_DIR:", process.env.VIDEO_INPUT_DIR);
console.log("🛠️ process.cwd():", process.cwd());

const VIDEO_INPUT_DIR = process.env.VIDEO_INPUT_DIR
  ? path.resolve(process.env.VIDEO_INPUT_DIR)
  : path.resolve(process.cwd(), 'videos');

const OUTPUT_DIR = process.env.OUTPUT_DIR
  ? path.resolve(process.env.OUTPUT_DIR)
  : path.resolve(process.cwd(), 'results');

console.log("🔍 Final VIDEO_INPUT_DIR resolved to:", VIDEO_INPUT_DIR);
console.log("🔍 Final OUTPUT_DIR resolved to:", OUTPUT_DIR);

export function fileExists(fileName) {
  const fullPath = path.join(VIDEO_INPUT_DIR, fileName);
  console.log("🔎 Checking for file at:", fullPath);
  return fs.existsSync(fullPath);
}

export function getPath(fileName) {
  return path.join(VIDEO_INPUT_DIR, fileName);
}
