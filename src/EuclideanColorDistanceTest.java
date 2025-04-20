import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EuclideanColorDistanceTest {

    private final EuclideanColorDistance dist = new EuclideanColorDistance();

    /** Identical colors should have distance 0 */
    @Test
    public void identicalColors() {
        int color = 0xABCDEF;
        assertEquals(0.0, dist.distance(color, color), 1e-6);
    }

    /** Black (0x000000) to white (0xFFFFFF) is sqrt(3·255²) */
    @Test
    public void blackToWhite() {
        double expected = Math.sqrt(3 * 255 * 255);
        assertEquals(expected, dist.distance(0x000000, 0xFFFFFF), 1e-6);
    }

    /** Red to green should be sqrt(255² + 255²) */
    @Test
    public void redToGreen() {
        double expected = Math.sqrt(255*255 + 255*255);
        assertEquals(expected, dist.distance(0xFF0000, 0x00FF00), 1e-6);
    }

    /** Blue to yellow should be sqrt(255² + 255² + 255²) */
    @Test
    public void blueToYellow() {
        double expected = Math.sqrt(3 * 255 * 255);
        assertEquals(expected, dist.distance(0x0000FF, 0xFFFF00), 1e-6);
    }

    /** Arbitrary pair of colors */
    @Test
    public void arbitraryPair() {
        int c1 = 0x3366CC; // (51,102,204)
        int c2 = 0xCC9933; // (204,153, 51)
        int dr = 51  - 204;
        int dg = 102 - 153;
        int db = 204 -  51;
        double expected = Math.sqrt(dr*dr + dg*dg + db*db);
        assertEquals(expected, dist.distance(c1, c2), 1e-6);
    }

    /** Distance should be symmetric: d(a,b) == d(b,a) */
    @Test
    public void symmetricDistance() {
        int a = 0x123456;
        int b = 0xFEDCBA;
        assertEquals(dist.distance(a, b), dist.distance(b, a), 1e-6);
    }

    /** White to red: ΔG=255, ΔB=255 → sqrt(2·255²) */
    @Test
    public void whiteToRed() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFFFFFF, 0xFF0000), 1e-6);
    }

    /** White to green: ΔR=255, ΔB=255 → sqrt(2·255²) */
    @Test
    public void whiteToGreen() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFFFFFF, 0x00FF00), 1e-6);
    }

    /** White to blue: ΔR=255, ΔG=255 → sqrt(2·255²) */
    @Test
    public void whiteToBlue() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFFFFFF, 0x0000FF), 1e-6);
    }

    /** Gray shades: 0x808080 to 0x404040 → Δ=64 per channel */
    @Test
    public void grayShades() {
        double expected = Math.sqrt(3 * 64 * 64);
        assertEquals(expected, dist.distance(0x808080, 0x404040), 1e-6);
    }

    /** Red to blue: ΔR=255, ΔG=0, ΔB=-255 → sqrt(2·255²) */
    @Test
    public void redToBlue() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFF0000, 0x0000FF), 1e-6);
    }
}
