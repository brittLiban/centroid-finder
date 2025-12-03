import { getPath } from "./fileUtils.js";
import { spawn } from "child_process";
import ffmpegStatic from "ffmpeg-static";

// use ffmpeg-static in dev, falls back to system ffmpeg in Docker
const ffmpegPath = ffmpegStatic || 'ffmpeg';

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
