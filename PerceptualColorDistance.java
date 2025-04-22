public class PerceptualColorDistance {

    /**
     * Calculates the perceptual color difference between two RGB colors
     * using the Delta E 1976 (ΔE76) formula in the Lab color space.
     *
     * @param rgb1 the first color (0xRRGGBB)
     * @param rgb2 the second color (0xRRGGBB)
     * @return the perceptual distance (ΔE) between the two colors
     */
    public static double deltaE(int rgb1, int rgb2) {
        double[] lab1 = rgbToLab(rgb1); // Convert first color to Lab
        double[] lab2 = rgbToLab(rgb2); // Convert second color to Lab

        // Compute Euclidean distance in Lab space
        return Math.sqrt(
            Math.pow(lab1[0] - lab2[0], 2) +  
            Math.pow(lab1[1] - lab2[1], 2) + 
            Math.pow(lab1[2] - lab2[2], 2)    
        );
    }
}