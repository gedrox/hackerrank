package codechef.vaiman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.io.PrintWriter;
import java.util.TreeSet;
 
public class Correct {
 
    private static final long MOD = 1_000_000_007;

    private static final long fac[] = new long[4_000_001];
    static {
        prep();
    }
    
    public static void main(String[]args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        StringTokenizer st = new StringTokenizer(br.readLine());
        int p = Integer.parseInt(st.nextToken());
        int q = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
 
        TreeSet<Constraint> cs = new TreeSet<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());
            int k = Integer.parseInt(st.nextToken());
            if (n > p || k > q || (k > 0 && n - c < k)) continue;
            cs.add(new Constraint(n, k, false));
        }

        long answer = solve(p, q, c, cs);

        pw.println(answer);
        pw.flush();
    }

    public static long solve(int p, int q, int c, TreeSet<Constraint> cs) {

        cs.removeIf(con -> con.n > p || con.k > q || (con.k > 0 && con.n - c < con.k));
        
        long answer = 0;
        cs.add(new Constraint(p, q, true));

        if (q <= 0 || p - c >= q) {
            long t0 = System.currentTimeMillis();
            int times = 0;
            for (Constraint con : cs) {
                //debug(con);
                con.coef = catalanTriangleWithOffset(con.n, con.k, 1, c);
                for (Constraint cov : cs) {
                    if (cov.n >= con.n && cov.k >= con.k) break;
                    if (cov.n > con.n) continue;
                    times++;
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
                    answer = res;
                    break;
                }
            }

            System.err.println(System.currentTimeMillis() - t0);
            System.err.println(times);
        }
        return answer;
    }


    public static class Constraint implements Comparable<Constraint> {
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
 
    
 
    public static void prep() {
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
} 