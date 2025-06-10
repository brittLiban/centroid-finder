import fs from 'fs';
import { fileExists } from '../utils/fileUtils.js';
import { retrieveThumbnail } from '../utils/videoUtils.js';
import path from 'path';
import { v4 as uuidv4 } from 'uuid';
import { spawn } from 'child_process';
import csv from 'csvtojson';
import { createJob, setJobDone, setJobError, getJob } from '../utils/jobStoreUtils.js';

const getHome = async (req, res) => {
  res.send('The default route sends something!');
};

const getVideos = async (req, res) => {
  try {
    const videos = fs.readdirSync(path.resolve('processor/videos'));
    res.status(200).send(videos);
  } catch (err) {
    console.error('error while trying to read dir', err);
    res.status(500).send('Something went wrong while reading the video folder.');
  }
};

const getThumbnail = async (req, res) => {
  const videoDir = path.resolve('processor/videos');
  const videos = fs.readdirSync(videoDir);
  const fileName = req.params.fileName;

  if (!fileExists(fileName)) {
    res.status(500).send(
      `The video you selected ${fileName} does not exist. Please select from the following: ${videos}`
    );
    return;
  }

  try {
    const ffmpegRaw = retrieveThumbnail(fileName);
    res.status(200).setHeader('Content-Type', 'image/jpeg');
    ffmpegRaw.stdout.pipe(res);

    ffmpegRaw.stderr.on('data', (data) => {
      console.error(`ffmpeg stderr: ${data}`);
    });

    ffmpegRaw.on('error', (err) => {
      console.error('ffmpeg error:', err);
      res.status(500).json({ error: 'Error generating thumbnail' });
    });
  } catch (err) {
    console.error(`Was unable to convert ${fileName} into a thumbnail`);
  }
};

const postProcessVideo = async (req, res) => {
  const videoLocale = req.params.fileName;
  const targetColor = req.query.targetColor;
  const threshold = req.query.threshold;

  const jobId = uuidv4();
  createJob(jobId);

  const outputDir = path.resolve('outputCsv');
  const outputCsvPath = path.join(outputDir, `${jobId}.csv`);
  const videoDir = path.resolve('processor/videos');

  if (!fileExists(videoLocale)) {
    const videos = fs.readdirSync(videoDir);
    return res.status(500).send(
      `The video you selected (${videoLocale}) does not exist. Available videos: ${videos}`
    );
  }

  const isValidHex = /^#?[0-9A-Fa-f]{6}$/;
  if (!isValidHex.test(targetColor)) {
    return res.status(400).send("Invalid hex color code. Use format like 'FF5733'.");
  }

  const jarPath = path.resolve('processor/target/centroidfinder-1.0-SNAPSHOT-jar-with-dependencies.jar');
  const videoPath = path.resolve('processor/videos', videoLocale);

  const process = spawn('java', [
    '-jar',
    jarPath,
    videoPath,
    outputCsvPath,
    targetColor,
    threshold,
  ]);

  process.stdout.on('data', (data) => {
    console.log(`Java stdout: ${data}`);
  });

  process.stderr.on('data', (data) => {
    console.error(`Java stderr: ${data}`);
  });

  process.on('close', (code) => {
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

  return res.status(202).json({ jobId });
};

const getCSVasJSON = async (req, res) => {
  const jobId = req.params.jobId;
  const csvPath = path.resolve('outputCsv', `${jobId}.csv`);

  if (!fs.existsSync(csvPath)) {
    return res.status(404).send('CSV file not found.');
  }

  try {
    const jsonArray = await csv().fromFile(csvPath);
    res.status(200).json(jsonArray);
  } catch (err) {
    console.error('Error reading CSV file:', err);
    res.status(500).send('Error processing CSV file.');
  }
};

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
  getJobStatus,
};
