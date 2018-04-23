package cj2018r1a.C;

//import org.junit.Assert;
//import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Solution {

    static int N, P;
    static int[] W, H;
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static void init() {
        W = new int[N];
        H = new int[N];
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

    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        int T = readInt();
        for (int t_i = 1; t_i <= T; t_i++) {
            N = readInt();
            P = readInt();
            init();
            for (int i = 0; i < N; i++) {
                W[i] = readInt();
                H[i] = readInt();
            }

            double answer = solve();

            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(answer)
                    .append('\n');
        }
        System.out.print(sb);
    }

    private static double solve() {
        // reminder to cover
        int rem = P;
        Interval[] intervals = new Interval[N];
        for (int i = 0; i < N; i++) {
            rem -= 2 * W[i] + 2 * H[i];
            intervals[i] = new Interval(i, 2 * Math.min(W[i], H[i]), 2 * Math.sqrt(W[i] * W[i] + H[i] * H[i]));
        }
        
        TreeSet<Interval> sorted = new TreeSet<>(Comparator.comparing(interval -> interval.min));
        sorted.add(Interval.EMPTY);

        for (int i = 0; i < N; i++) {
            ArrayList<Interval> newIntervals = new ArrayList<>();
            for (Interval interval : sorted) {
                Interval newInt = interval.add(intervals[i]);
                if (newInt.min <= rem) newIntervals.add(newInt);
            }
            sorted.addAll(newIntervals);

            Interval prev = null;
            for (Iterator<Interval> iterator = sorted.iterator(); iterator.hasNext(); ) {
                Interval interval = iterator.next();
                if (prev != null && prev.max >= interval.min) {
                    iterator.remove();
                    prev.max = interval.max;
                } else {
                    prev = interval;
                }
            }
        }

        double bestSize = Math.min(rem, sorted.last().max);
        
        return P - (rem - bestSize);
    }
    
    private static double solveRandom() {
        // reminder to cover
        int rem = P;
        Interval[] intervals = new Interval[N];
        for (int i = 0; i < N; i++) {
            rem -= 2 * W[i] + 2 * H[i];
            intervals[i] = new Interval(i, 2 * Math.min(W[i], H[i]), 2 * Math.sqrt(W[i] * W[i] + H[i] * H[i]));
        }
        
        if (rem == 0) return P;

        double bestCoverage = 0;
        
        // 1) order by min
        ArrayList<Comparator<Interval>> cmp = new ArrayList<>();
        cmp.add(Comparator.comparing(interval -> interval.min));
        cmp.add(Comparator.comparing(interval -> -interval.min));
        cmp.add(Comparator.comparing(interval -> interval.max));
        cmp.add(Comparator.comparing(interval -> -interval.min));
        cmp.add(Comparator.comparing(interval -> interval.max / interval.min));
        cmp.add(Comparator.comparing(interval -> -interval.max / interval.min));

        ArrayList<Integer> randomSort = new ArrayList<>();
        for (int i = 0; i < N; i++) randomSort.add(i);
        
        for (int i = 0; i < 1000; i++) {
            Collections.shuffle(randomSort);
            cmp.add(Comparator.comparing(interval -> randomSort.get(interval.i)));
        }
        
        int bestCmp = -1;

        for (int cmpi = 0; cmpi < cmp.size(); cmpi++) {
            Comparator<Interval> comparator = cmp.get(cmpi);
            Arrays.sort(intervals, comparator);

            double nextBest = findBest(intervals, rem);
            if (nextBest > bestCoverage) {
                if (Math.abs(nextBest - bestCoverage) > 1e-10) {
                    bestCmp = cmpi;
                }
                bestCoverage = nextBest;
            }

            if (Math.abs(bestCoverage - rem) < 1e-10) break;
        }

        bestHits[Math.min(bestCmp, 6)]++;

        return P - (rem - bestCoverage);
    }
    
    static int[] bestHits = new int[7];

    private static double findBest(Interval[] intervals, int rem) {
        double sum = 0;
        boolean[] choice = new boolean[N];

        for (Interval interval : intervals) {
            if (sum + interval.min <= rem) {
                sum += interval.min;
                choice[interval.i] = true;
            }
        }

        for (int i = 0; i < N; i++) {
            if (choice[i]) {
                double toAdd = rem - sum;
                double canAdd = Math.min(intervals[i].max - intervals[i].min, toAdd);
                sum += canAdd;
            }
        }
        
        return sum;
    }

    static class Interval {
        public static final Interval EMPTY = new Interval(-1, 0, 0);
        int i;
        final int min;
        double max;

        public Interval(int i, int min, double max) {
            this.i = i;
            this.min = min;
            this.max = max;
        }
        
        public Interval add(Interval that) {
            return new Interval(-1, this.min + that.min, this.max + that.max);
        }
    }
    
//    @Test
//    public void sample1() {
//        N = 1;
//        P = 7;
//        init();
//        W[0] = 1;
//        H[0] = 1;
//        Assert.assertEquals(6.828427, solve(), 1e-6);
//    }
//
//    @Test
//    public void sample2() {
//        N = 2;
//        P = 920;
//        init();
//        for (int i = 0; i < N; i++) {
//            W[i] = 50;
//            H[i] = 120;
//        }
//        Assert.assertEquals(920, solve(), 1e-6);
//    }
//
//    @Test
//    public void sample3() {
//        N = 1;
//        P = 32;
//        init();
//        W[0] = 7;
//        H[0] = 4;
//        Assert.assertEquals(32, solve(), 1e-6);
//    }
//
//    @Test
//    public void testToFail() {
//        N = 3;
//        P = 240;
//        W = new int[]{10, 20, 30};
//        H = new int[]{20, 30, 10};
//
//        System.out.println(solve());
//    }
//
//    @Test
//    public void testBig() {
//
//        double[] answers = {68984.0, 82857.0, 87917.0, 52445.0, 79600.0, 85345.8576388653, 85092.35012259032, 67573.0, 91946.69449639916, 62043.0, 64087.0, 81935.0, 56937.0, 70309.0, 70623.0, 92478.37822173219, 64233.0, 83303.0, 79250.0, 53846.0, 74352.0, 86495.59364496238, 90770.25138680817, 71787.0, 87393.86351467652, 60094.0, 58663.0, 77055.0, 57200.0, 73485.0, 72514.0, 96264.63222469471, 69656.0, 84920.0, 90705.17194797525, 49696.0, 81752.0, 84842.21205805318, 54359.0, 66609.0, 89581.73056446208, 60891.0, 67820.0, 80902.0, 59480.0, 80688.0, 77181.0, 90898.16196642912, 72733.0, 91349.0, 84648.0, 53775.0, 83212.0, 92213.46136742942, 50830.0, 74180.0, 89733.35871815088, 61173.0, 62789.0, 75490.0, 53172.0, 72288.0, 76721.0, 83769.5549230692, 69833.0, 88957.0, 88048.31056944386, 57347.0, 87502.0, 48148.0, 53420.0, 67948.0, 90861.35700582087, 62158.0, 63106.0, 86988.0, 59070.0, 74576.0, 81170.0, 90118.99355474247, 66252.0, 83119.0, 92602.46100682445, 59717.0, 83204.0, 86796.81367671283, 53308.0, 68930.0, 91757.28886364211, 63980.0, 66710.0, 83171.0, 60029.0, 77010.0, 73651.0, 89597.69740705384, 72462.0, 89801.51371261732, 96563.0536999135, 53916.0};
//
//        for (int t_i = 0; t_i < 100; t_i++) {
//            N = 100;
//            P = 0;
//            init();
//            Random r = new Random(t_i);
//            for (int i = 0; i < N; i++) {
//                W[i] = r.nextInt(250) + 1;
//                H[i] = r.nextInt(250) + 1;
//                P += 2 * W[i] + 2 * H[i];
//            }
//            double mult = 1 + 1 * r.nextDouble();
//            P = (int) (P * mult);
//
//            double answer = solve();
//            Assert.assertTrue(answer <= P);
//            System.out.println(answer + ", P=" + P + ", mult=" + mult);
//            Assert.assertEquals(answers[t_i], answer, 1e-6);
//        }
//
//        System.out.println(Arrays.toString(bestHits));
//
////        System.out.print("[");
////        for (double answer : answers) {
////            System.out.print(answer + ", ");
////        }
////        System.out.println("]");
//    }
}
