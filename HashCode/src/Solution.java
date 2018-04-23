import org.junit.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;


public class Solution {

    public static int BONUS_FLEETS = 0;
    static String[] files = {
            "a_example.in",
            "b_should_be_easy.in",
            "c_no_hurry.in",
            "d_metropolis.in",
            "e_high_bonus.in"
    };
    private static Solution inst = new Solution();
    String fileName;
    int R, C, F, N, B, T;
    int[] a, b, x, y, s, f, p;
    Pos[] ab, xy;
    int POINTS = 0;
    int TOTAL_POINTS = 0;
    boolean VERBOSE = true;
    int[] BENCHMARKS = {
            10,
            176_877,
            15_817_815,
            11_816_602,
            21_465_945
    };

    int BENCHMARK = 49_272_812;
    private boolean[] taken;
    private Pos[] f_pos;
    private int[][] answer;
    private int[] count;
    private int[] step;

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
    public void runOne() throws IOException {
        int file_i = 'd' - 'a';
        inst.VERBOSE = false;
        for (int i = 0; i < 1; i++) {
//            BONUS_FLEETS = i;
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

    private void processFile(String fname, int bench) throws IOException {
        fileName = fname;
        fname = "in/" + fname;
        Scanner in = new Scanner(new FileInputStream(fname));
        readInput(in);
        solve();

        String sb = buildOutput();

        String out = fname.replace(".in", ".out")
                .replace("in/", "");

        System.out.printf("(%s) POINTS: %,d (%.0f%% better, %d)%n",
                fileName.charAt(0), inst.POINTS, 100d * (inst.POINTS - bench) / bench, inst.POINTS - bench);

        Files.write(Paths.get(out), sb.getBytes());
    }

    @Test
    @Ignore
    public void test() {
        Scanner in = new Scanner("3 4 2 3 2 10\n" +
                "0 0 1 3 2 9\n" +
                "1 2 1 0 0 9\n" +
                "2 0 2 2 0 9");

        readInput(in);

        int[][] answer = solve();

        System.out.println("TOTAL points: " + TOTAL_POINTS);
        System.out.println(Arrays.deepToString(answer));
    }

    private int[][] solve() {
        POINTS = 0;
        ArrayList<Integer> by_start = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            by_start.add(i);
        }

        Function<Integer, Integer> latestStart = i -> f[i] - p[i];
        Function<Integer, Integer> byStart = i -> s[i];
        Function<Integer, Integer> byPoints = i -> -p[i];

        by_start.sort(Comparator.comparing(byStart).thenComparing(byPoints));
        taken = new boolean[N];
        answer = new int[F][N];
        count = new int[F];
        step = new int[F];
        f_pos = new Pos[F];
        for (int f_i = 0; f_i < F; f_i++) {
            f_pos[f_i] = new Pos(0, 0);
        }


//        boolean anyMoved = true;
//        while (anyMoved) {
//            Option best = null;
//            anyMoved = false;
//            Random r = new Random(0);
//
//            for (int i = 0; i < 10000; i++) {
//                int f_i = r.nextInt(F);
//                int r_i = r.nextInt(N);
//                if (taken[r_i]) continue;
//
//                Option option = new Option(f_i, r_i);
//                if (!option.canTake()) {
//                    continue;
//                }
//
//                if (best == null || option.compareTo(best) < 0) {
//                    best = option;
//                }
//            }
//            if (best != null) {
//                best.take();
//                anyMoved = true;
//            }
//        }

        ArrayList<Integer> fleets = new ArrayList<>();
        for (int i = 0; i < F; i++) {
            fleets.add(i);
        }

        boolean anyMoved = true;
        while (anyMoved) {
            anyMoved = false;
//            Collections.shuffle(fleets);
            for (int f_i : fleets) {

//            boolean anyMoved = true;
//            while (anyMoved) {
//                anyMoved = false;
                Option best = null;

//                while (true) {

                for (int r_i : by_start) {

                    Option o = new Option(f_i, r_i);

                    if (!o.canTake()) continue;

                    if (f_i < BONUS_FLEETS && o.bonus == 0) continue;

                    if (best == null || o.compareTo(best) < 0) {
                        if (best != null) {
//                            if (Math.random()/10 > o.pps - best.pps) {
                            best = o;
//                            } else {
//                                System.err.println(o.pps - best.pps);
//                            }
                        } else {
                            best = o;
                        }
                    }

//                    Function<Integer, Integer> cmp = i -> i == N ? o.canFinish - 1 : f[i] - p[i];
//                    int next = Collections.binarySearch(by_start, N, Comparator.comparing(cmp));
//                    if (next < 0) next = -next - 1;
//
//                    int checkNext = 10;
//                    int checkTill = Math.min(by_start.size(), next + checkNext);
//                    
//                    for (int i = next; i <= checkTill; i++) {
//                        int r_j = by_start.size() == i ? r_i : by_start.get(i);
//
//                        Option2 option = new Option2(f_i, r_i, r_j);
//
//                        // can we deliver in time? yes -- just take it
////                        Option option = new Option(f_i, r_i);
//                        if (!option.canTake()) {
//                            continue;
//                        }
//
////                        if (r.nextInt(10) == 0) continue;
//
//                        if (best == null || option.compareTo(best) < 0) {
//                            best = option;
//                        }
//                    }
                }

//                }

                if (best != null) {
                    by_start.remove((Integer) best.r_i);
//                    if (best.option2 != null) by_start.remove((Integer) best.option2.r_i);
                    best.take();
                    anyMoved = true;

//                    if (by_start.size() % 100 <= 1) {
//                        System.out.println(by_start.size() + " left");
//                    }
                }
//                answer[f_i] = Arrays.copyOfRange(answer[f_i], 0, count[f_i]);
            }

        }

        TOTAL_POINTS += POINTS;
        return answer;
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
        int f_i, r_i, canStart, canFinish, bonus, points, distToStart, waitTime, timeToStart;
        double pps;

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

            if (f_i < BONUS_FLEETS) {
                int cmp;
                if ((cmp = Double.compare(that.bonus, this.bonus)) != 0) return cmp;
                if ((cmp = Double.compare(this.waitTime, that.waitTime)) != 0) return cmp;
                return 0;
            } else {
                int cmp;
                
//                if (this.waitTime/10 == that.waitTime/10) {
//                    boolean near1 = this.canFinish >= T - 10;
//                    boolean near2 = that.canFinish >= T - 10;
//                    
//                    if (near1 != near2) return near2 ? -1 : 1;
////                    if ((cmp = Double.compare(that.canFinish, this.canFinish)) != 0) return cmp;
//                }
                
//                if ((cmp = Double.compare(this.waitTime / 100, that.waitTime / 100)) != 0) return cmp;
                if ((cmp = Double.compare(this.waitTime, that.waitTime)) != 0) return cmp;
//                if ((cmp = Double.compare(that.canFinish, this.canFinish)) != 0) return cmp;
//                if ((cmp = Double.compare(this.timeToStart, that.timeToStart)) != 0) return cmp;
//                if ((cmp = Double.compare(Math.round(that.pps * 100000), Math.round(this.pps * 100000))) != 0) return cmp;
//                if ((cmp = Double.compare(this.distToStart, that.distToStart)) != 0) return cmp;
                if ((cmp = Double.compare(that.pps, this.pps)) != 0) return cmp;
//                if ((cmp = Double.compare(that.points, this.points)) != 0) return cmp;
//                if ((cmp = Double.compare(Math.round(that.pps * 100000), Math.round(this.pps * 100000))) != 0) return cmp;
                return 0;
            }
//            return that.canStart - this.canStart;
//            return that.bonus - this.bonus;
        }

        public int oldCompare(Option that) {
//            if (this.canStart / 100 != that.canStart / 100) return this.canStart - that.canStart;
//            if (this.distToStart / 100 != that.distToStart / 100) return this.distToStart - that.distToStart;
//            if (this.bonus != that.bonus) return that.bonus - this.bonus;
//            if (this.score() != that.score()) return Long.compare(that.score(), this.score());

            if (this.canStart != that.canStart) return this.canStart - that.canStart;
            if (this.waitTime != that.waitTime) return this.waitTime - that.waitTime;
            if (this.points != that.points) return that.points - this.points;

            int cmp;
            if ((cmp = comp(this.canStart, that.canStart, 0.5)) != 0) return cmp;
            if ((cmp = comp(this.distToStart, that.distToStart, 0.5)) != 0) return cmp;
            if ((cmp = comp(that.points, this.points, 0.5)) != 0) return cmp;

            if ((cmp = comp(that.points, this.points, 0.5)) != 0) return cmp;

            if ((cmp = comp(this.canStart, that.canStart, 0.8)) != 0) return cmp;
            if ((cmp = comp(this.distToStart, that.distToStart, 0.8)) != 0) return cmp;
            if ((cmp = comp(that.points, this.points, 0.8)) != 0) return cmp;

            if (this.points != that.points) return that.points - this.points;

//            if (this.distToStart != that.distToStart) return this.distToStart - that.distToStart;
            if (this.canStart != that.canStart) return this.canStart - that.canStart;
            if (this.canFinish != that.canFinish) return this.canFinish - that.canFinish;

            return 0;
        }

        private Integer comp(int thisVal, int thatVal, double ratio) {
            assert ratio < 1;
            if ((thisVal + 1d) / (thatVal + 1) < ratio) return -1;
            if ((thisVal + 1d) / (thatVal + 1) > 1 / ratio) return 1;
            return 0;
        }
    }

    private class Option2 implements Comparable<Option2> {
        Option option;
        Option option2;

        double pps;

        public Option2(int f_i, int r_i, int r_j) {
            option = new Option(f_i, r_i);

            if (r_i != r_j) {
                option2 = new Option(f_i, r_j, xy[r_i], option.canFinish);
                pps = 1d * (option.points + option2.points) / (option2.canFinish - step[f_i]);
            } else {
                pps = option.pps;
            }

        }

        boolean canTake() {
            return option.canTake() && (option2 == null || option2.canTake());
        }

        void take() {
//            System.out.println(option2 != null);
            option.take();
            if (option2 != null) option2.take();
        }

        @Override
        public int compareTo(Option2 that) {
            return Double.compare(that.pps, this.pps);
        }
    }
}
