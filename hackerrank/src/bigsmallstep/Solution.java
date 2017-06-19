package bigsmallstep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {
    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(bi.readLine());
        long t0 = System.currentTimeMillis();
        for (int i = 0; i < q; i++) {
            String[] split = bi.readLine().split(" ");
            int a = Integer.parseInt(split[0]);
            int b = Integer.parseInt(split[1]);
            int d = Integer.parseInt(split[2]);
            System.out.println(solve(a, b, d));
        }
        System.err.println(System.currentTimeMillis() - t0);
    }

    private static int solve(int a, int b, int d) {
//        if (d == 0) return 0;
//        if (a == d || b == d) return 1;
//        int c = Math.max(a, b);
//        if (d < c) return 2;
//        if (d % c == 0) return d / c;
//        return d / c + 1;

        if (a == b) {
            if (d % a == 0) {
                return d / a;
            } else {
                return Math.max(d / a, 1) + 1;
            }
        }
        if (a < b) {
            return solve(b, a, d);
        }

        if (d % a == 0) {
            return d / a;
        }
        if (d < a && d % b == 0) {
            return Math.min(2, d / b);
        }

        return Math.max(1, d / a) + 1;
    }
}
