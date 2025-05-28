import { getPath } from "./fileUtils.js";
import { spawn } from "child_process";

// how to retrieve a frame from a file
// ex: http://localhost:3000/thumbnail/:filename
export function retrieveThumbnail(fileName) {
    const videoPath = getPath(fileName);

    // Return an ffmpeg stream that outputs a single JPEG frame
    const ffmpeg = spawn('ffmpeg', [
        '-loglevel', 'error',
        '-i', videoPath,       // input file
        '-frames:v', '1',      // only extract 1 frame
        '-f', 'image2',        // use image format
        '-vcodec', 'mjpeg',    // encode as JPEG
        'pipe:1'               // send to stdout (not to a file)
    ]);

    // outputting 1 frame to the output stream.
    // it's writing to an output stream, not a file
    return ffmpeg; // return the full process so you can access stdout and stderr
}