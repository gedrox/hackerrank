package zeromovenim;

import org.junit.Test;

import java.util.Scanner;

public class NimAgain {
    public static void main(String[] args) {
        int[] a = new int[100001];
        int d = 1;
        for (int i = 1; i + d < a.length; i++) {
            if (a[i] == 0) {
                a[i] = i + d;
                a[i + d] = i;
                d++;
            }
        }

        Scanner in = new Scanner(System.in);
        int g = in.nextInt();
        for(int a0 = 0; a0 < g; a0++){
            int n = in.nextInt();
            int[] piles = new int[n];
            int x = 0;
            for(int piles_i=0; piles_i < n; piles_i++){
                piles[piles_i] = in.nextInt();
                x ^= piles[piles_i];
            }
            if (n == 2) {
                System.out.println(a[piles[0]] == piles[1] ? "Watson" : "Sherlock");
            } else {
                System.out.println(x == 0 ? "Watson" : "Sherlock");
            }
        }
    }

    @Test
    public void test2() {
        int MAX = 20;
        for (int p1 = 1; p1 < MAX; p1++) {
            for (int p2 = p1 + 1; p2 < MAX; p2++) {

                boolean b = canWinBrute(new int[]{p1, p2}, false);
                if (!b) {
                    System.out.printf("%4s %4s: %s (%d, %d)%n",
                            Integer.toBinaryString(p1),
                            Integer.toBinaryString(p2),
                            b ? "W" : "L", p1, p2);
                }
            }
        }

    }

    @Test
    public void test3() {

        int MAX = 10;
        for (int p1 = 1; p1 < MAX; p1++) {
            for (int p2 = p1; p2 < MAX; p2++) {
                for (int p3 = p2; p3 < MAX; p3++) {
//                    for (int p4 = p3; p4 < MAX; p4++) {

                    boolean b = canWinBrute(new int[]{p1, p2, p3}, false);
                    if (b ^ ((p1 ^ p2 ^ p3) != 0)) {
                        System.out.println(p1 + ", " + p2  + ", " + p3);
                    }
                    if (!b) {
//                            System.out.printf("%d+ %d+ %d+: %s%n", p1, p2, p3, b ? "W" : "L");
                        System.out.printf("%4s %4s %4s: %s%n",
                                Integer.toBinaryString(p1),
                                Integer.toBinaryString(p2),
                                Integer.toBinaryString(p3),
//                                    Integer.toBinaryString(p4),
                                b ? "W" : "L");
                    }
//                    }
                }
            }
        }
    }

    private static void check(int... p) {
        System.out.println(canWinBrute(p, false));
    }

    private static boolean canWinBrute(int[] p, boolean first) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < p.length; i++) {
            int count = p[i];
            if (count < min) min = count;
            for (int leave = 0; leave < count; leave++) {
                p[i] = leave;
                boolean canWin = canWinBrute(p, !first);
                p[i] = count;
                if (!canWin) {
//                    if (first) System.out.println(leave + " " + i);
                    return true;
                }
            }
        }

        for (int i = 1; i <= min; i++) {
            for (int j = 0; j < p.length; j++) {
                p[j] -= i;
            }
            boolean canWin = canWinBrute(p, !first);
            for (int j = 0; j < p.length; j++) {
                p[j] += i;
            }
            if (!canWin) {
                return true;
            }
        }

        return false;
    }

}
