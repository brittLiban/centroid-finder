import fs from 'fs';
import { fileExists } from '../utils/fileUtils.js'
import { retrieveThumbnail } from '../utils/videoUtils.js'
import path from 'path';
import { spawn } from 'child_process';
import csv from 'csvtojson';

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
        res.status(500).send("The video you selected " + fileName + " does not exist. Please select from the following: " + videos);
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

//java -jar target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar ensantina.mp4 ensantina_tracking.csv 5a020c 60 
//wanted command

const postProcessVideo = async (req, res) => {
    const videoLocale = req.params.fileName;
    const targetColor = req.query.targetColor; // hex
    const threshold = req.query.threshold; // int

    const videoDir = path.resolve('processor/videos');

    // Check if video file exists
    if (!fileExists(videoLocale)) {
        const videos = fs.readdirSync(videoDir);
        return res.status(500).send(`The video you selected (${videoLocale}) does not exist. Available videos: ${videos}`);
    }

    // Validate hex color
    const isValidHex = /^#?[0-9A-Fa-f]{6}$/;
    if (!isValidHex.test(targetColor)) {
        return res.status(400).send("Invalid hex color code. Use format like 'FF5733'.");
    }

    // absolute path for .jar
    const jarPath = path.resolve('../processor/target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar');
    // absolute path for video
    const videoPath = path.resolve('../processor/videos', videoLocale);

    // Build the Java command
    const process = spawn('java', [
        '-jar',
        jarPath,
        videoPath,
        'ensantina_tracking.csv',
        targetColor,
        threshold
    ]);

    // Capture output
    process.stdout.on('data', (data) => {
        console.log(`Java stdout: ${data}`);
    });

    process.stderr.on('data', (data) => {
        console.error(`Java stderr: ${data}`);
    });

    process.on('close', (code) => {
        if (code === 0) {
            res.status(200).send(`Video processing for "${videoLocale}" completed successfully!`);
        } else {
            res.status(500).send(`Video processing failed with exit code ${code}.`);
        }
    });

    process.on('error', (err) => {
        console.error('Failed to start Java process:', err);
        res.status(500).send('Error running Java process.');
    });
};

// It displays the coordinates of the frame as a json
const getCSVasJSON = async (req, res) => {
    // Resolve the absolute path to the CSV file
    const csvPath = path.resolve('ensantina_tracking.csv');

    // Check if the CSV file exists
    if (!fs.existsSync(csvPath)) {
        return res.status(404).send('CSV file not found.');
    }

    try {

        // Use csvtojson to convert the CSV file to a JSON array
        const jsonArray = await csv().fromFile(csvPath);
        res.status(200).json(jsonArray);

    } catch (err) {

        // Handle any errors during CSV parsing
        console.error('Error reading CSV file:', err);
        res.status(500).send('Error processing CSV file.');
    }
};

export default { getHome, getVideos, getThumbnail, postProcessVideo, getCSVasJSON}