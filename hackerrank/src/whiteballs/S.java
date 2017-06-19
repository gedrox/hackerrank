package whiteballs;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class S {
    public static boolean BOOLEAN = false;
    private static long[] F;
    static Random r = new Random();

    static void initF(int n, int k) {
        F = new long[Math.min(n + 1, 21)];
        if (n - k >= F.length) return;
        F[n - k] = 1;
        for (int i = n - k + 1; i < F.length; i++) {
            F[i] = i * F[i - 1];
            if (F[i] < F[i - 1]) throw new RuntimeException();
        }
    }

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

    static long[][] cache(int N, int finalLength) {
        if (finalLength > 19) return new long[0][];
        long[][] sum = new long[N][];

        sum[finalLength] = new long[1 << finalLength];

        for (int L = finalLength + 1; L < N; L++) {
            sum[L] = new long[1 << L];

            for (int b = 0; b < sum[L].length; b++) {
                for (int r = 0; r < L; r++) {
                    sum[L][b] += Math.max(
                            F[L - 1] * check(b, r) + sum[L - 1][remove(b, r)],
                            F[L - 1] * check(b, L - 1 - r) + sum[L - 1][remove(b, L - 1 - r)]);
                }
                if (sum[L][b] < 0) throw new RuntimeException(L + " !");
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

//    @Test
//    public void remBit() {
//        int x = 42;
//        System.out.println(Integer.toBinaryString(x));
//        System.out.println(Integer.toBinaryString(remove(x, 0)));
//        System.out.println(Integer.toBinaryString(remove(x, 1)));
//        System.out.println(Integer.toBinaryString(remove(x, 2)));
//        System.out.println(Integer.toBinaryString(remove(x, 3)));
//        System.out.println(Integer.toBinaryString(remove(x, 4)));
//        System.out.println(Integer.toBinaryString(remove(x, 5)));
//        System.out.println(Integer.toBinaryString(remove(x, 6)));
//        System.out.println(Integer.toBinaryString(remove(x, 7)));
//        System.out.println(Integer.toBinaryString(remove(x, 8)));
//    }
//
    @Test
    public void testCache() {
        long[][] cache = cache(20, 2);
//        System.out.println(cache[3][3]);
//        System.out.println(cache[cache.length - 1][cache[cache.length - 1].length - 1]);

        System.out.println(Arrays.toString(cache[2]));
        System.out.println(Arrays.toString(cache[3]));
        System.out.println(Arrays.toString(cache[4]));
        System.out.println(Arrays.toString(cache[5]));
        System.out.println(Arrays.toString(cache[6]));
        System.out.println(Arrays.toString(cache[7]));
//        System.out.println(1d * cache[4][10] / (F[4] / F[2]));
//        System.out.println(Arrays.deepToString(cache));
    }

    static HashMap<Integer, Double>[] bigcache = new HashMap[31];
    static double solve(int n, int k, int X) {

        for (int i = 0; i < bigcache.length; i++) {
            bigcache[i] = new HashMap<>();
        }

        initF(n, k);
        int finalLength = n - k;
        long[][] cache = cache(Math.min(20, n + 1), finalLength);

        return solveInner(n, k, X, cache);
    }

    private static double solveInner(int n, int k, int X, long[][] cache) {
        if (k == 0) {
            return 0;
        }
        if (n < cache.length) {
            return 1d * cache[n][X] / F[n];
        } else {

            if (bigcache[n].containsKey(X)) {
                return bigcache[n].get(X);
            }

            double res = 0;
            for (int i = 0; i < n; i++) {
                int wh1 = check(X, i);
                int wh2 = check(X, n - 1 - i);

//                if (BOOLEAN && wh1 != wh2) {
//                    if (wh1 == 1) {
//                        res += wh1 + solveInner(n - 1, k - 1, remove(X, i), cache);
//                    }  else {
//                        res += wh2 + solveInner(n - 1, k - 1, remove(X, n - 1 - i), cache);
//                    }
//                } else {

//                    int mirrL = mirrBlack(remove(X, i), n - 1);
//                    int mirrR = mirrBlack(remove(X, n - 1 - i), n - 1);
//
//                    if (mirrL < mirrR) {
//                        res += wh1 + solveInner(n - 1, k - 1, remove(X, i), cache);
//                    } else {
//                        res += wh2 + solveInner(n - 1, k - 1, remove(X, n - 1 - i), cache);
//                    }

                    res += Math.max(
                            wh1 + solveInner(n - 1, k - 1, remove(X, i), cache),
                            wh2 + solveInner(n - 1, k - 1, remove(X, n - 1 - i), cache)
                    );
//                }
            }

            res /= n;
            bigcache[n].put(X, res);

            return res;
        }
    }

    private static double mc(long[][] cache, int n, int k, int X) {

        double res = 0;

        for (int i = 0; i < k; i++) {

            if (n - i < cache.length) {
                return res + 1d * cache[n - i][X] / F[n - i];
            }

            int guess = r.nextInt(n - i);
            int left = check(X, guess);
            int right = check(X, n - 1 - i - guess);

            if (left == 1 && right == 0) {
                X = remove(X, guess);
                res++;
            } else if (right == 1 && left == 0) {
                X = remove(X, n - 1 - i - guess);
                res++;
            } else {
                if (left == 1 && right == 1) {
                    res++;
                }

                int mirrL = mirrBlack(remove(X, guess), n - i - 1);
                int mirrR = mirrBlack(remove(X, n - 1 - i - guess), n - i - 1);

                if (mirrL < mirrR) {
                    X = remove(X, guess);
                } else {
                    X = remove(X, n - 1 - i - guess);
                }
            }
        }

        return res;
    }

    private static int mirrBlack(int X, int n) {
        int c = 0;
        for (int i = 0; i < n; i++) {
            if (check(X, i) == 0 && check(X, n - 1 - i) == 0) {
                c++;
            }
        }
        return c;
    }

    @Test
    public void testTime() {
        System.out.println(solve(19, 19, ballsToInt("WWWWWWWWWWWWWWWWWWB")));
    }

    @Test
    public void testSpeed() {
        Object[] slowest = new Object[5];
        slowest[0] = 0L;

        for (int i = 0; i < 100; i++) {
//            int n = r.nextInt(30) + 1;
            int n = 30;
            int k = r.nextInt(n) + 1;
            int X = r.nextInt(1 << n);

            double val = -1;

            long t0 = System.currentTimeMillis();
            for (int i1 = 0; i1 < 3; i1++) {
//                BOOLEAN = i1 == 0 ? true : false;
                double val2 = solve(n, k, X);
                if (val != -1 && val != val2) {
                    throw new RuntimeException(String.format("%f != %f (%d %d %s)", val, val2, n, k, Integer.toBinaryString(X)));
                }
                val = val2;
            }
            long d = (System.currentTimeMillis() - t0)/3;

            if ((Long) slowest[0] < d) {
                slowest = new Object[] {d, n, k, X, val};
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

//30 23 110100110100111100011111100110
//16.679424
//16.679444 -- false
//16.67944499969449
//16.67944499969449