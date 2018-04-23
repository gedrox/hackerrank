import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class SolutionWithReplace {

    public static final Pos POS0 = new Pos(0, 0);
    static String[] files = {
            "a_example.in",
            "b_should_be_easy.in",
            "c_no_hurry.in",
            "d_metropolis.in",
            "e_high_bonus.in"
    };
    static SolutionWithReplace inst = new SolutionWithReplace();
    String fileName;
    int R, C, F, N, B, T;
    int[] a, b, x, y, s, f, p;
    Pos[] ab, xy;
    int POINTS = 0;
    int[] POINTS_BY_CAR;
    int TOTAL_POINTS = 0;
    boolean VERBOSE = true;
    int[] BENCHMARKS = {
            10,
            176_877,
            15_818_350,
            12_360_560,
            21_465_945
    };

    int BENCHMARK = 49_272_812;
    boolean[] taken;
    Pos[] f_pos;
    int[][] answer;
    int[] count;
    int[] step;
    int bench;
    ArrayList<Integer> by_start;
    int startPoints;
    long startTime;
    ArrayList<Long> imprTime = new ArrayList<>();
    ArrayList<Integer> imprPoints = new ArrayList<>();
    public static final double[] INITIAL_CF = new double[]{100, 1, 50, 1, 5, 1};

    String buildOutput() {
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
    public void runOne() throws IOException {
        int file_i = 'd' - 'a';
        inst.VERBOSE = false;
        for (int i = 0; i < 1; i++) {
            inst.processFile(files[file_i], BENCHMARKS[file_i]);
        }

        int notTaken = 0;
        for (boolean b1 : inst.taken) {
            if (!b1) {
                notTaken++;
            }
        }
        System.out.println(notTaken);
    }

    @Test
    public void runAllFiles() throws IOException {
        inst.VERBOSE = false;
        for (int file_i = 0; file_i < files.length; file_i++) {
            inst.processFile(files[file_i], BENCHMARKS[file_i]);
        }

        System.out.printf("TOTAL: %,d (%.0f%% better)%n", inst.TOTAL_POINTS, 100d * (inst.TOTAL_POINTS - BENCHMARK) / BENCHMARK);
    }

    static boolean SILENT = false;
    
    void processFile(String fname, int bench) throws IOException {
        this.bench = bench;
        fileName = fname;
        fname = "in/" + fname;
        Scanner in = new Scanner(new FileInputStream(fname));
        readInput(in);

//        int[] len = p.clone();
//        Arrays.sort(len);

//        SILENT = true;
//        while (true) {
//            System.out.print(Arrays.toString(INITIAL_CF));
//            solve(System.currentTimeMillis() + 10000);
//            System.out.println("\t" + POINTS);
//            for (int i = 0; i < INITIAL_CF.length; i++) {
//                INITIAL_CF[i] = 100 * Math.random();
//            }
//            if (false) break;
//        }
        solve();

        writeOutputFile();
    }

    void writeOutputFile() throws IOException {
        String sb = buildOutput();

        String out = fileName.replace(".in", ".out");
        if (inst.POINTS > bench) {
            out = out.replace(".out", "_" + inst.POINTS + ".out");
        }

        if (!SILENT) System.out.printf("(%s) POINTS: %,d (%.0f%% better, %d)%n",
                fileName.charAt(0), inst.POINTS, 100d * (inst.POINTS - bench) / bench, inst.POINTS - bench);

        Files.write(Paths.get(out), sb.getBytes());
    }

    int[][] solve() throws IOException {
        return solve(Long.MAX_VALUE);
    }
    
    int[][] solve(long solveUntil) throws IOException {
        init();

        ArrayList<Integer> fleets = new ArrayList<>();
        for (int i = 0; i < F; i++) {
            fleets.add(i);
        }

        File source = new File(fileName.replace(".in", "") + "_" + bench + ".out");
        if (source.exists()) {
            Scanner bsc = new Scanner(source);
            for (int f_i = 0; f_i < F; f_i++) {
                int c = bsc.nextInt();
                for (int i = 0; i < c; i++) {
                    int r_i = bsc.nextInt();
                    Option o = new Option(f_i, r_i);
                    if (!o.canTake()) {
                        throw new RuntimeException();
                    }
                    o.take();
                    by_start.remove((Integer) r_i);
                }
            }
            if (bench != POINTS) {
                throw new RuntimeException();
            }
        } else {

            boolean anyMoved = true;
            while (anyMoved) {
                anyMoved = false;

                for (int f_i : fleets) {
                    //                while (findNextBest(f_i)) {}
                    boolean found = findNextBest(f_i);
                    if (found) anyMoved = true;
                }
            }
        }

        // here we can try to push something around...
        if (!SILENT) System.out.println("Remaining rides: " + by_start.size());

        this.startPoints = POINTS;
        this.startTime = System.currentTimeMillis();

        int timesWithoutImprovement = 0;
        long lastSuccessTime = System.currentTimeMillis();

        while (System.currentTimeMillis() < solveUntil) {
            //&&
                //timesWithoutImprovement < 6 * F && 
//                (System.currentTimeMillis() - lastSuccessTime) < 15 * 60e3) {

            fleets.sort(Comparator.comparing(i -> POINTS_BY_CAR[i]));
            int f_i = fleets.get((int) (Math.pow(Math.random(), 10) * F));

            boolean better = tryRegenerateFleet(f_i, timesWithoutImprovement);
            if (!better) {
                timesWithoutImprovement++;
                if (timesWithoutImprovement % 10_000 == 0) {
                    if (!SILENT) System.out.printf("No improvement for %,d ", timesWithoutImprovement);
                    for (int i = 0; i < INITIAL_CF.length; i++) {
                        INITIAL_CF[i] = Math.max(0, 100 * Math.random() - 10);
                    }
                    INITIAL_CF[0] /= 100;
//                    INITIAL_CF[1] /= 100;
//                    INITIAL_CF[2] /= 100;
                    INITIAL_CF[3] /= 100;
                    INITIAL_CF[4] /= 100;
//                    INITIAL_CF[5] *= 10000;
                    if (!SILENT) System.out.println(Arrays.toString(INITIAL_CF));
                }
            } else {
                timesWithoutImprovement = 0;
                lastSuccessTime = System.currentTimeMillis();
            }
        }

        // try to put any 

        TOTAL_POINTS += POINTS;
        return answer;
    }

    void init() {
        POINTS = 0;
        POINTS_BY_CAR = new int[F];
        by_start = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            by_start.add(i);
        }

        taken = new boolean[N];
        answer = new int[F][N];
        count = new int[F];
        step = new int[F];
        f_pos = new Pos[F];
        for (int f_i = 0; f_i < F; f_i++) {
            f_pos[f_i] = POS0;
        }
    }

    boolean tryRegenerateFleet(int f_i, int timesWithoutImprovement) throws IOException {
        // try to regenerate some answer
        int[] oldAnswer = answer[f_i];
        int oldStep = step[f_i];
        int oldCount = count[f_i];
        int oldPoints = POINTS;
        int oldPointsByCar = POINTS_BY_CAR[f_i];
        ArrayList<Integer> oldByStart = new ArrayList<>(by_start);
        Pos oldPos = f_pos[f_i];

        answer[f_i] = new int[N];
        step[f_i] = 0;
        count[f_i] = 0;
        POINTS -= oldPointsByCar;
        POINTS_BY_CAR[f_i] = 0;
        f_pos[f_i] = POS0;

        for (int i = 0; i < oldCount; i++) {
            taken[oldAnswer[i]] = false;
            by_start.add(oldAnswer[i]);
        }

        // regenerate greedy
        regenerateGreedy(f_i);

        if (POINTS_BY_CAR[f_i] <= oldPointsByCar) {
            answer[f_i] = oldAnswer;
            step[f_i] = oldStep;
            count[f_i] = oldCount;
            POINTS = oldPoints;
            POINTS_BY_CAR[f_i] = oldPointsByCar;
            by_start = oldByStart;
            f_pos[f_i] = oldPos;
            return false;
        } else {
            imprTime.add(System.currentTimeMillis());
            imprPoints.add(POINTS);

//            System.out.println(Arrays.toString(cf));

            double rate = 1d * (POINTS - imprPoints.get(Math.max(0, imprPoints.size() - 10))) /
                    (System.currentTimeMillis() - imprTime.get(Math.max(0, imprTime.size() - 10)));

            if (!SILENT) System.out.printf("BETTER by %d, looped for %d (total %,d) improvement rate %.2f%n",
                    POINTS_BY_CAR[f_i] - oldPointsByCar, timesWithoutImprovement, POINTS,
                    rate);
            if (POINTS > bench) {
                writeOutputFile();
            }


            return true;
        }
    }

    void regenerateGreedy(int f_i) {
        boolean found;
        do {
            found = findNextBest(f_i);
        } while (found);
    }

    boolean findNextBest(int f_i) {
        Option best;
        // some random
        double[] cf = INITIAL_CF.clone();
        for (int i = 0; i < cf.length; i++) cf[i] *= Math.random();
        Comparator<Option> cmp = Comparator.comparing(x ->
                -cf[0] * x.pps
                        + cf[1] * x.distToStart
                        + cf[2] * x.waitTime
                        + cf[3] * x.canStart
                        - cf[4] * x.bonus
                        + cf[5] * x.rand);

//        Comparator<Option> cmp = Comparator.comparing(x -> x.rand);
        
        best = null;
        for (int r_i : by_start) {
            Option o = new Option(f_i, r_i);
            if (o.canTake() && (best == null || cmp.compare(o, best) < 0)) {
                best = o;
            }
        }

        if (best != null) {
            by_start.remove((Integer) best.r_i);
            best.take();
            return true;
        }
        return false;
    }

    void addPoints(int f_i, int r_i, boolean onTime) {

        if (VERBOSE) {
            if (onTime) {
                System.out.printf("Driver %d takes ride %d, gets %d points, bonus %d%n",
                        f_i, r_i, p[r_i], B);
            } else {
                System.out.printf("Driver %d takes ride %d, gets %d points, no bonus%n",
                        f_i, r_i, p[r_i]);
            }
        }

        int total = p[r_i] + (onTime ? B : 0);

        POINTS += total;
        POINTS_BY_CAR[f_i] += total;
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
        int f_i, r_i, canStart, canFinish, bonus, points, distToStart, waitTime, timeToStart, tooLong;
        double pps, rand = Math.random();

        public Option(int f_i, int r_i) {
            this(f_i, r_i, f_pos[f_i], step[f_i]);
        }

        public Option(int f_i, int r_i, Pos start_pos, int start_time) {
            this.f_i = f_i;
            this.r_i = r_i;

            distToStart = start_pos.dist(ab[r_i]);
            canStart = Math.max(start_time + distToStart, s[r_i]);
            timeToStart = canStart - start_time;
            waitTime = canStart - (start_time + distToStart);
            canFinish = canStart + p[r_i];
            bonus = canStart == s[r_i] ? B : 0;
            points = p[r_i] + bonus;

            pps = 1d * points / (canFinish - start_time);
            tooLong = p[r_i] > 10000 ? 1 : 0;
        }

        boolean canTake() {
            return canFinish <= f[r_i];
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
//            if ((cmp = Double.compare(this.tooLong, that.tooLong)) != 0) return cmp;
//            if ((cmp = Double.compare(that.points, this.points)) != 0) return cmp;
//            if ((cmp = Double.compare(this.waitTime, that.waitTime)) != 0) return cmp;
            if ((cmp = Double.compare(this.canStart, that.canStart)) != 0) return cmp;
            if ((cmp = Double.compare(that.pps, this.pps)) != 0) return cmp;
            return 0;
        }

    }

}
