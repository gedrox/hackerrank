package rotatematrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MatrixRotate {

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] split = bi.readLine().split(" ");
        int n = Integer.parseInt(split[0]);
        int m = Integer.parseInt(split[1]);
        int r = Integer.parseInt(split[2]);
        int a[][];

        a = new int[n][m];
        for (int i = 0; i < n; i++) {
            split = bi.readLine().split(" ");
            for (int j = 0; j < m; j++) {
                a[i][j] = Integer.parseInt(split[j]);
            }
        }
        rotate(n, m, a, r);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int i1 = 0; i1 < m; i1++) {
                sb.append(a[i][i1]).append(" ");
            }
            sb.append('\n');
        }

        System.out.print(sb.toString());
    }

    static void rotate(int n, int m, int[][] a, int r) {
        for (int i = 0; i < Math.min(n, m) / 2; i++) {
            int p = 2*(n - 2*i - 1) + 2*(m - 2*i - 1);
            int[] s = new int[p];

            int s_i = 0;
            for (int j1 = i; j1 < m - i - 1; j1++) {
                s[s_i++] = a[i][j1];
            }
            for (int i1 = i; i1 < n - i - 1; i1++) {
                s[s_i++] = a[i1][m - i - 1];
            }
            for (int j1 = m - i - 1; j1 > i; j1--) {
                s[s_i++] = a[n - i - 1][j1];
            }
            for (int i1 = n - i - 1; i1 > i; i1--) {
                s[s_i++] = a[i1][i];
            }

            s_i = r % p;

            for (int j1 = i; j1 < m - i - 1; j1++) {
                a[i][j1] = s[s_i++ % p];
            }
            for (int i1 = i; i1 < n - i - 1; i1++) {
                a[i1][m - i - 1] = s[s_i++ % p];
            }
            for (int j1 = m - i - 1; j1 > i; j1--) {
                a[n - i - 1][j1] = s[s_i++ % p];
            }
            for (int i1 = n - i - 1; i1 > i; i1--) {
                a[i1][i] = s[s_i++ % p];
            }
        }
    }

}
