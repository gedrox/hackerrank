package definerandomwalks;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Scanner;
import java.util.stream.LongStream;

public class BitBetterMaybe {
    public static final long MOD = 998244353;
    public static final BigInteger BIG_MOD = BigInteger.valueOf(MOD);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        long t0 = System.currentTimeMillis();
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();

        int[] next = new int[n];
        for (int f_i = 0; f_i < n; f_i++) {
            next[f_i] = in.nextInt() - 1;
        }
        long[] p = new long[m];
        for (int p_i = 0; p_i < m; p_i++) {
            p[p_i] = in.nextInt();
        }

//        // Maybe we're lucky and all are in loops?
//        if (m * k > 160000) {
//            long answ = divMod(1, n);
//            for (int i = 0; i < n; i++) {
//                System.out.println(answ);
//            }
//            return;
//        }

        long[] q = calcQ(m, p, k);
        long[][] c = calcC(n, m, k, next);
        long[] answer = solve(n, q, c);

        for (long row : answer) {
            System.out.println(row);
        }
//        System.err.println(System.currentTimeMillis() - t0);
    }

    // x/y mod MOD
    static long divMod(long x, long y) {
        return BigInteger.valueOf(y)
                .modInverse(BIG_MOD)
                .multiply(BigInteger.valueOf(x))
                .mod(BIG_MOD)
                .longValue();
    }

    // O(n * m * k)
    static long[] solve(int n, long[] Q, long[][] C) {
        long[] res = new long[n];
        for (int node = 0; node < n; node++) {
            res[node] = solve(n, Q, C, node);
        }
        return res;
    }

    // O(m * k)
    static long solve(long n, long[] Q, long[][] C, int node) {
        long res = 0;
        for (int steps = 0; steps < Q.length; steps++) {
            res = (long) (((long) res + (long) Q[steps] * C[node][steps]) % MOD);
        }
        return divMod((long) res, n);
    }

    // Q - probabilities of going particular distance
    static long[] calcQ(int m, long[] p, long k) {
        assert p.length == m;

        // precalculate p/sum(p)
        long[] pInv = new long[m];
        long pSum = LongStream.of(p).sum() % MOD;
        long pSumInv = divMod(1, pSum);
        for (int i = 0; i < m; i++) {
            pInv[i] = (long) ((((long) p[i]) * pSumInv) % MOD);
        }

        // way too slow
        long[] Q = new long[1];
        Q[0] = 1;
        for (int i = 1; i <= k; i++) {
            int size = i * (m - 1) + 1;
            long[] newQ = new long[size];
            for (int j = 0; j < Q.length; j++) {
                for (int t = 0; t < m; t++) {
                    newQ[j + t] = (long) (((long) newQ[j + t] + (long) Q[j] * pInv[t]) % MOD);
                }
            }
            Q = newQ;
        }

        return Q;
    }

    // count of ways to get to node i with total jumps of j
    static long[][] calcC(int n, int m, int k, int[] next) {
        long[][] steps = new long[n][];

        for (int i = 0; i < n; i++) {
            steps[i] = new long[(m - 1) * k + 1];
            steps[i][0] = 1;
        }

        for (int i = 1; i <= (m - 1) * k; i++) {
            for (int j = 0; j < n; j++) {
                steps[next[j]][i] = (long) (((long) steps[next[j]][i] + steps[j][i - 1]) % MOD);
            }
        }

        return steps;
    }
    
    @Test
    public void test() {
        
        int n = 60000;
        int m = 10;
        int k = 1000;
        long[] p = new long[m];
        for (int i = 0; i < m; i++) {
            p[i] = 1;
        }
        
        long[] q = calcQ(m, p, k);
//        long[][] c = calcC(n, m, k, next);
//        long[] answer = solve(n, q, c);
    }
    
    @Test
    public void test222() {
        int[][] a = new int[2][];
        int[][] b = new int[2][2];

        System.out.println();
    }

}

