package hackerrank;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GridMax {

    private static int n;
    private static int m;
    private static int[][] A;
    private static int[][] MAX;

    public static void main(String[] args) throws IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new FileReader("src/input19.txt"));
        String[] split = in.readLine().split(" ");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        A = new int[n][m];
        for (int A_i = 0; A_i < n; A_i++) {
            split = in.readLine().split(" ");
            for (int A_j = 0; A_j < m; A_j++) {
                A[A_i][A_j] = Integer.parseInt(split[A_j]);
            }
        }
        int result = matrixLand2(false);
        System.out.println(result);
        in.close();
    }

    private static int matrixLand3(boolean slow) {

        int[] maxIn = new int[m];

        for (int row = 0; row < n; row++) {

            int[] maxOut = new int[m];
            for (int i = 0; i < m; i++) {
            }
            
//            LinkedList<State2> q = new LinkedList<>();
//            for (int col = 0; col < m; col++) {
//                q.add(new State2(col, maxIn[col] + A[row][col]));
//                maxOut[i] = Integer.MIN_VALUE;
//            }
//            while (!q.isEmpty()) {
//                State2 state = q.pollFirst();
//                if (state)
//            }
        }
        
        int maxOverall = Integer.MIN_VALUE;
        for (int m : maxIn) {
            maxOverall = Math.max(maxOverall, m);
        }
        return maxOverall;
    }
    
    static class State2 {
        int col, val;
        boolean back = false;

        public State2(int col, int val) {
            this.col = col;
            this.val = val;
        }
    }
    
    private static int matrixLand() {

        MAX = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                MAX[i][j] = Integer.MIN_VALUE;
            }
        }

        LinkedList<State> q = new LinkedList<>();

        for (int col = 0; col < m; col++) {
            q.add(new State(0, col, A[0][col], col, col));
            MAX[0][col] = A[0][col];
        }

        while (!q.isEmpty()) {
            State pair = q.pollFirst();
            // left
            State newPair;
            if ((newPair = pair.tryLeft()) != null) q.add(newPair);
            if ((newPair = pair.tryRight()) != null) q.add(newPair);
            if ((newPair = pair.tryDown()) != null) q.add(newPair);
        }

        return 0;
    }

    private static int matrixLand2(boolean slow) {

        int[] maxIn = new int[m];
//        int times = 0;

        for (int row = 0; row < n; row++) {
//            long t0 = System.currentTimeMillis();
            
            int[] maxOut = new int[m];
            for (int i = 0; i < m; i++) {
                maxOut[i] = Integer.MIN_VALUE;
            }

            int[] sumTill = new int[m];
            sumTill[0] = A[row][0];
            for (int col = 1; col < m; col++) {
                sumTill[col] = sumTill[col - 1] + A[row][col];
            }

            int[] maxEndingAfter = new int[m];
            maxEndingAfter[m - 1] = sumTill[m - 1];
            for (int col = m - 2; col >= 0; col--) {
                maxEndingAfter[col] = Math.max(maxEndingAfter[col + 1], sumTill[col]);
            }

            int[] minEndingBefore = new int[m];
            minEndingBefore[0] = 0;
            for (int col = 1; col < m; col++) {
                minEndingBefore[col] = Math.min(minEndingBefore[col - 1], sumTill[col - 1]);
            }
            
            if (row == 0) {
                for (int col = 0; col < m; col++) {
                    maxOut[col] = maxEndingAfter[col] - minEndingBefore[col];
                }
            } else if (slow) {
                for (int inCol = 0; inCol < m; inCol++) {
                    for (int outCol = 0; outCol < m; outCol++) {
                        maxOut[outCol] = Math.max(maxOut[outCol], maxIn[inCol] + maxEndingAfter[Math.max(inCol, outCol)] - minEndingBefore[Math.min(inCol, outCol)]);
                    }
                }
            } else {

                Integer[] maxIndex = new Integer[m];
                for (int col = 0; col < m; col++) {
                    maxIndex[col] = col;
                }

                int[] directSum = new int[m];

                for (int inCol : maxIndex) {
                    int proposedMax = maxIn[inCol] + maxEndingAfter[inCol] - minEndingBefore[inCol];
                    directSum[inCol] = proposedMax;
                    maxOut[inCol] = proposedMax;
                }

                Arrays.sort(maxIndex, Comparator.comparing(idx -> -directSum[idx]));
                
                for (int inCol : maxIndex) {
                    int proposedMax;

                    // right
                    for (int outCol = inCol + 1; outCol < m; outCol++) {
                        proposedMax = maxIn[inCol] + maxEndingAfter[Math.max(inCol, outCol)] - minEndingBefore[Math.min(inCol, outCol)];
                        if (maxOut[outCol] >= proposedMax) {
                            break;
                        }
                        maxOut[outCol] = proposedMax;
                    }

                    // left
                    for (int outCol = inCol - 1; outCol >= 0; outCol--) {
                        proposedMax = maxIn[inCol] + maxEndingAfter[Math.max(inCol, outCol)] - minEndingBefore[Math.min(inCol, outCol)];
                        if (maxOut[outCol] >= proposedMax) {
                            break;
                        }
                        maxOut[outCol] = proposedMax;
                    }
                }
            }

//            System.err.println("Done with row #" + row + " in " + (System.currentTimeMillis() - t0) + "ms");
            maxIn = maxOut;
        }

//        System.err.println(times);
        int maxOverall = Integer.MIN_VALUE;
        for (int m : maxIn) {
            maxOverall = Math.max(maxOverall, m);
        }
        return maxOverall;
    }

    @Test
    public void test() {
        n = 10;
        m = 4000000/n;
        
//        n = 10;
//        m = 10;
        
        while (true) {
            long t0 = System.currentTimeMillis();

            A = new int[n][m];
            Random r = new Random(t0);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
//                A[i][j] = (((i + j) % 2 == 0) ? 1 : -1) * (r.nextInt(250) + 1);
//                    A[i][j] = r.nextInt(250) - 250;
//                    A[i][j] = (-10 * i - 11* j) % 501 - 250;
//                    A[i][j] = (i % 2 == 0) ? -j-1 : -m + j;
//                    A[i][j] = (i % 2 == 0) ? -10 : r.nextInt(251);
//                    A[i][j] = 250;
                    A[i][j] = r.nextInt(501) - 250;
//                A[i][j] = 1;
                }
            }

            int res = matrixLand2(false);
            long spent = System.currentTimeMillis() - t0;
            
            System.out.println(res + " in " + spent + "ms (" + t0 + ")");

            if (spent > 4000) {
                System.err.println("Failed with seed " + t0);
            }
            
        }
    }

    static class State {
        int row;
        int col;
        int val;
        int min;
        int max;

        public State(int row, int col, int val, int min, int max) {
            this.row = row;
            this.col = col;
            this.val = val;
            this.min = min;
            this.max = max;
        }

        public State tryLeft() {
            if (col == 0) return null;
            return moveSides(col - 1);
        }

        public State tryRight() {
            if (col == m - 1) return null;
            return moveSides(col + 1);
        }

        public State tryDown() {
            if (row == n - 1) return null;
            int newVal = val;
            newVal += A[row + 1][col];
            // state might be better... even if it's worse
            if (MAX[row + 1][col] >= newVal) {
                return null;
            }
            MAX[row + 1][col] = newVal;
            return new State(row + 1, col, newVal, col, col);
        }

        private State moveSides(int newCol) {
            int newVal = val;
            if (newCol < min || newCol > max) {
                newVal += A[row][newCol];
            }
            // state might be better... even if it's worse
            if (MAX[row][newCol] >= newVal) {
                return null;
            }
            MAX[row][newCol] = newVal;
            return new State(row, newCol, newVal, Math.min(min, newCol), Math.max(max, newCol));
        }
    }
}
