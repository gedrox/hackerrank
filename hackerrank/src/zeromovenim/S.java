package zeromovenim;

import org.junit.Test;

public class S {
    public static void main(String[] args) {
//        check(1, 2);
//        check(2, 2);
//        check(10, 10);
//        check(1, 2, 3);
//        check(1, 2, 4);
//        check(1, 2, 7);
//        check(1, 2, 8);
//        check(1, 2, 3, 4);
//        check(1, 2, 4, 7);
//        check(1, 2, 4, 8);

//        check(1, 8, 10);
//        check(1, 9, 11);
//        check(1, 10, 12);
//        check(1, 11, 13);
        check(7,7,7);
    }

    @Test
    public void test2() {
        System.out.println((2*1+1) ^ (2*2+1) ^ (2*4+1));
    }

    @Test
    public void test3() {

        int MAX = 10;
        for (int p1 = 1; p1 < MAX; p1++) {
            for (int p2 = p1; p2 < MAX; p2++) {
                for (int p3 = p2; p3 < MAX; p3++)
                {
//                    for (int p4 = p3; p4 < MAX; p4++) {

                        boolean b = canWinBrute(new int[]{p1, p2, p3}, null, false);
                        if (!b) {
//                            System.out.printf("%d+ %d+ %d+: %s%n", p1, p2, p3, b ? "W" : "L");
                            System.out.printf("%4s+ %4s+ %4s+: %s%n",
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
//        int[] p = {1, 2, 3};
//        boolean[] used = {true, false, true};
//        System.out.println(canWinBrute(p, used, false));
    }

    private static void check(int... p) {
        System.out.println(canWinBrute(p, null, false));
//        System.out.println(canWinBetter(p, null));
    }

    private static boolean canWinBrute(int[] p, boolean[] used, boolean first) {
        if (used == null) used = new boolean[p.length];

        for (int i = 0; i < used.length; i++) {
            if (!used[i] && p[i] > 0) {
                used[i] = true;
                boolean canWin = canWinBrute(p, used, !first);
                used[i] = false;
                if (!canWin) {
//                    if (first) System.out.println("0 " + i);
                    return true;
                }
            }
        }

        for (int i = 0; i < p.length; i++) {
            int count = p[i];
            for (int leave = 0; leave < count; leave++) {
                p[i] = leave;
                boolean canWin = canWinBrute(p, used, !first);
                p[i] = count;
                if (!canWin) {
//                    if (first) System.out.println(leave + " " + i);
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean canWinBetter(int[] p, boolean[] used) {
        if (used == null) used = new boolean[p.length];

        for (int i = 0; i < used.length; i++) {
            if (!used[i] && p[i] > 0) {
                used[i] = true;
                boolean canWin = canWinBetter(p, used);
                used[i] = false;
                if (!canWin) {
                    return true;
                }
            }
        }

        int xor = 0;
        for (int i : p) {
            xor ^= i;
        }

        if (xor != 0) {
            for (int i = 0; i < p.length; i++) {
                int count = p[i];
                int leave = count ^ xor;
                if (leave < count) {
                    p[i] = leave;
                    boolean canWin = canWinBetter(p, used);
                    p[i] = count;
                    if (!canWin) {
                        return true;
                    }
                }
            }
        }

        for (int i = 0; i < p.length; i++) {
            int count = p[i];
            if (count > 0) {
                p[i] = 0;
                boolean canWin = canWinBetter(p, used);
                p[i] = count;
                if (!canWin) {
                    return true;
                }
            }
        }

        return false;
    }
}
