package io.github.brittLiban.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Collections;

/**
 * Unit tests for LargestBlobProcessor without using Mockito.
 */
public class LargestBlobProcessorTest {
    // use the same frame number for every test
    private static final int FRAME_INDEX = 7;

    @Test
    // Test that a single blob returns its own size and center
    void singleGroupIsReturned() {
        Group group = new Group(10, new Coordinate(4, 5));
        ImageGroupFinder finder = frame -> List.of(group);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",4,5", result.toCsvRow());
    }

    @Test
    // Test that no blobs gives "NA" for x and y
    void noGroupsYieldsNaCoordinates() {
        ImageGroupFinder finder = frame -> Collections.emptyList();
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",NA,NA", result.toCsvRow());
    }

    @Test
    // Test picking the largest blob among three blobs
    void picksLargestOfMultipleGroups() {
        Group smallGroup  = new Group(2, new Coordinate(1, 1));
        Group largeGroup  = new Group(5, new Coordinate(8, 9));
        Group mediumGroup = new Group(3, new Coordinate(2, 3));
        ImageGroupFinder finder = frame -> List.of(smallGroup, largeGroup, mediumGroup);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",8,9", result.toCsvRow());
    }

    @Test
    // Test tie in size: first blob stays first
    void tieSizeKeepsFirstGroup() {
        Group firstGroup  = new Group(5, new Coordinate(1, 1));
        Group secondGroup = new Group(5, new Coordinate(2, 2));
        ImageGroupFinder finder = frame -> List.of(firstGroup, secondGroup);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",1,1", result.toCsvRow());
    }

    @Test
    // Test that zero-size blobs are treated as no blob
    void zeroSizeGroupsYieldNaCoordinates() {
        Group zeroA = new Group(0, new Coordinate(0, 0));
        Group zeroB = new Group(0, new Coordinate(10, 10));
        ImageGroupFinder finder = frame -> List.of(zeroA, zeroB);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",NA,NA", result.toCsvRow());
    }

    @Test
    // Test picking the largest blob at the end of the list
    void largestAtEndIsPicked() {
        Group first  = new Group(1, new Coordinate(0, 0));
        Group second = new Group(3, new Coordinate(5, 5));
        Group third  = new Group(10, new Coordinate(9, 9));
        ImageGroupFinder finder = frame -> List.of(first, second, third);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",9,9", result.toCsvRow());
    }

    @Test
    // Test mix of zero and positive size: picks the positive
    void mixZeroAndPositiveGroupsPicksPositive() {
        Group zeroGroup     = new Group(0, new Coordinate(0, 0));
        Group positiveGroup = new Group(4, new Coordinate(2, 6));
        ImageGroupFinder finder = frame -> List.of(zeroGroup, positiveGroup);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",2,6", result.toCsvRow());
    }

    @Test
    // Test a single pixel blob at origin (0,0)
    void singlePixelGroupAtOrigin() {
        Group group = new Group(1, new Coordinate(0, 0));
        ImageGroupFinder finder = frame -> List.of(group);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",0,0", result.toCsvRow());
    }

    @Test
    // Test that custom frame index is used in output
    void customFrameIndexIsUsed() {
        Group group = new Group(3, new Coordinate(2, 2));
        ImageGroupFinder finder = frame -> List.of(group);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);
        int customIndex = 42;

        FrameResult result = processor.processFrame(dummyImage, customIndex);
        assertEquals(customIndex + ",2,2", result.toCsvRow());
    }

    @Test
    // Test negative centroid coordinates are treated as missing
    void negativeCentroidCoordinatesYieldNA() {
        Group group = new Group(4, new Coordinate(-2, -3));
        ImageGroupFinder finder = frame -> List.of(group);
        LargestBlobProcessor processor = new LargestBlobProcessor(finder);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_BYTE_BINARY);

        FrameResult result = processor.processFrame(dummyImage, FRAME_INDEX);
        assertEquals(FRAME_INDEX + ",NA,NA", result.toCsvRow());
    }
}
