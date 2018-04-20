package codechef.avgpr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static int readInt() throws IOException {
        int res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }
    
    public static void main(String[] args) throws IOException {
        int T = readInt();
        for (int t_i = 0; t_i < T; t_i++) {
            int N = readInt();
            int[] A = new int[N];
            for (int i = 0; i < N; i++) A[i] = readInt();

            int[] C = new int[2 * 1_000 + 1];
            for (int a : A) C[a + 1_000]++;

            long res = 0;
            for (int i = 0; i <= 2_000; i++) {
                res += (long) C[i] * (C[i] - 1) / 2;
                for (int j = i + 2; j <= 2_000; j += 2) {
                    if (C[(i + j) / 2] > 0) res += (long) C[i] * C[j];
                }
            }

            System.out.println(res);
        }
    }
}
