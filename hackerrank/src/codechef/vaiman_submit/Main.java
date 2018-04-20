package codechef.vaiman_submit;

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

    static {
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

    public static void main(String[] args) throws IOException {
        readInput();
        int solve;
        solve = solve();
        System.out.println(solve);
    }

    private static void initGB() {
        g = new int[M];
        b = new int[M];
    }

    static class Hole implements Comparable<Hole> {
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
            if (g + b != o.g + o.b) return g + b > o.g + o.b ? 1 : -1;
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
        // start with calculating total
        long total = catTr(p - c, q, 1);
        
        TreeSet<Hole> holes = new TreeSet<>();
        for (int i = 0; i < M; i++) {
            holes.add(new Hole(g[i], b[i]));
        }

        for (Hole holeA : holes) {
            for (Hole holeB : holes) {
                if (holeA.before(holeB)) {
                    holeB.before.add(holeA);
                } else if (holeA.g + holeA.b < holeB.g + holeB.b) {
                    break;
                }
            }
        }

        for (Hole h : holes) {
            if (h.g > p || h.b > q) continue;
            if (h.b > 0 && h.g - h.b < c) continue;

            h.hitsFirst = h.fromStart();

            for (Hole before : h.before) {
                h.hitsFirst -= before.hitsFirst * before.toHole(h);
                h.hitsFirst %= MOD;
            }
            total -= h.hitsFirst * h.toEnd();
            total %= MOD;
        }

        return (int) ((total + MOD) % MOD);
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
        return FB(n).multiply(FB(k).multiply(FB(n - k)).mod(BMOD).modInverse(BMOD)).mod(BMOD).intValue();
    }

    private static BigInteger FB(int i) {
        return BigInteger.valueOf(F[i]);
    }

}
