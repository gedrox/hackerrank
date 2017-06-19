package hc2017r2.poles;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class Poles {
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
        ArrayList<double[]> points = new ArrayList<>();
        points.add(new double[]{x, h});
        for (int i = 1; i < n; i++) {
            x = ((ax * x + bx) % cx) + 1;
            h = ((ah * h + bh) % ch) + 1;
            points.add(new double[]{x, h});
        }

        Collections.sort(points, (a1, a2) -> Double.compare(a1[0], a2[0]));

        ArrayList<double[]> newPoints = new ArrayList<>();

        for (int i = 1; i < points.size(); i++) {
            double x1 = points.get(i - 1)[0];
            double h1 = points.get(i - 1)[1];
            double x2 = points.get(i)[0];
            double h2 = points.get(i)[1];

            double y = (h1 + h2 - x2 + x1) / 2;
            if (y >= h1) {
                points.remove(i - 1);
                // need to compare again, maybe super tall
                i--;
            } else if (y >= h2) {
                points.remove(i);
                i--;
            }
        }

        for (int i = 1; i < points.size(); i++) {
            double x1 = points.get(i - 1)[0];
            double h1 = points.get(i - 1)[1];
            double x2 = points.get(i)[0];
            double h2 = points.get(i)[1];

            double y = (h1 + h2 - x2 + x1) / 2;
            newPoints.add(new double[]{x1 + (h1 - y), y});
        }

        newPoints.add(new double[]{points.get(0)[0] - points.get(0)[1], 0});
        newPoints.add(new double[]{points.get(points.size() - 1)[0] + points.get(points.size() - 1)[1], 0});

        points.addAll(newPoints);

        Collections.sort(points, (a1, a2) -> Double.compare(a1[0], a2[0]));

        double area = 0;

        for (int i = 1; i < points.size(); i++) {
            area += (points.get(i - 1)[1] + points.get(i)[1]) * (points.get(i)[0] - points.get(i - 1)[0]) / 2;
        }

        return area;
    }

    @Test
    public void test() {

        double sum = 0;
        for (int i = 1; i <= 800; i++) {
            double v = solve(i,
                    1, 13107, 5, 12917,
                    1, 12917, 5, 13107);
            System.out.println(v);
            sum += v;
        }
        System.out.println("SUM = " + sum);
    }
}
