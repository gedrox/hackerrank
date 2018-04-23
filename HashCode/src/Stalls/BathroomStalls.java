package Stalls;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class BathroomStalls {
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
//        BufferedReader br = new BufferedReader(new FileReader("src/google/codejam/bathroomstalls/C-large.in"));
        BufferedReader br = new BufferedReader(new FileReader("src/google/codejam/bathroomstalls/C-large.in"));
        int T = Integer.parseInt(br.readLine());
        for (int t_i = 1; t_i <= T; t_i++) {
            String[] s = br.readLine().split(" ");
            long n = Long.parseLong((s[0]));
            long k = Long.parseLong((s[1]));

            TreeMap<Long, Long> splits = new TreeMap<>();
            splits.put(n, 1L);

            long a = -1, b = -1;
            while (k > 0) {
                Map.Entry<Long, Long> max = splits.lastEntry();

                long times = Math.min(k, max.getValue());
                addSplit(splits, max.getKey(), -times);

                k -= times;

                a = max.getKey() / 2;
                addSplit(splits, a, times);

                b = (max.getKey() - 1) / 2;
                addSplit(splits, b, times);
            }

            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(a)
                    .append(" ")
                    .append(b)
                    .append('\n');
        }
        System.out.println(sb.toString());
        Files.write(Paths.get("src/google/codejam/bathroomstalls/out.txt"), sb.toString().getBytes());
    }

    static void addSplit(TreeMap<Long, Long> splits, long newSpl, long times) {
        long newCount = splits.containsKey(newSpl) ? splits.get(newSpl) + times : times;
        if (newCount > 0) {
            splits.put(newSpl, newCount);
        } else {
            splits.remove(newSpl);
        }
    }
}
