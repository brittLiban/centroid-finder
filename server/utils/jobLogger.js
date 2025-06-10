import fs from 'fs';
import path from 'path';

const jobLogPath = path.resolve('outputCsv/jobLog.json');

export function appendJobLog(entry) {
  let current = [];

  if (fs.existsSync(jobLogPath)) {
    try {
      const raw = fs.readFileSync(jobLogPath, 'utf-8');
      current = JSON.parse(raw);
    } catch (err) {
      console.error(' Failed to parse job log:', err);
    }
  }

  //to prepend
  current.unshift(entry);


  try {
    fs.writeFileSync(jobLogPath, JSON.stringify(current, null, 2));
  } catch (err) {
    console.error('❌ Failed to write job log:', err);
  }
}
