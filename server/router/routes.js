import express from 'express';
import controller from './../controller/controller.js';
// making router a Router via express
const router = express.Router();

const {
  getHome,
  getVideos,
  getThumbnail,
  postProcessVideo,
  getCSVasJSON,
  getJobStatus
} = controller;

router.get('/', getHome);

router.get('/api/videos', getVideos);

router.get('/thumbnail/:fileName', getThumbnail);

router.post('/process/:fileName', postProcessVideo);

router.get('/csvjson/:jobId', getCSVasJSON);

router.get('/process/:jobId/status', getJobStatus);

export default router;
