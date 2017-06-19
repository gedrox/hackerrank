package whiteballs;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class R {

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

    private static int check(int b, int r) {
        return (b & (1 << r)) > 0 ? 1 : 0;
    }

    private static int remove(int b, int r) {
        return (b >> (r + 1) << r) + (b & ((1 << r) - 1));
    }

    static HashMap<Integer, Double>[] bigcache = new HashMap[31];

    static double solve(int n, int k, int X) {
        for (int i = 0; i < bigcache.length; i++) {
            bigcache[i] = new HashMap<>();
        }

        return solveInner(n, k, X);
    }

    private static double solveInner(int n, int k, int X) {
        if (k == 0) {
            return 0;
        }
        if (bigcache[n].containsKey(X)) {
            return bigcache[n].get(X);
        }

//        if (n < 20) return 0;

        double res = 0;
        for (int i = 0; i < n; i++) {
            int wh1 = check(X, i);
            int wh2 = check(X, n - 1 - i);

            double first = wh1 + solveInner(n - 1, k - 1, remove(X, i));

//            if (first == k || first == Integer.bitCount(X)) {
////                System.err.println("Fast exit on " + k);
//                res += first;
//            } else {
                res += Math.max(
                        first,
                        wh2 + solveInner(n - 1, k - 1, remove(X, n - 1 - i))
                );
//            }
        }
        //30 23 1010100010110011011001100110
        res /= n;
        bigcache[n].put(X, res);

        return res;
    }

    @Test
    public void testSpeed() {
        Random r = new Random();
        Object[] slowest = new Object[5];
        slowest[0] = 0L;

        for (int i = 0; i < 100; i++) {
            int n = 30;
            int k = r.nextInt(n) + 1;
            int X = r.nextInt(1 << n);

            System.out.println(String.format("%d %d %s", n, k, Integer.toBinaryString(X)));

            long t0 = System.currentTimeMillis();
            double val = solve(n, k, X);
            long d = System.currentTimeMillis() - t0;

            System.out.println(d + "ms");

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
