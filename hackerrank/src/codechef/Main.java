package codechef;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.util.TreeSet;
 
public class Main {
 
    private static final long MOD = 1_000_000_007;
    private static boolean ok[][];
 
    public static void main(String[]args) throws IOException {
        prep();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        StringTokenizer st = new StringTokenizer(br.readLine());
        int p = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
 
        boolean doCheck = p*(long)q < 100_000_000;
 
        if (doCheck) {
            ok = new boolean[p + 1][q + 1];
            for (int i = 0; i <= p; i++) {
                Arrays.fill(ok[i], true);
            }
        }
 
        TreeSet<Constraint> cs = new TreeSet<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            if (n > p || k > q || (k > 0 && n - c < k)) continue;
            cs.add(new Constraint(n, k, false));
            if (doCheck) {
                ok[n][k] = false;
            }
        }
        cs.add(new Constraint(p, q, true));
 
        if (q > 0 && p - c < q) {
            pw.println(0);
            pw.flush();
            return;
        }
 
        for (Constraint con : cs) {
            //debug(con);
            con.coef = catalanTriangleWithOffset(con.n, con.k, 1, c);
            for (Constraint cov : cs) {
                if (cov.n >= con.n && cov.k >= con.k) break;
                if (cov.n > con.n) continue;
                int nn = con.n - cov.n;
                int nk = con.k - cov.k;
                int offset = (cov.n - c) - cov.k + 1;
                long incovcoef = catalanTriangleWithOffset(nn, nk, offset, 0);
                incovcoef = (incovcoef * cov.coef) % MOD;
                con.coef = (con.coef - incovcoef) % MOD;
            }
            if (con.n == p && con.k == q) {
                //debug(cs);
                long res = con.isGoal ? con.coef : 0;
                if (res < 0) res += MOD;
                if (doCheck) {
                    long otherres = check(p, q, c);
                    if (otherres != res) {
                        pw.println(otherres);
                    }
                    else {
                        pw.println(res);
                    }
                }
                else {
                    pw.println(res);
                }
                pw.flush();
                return;
            }
        }
        pw.flush();
    }
 
    static long check(int p, int q, int c)
    {
        long[][] dp = new long[p + 1][q + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= p; i++) {
            for (int j = 0; j <= q; j++) {
                if (!ok[i][j]) continue;
                dp[i][j] = dp[i - 1][j];
                if (i - j >= c && j > 0) {
                    dp[i][j] += dp[i][j - 1];
                    dp[i][j] %= MOD;
                }
            }
        }
        return dp[p][q];
    }
 
    private static final class Constraint implements Comparable<Constraint> {
        int n;
        int k;
        private boolean isGoal;
        long coef = 0;
 
        public Constraint(int n, int k, boolean isGoal) {
            this.n = n;
            this.k = k;
            this.isGoal = isGoal;
        }
 
        @Override
        public String toString() {
            return "Constraint{" +
                    "n=" + n +
                    ", k=" + k +
                    ", isGoal=" + isGoal +
                    ", coef=" + coef +
                    '}';
        }
 
        @Override
        public int compareTo(Constraint o) {
            if (n != o.n) return n - o.n;
            return k - o.k;
        }
    }
 
    private static final long fac[] = new long[4_000_001];
 
    public static final void prep() {
        fac[0] = 1;
        for (int i = 1; i < fac.length; i++) {
            fac[i] = (i * fac[i-1]) % MOD;
        }
    }
 
    private static long fpow(long a, long p) {
        // we want to break it down into powers of 2 and multiply those together
        long pow = 1;
        long av = a;
        while (p > 0) {
            if (p % 2 == 1) {
                pow = (pow * av) % MOD;
            }
            av = (av * av) % MOD;
            p /= 2;
        }
        return pow;
    }
 
    public static long nCr(int n, int r) {
        if (r < 0 || n <= 0) return 0;
        long nom = fac[n];
        long denom = (fac[r] * fac[n - r]) % MOD;
        long res = nom * fermatInverse(denom);
        return res % MOD;
    }
 
    public static long catalanTriangleWithOffset(int n, int k, int offset, int C) {
        long pos = nCr(n - C + k, k);
        long neg = nCr(n - C + k, k - offset);
        return (pos - neg) % MOD;
    }
 
    private static long fermatInverse(long a) {
        return fpow(a, MOD - 2);
    }
 
    public static void debug(Object...args) {
        System.out.println(Arrays.deepToString(args));
    }
} 