import fs from 'fs';
import path from 'path';

export function fileExists(fileName) {
  const videoDir = path.resolve('processor/videos'); 

  // Read all files in the videos directory
  const files = fs.readdirSync(videoDir);

  // Check if the target file is included
  return files.includes(fileName);
}

export function getPath(fileName) {
  const videoDir = path.resolve('processor/videos'); 
  return path.join(videoDir, fileName);
}
