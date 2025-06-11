import fs from 'fs';
import path from 'path';

const OUTPUT_DIR = process.env.OUTPUT_DIR || path.resolve(process.cwd(), 'results');
const jobLogPath = path.join(OUTPUT_DIR, 'jobLog.json');

// Make sure output directory exists
fs.mkdirSync(OUTPUT_DIR, { recursive: true });

export function appendJobLog(entry) {
  let current = [];

  if (fs.existsSync(jobLogPath)) {
    try {
      const raw = fs.readFileSync(jobLogPath, 'utf-8');
      current = JSON.parse(raw);
    } catch (err) {
      console.error('Failed to parse job log:', err);
    }
  }

  // Prepend new entry
  current.unshift(entry);

  try {
    fs.writeFileSync(jobLogPath, JSON.stringify(current, null, 2));
    console.log('Job log written:', jobLogPath);
  } catch (err) {
    console.error('Failed to write job log:', err);
  }
}
