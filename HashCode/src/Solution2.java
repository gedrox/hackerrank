import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class Solution2 {

    static String[] files = {
            "a_example.in",
            "b_should_be_easy.in",
            "c_no_hurry.in",
            "d_metropolis.in",
            "e_high_bonus.in"
    };
    private static Solution2 inst = new Solution2();
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
            11_812_165,
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
    public void runOne() throws IOException, ClassNotFoundException {
        int file_i = 1;
        inst.VERBOSE = true;
        for (int i = 0; i < 1; i++) {
            inst.processFile(files[file_i], BENCHMARKS[file_i]);
        }
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
        ArrayList<Integer> nearest00 = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            nearest00.add(i);
        }

        Pos pos00 = new Pos(0, 0);
        Function<Integer, Integer> zeroDist = i -> pos00.dist(ab[i]) + Math.max(0, s[i] - pos00.dist(ab[i]));

        nearest00.sort(Comparator.comparing(zeroDist));
        int[] near00 = new int[N];
        for (int i = 0; i < N; i++) near00[i] = nearest00.get(i);
        
        taken = new boolean[N];
        answer = new int[F][N];
        count = new int[F];
        step = new int[F];
        f_pos = new Pos[F];
        for (int f_i = 0; f_i < F; f_i++) {
            f_pos[f_i] = new Pos(0, 0);
        }

        int[][] near;
        if (false) {
            Integer[][] nearest = new Integer[N][];
            for (int i = 0; i < N; i++) {

                int earliestFinish = s[i] + p[i];

                ArrayList<Integer> cand = new ArrayList<>();

                for (int j = 0; j < N; j++) {
                    if (i == j) continue;
                    // can we make?
                    int earliestStart = Math.max(earliestFinish + xy[i].dist(ab[j]), s[j]);
                    int earliestNextFinish = earliestStart + p[j];
                    if (earliestNextFinish > f[j]) continue;

                    cand.add(j);
                }

                nearest[i] = cand.toArray(new Integer[cand.size()]);

                int finalI = i;
                Arrays.sort(nearest[i], Comparator.comparing(k -> 
                        xy[finalI].dist(ab[k]) + Math.max(0, s[k] - earliestFinish)));
                System.out.println(i);
            }

            near = new int[N][];
            for (int i = 0; i < N; i++) {
                near[i] = new int[nearest[i].length];
                for (int j = 0; j < nearest[i].length; j++) {
                    near[i][j] = nearest[i][j];
                }
            }

            FileOutputStream fos = new FileOutputStream("near/" + fileName + ".near");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(near);
            oos.close();
            fos.close();
        } else {
            FileInputStream fis = new FileInputStream("near/" + fileName + ".near");
            ObjectInputStream ois = new ObjectInputStream(fis);
            near = (int[][]) ois.readObject();
        }
        
        // we have "near[][]"
        
        for (int f_i = 0; f_i < F; f_i++) {
            int[] tryNext = near00;
            
            while (true) {
                int took = -1;

                for (int r_i : tryNext) {
                    if (taken[r_i]) continue;

                    Option option = new Option(f_i, r_i, f_pos[f_i], step[f_i]);
                    if (option.canTake()) {
                        option.take();
                        took = r_i;
                        break;
                    }
                }

                if (took == -1) break;
                
                tryNext = near[took];
            }
            
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
            if ((cmp = Double.compare(that.pps, this.pps)) != 0) return cmp;
            return that.bonus - this.bonus;
        }

    }

}
