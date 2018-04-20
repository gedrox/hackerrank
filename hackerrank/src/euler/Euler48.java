package euler;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Scanner;

public class Euler48 {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        long sum = getSum(n);

        System.out.println(sum);
    }

    private static long getSum(int n) {
        long MOD = 10000000000L;
        BigInteger BMOD = BigInteger.valueOf(MOD);
        long sum = 0;

        if (n >= 100000) sum = 3031782500L;
        if (n >= 200000) sum = 7370202500L;
        if (n >= 300000) sum = 3708622500L;
        if (n >= 400000) sum = 2047042500L;
        if (n >= 500000) sum = 2385462500L;
        if (n >= 600000) sum = 4723882500L;
        if (n >= 700000) sum = 9062302500L;
        if (n >= 800000) sum = 5400722500L;
        if (n >= 900000) sum = 3739142500L;
        if (n >= 1000000) sum = 4077562500L;
        if (n >= 1100000) sum = 6415982500L;
        if (n >= 1200000) sum = 754402500L;
        if (n >= 1300000) sum = 7092822500L;
        if (n >= 1400000) sum = 5431242500L;
        if (n >= 1500000) sum = 5769662500L;
        if (n >= 1600000) sum = 8108082500L;
        if (n >= 1700000) sum = 2446502500L;
        if (n >= 1800000) sum = 8784922500L;
        if (n >= 1900000) sum = 7123342500L;
        if (n >= 2000000) sum = 7461762500L;

        int start = n - (n % 100000) + 1;

        for (int i = start; i <= n; i++) {
            BigInteger b = BigInteger.valueOf(i);
            sum += b.modPow(b, BMOD).longValue();
            sum %= MOD;
            if (i % 100000 == 0) System.err.println(String.format("else if (n >= %d) {\n" +
                    "    start = %d;\n" +
                    "    sum = %dL;\n" +
                    "}", i, i + 1, sum));
        }
        
        return sum;
    }

    @Test
    public void test() {
        long sum = getSum(2000000);
        System.out.println(sum);
        assert sum == 7461762500L;
    }
}
