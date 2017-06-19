package hc2017r1.pie;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class PieProgress {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String s = bi.readLine();

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
            int m = Integer.parseInt(split[1]);
            int[][] C = new int[n][];
            for (int i = 0; i < n; i++) {
                C[i] = new int[m];
                split = bi.readLine().split(" ");
                for (int j = 0; j < m; j++) {
                    C[i][j] = Integer.parseInt(split[j]);
                }
            }
            sb.append("Case #")
                    .append(q_i)
                    .append(": ")
                    .append(solve(C, n, m))
                    .append('\n');
        }
        Files.write(Paths.get("src/hc2017r1/i-love-pies.txt"), sb.toString().getBytes());
        System.out.print(sb.toString());
    }

    static int solve(int[][] C, int n, int m) {
        for (int[] costs : C) {
            Arrays.sort(costs);
        }

        // each day's index of next to buy
        int[] ind = new int[n];

        // tax to pay if you buy next
        int[] tax = new int[n];
        for (int i = 0; i < n; i++) {
            tax[i] = 1;
        }

        int total = 0;
        for (int currDay = 0; currDay < n; currDay++) {
            int min = Integer.MAX_VALUE, possMin;
            int choice = -1;
            for (int prevDay = 0; prevDay <= currDay; prevDay++) {
                if (ind[prevDay] < m && (possMin = C[prevDay][ind[prevDay]] + tax[prevDay]) < min) {
                    min = possMin;
                    choice = prevDay;
                }
            }
            total += min;
            ind[choice]++;
            tax[choice] = 2 * ind[choice] + 1;
        }

        return total;
    }

}
