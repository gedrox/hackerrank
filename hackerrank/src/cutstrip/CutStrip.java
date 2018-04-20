package cutstrip;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Random;

public class CutStrip {

    int[][] a;
    int n;
    int m;
    int k;
    int[] colSum;
    int[] bonus;

    int sr, er;
    BufferedReader bi;
    
    HashMap<String, Long> total = new HashMap<>();
    HashMap<String, Integer> count = new HashMap<>();
    Long startTime;
    String currTicToc;
    
    final boolean DEBUG = false;

    int[] bestBonusesStarting;
    private int maxnm;
    private int maxnm2;

    int cutAStrip() {

        int best = Integer.MIN_VALUE;

        tic("bonusPrepare");
        maxnm = Math.max(n, m);
        maxnm2 = maxnm * maxnm;
        if (bestBonusesStarting == null) {
            bestBonusesStarting = new int[maxnm2 * maxnm];
        }
        for (int j = 0; j < m; j++) {
            for (int i = n - 1; i >= 0; i--) {
                int sumSoFar = 0;
                int _i = maxnm2*i+maxnm*j+i;
                for (int t = i; t >= 0; t--) {
                    if (i - t + 1 <= k) {
                        sumSoFar += -a[t][j];
                    }
                    bestBonusesStarting[_i] = Math.max(t == i ? 0 : bestBonusesStarting[_i+1], sumSoFar);
                    _i--;
                }
            }
        }
        toc();

        for (sr = 0; sr < n; sr++) {
            colSum = new int[m];
            bonus = new int[m];

            for (er = sr; er < n; er++) {
                int res = cutStrip2();
                if (res > best) {
                    best = res;
                }
            }
        }

        return best;
    }
    
    private int cutStrip2() {
        // rows go from sr to er, er is the new added
        tic("colSum");
        for (int i = 0; i < m; i++) {
            colSum[i] += a[er][i];
        }
        toc();

        tic("bonus");
        int _i = maxnm2*er+sr;
        for (int i = 0; i < m; i++) {
            if (bestBonusesStarting[_i] > bonus[i]) {
                bonus[i] = bestBonusesStarting[_i];
            }
            _i += maxnm;
        }
        toc();

        int best = 0;

        tic("max");
        int[] f = new int[m];
        int[] minSoFar = new int[m];
        int lastF = 0;
        int _minSoFar = 0;
        int _maxStarting = Integer.MIN_VALUE;

        for (int i = 0; i < m; i++) {
            lastF = f[i] = lastF + colSum[i];
            _minSoFar = minSoFar[i] = Math.min(_minSoFar, f[i]);
        }
        for (int i = m - 1; i >= 0; i--) {
            _maxStarting = Math.max(_maxStarting, f[i]);
            best = Math.max(best, _maxStarting - (i == 0 ? 0 : minSoFar[i - 1]) + bonus[i]);
        }
            
        toc();

        return best;
    }

    private void tic(String ticName) {
        if (DEBUG) {
            assert currTicToc == null;
            currTicToc = ticName;
            if (!total.containsKey(ticName)) {
                total.put(ticName, 0L);
                count.put(ticName, 0);
            }
            startTime = System.currentTimeMillis();
        }
    }

    private void toc() {
        if (DEBUG) {
            long end = System.currentTimeMillis();
            total.compute(currTicToc, (_k, v) -> v + (end - startTime));
            count.compute(currTicToc, (_k, v) -> v + 1);
            currTicToc = null;
        }
    }

    public static void main(String[] args) throws IOException {
        new CutStrip().run();
    }
    
    public void run() throws IOException {
        bi = new BufferedReader(new InputStreamReader(System.in));
        int N = readVal();
        int M = readVal();
        
        int[] stats = {0, 0, 0};

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int SUM = 0;
        
        k = readVal();
        int[][] A = new int[N][M];
        for (int A_i = 0; A_i < N; A_i++) {
            for (int A_j = 0; A_j < M; A_j++) {
                int v = readVal();
                A[A_i][A_j] = v;
                
                if (v > 0) stats[2]++;
                else if (v == 0) stats[1]++;
                else stats[0]++;
                
                min = Math.min(min, v);
                max = Math.max(max, v);
                
                SUM += v;
            }
        }
        
        if (stats[0] <= 1) {
            System.out.println(SUM - min);
            return;
        } else if (stats[2] == 1) {
            System.out.println(max);
            return;
        } else if (stats[2] == 0) {
            System.out.println(0);
            return;
        }

        a = A;
        n = N;
        m = M;
        int resultA = cutAStrip();

        // transpose
        transpose();
        int resultB = cutAStrip();

        System.out.println(Math.max(0, Math.max(resultA, resultB)));
    }

    int ch = -1;
    
    private int readVal() throws IOException {
        int sign = 1;
        int value = 0;
        while ((ch < '0' || ch > '9') && ch != '-') ch = bi.read();
        if (ch == '-') {
            sign = -1;
            ch = bi.read();
        }
        while (ch >= '0' && ch <= '9') {
            value *= 10;
            value += ch - '0';
            ch = bi.read();
        }
        value *= sign;
        return value;
    }

    private void transpose() {
        int[][] b = new int[a[0].length][a.length];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                b[j][i] = a[i][j];
            }
        }

        a = b;
        int t = n;
        n = m;
        m = t;
    }

    @Test
    public void test() {
        n = 250;
        m = n;
        k = 5;
        a = new int[n][m];
        Random r = new Random(0);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                a[i][j] = r.nextInt(10001) - 5000;
            }
        }

        int x = cutAStrip();
        System.out.println(x);
        transpose();
        int y = cutAStrip();
        System.out.println(y);

        System.out.println(total);
        System.out.println(count);
        
        assert x == 953047;
        assert y == 952892;
    }

    @Test
    public void test2() {
        n = 380;
        m = 380;
        k = 380;
        a = new int[n][m];
        Random r = new Random(0);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                a[i][j] = r.nextInt(10001) - 5000;
            }
        }

        int x = cutAStrip();
        System.out.println(x);
        transpose();
        int y = cutAStrip();
        System.out.println(y);

        System.out.println(total);
        System.out.println(count);
    }

    @Test
    public void sample0() {
        n = 2;
        m = 4;
        k = 2;
        a = new int[][]{
                {1, -3, 4, -5},
                {2, 1, -7, -2}
        };

        System.out.println(cutAStrip());
        transpose();
        System.out.println(cutAStrip());
    }

    @Test
    public void sample1() {
        n = 3;
        m = 4;
        k = 3;
        a = new int[][]{
                {-10, 1, 1, 1},
                {-10, 1, -1, 1},
                {-10, 1, 1, 1}
        };

        System.out.println(cutAStrip());
        transpose();
        System.out.println(cutAStrip());
    }
}
