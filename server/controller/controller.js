const getHome = async (req, res) =>{


    res.send('The default route sends something!');
    
}

const getVideos = async (req, res) =>{

    
    res.send('The videos are : ');
    
}

export default {getHome, getVideos}