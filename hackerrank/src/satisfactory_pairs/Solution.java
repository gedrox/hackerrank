package satisfactory_pairs;

import java.math.BigInteger;
import java.util.Scanner;

public class Solution {
    public static void main(String... args) {
        int n = new Scanner(System.in).nextInt();
        System.out.println(solve(n));
    }

    static int solve(int n) {
        int res = n-2 + ((n-1)/2 - (n-1)/3); // a==1

        int a1 = 2;
        long time = 0;

        for (int a = 2; a <= (n-1)/3; a++) {
            long start = System.currentTimeMillis();

//            int g2 = gcd(a, n);
//            if (g2 > 1) {
//                a /= g2;
//                n /= g2;
//            }
            if (true)

            for (int mod = 1; mod <= a; mod++) {
                int g = gcd(mod, a);
                if (n % g == 0) {
                    int a_ = a / g;
                    int mod_ = mod / g;
                    int n_ = n / g;

                    int y_a = ((n_ % a_) * BigInteger.valueOf(mod_).modInverse(BigInteger.valueOf(a_)).intValue()) % a_;
                    if (y_a == 0) y_a += a_;

                    int x = ((n_-1)/y_a - mod_) / a_;

//                    System.err.println(mod_ + " " + y_a);

                    res += x;
//                    System.err.print(x + "\t");
//                } else {
//                    System.err.print("0\t");
                }
            }

            else

            for (int y_a = 1; y_a <= a; y_a++) {
                int g = gcd(y_a, a);
                if (n % g == 0) {
                    int a_ = a / g;
                    int y_a_ = y_a / g;
                    int n_ = n / g;

                    int mod_ = ((n_ % a_) * BigInteger.valueOf(y_a_).modInverse(BigInteger.valueOf(a_)).intValue()) % a_;
                    if (mod_ == 0) mod_ += a_;

                    int x = ((n_-1)/mod_ - y_a_) / a_;

//                    System.err.println(mod_ + " " + y_a_);

                    res += x;
//                    System.err.print(x + "\t");
//                } else {
//                    System.err.print("0\t");
                }
            }

//            System.err.println("");

            time += System.currentTimeMillis() - start;

            if (a % 100 == 0) {
//                System.err.printf("%d\t%d\t%d%n", a1, a, time);
                time = 0;
                a1 = a + 1;
            }
        }
        return res;
    }

    private static boolean canSolve(int n, int a, int b) {
        int ab = gcd(a, b);
        if (ab > 1) {
            if (n % ab == 0) {
                a /= ab;
                b /= ab;
                n /= ab;
            } else {
                return false;
            }
        }

        if (a == 1) return true;

        int n_a = n % a;
        int y_a = (BigInteger.valueOf(b % a).modInverse(BigInteger.valueOf(a)).intValue() * n_a) % a;
        if (y_a == 0) y_a = a;

        return n - y_a * b > 0;
    }

    private static int gcd(int a, int b) {
        while (a != 0) {
            int tmp = b % a;
            b = a;
            a = tmp;
        }
        return b;
    }

}
