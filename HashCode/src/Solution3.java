import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Solution3 {

    public static final double ALPHA = 0.5;
    static String[] files = {
            "a_example.in",
            "b_should_be_easy.in",
            "c_no_hurry.in",
            "d_metropolis.in",
            "e_high_bonus.in"
    };
    private static Solution3 inst = new Solution3();
    String fileName;
    int R, C, F, N, B, T;
    int[] a, b, x, y, s, f, p;
    Pos[] ab, xy;
    int POINTS = 0;
    int TOTAL_POINTS = 0;
    boolean VERBOSE = true;
    int[] BENCHMARKS = {
            10,
            178_723,
            15_817_815,
            11_812_165,
            21_465_945
    };

    int BENCHMARK = 49_272_812;
    private boolean[] taken;
    private Pos[] f_pos;
    private int[][] answer;
    private int[] count;
    private int[] step;
    private int ants = 100;
    private int ANT_COLONIES = 10;

    private String buildOutput() {
        StringBuilder sb = new StringBuilder();
        for (int f_i = 0; f_i < answer.length; f_i++) {
            sb.append(count[f_i]);
            for (int r_i = 0; r_i < count[f_i]; r_i++) {
                sb.append(' ');
                sb.append(answer[f_i][r_i]);
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Test
    @Ignore
    public void runOne() throws IOException, ClassNotFoundException {
        int file_i = 3;
        inst.VERBOSE = false;
        inst.ANT_COLONIES = 100;
        inst.ants = 100;
        for (int i = 0; i < 1; i++) {
            inst.processFile(files[file_i], BENCHMARKS[file_i]);
        }
        System.out.printf("done");
    }

    @Test
    public void runAllFiles() throws IOException, ClassNotFoundException {
        inst.VERBOSE = false;
        for (int file_i = 0; file_i < files.length; file_i++) {
            inst.processFile(files[file_i], BENCHMARKS[file_i]);
        }

        System.out.printf("TOTAL: %,d (%.0f%% better)%n", inst.TOTAL_POINTS, 100d * (inst.TOTAL_POINTS - BENCHMARK) / BENCHMARK);
    }

    private void processFile(String fname, int bench) throws IOException, ClassNotFoundException {
        fileName = fname;
        fname = "in/" + fname;
        Scanner in = new Scanner(new FileInputStream(fname));
        readInput(in);
        solve();

        String sb = buildOutput();

        String out = fname.replace(".in", inst.POINTS > bench ? "_" + POINTS + ".out" : ".out")
                .replace("in/", "");

        System.out.printf("(%s) POINTS: %,d (%.0f%% better, %d)%n",
                fileName.charAt(0), inst.POINTS, 100d * (inst.POINTS - bench) / bench, inst.POINTS - bench);

        Files.write(Paths.get(out), sb.getBytes());
    }

    @Test
    @Ignore
    public void test() throws IOException, ClassNotFoundException {
        Scanner in = new Scanner("3 4 2 3 2 10\n" +
                "0 0 1 3 2 9\n" +
                "1 2 1 0 0 9\n" +
                "2 0 2 2 0 9");

        readInput(in);

        solve();

        System.out.println("TOTAL points: " + TOTAL_POINTS);
        System.out.println(Arrays.deepToString(answer));
    }

    private void solve() throws IOException, ClassNotFoundException {
        POINTS = 0;

        Pos pos00 = new Pos(0, 0);

        taken = new boolean[N];
        answer = new int[F][N];
        count = new int[F];
        step = new int[F];
        f_pos = new Pos[F];
        for (int f_i = 0; f_i < F; f_i++) {
            f_pos[f_i] = pos00;
        }
        
        ArrayList<Integer> waiting = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            waiting.add(i);
        }

        double[][] weights = new double[N + 1][N];
        for (int i = 0; i < N + 1; i++) {
            for (int i1 = 0; i1 < N; i1++) {
                weights[i][i1] = 1;
            }
        }
        
        for (int f_i = 0; f_i < F; f_i++) {

            int pointsBefore = POINTS;
            int bestPoints = 0;
            ArrayList<Integer> bestPath = null;
            int[] scores = new int[ants];
            
            int bestNotHit = 0;

            for (int ant_reboot = 0; ant_reboot < ANT_COLONIES; ant_reboot++) {

                ArrayList<Integer>[] paths = new ArrayList[ants];

                for (int ant_i = 0; ant_i < ants; ant_i++) {
                    paths[ant_i] = new ArrayList<>();

                    int prevNode = N;
                    while (true) {
                        // move randomly
                        WeightedRandom<Option> options = new WeightedRandom<>();
                        for (int r_i : waiting) {
                            Option o = new Option(f_i, r_i);
                            if (o.canTake()) {
//                                options.add(o, Math.max(1e-10, Math.pow(1d / (1 + o.distToStart), 3) * weights[prevNode][r_i]));
                                if (ant_reboot == 0 && ant_i == 0) {
                                    options.add(o, Math.max(Double.MIN_NORMAL, o.pps));
                                }
                                options.add(o, Math.max(Double.MIN_NORMAL, o.pps * weights[prevNode][r_i]));
                            }
                        }

                        if (options.total == 0) break;
                        Option best = options.rand();
                        if (ant_reboot == 0 && ant_i <= 1) {
                            best = options.top();
                        }

                        prevNode = best.r_i;
                        paths[ant_i].add(best.r_i);
                        best.take();
                    }

                    // rollback
                    scores[ant_i] = POINTS - pointsBefore;
                    if (scores[ant_i] > bestPoints) {
                        bestPoints = scores[ant_i];
                        bestPath = paths[ant_i];
                        bestNotHit = 0;
                    } else {
                        bestNotHit++;
                    }

                    for (int i = 0; i < count[f_i]; i++) {
                        taken[answer[f_i][i]] = false;
                    }
                    step[f_i] = 0;
                    count[f_i] = 0;
                    POINTS = pointsBefore;
                }

                //adjust weights

                for (int ant_i = 0; ant_i < ants; ant_i++) {
                    double adj = Math.max(0.5, Math.pow(1d * scores[ant_i] / bestPoints, ALPHA));
                    int prev = N;
                    for (int r_i : paths[ant_i]) {
                        weights[prev][r_i] *= adj;
                        prev = r_i;
                    }
                }
                System.out.print(bestPoints + ", ");
                
                if (bestNotHit > 5 * ants) break;
            }
            System.out.println();

            // do the best path
            if (bestPath != null) {
                for (int r_i : bestPath) {
                    new Option(f_i, r_i).take();
                    waiting.remove((Integer) r_i);
                }
            }

            if (f_i % 10 == 9) System.out.println(f_i + 1);
        }

        TOTAL_POINTS += POINTS;
    }

    private void addPoints(int f_i, int r_i, boolean onTime) {

        if (VERBOSE) {
            if (onTime) {
                System.out.printf("Driver %d takes ride %d, gets %d points, bonus %d%n",
                        f_i, r_i, p[r_i], B);
            } else {
                System.out.printf("Driver %d takes ride %d, gets %d points, no bonus%n",
                        f_i, r_i, p[r_i]);
            }
        }

        POINTS += p[r_i];
        if (onTime) POINTS += B;
    }

    void readInput(Scanner in) {
        R = in.nextInt();
        C = in.nextInt();
        F = in.nextInt();
        N = in.nextInt();
        B = in.nextInt();
        T = in.nextInt();

        a = new int[N];
        b = new int[N];
        x = new int[N];
        y = new int[N];
        s = new int[N];
        f = new int[N];
        ab = new Pos[N];
        xy = new Pos[N];
        p = new int[N];

        for (int r_i = 0; r_i < N; r_i++) {
            a[r_i] = in.nextInt();
            b[r_i] = in.nextInt();
            x[r_i] = in.nextInt();
            y[r_i] = in.nextInt();
            s[r_i] = in.nextInt();
            f[r_i] = in.nextInt();

            ab[r_i] = new Pos(a[r_i], b[r_i]);
            xy[r_i] = new Pos(x[r_i], y[r_i]);
            p[r_i] = ab[r_i].dist(xy[r_i]);
        }
    }

    static class WeightedRandom<T> {
        double total;
        ArrayList<Double> weights = new ArrayList<>();
        ArrayList<T> ids = new ArrayList<>();
        T top = null;
        double max = Double.MIN_VALUE;

        public void add(T id, double weight) {
            if (weight <= 0) return;

            total += weight;
            weights.add(total);
            ids.add(id);
            
            if (weight > max) {
                max = weight;
                top = id;
            }
        }

        public T rand() {
            double choice = total * Math.random();
            int pos = Collections.binarySearch(weights, choice);
            if (pos < 0) pos = -pos - 1;
            return ids.get(pos);
        }

        public T top() {
            return top;
        }
    }

    static class Pos {
        int x, y;

        public Pos(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }

        int dist(Pos that) {
            return Math.abs(this.x - that.x) + Math.abs(this.y - that.y);
        }
    }

    class Option implements Comparable<Option> {
        int f_i, r_i, canStart, canFinish, bonus, points, distToStart, waitTime;
        double pps;

        public Option(int f_i, int r_i) {
            this(f_i, r_i, f_pos[f_i], step[f_i]);
        }

        public Option(int f_i, int r_i, Pos start_pos, int start_time) {
            this.f_i = f_i;
            this.r_i = r_i;

            distToStart = start_pos.dist(ab[r_i]);
            canStart = Math.max(start_time + distToStart, s[r_i]);
            waitTime = canStart - (start_time + distToStart);
            canFinish = canStart + p[r_i];
            bonus = canStart == s[r_i] ? B : 0;
            points = p[r_i] + bonus;

            pps = 1d * points / (canFinish - start_time);
        }

        boolean canTake() {
            return !taken[r_i] && canFinish <= f[r_i];
        }

        void take() {
            taken[r_i] = true;
            answer[f_i][count[f_i]++] = r_i;
            step[f_i] = canFinish;
            f_pos[f_i] = xy[r_i];

            addPoints(f_i, r_i, canStart == s[r_i]);
        }

        @Override
        public String toString() {
            return "Option{" +
                    "f_i=" + f_i +
                    ", r_i=" + r_i +
                    ", canStart=" + canStart +
                    ", canFinish=" + canFinish +
                    ", points=" + points +
                    '}';
        }

        @Override
        public int compareTo(Option that) {
            int cmp;
            if ((cmp = Double.compare(that.pps, this.pps)) != 0) return cmp;
            return that.bonus - this.bonus;
        }

    }

}
