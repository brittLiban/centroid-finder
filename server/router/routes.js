import express from 'express';
import controller from './../controller/controller.js'

const router = express.Router();
const { getHome, getVideos, getThumbnail, postProcessVideo, getCSVasJSON, getJobStatus, getAllJobs } = controller;

router.get('/', getHome);

router.get('/api/videos', getVideos);

router.get('/thumbnail/:fileName', getThumbnail);

router.post('/process/:fileName', postProcessVideo);

router.get('/csvjson/:jobId', getCSVasJSON);

// check processing job status
router.get('/process/:jobId/status', getJobStatus);

router.get('/jobs', getAllJobs);

export default router;
