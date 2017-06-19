package gcj3.d;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class SolutionD {
    public static final String TEST_NAME = "sample2";
    public static final int[][] DIRECTIONS = new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

    public static void main(String[] args) throws IOException {
        String folder = "src/gcj3/d";

        BufferedReader br = new BufferedReader(new FileReader(folder + "/" + TEST_NAME + ".in"));

        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());

        nextCase:
        for (int t_i = 1; t_i <= T; t_i++) {

            String[] split = br.readLine().split(" ");
            int R = Integer.parseInt(split[0]);
            int C = Integer.parseInt(split[1]);
            int N = Integer.parseInt(split[2]);
            int D = Integer.parseInt(split[3]);

            long[][] max = new long[R][C];
            long[][] min = new long[R][C];
            LinkedList<long[]> q = new LinkedList<>();
            for (int i = 0; i < N; i++) {
                split = br.readLine().split(" ");
                int r = Integer.parseInt(split[0]) - 1;
                int c = Integer.parseInt(split[1]) - 1;
                int b = Integer.parseInt(split[2]);

                max[r][c] = b;
                min[r][c] = b;
                q.add(new long[]{r, c, b, b});
            }

            while (!q.isEmpty()) {
                long[] next = q.pollFirst();
                int r = (int) next[0];
                int c = (int) next[1];
                long prevMax = next[2];
                long prevMin = next[3];

//                if (max[r][c] != prevMax || min[r][c] != prevMin) {
//                    continue;
//                }

                for (int[] dir : DIRECTIONS) {
                    int r2 = r + dir[0];
                    if (r2 < 0) continue;
                    int c2 = c + dir[1];
                    if (c2 < 0) continue;
                    if (r2 >= R) continue;
                    if (c2 >= C) continue;

                    boolean ch = false;
                    if (max[r2][c2] == 0 || max[r2][c2] > prevMax + D) {
                        max[r2][c2] = prevMax + D;
                        ch = true;
                    }
                    if (min[r2][c2] == 0 || min[r2][c2] < prevMin - D) {
                        min[r2][c2] = prevMin - D;
                        ch = true;
                    }
                    if (ch) {
                        if (max[r2][c2] < min[r2][c2]) {
                            sb.append("Case #").append(t_i).append(": IMPOSSIBLE").append("\n");
                            continue nextCase;
                        }
                        
                        q.add(new long[]{r2, c2, max[r2][c2], min[r2][c2]});
                    }
                }
            }
            
            long sum = 0;
            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    sum += max[r][c] % 1000000007;
                    sum %= 1000000007;
                }
            }
            
            
            sb.append("Case #").append(t_i).append(": ").append(sum).append("\n");
        }

        Files.write(Paths.get(folder + "/" + TEST_NAME + ".out"), sb.toString().getBytes());
        System.out.println(sb.toString());
    }
}
