package hc2017r1.pie;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class TestPieProgress {
    @Test
    public void testSimple() {
        int[][] C = new int[300][];
        for (int i = 0; i < C.length; i++) {
            C[i] = new int[300];
            for (int j = 0; j < C[i].length; j++) {
                C[i][j] = 1000000;
            }
        }
        System.out.println(PieProgress.solve(C, C.length, C[0].length));

        final int[][] c = new int[][]{{1, 1}, {100, 100}, {10000, 10000}};
        System.out.println(PieProgress.solve(c, c.length, c[0].length));
    }

    @Test
    public void generateRandom() throws IOException {
        Random r = new Random();
        int T = 100;
        StringBuilder sb = new StringBuilder();
        sb.append(T).append('\n');
        for (int i = 0; i < T; i++) {
            int n = r.nextInt(300) + 1;
            int m = r.nextInt(300) + 1;
            sb.append(n).append(" ").append(m).append('\n');
            for (int i1 = 0; i1 < n; i1++) {
                for (int i2 = 0; i2 < m; i2++) {
                    sb.append(r.nextInt(1000000) + 1).append(" ");
                }
                sb.append('\n');
            }
        }
        Files.write(Paths.get("src/hc2017r1/pie-rand.in"), sb.toString().getBytes());
    }
}
