package stonediv;

import java.math.BigInteger;
import java.util.Scanner;

public class Solution2 {

    static int maxDepth = 0;

    @org.junit.Test
    public void test() {
        long n = 922334673882737115L;
        long[] s = {3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47};
        for (int i = 0; i < s.length / 2; i++) {
            long t = s[i];
            s[i] = s[s.length - 1 - i];
            s[s.length - 1 - i] = t;
        }
        System.out.println(solve(n, s));
    }

    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();
        for (int i = 0; i <= 36; i++) {


            long n = BigInteger.valueOf(3).pow(i).longValue();
        System.err.println(n);
            long[] s = {
//                    81 * 81 * 81 * 27, 81 * 81 * 81 * 9, 81 * 81 * 81 * 3,
//                    81 * 81 * 27, 81 * 81 * 9, 81 * 81 * 3,
//                    81 * 27, 81 * 9, 81 * 3,
                    81, 27, 9, 3};


//        Scanner sc = new Scanner(System.in);
//        long n = sc.nextLong();
//        int m = sc.nextInt();
//        long[] s = new long[m];
//        for (int i = 0; i < m; i++) {
//            s[i] = sc.nextLong();
//        }

            boolean firstCanWin = solve(n, s);
            System.out.print(i + " - ");
            System.out.println(firstCanWin ? "First" : "Second");
        }
        System.err.println(System.currentTimeMillis() - t0);
    }

    private static boolean solve(long n, long[] s) {

        for (long s_i : s) {
            if (((s_i % 2) == 0) && ((n % s_i) == 0)) {
                return true;
            }
        }

        return winExists(n, s, 1);
    }

    static boolean winExists(long n, long[] s, int depth) {
        maxDepth = Math.max(maxDepth, depth);
        for (long move : s) {
            if (n % move == 0) {
                if (!winExists(n/move, s, depth + 1)) {
                    return true;
                }
            }
        }
//        if (depth == 1) System.err.println(maxDepth);
        return false;
    }
}
