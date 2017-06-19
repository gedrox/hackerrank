package satisfactory_pairs;

import java.math.BigInteger;
import java.util.ArrayList;

public class SolutionTesting {
    public static void main(String[] args) {
//        int n = 37;
//        for (n = 4; n < 100; n++) {
//            ArrayList<Pair> res = solve(n);
//            System.out.println(n + " - " + res.size());
//        }
//
//        n = 3000;
//        ArrayList<Pair> res = solve(n);
//        System.out.println(n + " - " + res.size());

        int n = 98;

        System.out.println(solve(n, true));

//        int a = 7;
//
//        int n0 = 199 - 2*a + 1;
//
//        int sum = 0;
//        int res = 0;
//        for (int i = a-1; i > 0; i--) {
//            int next = n0/i/a - sum;
//            System.out.println(next);
//            sum += next;
//            res += i * next;
//        }
//
//        System.out.println(res);

//        int n4 = n0/4/5;
//        int n3 = n0/3/5-n4;
//        int n2 = n0/2/5-n3-n4;
//        int n1 = n0/1/5-n2-n3-n4;
//
//        System.out.println(n4*4+n3*3+n2*2+n1);

//        System.out.println(n4);
//        System.out.println(n3);
//        System.out.println(n2);
//        System.out.println(n1);

//        System.out.println(solve(3000, false).size());

//        System.out.println(res);
    }

    private static ArrayList<Pair> solve(int n, boolean out) {
        ArrayList<Pair> res = new ArrayList<>();

        for (int a = 1; a <= (n - 1) / 2; a++) {
//        for (int a = n; a >= 0; a--) {

//            int n0 = n - 2*a + 1;
//
//            int sum = 0;
//            int r = 0;
//            for (int i = a-1; i > 0; i--) {
//                int next = n0/i/a - sum;
//                sum += next;
//                r += i * next;
//            }
//
//            res.add(Pair.of(r, r));
//
//            if (true) continue;

//            if (out)
                for (int i = 0; i < a; i++) {
                    System.out.print(" ");
                }

            int X = 0;
            int x = 0;
            int o = 0;
//            for (int b = 0; b <= n; b++) {
            for (int b = a + 1; b <= n - a; b++) {
                char c = canSolve(n, a, b);
                if (out) System.out.print(c);
                if (c == 'X') {
                    res.add(Pair.of(a, b));
                }
                if (c == 'X') X++;
                if (c == ':') x++;
                if (c == '.') o++;
            }
            if (out)
//                for (int i = 0; i < a; i++) {
//                    System.out.print(".");
//                }
//            if (out) System.out.printf(" (X=%d, :=%d, .=%d, X+x=%d)", X, x, o, X + x);
            if (out) System.out.println();
        }
        return res;
    }

    private static char canSolve(int n, int a, int b) {

        if (a == 0 && b == 0) return '.';

        if (a == 0) return n % b == 0 ? 'X' : '.';
        if (b == 0) return n % a == 0 ? 'X' : '.';

        int ab = gcd(a, b);
        if (ab > 1) {
            if (n % ab == 0) {
                a /= ab;
                b /= ab;
                n /= ab;
            } else {
                return '.';
            }
        }

        int b_a = b % a;
        int n_a = n % a;

        // b_a * y = n_a (mod a)
        int y_a = (BigInteger.valueOf(b_a).modInverse(BigInteger.valueOf(a)).intValue() * n_a) % a;
        if (y_a == 0) y_a = a;

        return n - y_a * b > 0 ? 'X' : ',';

//        int x = 1;
//        while (true) {
//            int rem = n - x * a;
//            if (rem <= 0) return false;
//            if (rem % b == 0) return true;
//            x++;
//        }
    }

    private static int gcd(int a, int b) {
        while (a != 0) {
            int tmp = b % a;
            b = a;
            a = tmp;
        }
        return b;
    }

    static class Pair {
        int a, b;

        static Pair of(int a, int b) {
            Pair pair = new Pair();
            pair.a = a;
            pair.b = b;
            return pair;
        }

        public String toString() {
            return "(" + a + ", " + b + ")";
        }
    }
}

/*
== 10
[(1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (2, 3), (2, 4), (2, 6), (2, 8), (3, 4), (3, 7), (4, 6)]
== 11
[(1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10), (2, 3), (2, 5), (2, 7), (2, 9), (3, 4), (3, 5), (3, 8), (4, 7), (5, 6)]
 */