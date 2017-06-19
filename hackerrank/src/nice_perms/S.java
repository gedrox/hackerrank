package nice_perms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class S {

    static int MOD = 1000000007;

    public static void main(String[] args) {

        int N = 1001;
        int c[][] = new int[N][];

        long a[][] = new long[N][];
        a[0] = new long[] {1};
//        a[1] = new long[] {0, 1};
        for (int n = 0; n < N - 1; n++) {
            a[n + 1] = new long[a[n].length + 1];
            long[] next = a[n + 1];
            for (int m = 0; m < a[n].length; m++) {
                if (a[n][m] != 0) {
                    int noSplit = 2 * n + 1 - m;
                    next[m + 1] += a[n][m] * noSplit;
                    next[m] += a[n][m] * (m + noSplit * (noSplit - 1) / 2);
                    if (m > 0) {
                        next[m - 1] += a[n][m] * (m * noSplit);
                    }
                    if (m > 1) {
                        next[m - 2] += a[n][m] * (m * (m - 1) / 2);
                    }
                }
            }

            for (int i = 0; i < next.length; i++) {
                next[i] %= MOD;
            }

            c[n + 1] = new int[2 * N];
            for (int z = 0; z <= 2000 - 2 * (n + 1); z++) {
//                c[n+1][z] =
            }
//            c[n + 1][0] = (int) next[0];
//            System.out.println(next[0]);
        }

        Scanner sc = new Scanner(System.in);

        int q = sc.nextInt();

        for (int q_i = 0; q_i < q; q_i++) {
            int n = sc.nextInt();
            HashSet<Integer> x = new HashSet<>();
            int c1 = 0;
            int c2 = 0;

            for (int i = 0; i < n; i++) {
                int a_i = sc.nextInt();
                if (x.add(a_i)) {
                    c1++;
                } else {
                    c2++;
                    c1--;
                }
            }

            long[] b = a[c2];
            int L = 2 * c2;
            for (int i = 0; i < c1; i++) {
                long[] next = new long[b.length];

                for (int m = 0; m < b.length; m++) {
                    next[m] += b[m] * (L + 1 - m);
                    if (m > 0) {
                        next[m - 1] += b[m] * m;
                    }
                }

                for (int j = 0; j < next.length; j++) {
                    next[j] %= MOD;
                }

                b = next;
                L++;
            }

            System.out.println(b[0]);
        }
    }
}
