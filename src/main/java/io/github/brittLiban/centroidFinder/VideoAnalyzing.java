package io.github.brittLiban.centroidFinder;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class VideoAnalyzing {

    public List<BufferedImage> process(FFmpegFrameGrabber grabber) {
        
        //returning the frames list we will be using later
        List<BufferedImage> framesList = new ArrayList<>();

        try{
            //this is a class that will help us convert a raw frame from 
            //javaCV into something java can handle.
            Java2DFrameConverter converter = new Java2DFrameConverter();
            Frame frame;
            int frameIndex = 0;

            //while the grabber.grabImage() != null then do this
            while( (frame = grabber.grabImage()) != null){
                BufferedImage buffered = converter.convert(frame);


                if (buffered != null) {
                    
                } 

                if (buffered == null) {
                    System.out.println("Frame #" + frameIndex + " could not be converted.");
                }

            }

        }
    }

}
