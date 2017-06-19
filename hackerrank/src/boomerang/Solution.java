package boomerang;

import java.util.Arrays;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int T = in.nextInt();
        for (int i = 1; i <= T; i++) {
            int n = in.nextInt();
            int[] x = new int[n];
            int[] y = new int[n];
            for (int i1 = 0; i1 < n; i1++) {
                x[i1] = in.nextInt();
                y[i1] = in.nextInt();
            }

            System.out.println("Case #" + i + ": " + solve(x, y));
        }
//        System.out.println(solve(new int[]{0, 0, 0, 0, 0}, new int[]{0, 1, 2, 3, 4}));
    }

    static int solve(int[] x, int[] y) {
        int n = x.length;
        int[][] L = new int[n][];
        for (int i = 0; i < n; i++) {
            L[i] = new int[n];
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j) {
                    int d = (x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]);
                    L[i][j] = d;
                    L[j][i] = d;
                }
            }
        }

        for (int i = 0; i < n; i++) {
            Arrays.sort(L[i]);
        }

        int res = 0;

        for (int i = 0; i < n; i++) {
            int last = L[i][0];
            int count = 1;
            for (int j = 1; j <= n; j++) {
                if (j == n || last != L[i][j]) {
                    res += count * (count - 1) / 2;
                    count = 1;
                    if (j != n) last = L[i][j];
                } else {
                    count++;
                }
            }
        }

        return res;
    }
}
