package euler.euler025;

import java.math.BigInteger;
import java.util.Scanner;

public class Solution {

    static double DIV = Math.pow(10, .2);

    static long fib(int n) {
        double x = (Math.pow((1 + Math.sqrt(5)) / 2 / DIV, n) - (Math.pow((1 - Math.sqrt(5)) / 2 / DIV, n))) / Math.sqrt(5);
        if (Double.isInfinite(x)) {
            return Long.MAX_VALUE;
        }

        long exp = (long) Math.floor(Math.log10(x));
        double rem = x / Math.pow(10, exp);
        rem *= Math.pow(DIV, n % 5);
        if (rem >= 10) {
            return exp + 2 + n/5;
        } else {
            return exp + 1 + n/5;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t = 0; t < T; t++) {
            int N = sc.nextInt();

            int left = solveFast(N);
            System.out.println(left);
        }
    }

    static int[] collectSlow() {
        BigInteger f0 = BigInteger.ONE;
        BigInteger f1 = BigInteger.ONE;
        int c = 0;
        int i = 1;
        int[] d = new int[5003];
        while (c < 5000) {
            BigInteger f2 = f0.add(f1);
            f0 = f1;
            f1 = f2;
            c = f2.toString().length();
            if (d[c] == 0) {
                d[c] = i;
            }
            i++;
        }
        return d;
    }

    static int solveFast(int n) {
        int left = 0;
        int right = 31250;

        while (left < right) {
            int mid = (left + right) / 2;
            long fibMid = fib(mid);
            if (fibMid >= n) {
                right = mid;
            } else if (fibMid < n) {
                left = mid + 1;
            }
        }
        return left;
    }
}
