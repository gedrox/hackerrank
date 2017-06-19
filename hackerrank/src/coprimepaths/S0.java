package coprimepaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class S0 {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] line = bi.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int q = Integer.parseInt(line[1]);

        int a[] = new int[n];
        line = bi.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(line[i]);
        }

        ArrayList<Integer> next[] = new ArrayList[n];
        for (int i = 0; i < next.length; i++) {
            next[i] = new ArrayList<>();
        }
        for (int i = 0; i < n - 1; i++) {
            line = bi.readLine().split(" ");
            int x = Integer.parseInt(line[0]) - 1;
            int y = Integer.parseInt(line[1]) - 1;

            next[x].add(y);
            next[y].add(x);
        }

        Problem pr = build(a, next);

        for (int i = 0; i < q; i++) {
            line = bi.readLine().split(" ");
            int A = Integer.parseInt(line[0]) - 1;
            int B = Integer.parseInt(line[1]) - 1;

            System.out.println(pr.solve(A, B));
        }
    }

    private static Problem build(int[] a, ArrayList<Integer>[] next) {
        return new Problem(a, next);
    }

    private static class Problem {
        private int[] depth;
        private int[] p;
        private final ArrayList<Integer>[] next;
        private int[][] d;

        public Problem(int[] a, ArrayList<Integer>[] next) {
            this.depth = new int[a.length];
            this.next = next;
            this.p = new int[a.length];

            int root = 0;
            prepare(root);

            d = new int[a.length][];
            for (int i = 0; i < a.length; i++) {
                d[i] = new int[3];
                int d_i = 0;
                for (int prime : PRIMES) {
                    if (a[i] == 1) break;
                    if (a[i] % prime == 0) {
                        d[i][d_i++] = prime;
                        while (a[i] % prime == 0) a[i] = a[i] / prime;
                    }
                }

                if (a[i] != 1 && d_i != 3) {
                    d[i][d_i++] = a[i];
                }

                d[i] = Arrays.copyOf(d[i], d_i);
            }
        }

        private void prepare(int root) {
            p[root] = -1;
            depth[root] = 0;
            LinkedList<Integer> req = new LinkedList<>();
            req.add(root);
            while (!req.isEmpty()) {
                int PA = req.pollFirst();
                for (int nxt : next[PA]) {
                    if (nxt != p[PA]) {
                        p[nxt] = PA;
                        depth[nxt] = depth[PA] + 1;
                        req.add(nxt);
                    }
                }
            }
        }

        public long solve(int A, int B) {
            int d1 = depth[A];
            int d2 = depth[B];

            HashMap<Integer, Integer> s1 = new HashMap<>();
            HashMap<Integer, Integer> s2 = new HashMap<>();
            HashMap<Integer, Integer> s3 = new HashMap<>();

            if (d1 < d2) {
                int t = A;
                A = B;
                B = t;
            }

            for (int i = 0; i < Math.abs(d2 - d1); i++) {
                fillStats(s1, s2, s3, A);
                A = p[A];
            }

            while (A != B) {
                fillStats(s1, s2, s3, A);
                fillStats(s1, s2, s3, B);
                A = p[A];
                B = p[B];
            }

            fillStats(s1, s2, s3, A);

            int L = d1 + d2 - 2 * depth[A] + 1;
            long res = L * (L - 1) / 2;
            for (int c : s1.values()) {
                res -= c * (c - 1) / 2;
            }
            for (int c : s2.values()) {
                res += c * (c - 1) / 2;
            }
            for (int c : s3.values()) {
                res -= c * (c - 1) / 2;
            }

            return res;
        }

        private void fillStats(HashMap<Integer, Integer> s1, HashMap<Integer, Integer> s2, HashMap<Integer, Integer> s3, int a) {
            for (int i : d[a]) {
                s1.put(i, Optional.ofNullable(s1.get(i)).orElse(0) + 1);
            }

            for (int k = 0; k < d[a].length; k++) {
                for (int j = k + 1; j < d[a].length; j++) {
                    int i = d[a][k] * d[a][j];
                    s2.put(i, Optional.ofNullable(s2.get(i)).orElse(0) + 1);
                }
            }

            if (d[a].length == 3) {
                int i = d[a][0] * d[a][1] * d[a][2];
                s3.put(i, Optional.ofNullable(s3.get(i)).orElse(0) + 1);
            }
        }

    }

    public static final int[] PRIMES = getPrimes(3162);

    private static int[] getPrimes(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        boolean p[] = new boolean[(max - 1) / 2];
        for (int i = 0; i < p.length; i++) {
            if (!p[i]) {
                primes.add(2 * i + 3);
                int next = 3 * i + 3;
                while (next < p.length) {
                    p[next] = true;
                    next += 2 * i + 3;
                }
            }
        }

        int[] pr = new int[primes.size()];
        for (int i = 0; i < primes.size(); i++) {
            pr[i] = primes.get(i);
        }

        return pr;
    }
}
