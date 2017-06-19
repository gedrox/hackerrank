package taste_win;

import org.junit.Test;

import java.util.HashSet;
import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        System.out.println(solve(5,5));

//        if (Math.pow(2, n*m) < 1e7) {
//            System.out.println(solve(n, m));
//        } else {
//            System.out.println(0);
//        }
    }

    @Test
    public void test() {
        System.err.println("n\tm\tFLD\tFLE\tFWD\tFWE\tELD\tELE\tEWD\tEWE");
        for (int m = 1; m < 10; m++) {
            for (int n = 1; n < 10; n++) {
                if (Math.pow(2, n*m) > 1e7) {
                    break;
                }
                solve(n, m);
            }
        }
    }

    static int solve(int n, int m) {

        if (Math.pow(2, n*m) > 1e8) {
            throw new UnsupportedOperationException("Too large");
        }

        int[] piles = new int[n];
        int powM = (int) Math.pow(2, m);
        Integer[] st = new Integer[8]; // Zero/Result/Dupls
        for (int i = 0; i < 8; i++) {
            st[i] = 0;
        }

        for (int i = 0; i < (int) Math.pow(2, n*m); i++) {
            boolean b1 = false; // has zeroes
            boolean b2; // wins
            boolean b3 = false; // has dupls

            HashSet<Integer> duplChk = new HashSet<>();
            int xor = 0;

            int tmp = i;

            for (int j = 0; j < n; j++) {
                piles[j] = tmp % powM;
                tmp /= powM;

                b1 = b1 || piles[j] == 0;
                b3 = b3 || !duplChk.add(piles[j]);
                xor ^= piles[j];
            }

            b2 = xor != 0;

            st[(b1?4:0) + (b2?2:0) + (b3?1:0)]++;
        }

//        System.err.printf("%d\t%d\t", n, m);
//        System.err.printf("%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%n",
//                (Object[]) st);

        return st[2];
    }
}
