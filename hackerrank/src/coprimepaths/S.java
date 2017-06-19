package coprimepaths;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class S {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] line = bi.readLine().split(" ");
        int n = Integer.parseInt(line[0]);
        int q = Integer.parseInt(line[1]);

        long t1 = System.currentTimeMillis();

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

        System.err.println("Build took " + (System.currentTimeMillis() - t1));

        t1 = System.currentTimeMillis();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < q; i++) {
            line = bi.readLine().split(" ");
            int A = Integer.parseInt(line[0]) - 1;
            int B = Integer.parseInt(line[1]) - 1;

            sb.append(pr.solve(A, B)).append('\n');
        }

        System.err.println("Iterations took " + (System.currentTimeMillis() - t1));

        System.out.print(sb);
    }

    private static Problem build(int[] a, ArrayList<Integer>[] next) {
        return new Problem(a, next);
    }

    private static class Problem {
        public static final int STEP = 50;
        private int[] p;
        private int[] p100;
        private final ArrayList<Integer>[] next;
        private int[][] d;
        private int[] depth;

        ArrayList<Integer> unique1 = new ArrayList<>();
        ArrayList<Integer> unique2 = new ArrayList<>();
        ArrayList<Integer> unique3 = new ArrayList<>();

        HashMap<Integer, Integer> S1[];
        HashMap<Integer, Integer> S2[];
        HashMap<Integer, Integer> S3[];

        public Problem(int[] a, ArrayList<Integer>[] next) {
            this.next = next;
            this.p = new int[a.length];
            this.p100 = new int[a.length];
            this.depth = new int[a.length];
            S1 = new HashMap[a.length];
            S2 = new HashMap[a.length];
            S3 = new HashMap[a.length];

            int root = 0;


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

                if (a[i] != 1) {
                    d[i][d_i++] = a[i];
                }

                d[i] = Arrays.copyOf(d[i], d_i);

                for (int i1 : d[i]) {
                    unique1.add(i1);
                }
                for (int i1 = 0; i1 < d[i].length; i1++) {
                    for (int i2 = i1 + 1; i2 < d[i].length; i2++) {
                        unique2.add(d[i][i1] * d[i][i2]);
                    }
                }
                if (d_i == 3) {
                    unique3.add(d[i][0] * d[i][1] * d[i][2]);
                }
            }

            Collections.sort(unique1);
            Collections.sort(unique2);
            Collections.sort(unique3);

            unique1 = unique(unique1);
            unique2 = unique(unique2);
            unique3 = unique(unique3);

            System.err.println("PREP");

            prep(root);

//            int maxI = 0;
//            int maxV = -1;
//
//            for (int i = 0; i < depth.length; i++) {
//                if (maxV < depth[i]) {
//                    maxV = depth[i];
//                    maxI = i;
//                }
//            }
//
//            prep(maxI);
//
//            maxI = 0;
//            maxV = -1;
//            for (int i = 0; i < depth.length; i++) {
//                if (maxV < depth[i]) {
//                    maxV = depth[i];
//                    maxI = i;
//                }
//            }
//
//            for (int i = 0; i < maxV / 2; i++) {
//                maxI = p[maxI];
//            }
//
//            prep(maxI);
//
//            System.err.println("Chose as root " + maxI);
        }

        private ArrayList<Integer> unique(ArrayList<Integer> original) {
            int last = -1;
            ArrayList<Integer> un = new ArrayList<>();
            for (Integer val : original) {
                if (val != last) {
                    un.add(val);
                    last = val;
                }
            }
            return un;
        }

        private void prep(int root) {
            p100[root] = -1;
            p[root] = -1;
            depth[root] = 0;
            S1[root] = new HashMap<>();
            S2[root] = new HashMap<>();
            S3[root] = new HashMap<>();
            fillStats(S1[root], S2[root], S3[root], root);

            LinkedList<Integer> req = new LinkedList<>();
            req.add(root);
            while (!req.isEmpty()) {
                int PA = req.pollFirst();
                for (int CH : this.next[PA]) {
                    if (CH != p[PA]) {
                        depth[CH] = depth[PA] + 1;
                        p[CH] = PA;
                        req.add(CH);

                        if (depth[CH] < STEP) {
                            p100[CH] = -1;
                        } else if (depth[CH] == STEP) {
                            p100[CH] = 0;
                        } else {
                            int s = CH;
                            for (int i = 0; i < STEP; i++) {
                                s = p[s];
                            }
                            p100[CH] = s;
                        }

                        S1[CH] = (HashMap<Integer, Integer>) S1[PA].clone();
                        S2[CH] = (HashMap<Integer, Integer>) S2[PA].clone();
                        S3[CH] = (HashMap<Integer, Integer>) S3[PA].clone();
                        fillStats(S1[CH], S2[CH], S3[CH], CH);
                    }
                }
            }
        }

        public long solve(int A, int B) {
            int d1 = depth[A];
            int d2 = depth[B];

            HashMap<Integer, Integer> s1 = (HashMap<Integer, Integer>) S1[A].clone();
            HashMap<Integer, Integer> s2 = (HashMap<Integer, Integer>) S2[A].clone();
            HashMap<Integer, Integer> s3 = (HashMap<Integer, Integer>) S3[A].clone();
            add(s1, S1[B], 1);
            add(s2, S2[B], 1);
            add(s3, S3[B], 1);

            if (d1 < d2) {
                int t = A;
                A = B;
                B = t;

                t = d1;
                d1 = d2;
                d2 = t;
            }

            for (int i = 0; i < (d1 - d2) / STEP; i++) {
                A = p100[A];
            }
            for (int i = 0; i < (d1 - d2) % STEP; i++) {
                A = p[A];
            }

            while (p100[A] != p100[B]) {
                A = p100[A];
                B = p100[B];
            }

            while (A != B) {
                A = p[A];
                B = p[B];
            }

            add(s1, S1[A], -2);
            add(s2, S2[A], -2);
            add(s3, S3[A], -2);
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

        private void add(HashMap<Integer, Integer> x, HashMap<Integer, Integer> y, int times) {
            for (Map.Entry<Integer, Integer> e : y.entrySet()) {
                plusOne(x, e.getKey(), times * e.getValue());
            }
        }

        private void fillStats(HashMap<Integer, Integer> s1, HashMap<Integer, Integer> s2, HashMap<Integer, Integer> s3, int a) {
            for (int i : d[a]) {
                if (Collections.binarySearch(unique1, i) >= 0) {
                    plusOne(s1, i, 1);
                }
            }

            for (int k = 0; k < d[a].length; k++) {
                for (int j = k + 1; j < d[a].length; j++) {
                    int i = d[a][k] * d[a][j];
                    if (Collections.binarySearch(unique2, i) >= 0) {
                        plusOne(s2, i, 1);
                    }
                }
            }

            if (d[a].length == 3) {
                int i = d[a][0] * d[a][1] * d[a][2];
                if (Collections.binarySearch(unique3, i) >= 0) {
                    plusOne(s3, i, 1);
                }
            }
        }

        private void plusOne(HashMap<Integer, Integer> s1, int i, int one) {
            s1.put(i, Optional.ofNullable(s1.get(i)).orElse(0) + one);
        }

    }

    public static final int[] PRIMES = getPrimes(3162);
//    public static final int[] PRIMES = getPrimes(10000000);

//    static {
//        long t1 = System.currentTimeMillis();
//        System.err.println(getPrimes(3162).length);
//    }

    private static int[] getPrimes(int max) {
//        long t1 = System.currentTimeMillis();
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

//        System.err.println(System.currentTimeMillis() - t1);
//        System.err.println(pr[pr.length - 1]);

        return pr;
    }
}
