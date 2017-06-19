package stonediv;

import java.util.Arrays;
import java.util.Scanner;

public class Solution3 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        int m = sc.nextInt();
        long[] s = new long[m];
        for (int i = 0; i < m; i++) {
            s[i] = sc.nextLong();
        }

        boolean firstCanWin = solve(n, s);

        System.out.println(firstCanWin ? "First" : "Second");
    }

    private static boolean solve(long n, long[] s) {

        for (long s_i : s) {
            if (((s_i % 2) == 0) && ((n % s_i) == 0)) {
                return true;
            }
        }

        Arrays.sort(s);
        long[] reverse = new long[s.length];
        for (int i = 0; i < s.length; i++) {
            reverse[s.length - 1 - i] = s[i];
        }

        return winExists(n, reverse, 1);
    }

    static boolean winExists(long n, long[] s, int depth) {
        for (long move : s) {
            if (n % move == 0) {
                if (!winExists(n/move, s, depth + 1)) {
                    return true;
                }
            }
        }
        return false;
    }
}