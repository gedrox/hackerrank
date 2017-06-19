package euler.euler169;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class S {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BigInteger n = sc.nextBigInteger();

        int c = n.bitLength();
        ArrayList<Integer> a = new ArrayList<>();

        int L = 0;
        for (int i = 0; i < c; i++) {
            L++;
            if (n.testBit(i)) {
                a.add(L);
                L = 0;
            }
        }

//        System.err.println(a);

        long res[] = new long[2];
        res[0] = 1;

        for (Integer i : a) {
            long[] next = new long[2];

            next[0] = res[0] + res[1];
            next[1] = res[0] * (i - 1) + res[1] * i;

//            if (next[0] < 0 || next[1] < 0) throw new RuntimeException();

            res = next;
        }

        System.out.println(res[0] + res[1]);
    }
}
