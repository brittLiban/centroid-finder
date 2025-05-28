import fs from 'fs';
import { fileExists } from '../utils/fileUtils.js'
import { retrieveThumbnail } from '../utils/videoUtils.js'

const getHome = async (req, res) => {


    res.send('The default route sends something!');

}

const getVideos = async (req, res) => {

    try {
        const videos = fs.readdirSync('../processor/videos')
        console.log(videos);

        //when done with testing - remove this! 
        res.send('The videos are : ' + videos);
        res.status(200).json(videos); //making it more professional

    } catch (err) {
        console.error("error when trying to read dir", err);
        res.status(500).send('Something went wrong while reading the video folder.');
    }

}

// thumbnail/:fileName
const getThumbnail = async (req, res) => {

    const videos = fs.readdirSync('../processor/videos')
    const fileName = req.params.fileName;

    if (!fileExists(fileName)) {
        res.status(500).send("The video you selected" + fileName + " does not exist. Please select from the following: " + videos);
    }


    try {
        const ffmpegRaw = await retrieveThumbnail(fileName);
        res.status(200).setHeader('Content-Type', 'image/jpeg');
        //.pipe is used for sending binary data. Acts as res send. 
        //.pipe works by sending data as its being generated! 
        ffmpegRaw.stdout.pipe(res); // streams the res directly from the res to the client resp http

        //listens for any error message
        ffmpegRaw.stderr.on('data', (data) => {
            console.error(`ffmpeg stderr: ${data}`);
        });

        ffmpegRaw.on('error', (err) => {
            console.error('ffmpeg error:', err);
            res.status(500).json({ error: 'Error generating thumbnail' });
        });

        ffmpegRaw.stderr.on('data', (data) => {
            console.error(`ffmpeg stderr: ${data}`);
        });

    } catch (err) {
        console.error("Was unable to convert the given video: " + fileName + " into a thumbnail");
    }


}

export default { getHome, getVideos, getThumbnail }