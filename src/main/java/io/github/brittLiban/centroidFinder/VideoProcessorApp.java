package io.github.brittLiban.centroidFinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class VideoProcessorApp {
    public static void main(String[] args){
        //taking in the command args
        if (args.length < 4) {
            System.out.println("Usage: java VideoProcessorApp inputPath outputCsv targetColor threshold");
            return;
        }

        //reading in the args
        String inputVideoPath = args[0];
        String hexTargetColor = args[1];
        String outPutCsv = args[2];
        int threshold = 0;


        //validating the args

        try(FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath)){
            grabber.start();
            System.out.println("Total frames: " + grabber.getLengthInFrames());
            System.out.println("Video duration (s): " + grabber.getLengthInTime() / 1_000_000.0);
            grabber.stop();
        } catch(Exception e) {
            System.out.println("Failed to run the video");
        }

        try {
            threshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        // Parse the target color from a hex string (format RRGGBB) into a 24-bit integer (0xRRGGBB)
        int targetColor = 0;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }



    }//end of main 
}
