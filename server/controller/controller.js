import fs from 'fs';
import {fileExists} from '../utils/fileUtils.js'
import {retrieveThumbnail} from '../utils/videoUtils.js'

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

    }catch(err){
        console.error("error when trying to read dir", err);
        res.status(500).send('Something went wrong while reading the video folder.');
    }
    
}

// thumbnail/:fileName
const getThumbnail = async (req, res) => { 

    const videos = fs.readdirSync('../processor/videos')
    const fileName = req.params.fileName;

    if(!fileExists(fileName)){
       res.status(500).send("The video you selected" + fileName + " does not exist. Please select from the following: " + videos);
    }


    try{
        retrieveThumbnail(fileName);
    }catch(err){
        console.error("Was unable to convert the given video: " + fileName + " into a thumbnail");
    }
    
    
}

export default { getHome, getVideos }