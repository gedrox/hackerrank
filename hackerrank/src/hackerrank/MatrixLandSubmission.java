package hackerrank;

import org.junit.Test;

import java.io.*;
import java.util.*;

public class MatrixLandSubmission {

    private static int n;
    private static int m;
    private static int[][] A;
    
    @Test
    public void test() throws IOException {
        main(null);
    }

    public static void main(String[] args) throws IOException {
        
        long t0 = System.currentTimeMillis();

//        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new FileReader("src/input20.txt"));
        String[] split = in.readLine().split(" ");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        A = new int[n][m];
        for (int A_i = 0; A_i < n; A_i++) {
//            System.err.println((System.currentTimeMillis() - t0) + "ms"); t0 = System.currentTimeMillis();
//            System.err.println((System.currentTimeMillis() - t0) + "ms"); t0 = System.currentTimeMillis();
//            split = line.split(" ");

            String line = in.readLine();
            StringTokenizer tknzr = new StringTokenizer(line);
            for (int A_j = 0; A_j < m; A_j++) {
                A[A_i][A_j] = Integer.parseInt(tknzr.nextToken());
            }
        }

        System.err.println((System.currentTimeMillis() - t0) + "ms"); t0 = System.currentTimeMillis();
        
        int result = matrixLand();
        System.out.println(result);
        in.close();
    }

    private static int matrixLand() {

        long t0 = System.currentTimeMillis();
        
        int[] maxIn = new int[m];
        
        Integer[] maxIndex = new Integer[m];
        if (n > 1) {
            for (int col = 0; col < m; col++) {
                maxIndex[col] = col;
            }
        }
        int[] sumTill = new int[m];
        int[] maxEndingAfter = new int[m];
        int[] minEndingBefore = new int[m];
        
        System.err.println((System.currentTimeMillis() - t0) + "ms x"); t0 = System.currentTimeMillis();
        
        for (int row = 0; row < n; row++) {
            //long t0 = System.currentTimeMillis();

            int[] maxOut = new int[m];
            for (int i = 0; i < m; i++) {
                maxOut[i] = Integer.MIN_VALUE;
            }
            
            sumTill[0] = A[row][0];
            for (int col = 1; col < m; col++) {
                sumTill[col] = sumTill[col - 1] + A[row][col];
            }

            maxEndingAfter[m - 1] = sumTill[m - 1];
            for (int col = m - 2; col >= 0; col--) {
                maxEndingAfter[col] = Math.max(maxEndingAfter[col + 1], sumTill[col]);
            }

            minEndingBefore[0] = 0;
            for (int col = 1; col < m; col++) {
                minEndingBefore[col] = Math.min(minEndingBefore[col - 1], sumTill[col - 1]);
            }

            if (row == 0) {
                for (int col = 0; col < m; col++) {
                    maxOut[col] = maxEndingAfter[col] - minEndingBefore[col];
                }
            } else {

                for (int inCol : maxIndex) {
                    int proposedMax = maxIn[inCol] + maxEndingAfter[inCol] - minEndingBefore[inCol];
                    maxOut[inCol] = proposedMax;
                }

                if (m > 2000) Arrays.sort(maxIndex, Comparator.comparing(idx -> -maxOut[idx]));

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

        System.err.println((System.currentTimeMillis() - t0) + "ms"); t0 = System.currentTimeMillis();

        int maxOverall = Integer.MIN_VALUE;
        for (int m : maxIn) {
            maxOverall = Math.max(maxOverall, m);
        }
        
        System.err.println((System.currentTimeMillis() - t0) + "ms"); t0 = System.currentTimeMillis();
        
        return maxOverall;
    }


}
