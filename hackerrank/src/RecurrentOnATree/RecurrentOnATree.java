package RecurrentOnATree;

import org.junit.Test;

import java.io.*;
import java.util.*;

public class RecurrentOnATree {

    public static final int MOD = 1000000007;
    static HashMap<Long, Integer> ccache;
    static int[] smallF;
    static int[] parent;
    static int[][] ch;
    private static ArrayList<Integer>[] next;
    static int n;

    public static void main(String[] args) throws InterruptedException, IOException {
        long t0 = System.currentTimeMillis();
        prepare();

//        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader bi = new BufferedReader(new FileReader("src/RecurrentOnATree/case63.in"));
        n = Integer.parseInt(bi.readLine());

        setN(n);
        
        for (int i = 0; i < n - 1; i++) {
            String[] split = bi.readLine().split(" ");
            int u = Integer.parseInt(split[0]) - 1;
            int v = Integer.parseInt(split[1]) - 1;

            addEdge(u, v);
        }
        
        int[] c = new int[n];
        String[] split = bi.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            c[i] = Integer.parseInt(split[i]);
        }

        long res = solve(c);
        
        System.out.println(res);
        System.err.println(totalFib);
        System.err.println(System.currentTimeMillis() - t0);
    }

    static void setN(int n) {
        RecurrentOnATree.n = n;
        next = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            next[i] = new ArrayList<>();
        }
    }

    static void addEdge(int u, int v) {
        next[u].add(v);
        next[v].add(u);
    }

    static long solve(int[] c) {
        long t0 = System.currentTimeMillis();
        parent = new int[n];
        parent[0] = -1;
        ch = new int[n][];

        int[] q = new int[n];
        int S = 0, E = 1;

        // C sum from root to node
        long[] R = new long[n];
        R[0] = c[0];

        while (S < E) {
            int r = q[S++];
            ch[r] = new int[next[r].size() - (parent[r] == -1 ? 0 : 1)];
            int i = 0;
            
            for (int ch_i : next[r]) {
                if (ch_i != parent[r]) {
                    parent[ch_i] = r;
                    ch[r][i++] = ch_i;
                    q[E++] = ch_i;
                    R[ch_i] = R[r] + c[ch_i];
                }
            }
        }

//        System.err.println(System.currentTimeMillis() - t0);
        t0 = System.currentTimeMillis();

        long[] sumFib = new long[n];
        long[] sumFibMinus1 = new long[n];

        long res = 0;

        // start with leafs
        for (int i = q.length - 1; i >= 0; i--) {
            int r = q[i];
            
//            ccache.clear();
            t0 = System.currentTimeMillis();
            long fibC = fastFibonacciDoubling((long) c[r] - 2 * R[r]);
            long fibCMinus1 = fastFibonacciDoubling((long) c[r] - 2 * R[r] - 1);
            totalFib += System.currentTimeMillis() - t0;
            
            for (int ch_i : ch[r]) {
                sumFib[r] += sumFib[ch_i];
                sumFib[r] %= MOD;
                
                sumFibMinus1[r] += sumFibMinus1[ch_i];
                sumFibMinus1[r] %= MOD;
                
                res -= mult(sumFib[ch_i], sumFib[ch_i], fibC)
                        + mult(sumFibMinus1[ch_i], sumFibMinus1[ch_i], (fibC))
                        + mult(sumFibMinus1[ch_i], sumFibMinus1[ch_i], ( - fibCMinus1))
                        + 2 * mult(sumFib[ch_i], sumFibMinus1[ch_i], fibCMinus1);
                res %= MOD;
            }

//            ccache.clear();
            t0 = System.currentTimeMillis();
            sumFib[r] += fastFibonacciDoubling(R[r]);
            sumFib[r] %= MOD;
            
            sumFibMinus1[r] += fastFibonacciDoubling(R[r] - 1);
            sumFibMinus1[r] %= MOD;

            totalFib += System.currentTimeMillis() - t0;
            
            res += mult(sumFib[r], sumFib[r], fibC)
                    + mult(sumFibMinus1[r], sumFibMinus1[r], (fibC - fibCMinus1))
                    + 2 * mult(sumFib[r], sumFibMinus1[r], fibCMinus1);
            res %= MOD;
        }

//        System.err.println(System.currentTimeMillis() - t0);

        if (res < 0) res += MOD;
        return res;
    }

    static long mult(long a, long b, long c) {
        return ((((long) a * b) % MOD) * c) % MOD;
    }

    static void prepare(int size) {
        ccache = new HashMap<>();
        smallF = new int[size + 1];
        int a = 1, b = 1;
        int pos = 2;

        smallF[0] = 1;
        smallF[1] = 1;

        while (pos < smallF.length) {
            int c = (a + b) % MOD;
            a = b;
            b = c;
            smallF[pos++] = c;
        }
    }

    static long totalFib = 0;
    
    @Test
    public void test2() {
        prepare();
        System.out.println(fastFibonacciDoubling(-4));
        System.out.println(recFib(-4));
    }

    private static int fastFibonacciDoubling(long n) {
        n++;
        if (n == -1) return 0;
        if (n < 0) {
            int i = fastFibonacciDoubling(-n - 1);
            if (n % 2 == 0) {
                i = (-i + MOD) % MOD;
            }
            return i;
        }
        long a = 0;
        long b = 1;
//        long m = 0;
        for (int i = 63 - Long.numberOfLeadingZeros(n); i >= 0; i--) {
            // Loop invariant: a = F(m), b = F(m+1)
//            assert a.equals(slowFibonacci(m));
//            assert b.equals(slowFibonacci(m+1));

            // Double it
            long d = (a * ((b << 1) - a)) % MOD;
            long e = (a * a + b * b) % MOD;
            a = d;
            b = e;
//            m *= 2;
            
//            assert a.equals(slowFibonacci(m));
//            assert b.equals(slowFibonacci(m+1));

            // Advance by one conditionally
            if (((n >>> i) & 1) != 0) {
                long c = (a + b) % MOD;
                a = b;
                b = c;
//                m++;
//                assert a.equals(slowFibonacci(m));
//                assert b.equals(slowFibonacci(m+1));
            }
        }
        return (int) a;
    }
    
    static int recFib(long i) {
        if (i == -1) return 0;
        if (i < 0) {
            int res = recFib(-i-2);
            if (i % 2 == 0) {
                return res;
            } else {
                return (MOD - res) % MOD;
            }
        }
        
        if (i < smallF.length) return smallF[(int) i];
        if (ccache.containsKey(i)) return ccache.get(i);

        int res;

        if (i % 2 == 0) {
            if ((i / 2) % 2 == 0) {
                long fHalf = recFib(i / 2);
                long fHalfDown = recFib(i / 2 - 1);
                res = (int) ((fHalf * fHalf + fHalfDown * fHalfDown) % MOD);
            } else {
                long fHalfDown = recFib(i / 2 - 1);
                long fHalf = recFib(i / 2);
                res = (int) ((fHalf * fHalf + fHalfDown * fHalfDown) % MOD);
            }
        } else {
            if (ccache.containsKey(i / 2 - 1)) {
                long fHalf = recFib(i / 2);
                long fHalfDown = recFib(i / 2 - 1);
                res = (int) ((fHalf * fHalf + 2 * fHalf * fHalfDown) % MOD);
            } else {
                long fHalf = recFib(i / 2);
                long fHalfUp = recFib(i / 2 + 1);
                res = (int) ((-fHalf * fHalf + 2L * fHalf * fHalfUp) % MOD);
                if (res < 0) res += MOD;
            }
        }

        ccache.put(i, res);

        return res;
    }

    static void prepare() {
        prepare((int) 1e8);
    }
}
