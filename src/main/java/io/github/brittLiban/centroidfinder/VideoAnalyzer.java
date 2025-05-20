package io.github.brittLiban.centroidfinder;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VideoAnalyzer {

    public List<BufferedImage> processVideo(VideoCapture video) {
        List<BufferedImage> frames = new ArrayList<>();
        Mat frame = new Mat();

        while (video.read(frame)) {
            BufferedImage img = OpenCVUtils.convertMatToBufferedImage(frame);
            if (img != null) {
                frames.add(img);
            }
        }

        System.out.println("Video split into " + frames.size() + " frames.");
        return frames;
    }
}
