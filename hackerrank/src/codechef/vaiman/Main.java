package codechef.vaiman;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.*;

public class Main {

    static final int MOD = 1_000_000_007;
    static final BigInteger BMOD = BigInteger.valueOf(MOD);
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int p, q, c, M;
    static int[] g = {}, b = {};
    static int[] F = new int[5_000_000];

    static void init() {
        F[0] = 1;
        for (int i = 1; i < F.length; i++) {
            F[i] = (int) (((long) F[i - 1] * i) % MOD);
        }
    }

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

//    public static void main(String[] args) throws IOException {
//        readInput();
//        int solve;
//        solve = solve();
//        System.out.println(solve);
//    }

    static int trivial() {

        HashMap<Integer, HashSet<Integer>> holes = new HashMap<>();
        for (int i = 0; i < M; i++) {
            holes.computeIfAbsent(g[i], HashSet::new).add(b[i]);
        }

        long[] col = {1};
        for (int goodDone = 1; goodDone <= p; goodDone++) {
            int maxBad = Math.min(q, Math.max(0, goodDone - c));
            long[] newCol = new long[maxBad + 1];
            for (int badDone = 0; badDone < newCol.length; badDone++) {
                if (holes.containsKey(goodDone) && holes.get(goodDone).contains(badDone)) {
                    newCol[badDone] = 0;
                } else {
                    if (badDone > 0) {
                        newCol[badDone] += newCol[badDone - 1];
                    }
                    if (badDone < col.length) {
                        newCol[badDone] += col[badDone];
                    }
                    newCol[badDone] %= MOD;
                }
            }

            col = newCol;
        }

        return q < col.length ? (int) col[q] : 0;
    }

    @Test
    public void sample() {
        p = 5;
        q = 2;
        c = 2;
        M = 6;
        g = new int[] {3,1,4,5,2,5};
        b = new int[] {4,5,4,0,2,0};

        solveAndCompare();
    }

    @Test
    public void onlyGood() {
        p = 5;
        q = 0;
        c = 6;
        M = 0;
        initGB();

        solveAndCompare();
    }

    @Test
    public void oneHole() {
        p = 5;
        q = 5;
        c = 0;
        M = 1;
        g = new int[] {2};
        b = new int[] {0};

        solveAndCompare();
    }

    @Test
    public void holesInEnd() {
        p = 10;
        q = 10;
        c = 0;
        M = 1;
        g = new int[] {10};
        b = new int[] {10};

        solveAndCompare();
    }

    @Test
    public void manyHoles() {
        p = 10;
        q = 10;
        c = 0;
        M = 5;
        g = new int[] {6, 5, 1, 100, 10};
        b = new int[] {1, 2, 6, 0, 0};

        solveAndCompare();
    }

    @Test
    public void twoBadHoles() {
        p = 10;
        q = 10;
        c = 0;
        M = 2;
        g = new int[] {2, 5};
        b = new int[] {2, 5};

        solveAndCompare();
    }

    @Test
    public void threeDeadlyBadHoles() {
        p = 10;
        q = 10;
        c = 0;
        M = 3;
        g = new int[] {2, 5, 8};
        b = new int[] {2, 5, 6};

        solveAndCompare();
    }
    
    @Test
    public void timeTest() {
        Random r = new Random(0);
        p = 2_000_000;
        q = 2_000_000;
        c = 0;
        M = 3_000;// M = 10;
        initGB();
        for (int i = 0; i < M; i++) {
            g[i] = r.nextInt(p + 1);
            b[i] = r.nextInt(g[i] + 1);
        }
        
//        M = 6;
//        g = new int[] {2, 3, 1_000_000, 1_500_000, 1_789_987, 2_000_000};
//        b = new int[] {3, 2, 1_000_000, 1_500_000, 1_345_876, 1_000_000};
        
        solveAndCompare();
    }

    private static void initGB() {
        g = new int[M];
        b = new int[M];
    }

    private void solveAndCompare() {
        assert M == g.length;
        assert M == b.length;
        
        int solve = solve();
        System.out.println(solve);
        TreeSet<Correct.Constraint> cs = new TreeSet<>();
        for (int i = 0; i < M; i++) {
            cs.add(new Correct.Constraint(g[i], b[i], false));
        }

//        int trivial = trivial();
        Assert.assertEquals(Correct.solve(p, q, c, cs), solve);
    }
    
    static final class Hole implements Comparable<Hole> {
        int g, b;
        ArrayList<Hole> before = new ArrayList<>();
        long hitsFirst;

        public Hole(int g, int b) {
            this.g = g;
            this.b = b;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Hole hole = (Hole) o;
            return g == hole.g && b == hole.b;
        }

        @Override
        public int hashCode() {
            return Objects.hash(g, b);
        }

        @Override
        public int compareTo(Hole o) {
            if (equals(o)) return 0;
            if ((long) g + b != (long) o.g + o.b) return (long) g + b > (long) o.g + o.b ? 1 : -1;
            return g > o.g ? 1 : -1;
        }

        public boolean before(Hole o) {
            return !this.equals(o) && g <= o.g && b <= o.b;
        }
        
        long fromStart() {
            return catTr(this.g - c, this.b, 1); 
        }
        
        long toHole(Hole o) {
            return catTr(o.g - this.g, o.b - this.b, this.g - this.b - c + 1);
        }
        
        long toEnd() {
            return catTr(p - this.g, q - this.b, this.g - this.b - c + 1);
        }
    }

    private static int solve() {
        
        if (F[0] == 0) init();
        
        long t0 = System.currentTimeMillis();
        
        // start with calculating total
        long total = catTr(p - c, q, 1);

        System.err.println(System.currentTimeMillis() - t0);
        t0 = System.currentTimeMillis();
        
        
        TreeSet<Hole> holes = new TreeSet<>();
        for (int i = 0; i < M; i++) {
            Hole h = new Hole(g[i], b[i]);
            if (h.g > p || h.b > q) continue;
            if (h.b > 0 && h.g - h.b < c) continue;
            holes.add(h);
        }

        System.err.println(System.currentTimeMillis() - t0);
        t0 = System.currentTimeMillis();

        for (Hole holeA : holes) {
            for (Hole holeB : holes) {
                if (holeB.before(holeA)) {
                    holeA.before.add(holeB);
                } else if ((long) holeA.g + holeA.b < (long) holeB.g + holeB.b) {
                    break;
                }
            }
        }
        
        Hole[] holes2 = holes.toArray(new Hole[0]);

        System.err.println(System.currentTimeMillis() - t0);
        t0 = System.currentTimeMillis();
        int times=0;
        
        for (Hole h : holes2) {
            h.hitsFirst = h.fromStart();

            for (Hole before : h.before) {
                long diff = before.hitsFirst * before.toHole(h);
                h.hitsFirst = (h.hitsFirst - diff) % MOD;
                times++;
            }

//            for (Hole before : holes2) {
//                if (before.before(h)) {
//                    long diff = before.hitsFirst * catTr(h.g - before.g, h.b - before.b, before.g - before.b - c + 1);
//                    h.hitsFirst = (h.hitsFirst - diff) % MOD;
//                } else if ((long) h.g + h.b < (long) before.g + before.b) {
//                    break;
//                }
//            }
            
            total -= (h.hitsFirst * h.toEnd()) % MOD;
            total %= MOD;
        }

        System.err.println(System.currentTimeMillis() - t0);
        System.err.println(times);

        return (int) ((total + MOD) % MOD);
    }

    private static long oldLogic(long total) {
        int shortestZero = Integer.MAX_VALUE;
        TreeMap<Integer, Integer> patterns = new TreeMap<>();
        for (int i = 0; i < M; i++) {
            if (g[i] > p) continue;
            if (b[i] > q) continue;
            if (b[i] > 0 && g[i] - (b[i] - 1) <= c) continue;

            if (b[i] == 0) {
                shortestZero = Math.min(shortestZero, g[i]);
            } else {
                if (patterns.containsKey(g[i])) {
                    patterns.put(g[i], Math.min(b[i], patterns.get(g[i])));
                } else {
                    patterns.put(g[i], b[i]);
                }
            }
        }

        while (!patterns.isEmpty() && patterns.lastKey() >= shortestZero) {
            patterns.remove(patterns.lastKey());
        }

        if (shortestZero != Integer.MAX_VALUE) {
            patterns.put(shortestZero, 0);
        }

        for (Map.Entry<Integer, Integer> e : patterns.entrySet()) {
            int good = e.getKey();
            int bad = e.getValue();
            total -= catTr(p - good, q - bad, good - (p - c) + 1 - bad);
            total += MOD;
            total %= MOD;
        }
        return total;
    }

    static void readInput() throws IOException {
        p = readInt();
        q = readInt();
        c = readInt();
        M = readInt();

        initGB();

        for (int i = 0; i < M; i++) {
            g[i] = readInt();
            b[i] = readInt();
        }
    }
    
    @Test
    public void showTable() {
        int cnt = 4;
        for (int n = 0; n < cnt; n++) {
            for (int k = 0; k < cnt; k++) {
                System.out.printf("%6d", catTr(n, k, 1));
            }
            System.out.println();
        }
        System.out.println();
        for (int n = 0; n < cnt; n++) {
            for (int k = 0; k < cnt; k++) {
                System.out.printf("%6d", catTr(n, k, 1) == 0 ? 0 : 
                        catTr(cnt - 1, cnt - 1, 1) - catTr(n, k, 1) * catTr(cnt - 1 - n, cnt - 1 - k, n - k + 1));
            }
            System.out.println();
        }
    }

    static long catTr(int n, int k, int m) {
        
        if (n < 0 && k == 0) {
            return 1;
        }
        
        if (k >= n + m) {
            return 0;
        }

        long v = C(n + k, k);

        if (k < m) {
            return (int) v;
        } else {
            return (int) ((v - C(n + k, k - m) + MOD) % MOD);
        }
    }

    private static int C(int n, int k) {
        return (int) ((long) F[n] * MOD_INV((int) (((long) F[k] * F[n - k]) % MOD)) % MOD);
    }

    private static int MOD_INV(int i) {
        int pow = MOD - 2;
        int ipow = i;
        int res = 1;
        while (pow != 0) {
            if (pow % 2 == 1) res = (int) (((long)res * ipow) % MOD);
            pow >>= 1;
            ipow = (int) (((long) ipow * ipow) % MOD);
        }
        return res;
    }

}
