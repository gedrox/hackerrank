package expertComputation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExpertComputation2 {

    static final int MOD = 1_000_000_007;
    static int n;
    static int ch = -1;
    static BufferedReader bi;

    public static void main(String[] args) throws IOException {

        bi = new BufferedReader(new InputStreamReader(System.in));

        n = readVal();
        int[] x = new int[n + 1];
        int[] y = new int[n + 1];
        int[] z = new int[n + 1];

        readArray(x);
        readArray(y);
        readArray(z);

        int G_i = solve(x, y, z);

        System.out.println(G_i);
    }

    static int readVal() throws IOException {
        int sign = 1;
        int value = 0;
        while ((ch < '0' || ch > '9') && ch != '-') ch = bi.read();
        if (ch == '-') {
            sign = -1;
            ch = bi.read();
        }
        while (ch >= '0' && ch <= '9') {
            value *= 10;
            value += ch - '0';
            ch = bi.read();
        }
        value *= sign;
        return value;
    }

    private static int solve(int[] x, int[] y, int[] z) {
        int[] h = new int[n + 1];
        int[] c = new int[n + 1];
        int l_i;

        int G_i = 0;

        for (int i = 1; i <= n; i++) {

            if (i == 1) {
                h[1] = x[1];
                c[1] = y[1];
                l_i = z[1];
            } else {
                h[i] = x[i] ^ G_i;
                c[i] = y[i] ^ G_i;
                l_i = z[i] ^ G_i;
            }

            long F_i = Long.MIN_VALUE;

            for (int j = 1; j <= i - l_i; j++) {
                long candidate = (long) h[j] * c[i] - (long) c[j] * h[i];
                if (candidate > F_i) {
                    F_i = candidate;
                }
            }

            G_i = (int) (G_i + (F_i % MOD)) % MOD;
            if (G_i < 0) G_i += MOD;
        }
        
        return G_i;
    }

    private static void readArray(int[] h) throws IOException {
        for (int i = 1; i <= n; i++) h[i] = readVal();
    }

}
