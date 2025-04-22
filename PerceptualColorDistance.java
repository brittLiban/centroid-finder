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

    /**
     * Converts a 24-bit RGB color to CIELAB color space (Lab).
     *
     * @param rgb the color (0xRRGGBB)
     * @return an array [L, a, b] representing the Lab color
     */
    private static double[] rgbToLab(int rgb) {
        // Extract red, green, and blue components
        double r = ((rgb >> 16) & 0xFF) / 255.0;
        double g = ((rgb >> 8) & 0xFF) / 255.0;
        double b = (rgb & 0xFF) / 255.0;

        // Apply gamma correction (sRGB to linear RGB)
        r = (r > 0.04045) ? Math.pow((r + 0.055) / 1.055, 2.4) : r / 12.92;
        g = (g > 0.04045) ? Math.pow((g + 0.055) / 1.055, 2.4) : g / 12.92;
        b = (b > 0.04045) ? Math.pow((b + 0.055) / 1.055, 2.4) : b / 12.92;

        // Convert linear RGB to CIE XYZ (using D65 reference white)
        double x = (r * 0.4124 + g * 0.3576 + b * 0.1805) / 0.95047;
        double y = (r * 0.2126 + g * 0.7152 + b * 0.0722) / 1.00000;
        double z = (r * 0.0193 + g * 0.1192 + b * 0.9505) / 1.08883;

        // Convert XYZ to Lab using helper function
        x = labHelper(x);
        y = labHelper(y);
        z = labHelper(z);

        // Compute final Lab components
        double L = 116 * y - 16;
        double a = 500 * (x - y);
        double bVal = 200 * (y - z);

        return new double[]{L, a, bVal};
    }

    /**
     * Helper function for Lab conversion. Applies cube root or linear approximation.
     */
    private static double labHelper(double t) {
        return (t > 0.008856) ? Math.cbrt(t) : (7.787 * t + 16.0 / 116);
    }
}