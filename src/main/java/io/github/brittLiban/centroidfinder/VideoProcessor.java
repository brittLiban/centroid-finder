package io.github.brittLiban.centroidfinder;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.core.Mat;

//this is neccesary for every single openCV project with Java. nothing will happen without this

public class VideoProcessor {
    public static void main(String[] args) {
        //this is loading it in so it alllows us to load the openCV
        OpenCVLoader loader = new OpenCVLoader(); 


        if (args.length < 4) {
            System.out.println("Usage: java -jar videoprocessor.jar <inputPath> <outputCsv> <targetColor> <threshold>");
        return;
        }

        String inputPath = args[0];
        String outPutCSV = args[1];
        String targetColorHex = args[2];
        int threshold = 0;

        try {
            threshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        

    }
}
 