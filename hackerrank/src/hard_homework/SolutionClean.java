package hard_homework;

import java.util.*;

public class SolutionClean {

    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        double MAX = solve(n);
        System.out.printf("%.9f%n", MAX);
    }

    static double solveCool(int n) {
        return Math.sin(((n - Math.PI / 2) % (2*Math.PI) + Math.PI/2)/3.0);
    }

    static double solve(int n) {
        int n3 = n - 3;

        int L = n - 2;

        double max = solveCool(n);

        List<Pair> V = new ArrayList<>();

        double threshold = n > 1000000 ? 0.001 : (n > 100000 ? 0.01 : 0.1);

        double[] sin = new double[L];
        for (int i = 0; i < L; i++) {
            sin[i] = Math.sin(i + 1);
            if (n < 100 || Math.abs(sin[i] - max) < threshold) {
                V.add(Pair.of(i, sin[i]));
            }
        }

        Collections.sort(V);

        int chunk = n > 1000000 ? 2000 : (n > 100000 ? 10000 : 20000);

        // trim even more
        if (V.size() > chunk) {
            int sweetSpot = Collections.binarySearch(V, Pair.of(-1, max));
            if (sweetSpot < 0) sweetSpot = -sweetSpot-1;

            int min = Math.max(0, sweetSpot - chunk/2);
            V = V.subList(min, Math.min(V.size(), min + chunk));
        }

        double MAX = 0;
        double cand;

        for (int i = 0; i < V.size(); i++) {
            for (int j = i; j < V.size(); j++) {
                Pair A = V.get(i);
                Pair B = V.get(j);

                int C_i = n3 - A.x - B.x;
                if (C_i >= 0) {
                    cand = A.sin + B.sin + sin[C_i];
                    if (cand > MAX) {
                        MAX = cand;
                    }
                }
            }
        }

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
        public int compareTo(Pair o) {
            return (int) Math.signum(o.sin - this.sin);
        }
    }
}
