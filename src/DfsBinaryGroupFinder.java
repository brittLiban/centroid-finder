import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
    /**
     * Finds connected pixel groups of 1s in an integer array representing a binary
     * image.
     * 
     * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    //  * NEED TO DO
     * If the array or any of its subarrays are null, a NullPointerException
     * is thrown. If the array is otherwise invalid, an IllegalArgumentException
     * is thrown.
     *
     * Pixels are considered connected vertically and horizontally, NOT diagonally.
     * The top-left cell of the array (row:0, column:0) is considered to be
     * coordinate
     * (x:0, y:0). Y increases downward and X increases to the right. For example,
     * (row:4, column:7) corresponds to (x:7, y:4).
     *
     * The method returns a list of sorted groups. The group's size is the number
     * of pixels in the group. The centroid of the group
     * is computed as the average of each of the pixel locations across each
     * dimension.
     * For example, the x coordinate of the centroid is the sum of all the x
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * Similarly, the y coordinate of the centroid is the sum of all the y
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * The division should be done as INTEGER DIVISION.
     *
     * The groups are sorted in DESCENDING order according to Group's compareTo
     * method
     * (size first, then x, then y). That is, the largest group will be first, the
     * smallest group will be last, and ties will be broken first by descending
     * y value, then descending x value.
     * 
     * @param image a rectangular 2D array containing only 1s and 0s
     * @return the found groups of connected pixels in descending order
     */
    @Override
    public List<Group> findConnectedGroups(int[][] image) {

        if (image == null) throw new NullPointerException();
        if (image.length == 0 || image[0] == null || image[0].length == 0) {
            throw new IllegalArgumentException();
        }

        boolean[][] visited = new boolean[image.length][image[0].length];
        List<Group> groups = new ArrayList<>();

        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[0].length; c++) {
                if (image[r][c] == 1 && !visited[r][c]) {
                    // dfs to grab the whole blob
                    List<int[]> blob = findConnectedGroups(image, new int[]{r, c}, visited);
                    int size  = blob.size();
                    int sumX  = 0;
                    int sumY  = 0;
                    // p[0] = row (y), p[1] = col (x)
                    for (int[] p : blob) {
                    sumY += p[0];
                    sumX += p[1];
                    }

                    Coordinate centroid = new Coordinate(sumX / size, sumY / size); 
                    groups.add(new Group(size, centroid));                     
               }
            }
        }


        //is this the part that sorts everything? - Liban
        Collections.sort(groups, Collections.reverseOrder());
 
        return groups;
    }
    // helper to find the four neighbour 1‑pixels of current
    public static List<int[]> connectedOnes(int[][] image, int[] current) {
        int curR = current[0], curC = current[1];
        List<int[]> connected = new ArrayList<>();

        // UP
        int newR = curR - 1, newC = curC;
        if (newR >= 0 && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        // DOWN
        newR = curR + 1; newC = curC;
        if (newR < image.length && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        // LEFT
        newR = curR; newC = curC - 1;
        if (newC >= 0 && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        // RIGHT
        newR = curR; newC = curC + 1;
        if (newC < image[0].length && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        return connected;
    }
       //depth first search gather one whole blob of 1‑pixels
    public static List<int[]> findConnectedGroups(int[][] image, int[] current, boolean[][] visited) {

        int curR = current[0], curC = current[1];
        if (visited[curR][curC]) return new ArrayList<>();

        visited[curR][curC] = true;
        List<int[]> result = new ArrayList<>();
        result.add(current);

        for (int[] next : connectedOnes(image, current)) {
            result.addAll(findConnectedGroups(image, next, visited));
        }
        return result;
    }
}