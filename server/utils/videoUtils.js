import { getPath } from "./fileUtils.js";
import { spawn } from "child_process";

// use system-installed ffmpeg (inside Docker)
const ffmpegPath = 'ffmpeg';

export function retrieveThumbnail(fileName) {
    const videoPath = getPath(fileName);

    const ffmpeg = spawn(ffmpegPath, [
        '-loglevel', 'error',
        '-i', videoPath,
        '-frames:v', '1',
        '-f', 'image2',
        '-vcodec', 'mjpeg',
        'pipe:1'
    ]);

    return ffmpeg;
}
