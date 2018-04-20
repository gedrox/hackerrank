package hackerrank;

import java.io.*;
import java.util.*;

public class MaxIn {

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
        int result = matrixLand();
        System.out.println(result);
        in.close();
    }

    private static int matrixLand() {

        int[] maxIn = new int[m];
//        int times = 0;

        for (int row = 0; row < n; row++) {
            //long t0 = System.currentTimeMillis();

            int[] maxOut = new int[m];
            for (int i = 0; i < m; i++) {
                maxOut[i] = Integer.MIN_VALUE;
            }

            int[] intervalStart = new int[m];
            int[] intervalEnd = new int[m];
            
            int[] sumTill = new int[m];
            sumTill[0] = A[row][0];
            for (int col = 1; col < m; col++) {
                sumTill[col] = sumTill[col - 1] + A[row][col];
            }

            int[] maxEndingAfter = new int[m];
            maxEndingAfter[m - 1] = sumTill[m - 1];
            intervalEnd[m - 1] = m - 1;
            for (int col = m - 2; col >= 0; col--) {
                if (sumTill[col] > maxEndingAfter[col + 1]) {
                    maxEndingAfter[col] = sumTill[col];
                    intervalEnd[col] = col;
                } else {
                    maxEndingAfter[col] = maxEndingAfter[col + 1];
                    intervalEnd[col] = intervalEnd[col + 1];
                }
            }

            int[] minEndingBefore = new int[m];
            minEndingBefore[0] = 0;
            intervalStart[0] = 0;
            for (int col = 1; col < m; col++) {
                if (sumTill[col - 1] < minEndingBefore[col - 1]) {
                    minEndingBefore[col] = sumTill[col - 1];
                    intervalStart[col] = col;
                } else {
                    minEndingBefore[col] = minEndingBefore[col - 1];
                    intervalStart[col] = intervalStart[col - 1];
                }
            }

            if (row == 0) {
                for (int col = 0; col < m; col++) {
                    maxOut[col] = maxEndingAfter[col] - minEndingBefore[col];
                }
            } else {

                Integer[] maxIndex = new Integer[m];
                for (int col = 0; col < m; col++) {
                    maxIndex[col] = col;
                }

                int[] finalMaxIn = maxIn;
                Arrays.sort(maxIndex, Comparator.comparing(idx -> -finalMaxIn[idx]));

                for (int inCol : maxIndex) {
                    int proposedMax = maxIn[inCol] + maxEndingAfter[inCol] - minEndingBefore[inCol];
                    maxOut[inCol] = proposedMax;
                }

                for (int inCol : maxIndex) {
                    int proposedMax;
                    // right
                    for (int outCol = inCol + 1; outCol < m; outCol++) {
//                        times++;
                        proposedMax = maxIn[inCol] + maxEndingAfter[Math.max(inCol, outCol)] - minEndingBefore[Math.min(inCol, outCol)];
                        if (maxOut[outCol] >= proposedMax) {
                            break;
                        }
                        maxOut[outCol] = proposedMax;
                    }

                    // left
                    for (int outCol = inCol - 1; outCol >= 0; outCol--) {
//                        times++;
                        proposedMax = maxIn[inCol] + maxEndingAfter[Math.max(inCol, outCol)] - minEndingBefore[Math.min(inCol, outCol)];
                        if (maxOut[outCol] >= proposedMax) {
                            break;
                        }
                        maxOut[outCol] = proposedMax;
                    }
                }
            }

            //System.err.println("Done with row #" + row + " in " + (System.currentTimeMillis() - t0) + "ms");
            maxIn = maxOut;
        }

        int maxOverall = Integer.MIN_VALUE;
        for (int m : maxIn) {
            maxOverall = Math.max(maxOverall, m);
        }
        return maxOverall;
    }


}
