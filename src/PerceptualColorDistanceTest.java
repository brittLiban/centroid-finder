import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class PerceptualColorDistanceTest {
    private final PerceptualColorDistance perceptual = new PerceptualColorDistance();

    @Test
    public void testPerceptualDistance_blackToWhite() {
        int black = 0x000000;
        int white = 0xFFFFFF;

        double distance = perceptual.distance(black, white);

        // Expect perceptual distance ~100 between black and white
        assertTrue(distance > 99 && distance < 101, "Expected ~100, got: " + distance);
    }

    @Test 
    public void testWhiteToBlack() {
        assertEquals(100.0, perceptual.distance(0xFFFFFF, 0x000000), 1e-2);
    }

    @Test 
    public void testYellowToBlack() {
        assertEquals(137.21, perceptual.distance(0xFFFF00, 0x000000), 1e-2);
    }

    @Test 
    public void testYellowToWhite() {
        assertEquals(96.96, perceptual.distance(0xFFFF00, 0xFFFFFF), 1e-2);
    }

    @Test 
    public void testRedToGreen() {
        assertEquals(170.58, perceptual.distance(0xFF0000, 0x00FF00), 1e-2);
    }

    @Test 
    public void testBlueToMagenta() {
        assertEquals(57.95, perceptual.distance(0x0000FF, 0xFF00FF), 1e-2);
    }

    @Test
    public void testBlackToWhite_isFurthest() {
        double distance = perceptual.distance(0x000000, 0xFFFFFF);
        assertTrue(distance > 99, "Black to White should have a high perceptual distance");
    }

    @Test
    public void testSameColor_zeroDistance() {
        assertEquals(0.0, perceptual.distance(0xABCDEF, 0xABCDEF), 1e-6);
    }

    @Test
    public void testRedToYellow_isCloserThanToBlue() {
        double redToYellow = perceptual.distance(0xFF0000, 0xFFFF00); // Red to Yellow
        double redToBlue = perceptual.distance(0xFF0000, 0x0000FF);   // Red to Blue
        assertTrue(redToYellow < redToBlue, "Red is perceptually closer to yellow than blue");
    }

    @Test
    public void testThresholdEdgeCase() {
        int base = 0xFFD700; // Gold-ish
        int close = 0xFFD600; // Nearly identical
        double d = perceptual.distance(base, close);
        assertTrue(d < 1.0, "Very similar colors should be under threshold");
    }

    @Test
    public void testCompareToEuclidean() {
    ColorDistanceFinder euc = new EuclideanColorDistance();
    ColorDistanceFinder perc = new PerceptualColorDistance();
    
    int a = 0xFF0000, b = 0x00FF00;

    double euclidean = euc.distance(a, b);
    double perceptual = perc.distance(a, b);

    assertNotEquals(euclidean, perceptual, "Perceptual distance should differ from Euclidean");
    }

}
