import express from 'express';
import controller from './../controller/controller.js'
//making router a Router via express
const router = express.Router();
const {getHome} = controller 



router.get('/', getHome);

router.get('/home', getHome);



export default router;
