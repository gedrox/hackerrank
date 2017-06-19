package definerandomwalks;

import org.junit.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.LongStream;

public class DefineRandomWalks {
    public static final int MOD = 998244353;
    public static final BigInteger BIG_MOD = BigInteger.valueOf(MOD);

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        int k = in.nextInt();

//        if (m * k > 160000) throw new RuntimeException("too much..");

        int[] next = new int[n];
        for (int f_i = 0; f_i < n; f_i++) {
            next[f_i] = in.nextInt() - 1;
        }
        long[] p = new long[m];
        for (int p_i = 0; p_i < m; p_i++) {
            p[p_i] = in.nextInt();
        }

        long t0 = System.currentTimeMillis();
        int[] inLoop = getLoop(n, next);
        long[] q = calcQ(m, p, k);
        long[][] c = calcC(n, m, k, next, inLoop);
        long[] answer = solve(n, q, c, inLoop);

        for (long row : answer) {
            System.out.println(row);
        }
        System.err.println(System.currentTimeMillis() - t0);
    }

    private static int[] getLoop(int n, int[] next) {
        int[] inLoop = new int[n];
        int[] visited = new int[n];
        
        int[] depth = new int[n];

        nextI:
        for (int i = 0; i < n; i++) {
            // not yet in loop
            if (inLoop[i] == 0) {
                int counter = -1;
                int go = i;
                while (visited[go] != i + 1) {
//                    if (inLoop[go] != 0) {
//                        depth[i] = - counter - depth[go];
//                        continue nextI;
//                    }
                    visited[go] = i + 1;
                    go = next[go];
                    inLoop[go] = counter--;
                }

                depth[i] = - counter;

                int loopSize = inLoop[go] - counter;

                while (inLoop[go] < 0) {
                    inLoop[go] = loopSize;
                    go = next[go];
                }

                // set
            }
        }
        return inLoop;
    }

    // x/y mod MOD
    static long divMod(long x, long y) {
        return BigInteger.valueOf(y)
                .modInverse(BIG_MOD)
                .multiply(BigInteger.valueOf(x))
                .mod(BIG_MOD)
                .longValue();
    }

    // O(n * m * k) :((
    static long[] solve(int n, long[] Q, long[][] C, int[] inLoop) {
        long[] res = new long[n];
        long inv1 = divMod(1, n);
        for (int node = 0; node < n; node++) {
            res[node] = solve(n, Q, C, node, inLoop[node]);
            if (inLoop[node] > 0) {
                res[node] += inv1;
                res[node] %= MOD;
            }
        }
        return res;
    }

    // O(m * k)
    static long solve(long n, long[] Q, long[][] C, int node, int loopSize) {
        long res = 0;
        for (int steps = 0; steps < Q.length; steps++) {
            if (loopSize <= 0 && C[node][steps] == 0) {
                break;
            }
            res = (res + Q[steps] * C[node][steps]) % MOD;
        }
        return divMod(res, n);
    }

    // Q - probabilities of going particular distance
    static long[] 
    calcQ(int m, long[] p, long k) {
        assert p.length == m;

        // precalculate p/sum(p)
        long[] pInv = new long[m];
        long pSum = LongStream.of(p).sum() % MOD;
        long pSumInv = divMod(1, pSum);
        for (int i = 0; i < m; i++) {
            pInv[i] = (p[i] * pSumInv) % MOD;
        }

        System.err.println("first ok");

        // way too slow
        long[] Q = new long[1];
        Q[0] = 1;
        for (int i = 1; i <= k; i++) {
            int size = i * (m - 1) + 1;
            long[] newQ = new long[size];
            for (int j = 0; j < Q.length; j++) {
                for (int t = 0; t < m; t++) {
                    newQ[j + t] = ((long) newQ[j + t] + (long) Q[j] * pInv[t]) % MOD;
                }
            }
            Q = newQ;
            System.out.println(i);
        }

        return Q;
    }

    // count of ways to get to node i with total jumps of j
    static long[][] calcC(int n, int m, int k, int[] next, int[] inLoop) {
        ArrayList<Long>[] steps = new ArrayList[n];

//        LinkedList<int[]> q = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            steps[i] = new ArrayList<>();
            if (inLoop[i] <= 0) {
                steps[i].add(0, 1L);
//                q.add(new int[]{i, 0});


            }
        }

//        while (!q.isEmpty()) {
//            int[] el = q.pollFirst();
//
//        }

//        long[][] steps = new long[n][];
//        for (int i = 0; i < n; i++) {
//            steps[i] = new long[(m - 1) * k + 1];
//            if (inLoop[i] <= 0) {
//                steps[i][0] = 1L;
//            }
//        }
//
//        for (int i = 1; i <= (m - 1) * k; i++) {
//            for (int j = 0; j < n; j++) {
//                steps[next[j]][i] = (steps[next[j]][i] + steps[j][i - 1]) % MOD;
//            }
//        }
//
//        return steps;
        return null;
    }

    @Test
    public void testQ() {
        int m = 100000;
//        long[] p = {332748118, 332748118, 332748118, 0, 0};
        long[] p = new long[m];
        for (int i = 0; i < m; i++) {
            p[i] = i + 1;
        }

        long t0 = System.currentTimeMillis();
        long[] Q = calcQ(m, p, 1000);
        System.err.println("Spent " + (System.currentTimeMillis() - t0) + "ms");

        System.out.println(Q.length);
    }

    @Test
    public void testC() {
        Random rand = new Random();
        int n = 4; //6e4;
        int m = 100; //1e5;
        int k = 1000; //1e3;

//        int[] next = new int[]{1, 1};
        int[] next = new int[]{1, 2, 1, 3};
//        int[] next = new long[n];
//        for (long i = 0; i < n; i++) {
//            next[i] = rand.nextInt(n);
//        }

//        long[] p = {1, 1};
        long[] p = new long[m];
        for (int i = 0; i < m; i++) {
            p[i] = rand.nextInt(MOD);
        }

        int[] inLoop = getLoop(n, next);
        long[] q = calcQ(m, p, k);
        long[][] c = calcC(n, m, k, next, inLoop);
        long[] answer = solve(n, q, c, inLoop);

        System.out.println(Arrays.toString(answer));

        System.out.println(divMod(1, 8));
        System.out.println(divMod(7, 8));
    }

    @Test
    public void testCase() {
        int n = 4, m = 5, k = 1;
        int[] next = {1, 2, 1, 3};
        long[] p = {332748118, 332748118, 332748118, 0, 0};

        int[] inLoop = getLoop(n, next);
        long[] q = calcQ(m, p, k);
        long[][] c = calcC(n, m, k, next, inLoop);
        long[] answer = solve(n, q, c, inLoop);

        System.out.println(Arrays.toString(answer));

    }

    @Test
    public void testSum() {
        int k = 10;
        double[] p = {.25, .25, .25, .25};
        int m = p.length;

        // repeat k times, (p_0 + p_1 + p_2 + ... + p_(m-1))^k
        // but let's calculate every second
//        double[][] q =

    }

    @Test
    public void testLoop() {
        int n = 60000;
        int m = 100000;
        int k = 1000;

        int[] next = new int[n];
        for (int i = 0; i < n; i++) {
            next[i] = (i + 1) % n;
        }

        long[] p = new long[m];
        for (int i = 0; i < m; i++) {
            p[i] = 1;
        }

        int[] inLoop = getLoop(n, next);
        System.err.println("in loop done");
        long[][] c = calcC(n, m, k, next, inLoop);
        System.err.println("c");
        long[] q = calcQ(m, p, k);
        System.err.println("q");
        long[] answer = solve(n, q, c, inLoop);
        System.err.println("answer");

        System.out.println(answer[0]);
    }
}

