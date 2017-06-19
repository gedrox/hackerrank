package hard_homework;

import java.util.*;

public class Solution {

    public static int MAX_INDEX_DIFF = -1;
    public static double MAX_VAL_DIFF = 0;
    public static int CHUNK = -1;
    public static double THRESHOLD = -1;
    public static int INIT_SIZE;

    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        double MAX = solve(n);
        System.out.printf("%.9f%n", MAX);
    }

    static double solveCool(int n) {
        return 3 * Math.sin(((n - Math.PI / 2) % (2 * Math.PI) + Math.PI / 2)/3);
    }

    static double solve(int n) {
        return solve(n, true);
    }

    static double solve(int n, boolean doItFast) {
        int n3 = n - 3;

        int L = n - 2;

        double max = solveCool(n);

        List<Pair> V = new ArrayList<>();

        double threshold = n > 1000000 ? 0.001 : (n > 100000 ? 0.01 : 0.1);
        if (THRESHOLD != -1) threshold = THRESHOLD;

        double[] sin = new double[L];
        for (int i = 0; i < L; i++) {
            sin[i] = Math.sin(i + 1);
            if (n < 100 || Math.abs(sin[i] - max/3) < threshold) {
                V.add(Pair.of(i, sin[i]));
            }
        }

        Collections.sort(V);

        int chunk = n > 1000000 ? 2000 : (n > 100000 ? 10000 : 20000);
        if (CHUNK != -1) chunk = CHUNK;

        INIT_SIZE = V.size();

        // trim even more
        if (V.size() > chunk && doItFast) {
            int sweetSpot = Collections.binarySearch(V, Pair.of(-1, max/3));
            if (sweetSpot < 0) sweetSpot = -sweetSpot-1;

            int min = Math.max(0, sweetSpot - chunk/2);
            V = V.subList(min, Math.min(V.size(), min + chunk));
        }

        double MAX = 0;
        double cand;
        int[] xs = {0, 0, 0};

        for (int i = 0; i < V.size(); i++) {
            for (int j = i; j < V.size(); j++) {
                Pair A = V.get(i);
                Pair B = V.get(j);

                int C_i = n3 - A.x - B.x;
                if (C_i >= 0) {
                    cand = A.sin + B.sin + sin[C_i];
                    if (cand > MAX) {
                        MAX = cand;
                        xs = new int[]{A.x+1, B.x+1, C_i+1};
                    }
                }
            }
        }

//        System.err.printf("For n = %d solution is %s (%.2f+%.2f+%.2f=%.2f)%n", n, Arrays.toString(xs),
//                Math.sin(xs[0]), Math.sin(xs[1]), Math.sin(xs[2]), Math.sin(xs[0])+Math.sin(xs[1])+Math.sin(xs[2]));

        int x = -Collections.binarySearch(V, Pair.of(-1, max / 3)) - 1;
        int x1 = Collections.binarySearch(V, Pair.of(-1, Math.sin(xs[0])));
        int x2 = Collections.binarySearch(V, Pair.of(-1, Math.sin(xs[1])));
        int x3 = Collections.binarySearch(V, Pair.of(-1, Math.sin(xs[2])));

//        System.err.println(x);
//        System.err.println(x1);
//        System.err.println(x2);
//        System.err.println(x3);

        MAX_INDEX_DIFF = Math.max(Math.max(Math.abs(x - x1), Math.abs(x - x2)), Math.abs(x - x3));
        MAX_VAL_DIFF = Math.max(Math.max(Math.abs(max/3-Math.sin(xs[0])), Math.abs(max/3-Math.sin(xs[1]))), Math.abs(max/3-Math.sin(xs[2])));

        return MAX;
    }

    static class Pair implements Comparable<Pair> {
        int x;
        double sin;

        static Pair of(int x, double sin) {
            Pair p = new Pair();
            p.x = x;
            p.sin = sin;
            return p;
        }

        @Override
        public String toString() {
            return String.format("sin(%d)=%.3f", x+1, sin);
        }

        @Override
        public int compareTo(Pair o) {
            return (int) Math.signum(o.sin - this.sin);
        }
    }
}
