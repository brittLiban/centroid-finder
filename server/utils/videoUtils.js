import { getPath } from "./fileUtils.js"

//how to retrieve a frame from a file

//ex: http://localhost:3000/thumbnail/:filename
export function retrieveThumbnail(fileName) {
    const videoPath = getPath(fileName);

    // Return an ffmpeg stream that outputs a single JPEG frame
    return spawn('ffmpeg', [
        '-i', videoPath, //input file
        '-frames:v', '1', //# only extract 1 frame
        '-f', 'image2', //use image format
        '-vcodec', 'mjpeg', //encode as JPEG
        'pipe:1' // send to stdout
    ]);

    //outputting 1 ffmpeg to the output stream.
    // its writting to a output not a file

     return ffmpeg.stdout;


}