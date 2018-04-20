package expertComputation;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class ExpertComputation {

    public static final int MOD = 1_000_000_007;
    static int ch = -1;
    static BufferedReader bi;
    private static int n;

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

    private static int solve(int[] x, int[] y, int[] z) {
        int[] h = new int[n + 1];
        int[] c = new int[n + 1];
        int l_i;

        int G_i = 0;

//        int split = 10;
//        TreeSet<Row>[] ordered = new TreeSet[split];
//        double[] ratio = new double[split];
//        for (int i = 0; i < split; i++) {
//            double finalI = Math.PI * i / 2 / split;
//            ordered[i] = new TreeSet<>(Comparator.comparing(row -> -Math.cos(finalI) * row.h + Math.sin(finalI) * row.c));
//            ratio[i] = Math.tan(finalI);
//        }

//        nextOne:
        for (int i = 1; i <= n; i++) {

            h[i] = x[i] ^ G_i;
            c[i] = y[i] ^ G_i;
            l_i = z[i] ^ G_i;

            // guess next G_i
//            if (i != n && false) {
//                for (int l_guess = 0; l_guess < 100; l_guess++) {
//                    int G_guess = l_guess ^ z[i + 1];
//                    int h_guess = G_guess ^ x[i + 1];
//                    if (h_guess <= h[i] || h_guess > 10_000_000) continue;
//                    int c_guess = G_guess ^ y[i + 1];
//                    if (c_guess < 1 || c_guess > 10_000_000) continue;
//
//                    // good enough...
//                    G_i = G_guess;
//
//                    System.err.println("My guess is G[" + i + "] = " + G_i);
//
//                    continue nextOne;
//                }
//            }

//            assert h[i] > h[i - 1];
//            assert h[i] <= 10_000_000;
//            assert c[i] >= 1;
//            assert c[i] <= 10_000_000;

//            Row e = new Row(i, h[i], c[i]);
//            for (int i1 = 0; i1 < split; i1++) {
//                ordered[i1].add(e);
//            }

//            long F_i = Long.MIN_VALUE;
//
////            assert l[i] >= 0;
////            assert l[i] <= i - 1;
//
////            if (true || i - l[i] < 100) {
//            for (int j = 1; j <= i - l_i; j++) {
//                long candidate = (long) h[j] * c[i] - (long) c[j] * h[i];
//                if (candidate > F_i) {
//                    F_i = candidate;
//                }
//            }
////            } else {
////
////                double tan = 1d * h[i] / c[i];
////                int pos = Arrays.binarySearch(ratio, tan);
////                if (pos < 0) pos = -pos - 2;
////
////                long limitH = 0;
////                for (Row row : ordered[pos]) {
////                    int j = row.index;
////                    if (h[j] <= limitH) break;
////                    if (row.index > i - l[i]) continue;
////                    long candidate = (long) h[j] * c[i] - (long) c[j] * h[i];
////                    if (candidate > F_i) {
////                        F_i = candidate;
////                        limitH = (candidate + h[i]) / c[i];
////                    }
////                }
////            }
//
////            if (F_i == Long.MIN_VALUE) {
////                throw new RuntimeException();
////            }
//
//            G_i = (int) (G_i + (F_i % MOD)) % MOD;
//            if (G_i < 0) G_i += MOD;
            
            G_i = nextG(h, c, l_i, i, G_i);

//            System.err.println("F[" + i + "] = " + F_i);
//            System.err.println("G[" + i + "] = " + G_i);
        }
        return G_i;
    }

    private static void readArray(int[] h) throws IOException {
        for (int i = 1; i <= n; i++) h[i] = readVal();
    }

    static int nextG(int[] h, int[] c, int l_i, int i, int G_i1) {
        long max = Long.MIN_VALUE;
        for (int j = i - l_i; j > 0; j--) {
            long cand = (long) h[j] * c[i] - (long) c[j] * h[i];
            max = Math.max(max, cand);
        }
//        System.err.println(100d * "");

        int G_i = (int) (((long) G_i1 + max) % MOD);
        if (G_i < 0) G_i += MOD;
        return G_i;
    }

    @Test
    public void test() {
        n = 3;
        int[] x = {0, 2, 6, 4};
        int[] y = {0, 1, 9, 11};
        int[] z = {0, 0, 0, 14};

        int solve = solve(x, y, z);
        System.out.println(solve);
//        for (int i = 0; i < n; i++) {
//            x[i] = 
//        }
//        solve()
    }

    @Test
    public void test1() {
        n = 5;

        int[] x = {0, 3, 4, 14, 5, 6};
        int[] y = {0, 4, 9, 12, 8, 9};
        int[] z = {0, 0, 0, 9, 12, 13};

        int solve = solve(x, y, z);
        System.out.println(solve);
    }

    @Test
    public void build() {
        
        long t0 = System.currentTimeMillis();
        
        n = 0;
        int[] x = new int[1_000_001];
        int[] y = new int[1_000_001];
        int[] z = new int[1_000_001];

        int[] h = new int[1_000_001];
        int[] c = new int[1_000_001];
        int[] j = new int[1_000_001];

        int[] G = new int[1_000_001];

        Random r = new Random(0);

        int N = 50_000;
        for (int i = 1; i <= N; i++) {
            n = i;

            h[i] = h[i - 1] + 1 + r.nextInt(15);
            c[i] = r.nextInt(10_000_000) + 1;
            j[i] = r.nextInt(i);

            x[i] = h[i] ^ G[i - 1];
            y[i] = c[i] ^ G[i - 1];
            z[i] = j[i] ^ G[i - 1];

            G[i] = nextG(h, c, j[i], i, G[i - 1]);
        }
        System.out.println((System.currentTimeMillis() - t0) / 1000d + "s");


//        System.out.println(Arrays.toString(Arrays.copyOfRange(x, 1, n + 1)));
//        System.out.println(Arrays.toString(Arrays.copyOfRange(y, 1, n + 1)));
//        System.out.println(Arrays.toString(Arrays.copyOfRange(z, 1, n + 1)));

        t0 = System.currentTimeMillis();
        int test = solve(x, y, z);
        assert test == G[n];
        System.out.println((System.currentTimeMillis() - t0) / 1000d + "s");
    }

    private String join(int[] x) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        Arrays.stream(Arrays.copyOfRange(x, 1, n + 1))
                .forEach(i -> stringJoiner.add("" + i));
        return stringJoiner.toString();
    }

//    static class Row implements Comparable<Row> {
//        int index, h, c;
//
//        public Row(int index, int h, int c) {
//            this.index = index;
//            this.h = h;
//            this.c = c;
//        }
//
//        @Override
//        public int compareTo(Row o) {
//            return Integer.compare(o.h, this.h);
//        }
//    }
}
