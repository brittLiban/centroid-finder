public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue component,
     * each ranging from 0 to 255.
     * 
     * The Euclidean color distance is calculated by treating each color as a point
     * in 3D space (red, green, blue) and applying the Euclidean distance formula:
     * 
     * sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
     * 
     * This gives a measure of how visually different the two colors are.
     * 
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the Euclidean distance between the two colors
     */
    @Override
    public double distance(int colorA, int colorB) {
        return 0;
    }


     /**
     *comments which will help us for progressing on wave 3
     * Helper method extract the individual red, green, and blue components
     * from a 24‑bit hex color (0xRRGGBB).  
     * Returns an int array where:
     *   index 0  red   (0‑255)
     *   index 1  green (0‑255)
     *   index 2  blue  (0‑255)
     * we can reuse this array to simplify the distance calculation.
     */
    private int[] rgb(int color) {
        // extract red, green, blue (all 0‑255)
        return 0;
    }
    
}
