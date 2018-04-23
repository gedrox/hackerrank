package w37;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * https://www.hackerrank.com/contests/w37/challenges/two-efficient-teams
 */
public class TwoEfficientTeams {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int n, m;
    static int[] k;
    static int[] f;
    static int[][] a;

    static int readInt() throws IOException {
        return (int) readLong();
    }

    private static int[] readIntArray(int n) throws IOException {
        int[] V = new int[n];
        for (int i = 0; i < n; i++) V[i] = readInt();
        return V;
    }

    private static long[] readLongArray(int n) throws IOException {
        long[] V = new long[n];
        for (int i = 0; i < n; i++) V[i] = readLong();
        return V;
    }

    static long readLong() throws IOException {
        long res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }

    static char readChar() throws IOException {
        while (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') ch = br.read();
        char c = (char) ch;
        ch = br.read();
        return c;
    }

    static void init() {
        k = new int[m];
        f = new int[m];
        a = new int[m][];
    }

    public static void main(String[] args) throws IOException {
        init();
        n = readInt();
        m = readInt();

        for (int m_i = 0; m_i < m; m_i++) {
            k[m_i] = readInt();
            f[m_i] = readInt();
            a[m_i] = readIntArray(k[m_i]);
        }

        long answer = solve();

        System.out.println(answer);
    }

    private static long solve() {

        long[][] weights = new long[n][n];
        for (int i = 0; i < m; i++) {
            for (int x = 0; x < k[i]; x++) {
                for (int y = 0; y < k[i]; y++) {
                    if (x != y) weights[a[i][x] - 1][a[i][y] - 1] += (4 - k[i]) * f[i];
                }
            }
        }
        
        long sum = Arrays.stream(f).asLongStream().sum();
        
//        if (!isConnected(weights)) {
//            return sum;
//        }

        return sum - getMinCut(weights) / 2;
    }

    private static boolean isConnected(long[][] gr) {
        // is it even fully connected?
        boolean[] visited = new boolean[n];
        LinkedList<Integer> q = new LinkedList<>();
        q.add(0);
        visited[0] = true;

        while (!q.isEmpty()) {
            int first = q.pollFirst();
            for (int i = 0; i < gr[first].length; i++) {
                if (gr[first][i] > 0 && !visited[i]) {
                    visited[i] = true;
                    q.add(i);
                }
            }
        }

        for (boolean b : visited) {
            if (!b) return false;
        }
        
        return true;
    }
    
    static long getMinCut(long[][] weights) {
        boolean[] used = new boolean[n];
        long best = -1;

        for (int ph = n - 1; ph >= 0; ph--) {
            
            long[] w = weights[0].clone();
            boolean[] added = used.clone();
            int prev, last = 0;
            
            for (int i = 0; i < ph; i++) {
                prev = last;
                last = -1;
                for (int j = 1; j < n; j++) {
                    if (!added[j] && (last == -1 || w[j] > w[last])) {
                        last = j;
                    }
                }
                
                if (i == ph-1) {
                    for (int j = 0; j < n; j++) {
                        weights[prev][j] += weights[last][j];
                    }
                    for (int j = 0; j < n; j++) {
                        weights[j][prev] = weights[prev][j];
                    }
                    used[last] = true;
                    if (best == -1 || w[last] < best) {
                        best = w[last];
                    }
                } else {
                    for (int j = 0; j < n; j++) {
                        w[j] += weights[last][j];
                    }
                    added[last] = true;
                }
            }
        }

        return best;
    }

    @Test
    public void test2() {
        n = 2;
        m = 3;
        init();
        k = new int[]{2, 2, 2};
        f = new int[]{4, 5, 10};
        a = new int[][]{
                {1, 2},
                {1, 2},
                {1, 2},
        };

        Assert.assertEquals(0, solve());
        Assert.assertEquals(0, trivialSolve());
    }

    @Test
    public void testSample() {
        n = 4;
        m = 3;
        init();
        k = new int[]{2, 2, 3};
        f = new int[]{4, 5, 10};
        a = new int[][]{
                {1, 2},
                {1, 3},
                {2, 3, 4},
        };

        Assert.assertEquals(10, solve());
        Assert.assertEquals(10, trivialSolve());
    }

    @Test
    public void testNotConnected() {
        n = 4;
        m = 3;
        init();
        k = new int[]{2, 2, 3};
        f = new int[]{4, 5, 10};
        a = new int[][]{
                {1, 2},
                {1, 3},
                {1, 2, 3},
        };

        Assert.assertEquals(19, solve());
        Assert.assertEquals(19, trivialSolve());
    }
    
    @Test
    public void testBig() {
        
        Random r = new Random(0);
        while (true) {
            n = r.nextInt(20) + 3;
            m = r.nextInt(500) + 1;
            init();
            for (int i = 0; i < m; i++) {
                k[i] = r.nextInt(2) + 2;
                f[i] = r.nextInt(1_000_000_000) + 1;
                a[i] = new int[k[i]];
                while (a[i][0] == a[i][1] || a[i][0] == a[i][k[i] - 1]) {
                    for (int j = 0; j < k[i]; j++) {
                        a[i][j] = r.nextInt(n) + 1;
                    }
                }
            }

            long answer = solve();
            System.out.println(answer);
            long trivialSolve = trivialSolve();
            Assert.assertEquals(trivialSolve, answer);
        }
    }
    
    static long trivialSolve() {
        
        assert n <= 31;
        
        // first element always in
        long best = 0;
        for (int check = 1; check < (1<<n) - 1; check += 2) {
            long sum = 0;

            for (int i = 0; i < m; i++) {
                if (allEqual(check, a[i])) {
                    sum += f[i];
                }
            }
            best = Math.max(best, sum);
        }
        
        return best;
    }

    private static boolean allEqual(int check, int[] ints) {
        boolean expect = (check & (1 << (ints[0] - 1))) == 0;
        for (int i = 1; i < ints.length; i++) {
            if (expect != ((check & (1 << (ints[i] - 1))) == 0)) {
                return false;
            }
        }
        return true;
    }

}
