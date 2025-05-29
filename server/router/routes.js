import express from 'express';
import controller from './../controller/controller.js'
//making router a Router via express
const router = express.Router();
const {getHome , getVideos, getThumbnail, postProcessVideo, getCSVasJSON} = controller 



router.get('/', getHome);

router.get("/api/videos", getVideos);

router.get("/thumbnail/:fileName", getThumbnail);

router.get("/process/:fileName/targetColor/threshold", postProcessVideo);

router.get('/csvjson', getCSVasJSON);

export default router;
