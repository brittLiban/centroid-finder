package io.github.brittLiban.centroidfinder;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;
import org.opencv.core.Mat;

//this is neccesary for every single openCV project with Java. nothing will happen without this

public class VideoProcessor {
    public static void main(String[] args) {
        // this is loading it in so it alllows us to load the openCV
        OpenCVLoader loader = new OpenCVLoader();

        // seeing how many frames are getting used per second.
        VideoCapture cap = new VideoCapture("ensantina.mp4");
        double fps = cap.get(Videoio.CAP_PROP_FPS);
        System.out.println(fps);

        if (args.length < 4) {
            System.out.println("Usage: java -jar videoprocessor.jar <inputPath> <outputCsv> <targetColor> <threshold>");
            return;
        }

        String inputPath = args[0];
        String outPutCSV = args[1];
        String hexTargetColor = args[2];
        int threshold = 0;

        try {
            threshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        // Parse the target color from a hex string (format RRGGBB) into a 24-bit
        // integer (0xRRGGBB)
        int targetColor = 0;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }

        // taking in the video file
        VideoCapture video = new VideoCapture(inputPath);
        if (!video.isOpened()) {
            System.err.println("Failed to open video file: " + inputPath);
            return;
        }

        VideoAnalyzer analyze = new VideoAnalyzer();
        List<BufferedImage> frames = analyze.processVideo(video, 24); // updating the method so now it does 1 frame
                                                                      // every 23 seconds. Reducing the time by 24x

        // Set up centroid logic
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        try (PrintWriter writer = new PrintWriter("videoResults.csv")) {
            writer.println("frame,size,x,y"); // CSV header

            int frameIndex = 0;
            for (BufferedImage frame : frames) {
                List<Group> groups = groupFinder.findConnectedGroups(frame);
                for (Group group : groups) {
                    writer.println(frameIndex + "," + group.toCsvRow());
                }
                frameIndex++;
            }

            System.out.println("Video processing complete. CSV saved to " + "videoResults.csv");
        } catch (Exception e) {
            System.err.println("Error writing CSV.");
            e.printStackTrace();
        }
    }

}
