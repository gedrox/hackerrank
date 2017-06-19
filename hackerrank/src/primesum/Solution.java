package primesum;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Solution {
    private static final int MOD = 1000000007;
    static int[] P = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43};

    public static void main(String[] args) {

        ArrayList<int[]> nice = new ArrayList<>();

        long n_small[] = new long[400000];

        n_small[0] = 9;
        n_small[1] = 90;
        n_small[2] = 303;
        n_small[3] = 280;
        n_small[4] = 218;

        // start index of
        int START = 0;

        for (int i = 1; i <= 99999; i++) {
            int[] d = digits(i, 5);
            if (!check(d, 0, 3)) continue;
            if (!check(d, 0, 4)) continue;
            if (!check(d, 0, 5)) continue;
            if (!check(d, 1, 3)) continue;
            if (!check(d, 1, 4)) continue;
            if (!check(d, 2, 3)) continue;
            nice.add(d);

            if (i >= 10000 && START == 0) {
                START = nice.size() - 1;
            }
        }

        int NS = nice.size();

        ArrayList<Integer>[] next = new ArrayList[NS];
        for (int i = 0; i < NS; i++) {
            next[i] = new ArrayList<>();
            next:
            for (int j = 0; j < NS; j++) {
                for (int k = 0; k < 4; k++) {
                    if (nice.get(i)[k + 1] != nice.get(j)[k]) continue next;
                }
                next[i].add(j);
            }
        }

        // initialize
        long res[] = new long[NS];
        for (int i = START; i < NS; i++) {
            res[i] = 1;
        }

        for (int times = 5; times < 400000; times++) {
            long[] resNext = new long[NS];
            long sum = 0;
            for (int i = 0; i < NS; i++) {
                if (res[i] > 0) {
                    for (Integer nxt : next[i]) {
                        resNext[nxt] = (resNext[nxt] + res[i]) % MOD;
                    }
                    sum = (sum + next[i].size() * res[i]) % MOD;
                }
            }
            res = resNext;
            n_small[times] = sum;
        }

        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for (int a0 = 0; a0 < q; a0++) {
            int n = in.nextInt();

            System.out.println(n_small[n - 1]);
        }
    }

    private static boolean check(int[] d, int s, int l) {
        int sum = 0;
        for (int i = s; i < s + l; i++) {
            sum += d[i];
        }
        return Arrays.binarySearch(P, sum) >= 0;
    }

    private static int[] digits(int i, int SIZE) {
        int[] d = new int[SIZE];
        for (int i1 = 0; i1 < SIZE; i1++) {
            d[SIZE - 1 - i1] = i % 10;
            i /= 10;
        }
        return d;
    }

    @Test
    public void test() {

        int c;

        c = 0;
        for (int i = 1; i < 10; i++) {
            int[] d = digits(i, 3);
            if (check(d, 0, 3)) c++;
        }
        System.out.println(c);

        c = 0;
        for (int i = 10; i < 100; i++) {
            int[] d = digits(i, 3);
            if (check(d, 0, 3)) c++;
        }
        System.out.println(c);

        c = 0;
        for (int i = 100; i < 1000; i++) {
            int[] d = digits(i, 3);
            if (check(d, 0, 3)) c++;
        }
        System.out.println(c);

        c = 0;
        for (int i = 1000; i < 10000; i++) {
            int[] d = digits(i, 4);
            if (check(d, 0, 3)
                    && check(d, 0, 4)
                    && check(d, 1, 3)) c++;
        }
        System.out.println(c);

        c = 0;
        for (int i = 10000; i < 100000; i++) {
            int[] d = digits(i, 5);
            if (check(d, 0, 3)
                    && check(d, 0, 4)
                    && check(d, 0, 5)
                    && check(d, 1, 3)
                    && check(d, 1, 4)
                    && check(d, 2, 3)) c++;
        }
        System.out.println(c);

        c = 0;
        for (int i = 100000; i < 1000000; i++) {
            int[] d = digits(i, 6);
            if (check(d, 0, 3)
                    && check(d, 0, 4)
                    && check(d, 0, 5)
                    && check(d, 1, 3)
                    && check(d, 1, 4)
                    && check(d, 1, 5)
                    && check(d, 2, 3)
                    && check(d, 2, 4)
                    && check(d, 3, 3)) c++;
        }
        System.out.println(c);

        c = 0;
        for (int i = 1000000; i < 10000000; i++) {
            int[] d = digits(i, 7);
            if (check(d, 0, 3)
                && check(d, 0, 4)
                && check(d, 0, 5)
                && check(d, 1, 3)
                && check(d, 1, 4)
                && check(d, 1, 5)
                && check(d, 2, 3)
                && check(d, 2, 4)
                && check(d, 2, 5)
                && check(d, 3, 3)
                && check(d, 3, 4)
                && check(d, 4, 3)) c++;
        }
        System.out.println(c);
    }
}
