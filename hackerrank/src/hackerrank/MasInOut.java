package hackerrank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class MasInOut {

    static int n;
    static int m;
    static int[][] A;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
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
        int result = solve();
        System.out.println(result);
        in.close();
    }

    static int solve() {
        int[] maxIn = new int[m];
        int[] sumTill = new int[m];
        int[] maxEndingAfter = new int[m];
        int[] minEndingBefore = new int[m];

        for (int row = 0; row < n; row++) {
            int[] maxOut = new int[m];
            for (int j = 0; j < m; j++) {
                maxOut[j] = Integer.MIN_VALUE;
            }

            sumTill[0] = A[row][0];
            for (int col = 1; col < m; col++) {
                sumTill[col] = sumTill[col - 1] + A[row][col];
            }

            maxEndingAfter[m - 1] = sumTill[m - 1];
            for (int col = m - 2; col >= 0; col--) {
                maxEndingAfter[col] = Math.max(sumTill[col], maxEndingAfter[col + 1]);
            }

            minEndingBefore[0] = 0;
            for (int col = 1; col < m; col++) {
                minEndingBefore[col] = Math.min(sumTill[col - 1], minEndingBefore[col - 1]);
            }

            for (int dir : new int[] {1, -1}) {
                int start = dir > 0 ? 0 : m - 1;
                
                int[] fromTop = new int[m];
                fromTop[start] = maxIn[start] + A[row][start];
                for (int col = start + dir; col < m && col >= 0; col = col + dir) {
                    int greedy;
                    if (dir < 0) {
                        greedy = (col - dir < 0 || col - dir >= m) ? 0 : Math.max(0, maxEndingAfter[col - dir] - sumTill[col]);
                    } else {
                        greedy = (col - dir < 0 || col - dir >= m) ? 0 : Math.max(0, sumTill[col - dir] - minEndingBefore[col]);
                    }
                    fromTop[col] = Math.max(fromTop[col - dir], maxIn[col] + greedy) + A[row][col];
                }
                for (int col = 0; col < m; col++) {
                    int greedy;
                    if (dir > 0) {
                        greedy = (col + dir < 0 || col + dir >= m) ? 0 : Math.max(0, maxEndingAfter[col + dir] - sumTill[col]);
                    } else {
                        greedy = (col + dir < 0 || col + dir >= m) ? 0 : Math.max(0, sumTill[col + dir] - minEndingBefore[col]);
                    }
                    maxOut[col] = Math.max(maxOut[col], fromTop[col] + greedy);
                }
            }

            maxIn = maxOut;
//            System.err.println(Arrays.toString(maxIn));
        }

        int max = 0;
        for (int i : maxIn) {
            max = Math.max(i, max);
        }
        
        return max;
    }
}
