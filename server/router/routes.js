import express from 'express';
import controller from './../controller/controller.js'
//making router a Router via express
const router = express.Router();
const {getHome , getVideos} = controller 



router.get('/', getHome);

router.get("/api/videos", getVideos);



export default router;
