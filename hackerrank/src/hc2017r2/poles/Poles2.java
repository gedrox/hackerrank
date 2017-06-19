package hc2017r2.poles;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.TreeSet;

public class Poles2 {

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String s = bi.readLine();

        long longest = 0;

        // file path from "copy as path" feature
        if (s.contains(":") || s.contains("/")) {
            s = s.replace("\"", "");
            bi = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
            s = bi.readLine();
        }

        int q = Integer.parseInt(s);
        StringBuilder sb = new StringBuilder();
        for (int q_i = 1; q_i <= q; q_i++) {
            String[] split = bi.readLine().split(" ");
            int n = Integer.parseInt(split[0]);

            split = bi.readLine().split(" ");
            int x1 = Integer.parseInt(split[0]);
            int ax = Integer.parseInt(split[1]);
            int bx = Integer.parseInt(split[2]);
            int cx = Integer.parseInt(split[3]);

            split = bi.readLine().split(" ");
            int h1 = Integer.parseInt(split[0]);
            int ah = Integer.parseInt(split[1]);
            int bh = Integer.parseInt(split[2]);
            int ch = Integer.parseInt(split[3]);

            long t0 = System.currentTimeMillis();
            double answer = solve(n, x1, ax, bx, cx, h1, ah, bh, ch);
            longest = Math.max(longest, System.currentTimeMillis() - t0);

            sb.append("Case #")
                    .append(q_i)
                    .append(": ")
                    .append(answer)
                    .append('\n');

            System.out.print('.');
        }

        Files.write(Paths.get("src/hc2017r2/poles.txt"), sb.toString().getBytes());

        System.out.println();
        System.out.print(sb.toString());
        System.out.println();
        System.out.printf("Longest run was %dms%n", longest);
    }

    private static double solve(int n, long x, long ax, long bx, long cx, long h, long ah, long bh, long ch) {

        double[] area = new double[n];
        boolean[] ok = new boolean[n];

        TreeSet<double[]> peaks = new TreeSet<>((a1, a2) -> Double.compare(a1[0], a2[0]));

        double sum = 0;

        for (int i = 0; i < n; i++) {
            ok[i] = true;
            boolean calced = false;
            double[] newPoint = {x, h, 0};

            double[] start = peaks.floor(newPoint);
            for (double[] prev = peaks.floor(newPoint); prev != null; prev = peaks.lower(prev)) {
                double y_2 = (prev[1] + h - x + prev[0]) / 2;

                int j= -1;

                if (h <= y_2) {
                    area[i] = area[j];
                    ok[i] = false;
                    calced = true;
                    break;
                }

                if (prev[1] >= y_2) {
                    area[i] = area[j];
                    area[i] += h * h;
                    area[i] -= y_2 * y_2;
                    calced = true;
                    break;
                }
            }

            if (!calced) {
                area[i] = h * h;
            }

            if (ok[i]) {

                peaks.add(newPoint);
            }

            x = ((ax * x + bx) % cx) + 1;
            h = ((ah * h + bh) % ch) + 1;

            sum += area[i];
        }

        return sum;
    }
}
