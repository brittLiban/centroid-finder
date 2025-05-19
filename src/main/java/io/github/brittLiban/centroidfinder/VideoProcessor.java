package io.github.brittLiban.centroidfinder;

import java.io.File;

import javax.imageio.ImageIO;

public class VideoProcessor {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java -jar videoprocessor.jar <inputPath> <outputCsv> <targetColor> <threshold>");
        return;
        }

        String inputPath = args[0];
        String outPutCSV = args[1];
        String targetColorHex = args[2];
        int threshold = 0;

        try {
            threshold = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        

    }
}
