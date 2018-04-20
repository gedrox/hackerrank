package codechef.wghtnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Main {
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public static final int MOD = 1_000_000_007;
    public static final BigInteger BMOD = BigInteger.valueOf(MOD);

    static int readInt() throws IOException {
        return (int) readLong();
    }
    
    static long readLong() throws IOException {
        long res = 0;
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
        StringBuilder sb = new StringBuilder();
        for (int t_i = 0; t_i < T; t_i++) {
            long N = readLong();
            int W = readInt();
            
            int base = W < 0 ? Math.max(0, 10 + W) : Math.max(0, 9 - W);

            long answer = BigInteger.TEN.modPow(BigInteger.valueOf(N - 2), BMOD).intValue();
            answer = (base * answer) % MOD;

            sb.append(answer).append('\n');
        }
        System.out.print(sb);
    }
}
