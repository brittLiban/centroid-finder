package io.github.brittLiban.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * tests for VideoAnalyzing to check how it writes CSV lines and summary.
 */
public class VideoAnalyzingTest {
    // fake grabber that returns the frames we give it
    private static class TestGrabber extends FFmpegFrameGrabber {
        private final Frame[] frames;
        private int index = 0;
        TestGrabber(Frame... frames) {
            super("");
            this.frames = frames;
        }
        @Override public void start() {}
        @Override public void stop() {}
        @Override public Frame grabImage() {
            return (index < frames.length) ? frames[index++] : null;
        }
    }

    // no frames: only header should appear
    @Test void noFrames_writesOnlyHeader() throws Exception {
        FFmpegFrameGrabber grabber = new TestGrabber();
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((img, idx) -> new FrameResult(idx, 0, 0));
        analyzer.process(grabber, pw);
        String out = sw.toString().trim();
        assertEquals("frame,x,y", out);
    }

    // one good frame: header + one row
    @Test void oneValidFrame_writesHeaderAndOneRow() throws Exception {
        Java2DFrameConverter conv = new Java2DFrameConverter();
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
        Frame frame = conv.convert(img);
        FFmpegFrameGrabber grabber = new TestGrabber(frame);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((image, idx) -> new FrameResult(idx, idx, idx * 2));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals("frame,x,y", lines[0]);
        assertEquals("0,0,0", lines[1]);
    }

    // one bad frame: header + -1 row
    @Test void oneNullImage_writesMinusOneRow() throws Exception {
        Frame bad = new Frame();
        FFmpegFrameGrabber grabber = new TestGrabber(bad);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((img, idx) -> fail("should not run"));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals("frame,x,y", lines[0]);
        assertEquals("0, -1, -1", lines[1]);
    }

    // two good frames: rows in correct order
    @Test void twoValidFrames_writesTwoRowsInOrder() throws Exception {
        Java2DFrameConverter conv = new Java2DFrameConverter();
        BufferedImage img1 = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
        BufferedImage img2 = new BufferedImage(1, 1, BufferedImage.TYPE_3BYTE_BGR);
        Frame f1 = conv.convert(img1);
        Frame f2 = conv.convert(img2);
        FFmpegFrameGrabber grabber = new TestGrabber(f1, f2);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((image, idx) -> new FrameResult(idx, idx + 1, idx + 2));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals("0,1,2", lines[1]);
        assertEquals("1,2,3", lines[2]);
    }

    // two bad frames: both rows -1,-1
    @Test void twoNullFrames_writesTwoMinusOneRows() throws Exception {
        Frame b1 = new Frame();
        Frame b2 = new Frame();
        FFmpegFrameGrabber grabber = new TestGrabber(b1, b2);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((img, idx) -> fail("should not run"));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals("0, -1, -1", lines[1]);
        assertEquals("1, -1, -1", lines[2]);
    }

    // mix good and bad frames: check each output
    @Test void mixValidAndNull_writesCorrectRows() throws Exception {
        Java2DFrameConverter conv = new Java2DFrameConverter();
        Frame good = conv.convert(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY));
        Frame bad = new Frame();
        FFmpegFrameGrabber grabber = new TestGrabber(good, bad, good);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((image, idx) -> new FrameResult(idx, idx * 3, idx * 4));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals("0,0,0", lines[1]);
        assertEquals("1, -1, -1", lines[2]);
        assertEquals("2,6,8", lines[3]);
    }

    // bad then good: output order
    @Test void mixNullThenValid_writesCorrectRows() throws Exception {
        Java2DFrameConverter conv = new Java2DFrameConverter();
        Frame bad = new Frame();
        Frame good = conv.convert(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY));
        FFmpegFrameGrabber grabber = new TestGrabber(bad, good);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((image, idx) -> new FrameResult(idx, idx, idx));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals("0, -1, -1", lines[1]);
        assertEquals("1,1,1", lines[2]);
    }

    // custom result values appear in CSV
    @Test void customProcessorValues_shownInCsv() throws Exception {
        Java2DFrameConverter conv = new Java2DFrameConverter();
        Frame f = conv.convert(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY));
        FFmpegFrameGrabber grabber = new TestGrabber(f, f);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((image, idx) -> new FrameResult(idx, idx + 5, idx + 10));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals("0,5,10", lines[1]);
        assertEquals("1,6,11", lines[2]);
    }

    // line count equals frame count + header
    @Test void csvLineCount_matchesFrameCountPlusHeader() throws Exception {
        Java2DFrameConverter conv = new Java2DFrameConverter();
        Frame f = conv.convert(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY));
        FFmpegFrameGrabber grabber = new TestGrabber(f, f, f);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((image, idx) -> new FrameResult(idx, 0, 0));
        analyzer.process(grabber, pw);
        String[] lines = sw.toString().split("\\r?\\n");
        assertEquals(4, lines.length);
    }

    // performance message printed to console
    @Test void performanceSummary_printedToSystemOut() throws Exception {
        Java2DFrameConverter conv = new Java2DFrameConverter();
        Frame f = conv.convert(new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY));
        FFmpegFrameGrabber grabber = new TestGrabber(f, f);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        VideoAnalyzing analyzer = new VideoAnalyzing((image, idx) -> new FrameResult(idx, 0, 0));
        PrintStream oldOut = System.out;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(bos));
            analyzer.process(grabber, pw);
            String console = bos.toString();
            assertTrue(console.startsWith("Finished processing 2 frames in "));
        } finally {
            System.setOut(oldOut);
        }
    }
}
