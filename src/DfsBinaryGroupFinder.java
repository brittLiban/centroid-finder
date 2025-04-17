import java.util.ArrayList;
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
        int[] startingPoint = oneFinder(image);
        //fix the size of the boolean later to make it more robust
        boolean[][] visited = new boolean[image.length][image[0].length];

        List<int[]> allTheOnes = 

    }

    public static int[] oneFinder(int[][] image) {

        for (int r = 0; r > image[r].length; r++) {
            for (int c = 0; c > image[c].length; c++) {
                if (image[r][c] == 1) {
                    return new int[] { r, c };
                }
            }
        }

        throw new IllegalArgumentException("There isn't any 1's");
    }

    public static List<int[]> connectedOnes(int[][] image, int[] current) {
        int curR = current[0];
        int curC = current[1];
        List<int[]> connectedOnesList = new ArrayList<>();

        // UP
        int newR = curR - 1;
        int newC = curC;
        if (newR >= 0 && image[newR][newC] == 1) {
            connectedOnesList.add(new int[] { newR, newC });
        }

        // DOWN
        newR = curR + 1;
        newC = curC;
        if (newR < image.length && image[newR][newC] == 1) {
            connectedOnesList.add(new int[] { newR, newC });
        }

        // LEFT
        newR = curR;
        newC = curC - 1;
        if (newC >= 0 && image[newR][newC] == 1) {
            connectedOnesList.add(new int[] { newR, newC });
        }

        // RIGHT
        newR = curR;
        newC = curC + 1;
        if (newC < image[0].length && image[newR][newC] == 1) {
            connectedOnesList.add(new int[] { newR, newC });
        }

        return connectedOnesList;

    }

    public static List<int[]> findConnectedGroups(int[][] image, int[] current, boolean[][] visited) {
        int curR = current[0];
        int curC = current[1];

        if (visited[curR][curC])
            return new ArrayList<>();

        // this one does have a reach goal like food, it just wants to know all the
        // possible locals!

        visited[curR][curC] = true;

        List<int[]> result = new ArrayList<>();
        // ADDING THE CURRENT ITTERATION LOCALE TO THE POSSIBLE MOVES
        // only adding a local to the list WHEN AND IF WE VISIT IT
        // how can this problematic?
        result.add(current);

        // grabbing all the possible moves using the POSSIBLE MOVES method... FOR THE
        // CURRENT ITERATION
        List<int[]> moves = connectedOnes(image, current);
        for (int[] move : moves) {
            result.addAll(findConnectedGroups(image, move, visited));
        }

        return result;
    }
}
