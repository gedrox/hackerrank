package hc2017r1.zombie;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import static java.lang.Integer.parseInt;

public class Zombie {

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String line = bi.readLine();

        long longest = 0;

        // file path from "copy as path" feature
        if (line.contains(":") || line.contains("/")) {
            line = line.replace("\"", "");
            bi = new BufferedReader(new InputStreamReader(new FileInputStream(line)));
            line = bi.readLine();
        }

        int t = parseInt(line);
        StringBuilder sb = new StringBuilder();
        for (int t_i = 1; t_i <= t; t_i++) {
            String[] split = bi.readLine().split(" ");
            int n = parseInt(split[0]);
            int r = parseInt(split[1]);

            int[] x = new int[n];
            int[] y = new int[n];
            for (int i = 0; i < n; i++) {
                split = bi.readLine().split(" ");
                x[i] = parseInt(split[0]);
                y[i] = parseInt(split[1]);
            }

            long t0 = System.currentTimeMillis();
            long answer = solve(x, y, r);
            longest = Math.max(longest, System.currentTimeMillis() - t0);

            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(answer)
                    .append('\n');

            System.out.print('.');
        }

        Files.write(Paths.get("src/hc2017r1/i-love-zombie.txt"), sb.toString().getBytes());

        System.out.println();
        System.out.print(sb.toString());
        System.out.println();
        System.out.printf("Longest run was %dms%n", longest);
    }

    static int solve(int[] x, int[] y, int r) {
        int maxCount = 0;
        int[] bestRect = new int[4];

        int[][] countCache = new int[x.length][];

        for (int i = 0; i < x.length; i++) {
            int x_i = x[i];
            countCache[i] = new int[y.length];
            for (int j = 0; j < y.length; j++) {
                int y_i = y[j];
                int count = countZombies(x_i, y_i, x_i + r, y_i + r, x, y);
                countCache[i][j] = count;
                if (count > maxCount) {
                    maxCount = count;
                    bestRect = new int[]{x_i, y_i, x_i + r, y_i + r};
                }
            }
        }

        int nextMax = 0;

        for (int i = 0; i < x.length; i++) {
            int x_i = x[i];
            for (int j = 0; j < y.length; j++) {
                int y_i = y[j];
                int count = countCache[i][j];

                // We have collision!
                if (bestRect[0] <= x_i + r && x_i <= bestRect[2]
                        && bestRect[1] <= y_i + r && y_i <= bestRect[3]) {
                    int intersCount = countZombies(
                            Math.max(bestRect[0], x_i),
                            Math.max(bestRect[1], y_i),
                            Math.min(bestRect[2], x_i + r),
                            Math.min(bestRect[3], y_i + r),
                            x, y);
                    count -= intersCount;
                }

                if (count > nextMax) {
                    nextMax = count;
                }
            }
        }

        return maxCount + nextMax;
    }

    private static int countZombies(int x_0, int y_0, int x_1, int y_1, int[] x, int[] y) {
        int count = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i] >= x_0 && x[i] <= x_1 && y[i] >= y_0 && y[i] <= y_1) {
                count++;
            }
        }
        return count;
    }

    @Test
    public void testSmall() {
        System.out.println(solve(
                new int[]{1, 2, 2, 5, 6, 4, 4},
                new int[]{5, 3, 1, 1, 3, 4, 5},
                3));
    }

    @Test
    public void testRandom() {
        Random rand = new Random();
        int t = 50;
        for (int t_i = 0; t_i < t; t_i++) {
            int n = 50;
            int x[] = new int[n];
            int y[] = new int[n];
            for (int i = 0; i < n; i++) {
                x[i] = rand.nextInt(1000000001);
                y[i] = rand.nextInt(1000000001);
            }
            int r = rand.nextInt(1000000000) + 1;

            int answer = solve(x, y, r);
            if (answer < 2 || answer > n) throw new RuntimeException();
            System.out.println(answer);
        }
    }
}
