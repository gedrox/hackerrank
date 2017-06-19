package euler.euler162;

import java.math.BigInteger;
import java.util.Scanner;

public class Solution {

    static BigInteger B13 = BigInteger.valueOf(13);
    static BigInteger B14 = BigInteger.valueOf(14);
    static BigInteger B15 = BigInteger.valueOf(15);
    static BigInteger B16 = BigInteger.valueOf(16);

    static BigInteger MOD = BigInteger.valueOf(1000000007);

    public static void main(String[] args) {
        long fin = 0;
        int N = new Scanner(System.in).nextInt();
        for (int n = 3; n <= N; n++) {
            BigInteger BN = BigInteger.valueOf(n);
            BigInteger BN1 = BigInteger.valueOf(n - 1);

            BigInteger sum = B15.multiply(B16.modPow(BN1, MOD));

            sum = sum.subtract(B15.modPow(BN, MOD));
            sum = sum.subtract(B14.multiply(B15.modPow(BN1, MOD)));
            sum = sum.subtract(B14.multiply(B15.modPow(BN1, MOD)));

            sum = sum.add(B14.modPow(BN, MOD));
            sum = sum.add(B14.modPow(BN, MOD));
            sum = sum.add(B13.multiply(B14.modPow(BN1, MOD)));

            sum = sum.subtract(B13.modPow(BN, MOD));

            sum = sum.mod(MOD);

            int res = sum.intValue();
            fin = (fin + res) %  MOD.intValue();
        }

        if (fin < 0) {
            fin += MOD.intValue();
        }
        System.out.println(fin);
    }
}
