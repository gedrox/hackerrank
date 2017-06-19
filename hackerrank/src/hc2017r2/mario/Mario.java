package hc2017r2.mario;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Mario {
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
            int m = Integer.parseInt(split[1]);
            int k = Integer.parseInt(split[2]);

            long t0 = System.currentTimeMillis();
            int answer = solve(n, m, k);
            longest = Math.max(longest, System.currentTimeMillis() - t0);

            sb.append("Case #")
                    .append(q_i)
                    .append(": ")
                    .append(answer)
                    .append('\n');

            System.out.print('.');
        }

        Files.write(Paths.get("src/hc2017r2/mario.txt"), sb.toString().getBytes());

        System.out.println();
        System.out.print(sb.toString());
        System.out.println();
        System.out.printf("Longest run was %dms%n", longest);
    }

    static int solve(int n, int m, int k) {
        ArrayList<Integer> p = new ArrayList<>();

        if (2 * k + 3 <= n) {
            p.add((m - 1) / k + 1);
        }
        if (2 * k + 3 <= m) {
            p.add((n - 1) / k + 1);
        }

        if ((2 * k + 1 <= n && 2 * k + 3 <= m) || (2 * k + 1 <= m && 2 * k + 3 <= n)) {
            p.add(k == 1 ? 5 : 4);
        }

        if (p.isEmpty()) {
            return -1;
        }
        int min = Integer.MAX_VALUE;
        for (Integer integer : p) {
            min = Math.min(integer, min);
        }
        return min;
    }
}
