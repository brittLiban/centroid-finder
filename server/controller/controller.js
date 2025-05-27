import fs from 'fs';

const getHome = async (req, res) =>{


    res.send('The default route sends something!');
    
}

const getVideos = async (req, res) =>{
    const videos = fs.readdirSync('../processor/videos')
    console.log(videos)

    res.send('The videos are : ' + videos);
    
}

export default {getHome, getVideos}