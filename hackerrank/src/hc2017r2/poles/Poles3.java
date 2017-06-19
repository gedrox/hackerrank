package hc2017r2.poles;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.TreeSet;

public class Poles3 {

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
                    .append(String.format("%.2f", answer))
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

        TreeSet<double[]> p = new TreeSet<>((a1, a2) -> Double.compare(a1[0], a2[0]));

        double sum = 0;

        for (int i = 0; i < n; i++) {

            //System.err.println("Dropping point " + (i + 1) + " in (" + x + ", " + h + ")");

            double[] np = new double[]{x, h};
            double[] prev = p.lower(np);
            double[] next = p.higher(np);

            double[] same = p.ceiling(np);
            if (same != null && same[0] != np[0]) same = null;

            // under
            if (same != null && same[1] >= np[1]) {
                area[i] = area[i - 1];
            } else if (prev == null && next == null) {

                if (same != null) p.remove(same);

                p.add(np);
                p.add(new double[]{x - h, 0});
                p.add(new double[]{x + h, 0});

                area[i] = h * h;
//                System.err.println("initial " + (h * h));
            } else {

                double drop;
                area[i] = area[i - 1];

                // under
                if (prev != null && next != null && (next[1] - prev[1]) * (x - prev[0]) >= (h - prev[1]) * (next[0] - prev[0])) {
                    // nothing to do..
                } else {
                    if (same != null) p.remove(same);
                    if (next == null || prev == null) {
                        drop = 0;
                    } else {
                        drop = (next[1] - prev[1]) / (next[0] - prev[0]) * (x - prev[0]) + prev[1];
                    }

                    {
//                        if (prev == null) {
//                            area[i] += h * h / 2d;
//                            p.add(new double[]{x - h, 0});
//
//                            //System.err.println("Add full left " + (h * h / 2d));
//                        }

                        // where it drops?
                        double h10 = drop;
                        double h11 = h;
                        double currX = x;

                        for (; prev != null; prev = p.lower(prev)) {
                            double h00 = prev[1];
                            double h01 = h11 - (currX - prev[0]);

                            if (h01 > h00) {
                                area[i] += ((h11 - h10) + (h01 - h00)) * (currX - prev[0]) / 2d;

                                //System.err.println("Add trapec left " + (((h11 - h10) + (h01 - h00)) * (currX - prev[0]) / 2d));

                                p.remove(prev);
                            } else {
                                if (h00 == h10 && h00 == 0) {
                                    area[i] += (h11 - h10) * (h11 - h10) / 2d;
                                    //System.err.println("Add rather full left " + ((h11 - h10) * (h11 - h10) / 2d));
                                    p.add(new double[]{currX - h11, 0});
                                } else {
                                    area[i] += (h11 - h10) * (h11 - h10) / 4d;
                                    //System.err.println("Add quarter left " + ((h11 - h10) * (h11 - h10) / 4d));
                                    p.add(new double[]{currX - (h11 - h10) / 2d, (h11 + h10) / 2d});
                                }
                                h11 = 0;
                                break;
                            }

                            currX = prev[0];
                            h10 = h00;
                            h11 = h01;
                        }

                        if (h11 > 0) {
                            area[i] += h11 * h11 / 2;
                            p.add(new double[]{currX - h11, 0});
                            //System.err.println("Add full left " + (h11 * h11 / 2d));
                        }
                    }

                    {
//                        if (next == null) {
//                            area[i] += h * h / 2d;
//                            //System.err.println("Right full " + h * h / 2d);
//                            p.add(new double[]{x + h, 0});
//                        }

                        // where it drops?
                        double h00 = drop;
                        double h01 = h;
                        double currX = x;

                        for (; next != null; next = p.higher(next)) {
                            double h10 = next[1];
                            double h11 = h01 - (next[0] - currX);

                            if (h11 > h10) {
                                area[i] += ((h11 - h10) + (h01 - h00)) * (next[0] - currX) / 2d;
                                //System.err.println("Add right trapec " + ((h11 - h10) + (h01 - h00)) * (next[0] - currX) / 2d);
                                p.remove(next);
                            } else {
                                if (h00 == h10 && h00 == 0) {
                                    area[i] += (h01 - h00) * (h01 - h00) / 2d;
                                    //System.err.println("Add rather full right " + (h01 - h00) * (h01 - h00) / 2d);
                                    p.add(new double[]{currX + h01, 0});
                                } else {
                                    area[i] += (h01 - h00) * (h01 - h00) / 4d;
                                    //System.err.println("Add quarter right " + (h01 - h00) * (h01 - h00) / 4d);
                                    p.add(new double[]{currX + (h01 - h00) / 2d, (h01 + h00) / 2d});
                                }
                                h01 = 0;
                                break;
                            }

                            currX = next[0];
                            h00 = h10;
                            h01 = h11;
                        }

                        if (h01 > 0) {
                            area[i] += h01 * h01 / 2;
                            p.add(new double[]{currX + h01, 0});
                            //System.err.println("Add full right " + (h01 * h01 / 2d));
                        }
                    }

                    p.add(new double[]{x, h});
                }
            }

            x = ((ax * x + bx) % cx) + 1;
            h = ((ah * h + bh) % ch) + 1;

            sum += area[i];

//            System.err.println("Area so far is " + area[i]);
        }

        //System.err.println(Arrays.toString(area));

        return sum;
    }

    @Test
    public void test() {
//        System.out.println(solve(5, 1, 1, 0, 100, 1, 0, 0, 100));
//        System.out.println(solve(5,
//                1, 1, 0, 100,
//                1, 1, 0, 100));
//        System.out.println(solve(13,
//                3, 3, 0, 100,
//                7, 7, 0, 100));
        System.out.println(solve(800,
                1, 13107, 5, 12917,
                1, 12917, 5, 13107));
    }
}
