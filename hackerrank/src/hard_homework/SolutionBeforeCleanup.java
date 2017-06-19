package hard_homework;

import java.util.Arrays;
import java.util.Scanner;

public class SolutionBeforeCleanup {

    public static int MAX_POS_DIFF = 0;
    public static double MAX_VAL_DIFF = 0;
    public static double MAX_VAL_DIFF1 = 0;
    public static double MAX_VAL_DIFF2 = 0;
    public static double MAX_VAL_DIFF3 = 0;

    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        double MAX = n < 1000 ? solve(n) : solveCool(n);
        System.out.printf("%.9f%n", MAX);
    }

    static double solveCool(int n) {
        //=3*SIN(MOD(A2/3 - PI()/6, 2*PI()/3)+PI()/6)
        return 3 * Math.sin((n/3.0 - Math.PI / 6) % (2*Math.PI/3) + Math.PI/6);
    }

    static double solve(int n) {
        int n3 = n - 3;

        int L = n - 2;

        Pair[] v = new Pair[L];
        double[] sin = new double[L];
        for (int i = 0; i < L; i++) {
            sin[i] = Math.sin(i + 1);
            v[i] = Pair.of(i, sin[i]);
        }

        Arrays.sort(v);

        double max = solveCool(n);
        if (v.length > 1000) {
            int startPos = Math.abs(Arrays.binarySearch(v, Pair.of(-1, max / 3 + 0.1)));
            int endPos = Math.abs(Arrays.binarySearch(v, Pair.of(-1, max / 3 - 0.1)));
//            v = Arrays.copyOfRange()
        }
        int where = Arrays.binarySearch(v, Pair.of(-1, max / 3));
        if (where < 0) where = -where -1;

//        System.err.println(Arrays.toString(Arrays.copyOfRange(v, Math.max(where-10, 0), where+10)));

        int ba = 0;
        int bb = 0;

        double MAX = sin[ba] + sin[bb] + sin[n3 - ba - bb];
        double greatest2 = sin[ba] + sin[bb];
        int m_i = getPos(v, MAX, greatest2);

        int C;
        double cand;

        int[] xs = {ba+1, bb+1, n3-ba-bb+1};

        for (int i = 0; i < 2 * m_i - 1; i++) {
            for (int A = i / 2; A > -1; A--) {
                int B = i - A;
                C = n3 - v[A].x - v[B].x;
                if (C >= 0) {
                    cand = v[A].sin + v[B].sin + sin[C];
                    if (cand > MAX) {
                        MAX = cand;
                        greatest2 = v[A].sin + v[B].sin;
                        m_i = getPos(v, MAX, greatest2);
                        xs = new int[]{v[A].x+1, v[B].x+1, C+1};
                    }
                }

//                if (2 * v[A].sin + v[B].sin <= MAX) {
//                    m_i = 0;
//                    break;
//                }
            }
        }

        System.err.printf("For n = %d solution is %s (%.2f+%.2f+%.2f=%.2f) (%d, %d, %d)%n", n, Arrays.toString(xs),
                Math.sin(xs[0]), Math.sin(xs[1]), Math.sin(xs[2]), Math.sin(xs[0])+Math.sin(xs[1])+Math.sin(xs[2]),
                where - Arrays.binarySearch(v, Pair.of(-1, Math.sin(xs[0]))),
                where - Arrays.binarySearch(v, Pair.of(-1, Math.sin(xs[1]))),
                where - Arrays.binarySearch(v, Pair.of(-1, Math.sin(xs[2]))));

        MAX_POS_DIFF = Math.max(MAX_POS_DIFF, Math.abs(where - Arrays.binarySearch(v, Pair.of(-1, Math.sin(xs[0])))));
        MAX_POS_DIFF = Math.max(MAX_POS_DIFF, Math.abs(where - Arrays.binarySearch(v, Pair.of(-1, Math.sin(xs[1])))));
        MAX_POS_DIFF = Math.max(MAX_POS_DIFF, Math.abs(where - Arrays.binarySearch(v, Pair.of(-1, Math.sin(xs[2])))));

        MAX_VAL_DIFF = Math.max(MAX_VAL_DIFF, Math.abs(max/3 - Math.sin(xs[0])));
        MAX_VAL_DIFF = Math.max(MAX_VAL_DIFF, Math.abs(max/3 - Math.sin(xs[1])));
        MAX_VAL_DIFF = Math.max(MAX_VAL_DIFF, Math.abs(max/3 - Math.sin(xs[2])));

        double[] valDiffs = {Math.abs(max/3 - Math.sin(xs[0])),
                Math.abs(max/3 - Math.sin(xs[1])),
                Math.abs(max/3 - Math.sin(xs[2]))};

        Arrays.sort(valDiffs);

        MAX_VAL_DIFF1 = Math.max(MAX_VAL_DIFF1, valDiffs[0]);
        MAX_VAL_DIFF2 = Math.max(MAX_VAL_DIFF2, valDiffs[1]);
        MAX_VAL_DIFF3 = Math.max(MAX_VAL_DIFF3, valDiffs[2]);

        return MAX;
    }

    private static int getPos(Pair[] v, double MAX, double greatest2) {
        int pos = Arrays.binarySearch(v, Pair.of(-1, MAX - greatest2));
        if (pos >= 0) return pos;
        else return -pos - 1;
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
