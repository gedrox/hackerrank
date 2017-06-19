package whiteballs;

import org.junit.Test;
import w31.SubClass;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class S2 {

    //30 20 BWBWBWBWBWBWBWBWBWBWBWBWBWBWBW
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();

        String balls = in.next();

        int X = ballsToInt(balls);

        long t0 = System.currentTimeMillis();
        System.out.println(solve(n, k, X));
        System.err.println(System.currentTimeMillis() - t0);
    }

    private static int ballsToInt(String balls) {
        balls = balls.replace('W', '1').replace('B', '0');
        return Integer.parseInt(balls, 2);
    }

    static double[][] cache(int N, int finalLength) {
        if (finalLength >= N) return new double[0][];
        double[][] sum = new double[N][];

        sum[finalLength] = new double[1 << finalLength];

        for (int L = finalLength + 1; L < N; L++) {
            sum[L] = new double[1 << L];

            for (int b = 0; b < 1 << L; b++) {
                if (sum[L][b] == 0) {
                    for (int r = 0; r < L; r++) {
                        sum[L][b] += Math.max(
                                check(b, r) + sum[L - 1][remove(b, r)],
                                check(b, L - 1 - r) + sum[L - 1][remove(b, L - 1 - r)]);
                    }
                    sum[L][b] /= L;
                    sum[L][reverse(b, L)] = sum[L][b];
                }
            }
        }

        return sum;
    }

    private static int check(int b, int r) {
        return (b & (1 << r)) > 0 ? 1 : 0;
    }

    private static int remove(int b, int r) {
        return (b >> (r + 1) << r) + (b & ((1 << r) - 1));
    }

    static HashMap<Integer, Double>[] bigcache = new HashMap[31];

    static double solve(int n, int k, int X) {

        for (int i = 0; i < bigcache.length; i++) {
            bigcache[i] = new HashMap<>(1 << 20);
        }

        int finalLength = n - k;
        long t0 = System.currentTimeMillis();
        double[][] cache = cache(Math.min(23, n + 1), finalLength);
        System.err.println(System.currentTimeMillis() - t0);

        return solveInner(n, k, X, cache);
    }

    private static double solveInner(int n, int k, int X, double[][] cache) {
        if (k == 0) {
            return 0;
        }
        if (n < cache.length) {
            return cache[n][X];
        } else {

            if (bigcache[n].containsKey(X)) {
                return bigcache[n].get(X);
            }

            double res = 0;
            for (int i = 0; i < n; i++) {
                res += Math.max(
                        check(X, i) + solveInner(n - 1, k - 1, remove(X, i), cache),
                        check(X, n - 1 - i) + solveInner(n - 1, k - 1, remove(X, n - 1 - i), cache)
                );
            }

            res /= n;
            bigcache[n].put(X, res);
            bigcache[n].put(reverse(X, n), res);

            return res;
        }
    }

    private static int reverse(int x, int n) {
        int rev = 0;
        for (int i = 0; i < n; i++) {
            if (check(x, i) == 1) {
                rev += 1 << (n - 1 - i);
            }
        }
        return rev;
    }

    @Test
    public void testSpeed() {
        Random r = new Random();
        Object[] slowest = new Object[5];
        slowest[0] = 0L;

        for (int i = 0; i < 100; i++) {
//            int n = r.nextInt(30) + 1;
            int n = 30;
            int k = r.nextInt(n) + 1;
            int X = r.nextInt(1 << n);

            System.out.print(String.format("%d\t%d\t'%s", n, k, Integer.toBinaryString(X)));

            long t0 = System.currentTimeMillis();
            double val = solve(n, k, X);
            long d = System.currentTimeMillis() - t0;

            System.out.print("\t" + d);
            t0 = System.currentTimeMillis();
            double val2 = R.solve(n, k, X);
            if (Math.abs(val - val2) > 1e-7) {
                System.err.println(val);
                System.err.println(val2);
                throw new RuntimeException();
            }
            d = System.currentTimeMillis() - t0;
            System.out.println("\t" + d);

            if ((Long) slowest[0] < d) {
                slowest = new Object[]{d, n, k, X, val};
            }

        }

        System.out.println(Arrays.toString(slowest));
        // [757, 30, 17, 212697905, 10.294783100370195]
        //[766, 30, 22, 762110807, 16.081930331715707]
        //[1185, 30, 21, 884034283, 13.827672337726858]
        //                             827672451815959
        //WWBWBBWBWWBBBWBWBBWBWBWWWBWBWW
    }

}
