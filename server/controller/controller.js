import fs from 'fs';
import { fileExists } from '../utils/fileUtils.js'
import { retrieveThumbnail } from '../utils/videoUtils.js'
import path from 'path';
import { v4 as uuidv4 } from 'uuid';
import { spawn } from 'child_process';
import csv from 'csvtojson';
import { fileURLToPath } from 'url';
import { createJob, setJobDone, setJobError, getJob } from '../utils/jobStoreUtils.js';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const getHome = async (req, res) => {
    res.send('The default route sends something!');
}

const getVideos = async (req, res) => {
    try {
        const videos = fs.readdirSync('../processor/videos')
        res.status(200).send(videos);
    } catch (err) {
        console.error("error while trying to read dir", err);
        res.status(500).send('Something went wrong while reading the video folder.');
    }
}

// thumbnail/:fileName
const getThumbnail = async (req, res) => {
    const videos = fs.readdirSync('../processor/videos')
    const fileName = req.params.fileName;

    if (!fileExists(fileName)) {
        res.status(500).send(
            "The video you selected " + fileName +
            " does not exist. Please select from the following: " + videos
        );
        return;
    }

    try {
        const ffmpegRaw = retrieveThumbnail(fileName);
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
    } catch (err) {
        console.error("Was unable to convert the given video: " + fileName + " into a thumbnail");
    }
}

//java -jar target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar ensantina.mp4 ensantina_tracking.csv 5a020c 60 
//wanted command
const postProcessVideo = async (req, res) => {
    const videoLocale = req.params.fileName;
    const targetColor = req.query.targetColor; // hex
    const threshold = req.query.threshold;      // int

    const jobId = uuidv4();
    createJob(jobId);

    const outputDir = path.resolve('outputCsv');
    const outputCsvPath = path.join(outputDir, jobId + '.csv');
    const videoDir = path.resolve('processor/videos');

    // Check if video file exists
    if (!fileExists(videoLocale)) {
        const videos = fs.readdirSync(videoDir);
        return res.status(500).send(
            `The video you selected (${videoLocale}) does not exist. Available videos: ${videos}`
        );
    }

    // Validate hex color
    const isValidHex = /^#?[0-9A-Fa-f]{6}$/;
    if (!isValidHex.test(targetColor)) {
        return res.status(400).send("Invalid hex color code. Use format like 'FF5733'.");
    }

    // absolute path for .jar
    const jarPath = path.resolve(__dirname, '../../processor/target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar');



    // absolute path for video
const videoPath = path.join(
    path.dirname(path.dirname(__dirname)), // go from controller → server → centroid-finder
    'processor',
    'videos',
    videoLocale
);

    // Build the Java command
    const process = spawn('java', [
        '-jar',
        jarPath,
        videoPath,
        outputCsvPath,
        targetColor,
        threshold
    ]);

    // Set up listeners BEFORE returning response
    //we set up the java object as 'process' so we listen to that

    //every time the java program s.o.u.ts something we c.log it
    process.stdout.on('data', (data) => {
        console.log(`Java stdout: ${data}`);
    });

    //we c.log every error
    process.stderr.on('data', (data) => {
        console.error(`Java stderr: ${data}`);
    });

    //AFTER THE JAVA PROJECT COMPLETELY IS DONE RUNNING
    //it will do this
    process.on('close', (code) => {
        //code === 0 means success
        if (code === 0) {
            setJobDone(jobId, `http://localhost:3000/csvJson/${jobId}.csv`);
        } else {
            setJobError(jobId, `Exit code ${code}`);
        }
    });

    process.on('error', (err) => {
        console.error('Failed to start Java process:', err);
        setJobError(jobId, err.message);
    });

    // NOW return the response
    return res.status(202).json({ jobId });
};


// It displays the coordinates of the frame as a json
const getCSVasJSON = async (req, res) => {
    const jobId = req.params.jobId;
    // Resolve the absolute path to the CSV file
    const csvPath = path.resolve('outputCsv', `${jobId}.csv`);

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

// Report the status of a processing job
const getJobStatus = (req, res) => {
    try {
        const { jobId } = req.params;
        const job = getJob(jobId);
        if (!job) return res.status(404).json({ error: 'Job ID not found' });
        if (job.status === 'processing') return res.json({ status: 'processing' });
        if (job.status === 'done') return res.json({ status: 'done', result: job.result });
        return res.json({ status: 'error', error: job.error });
    } catch (err) {
        console.error('Error fetching job status', err);
        return res.status(500).json({ error: 'Error fetching job status' });
    }
};

export default {
    getHome,
    getVideos,
    getThumbnail,
    postProcessVideo,
    getCSVasJSON,
    getJobStatus
};
