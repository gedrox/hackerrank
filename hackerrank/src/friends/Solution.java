package friends;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(bi.readLine());
        for(int a0 = 0; a0 < t; a0++){

            String[] line = bi.readLine().split(" ");

            int n = Integer.parseInt(line[0]);

            Friends f = new Friends(n);
            long total = 0;
            long res = 0;

            int m = Integer.parseInt(line[1]);
            for(int a1 = 0; a1 < m; a1++){
                line = bi.readLine().split(" ");
                int x = Integer.parseInt(line[0]) - 1;
                int y = Integer.parseInt(line[1]) - 1;
                total += f.join(x, y);
                res += total;

                if (res < 0) throw new RuntimeException();
            }

            System.err.println(2 * res);

            Integer[] groupsFromLargest = new Integer[n];
            for (int i = 0; i < n; i++) {
                groupsFromLargest[i] = i;
            }
            Arrays.sort(groupsFromLargest, (i, j) -> f.count[j] - f.count[i]);

            res = 0;

            int rem = m;
            for (int g : groupsFromLargest) {
                if (f.count[g] <= 0) break;
                int size = f.count[g] + 1;
                rem -= size - 1;

                for (long i = 1; i < size; i++) {
                    res += i * (i + 1);
                }

                res += ((long) size) * (size - 1) * rem;
            }

            System.out.println(res);
        }
    }

    static class Friends {
        int[] group;
        int[] count;
        int[] next;

        Friends(int n) {
            group = new int[n];
            count = new int[n];
            next = new int[n];
            for (int i = 0; i < n; i++) {
                group[i] = i;
                next[i] = i;
            }
        }

        long join(int i, int j) {
            int g1 = group[i];
            int g2 = group[j];
            if (g1 == g2) return 0;

            if (count[g1] < count[g2]) {
                int tmp = g1;
                g1 = g2;
                g2 = tmp;
            }

            int nxt = g2;
            group[g2] = g1;
            while ((nxt = next[nxt]) != g2) {
                group[nxt] = g1;
            }

            int tmp2 = next[i];
            next[i] = next[j];
            next[j] = tmp2;

            long res = ((long) count[g1] + 1) * (count[g2] + 1);

            count[g1] += count[g2] + 1;
            count[g2] = -1;

            return res;
        }
    }

    @Test
    public void random() throws IOException {

        StringBuilder sb = new StringBuilder();

        int t = 1;
        sb.append(t).append('\n');
        int n = 100000;
        int m = 200000;

        Random r = new Random();

        for (int i = 0; i < t; i++) {
            sb.append(String.format("%d %d%n", n, m));

            for (int j = 0; j < m; j++) {
                sb.append(String.format("%d %d%n", r.nextInt(n) + 1, r.nextInt(n) + 1));
            }
        }

        Files.write(Paths.get("src/friends/in.txt"), sb.toString().getBytes());
    }
}
