package taste_win;

import java.math.BigInteger;
import java.util.Scanner;

public class SolutionCool {

    static int MOD = 1000000007;
    public static final BigInteger BMOD = BigInteger.valueOf(MOD);

    static int[] INV = new int[10000001];

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        System.out.println(solve(n, m));
    }

    static void getPrimes(int max) {

        INV[1] = 1;

        boolean p[] = new boolean[(max - 1) / 2];
        int[] div = new int[max + 1];
        for (int i = 0; i < p.length; i++) {
            if (!p[i]) {
                int prm = 2 * i + 3;
                INV[prm] = BigInteger.valueOf(prm).modInverse(BMOD).intValue();
                int next = prm + i;
                while (next < p.length) {
                    p[next] = true;
                    div[2*next+3] = prm;
                    next += prm;
                }
            }
        }

        for (int i = 9; i < div.length; i += 2) {
            if (div[i] != 0) {
                INV[i] = (int) ((((long) INV[i / div[i]]) * INV[div[i]]) % MOD);
            }
        }

        INV[2] = BigInteger.valueOf(2).modInverse(BMOD).intValue();
        for (int i = 4; i <= max; i += 2) {
            INV[i] = (int) ((((long) INV[i / 2]) * INV[2]) % MOD);
        }

    }

    static int solve(int n, int m) {

        getPrimes(n);

        int SMALL_M_POW = m < 31 ? (int) Math.pow(2, m) : Integer.MAX_VALUE;

        if (n >= SMALL_M_POW) {
            return 0;
        }

        int mPow = BigInteger.valueOf(2).modPow(BigInteger.valueOf(m), BMOD).intValue();

        long allPos = 1;
        for (int i = 1; i <= n; i++) {
            allPos = (allPos * (mPow - i)) % MOD;
        }
        if (allPos < 0) allPos += MOD;

        long C = 1;
        long BAD = 0;
        long BAD1 = 0, BAD2 = 0;

        if (m < 31 && n == SMALL_M_POW - 1) {
            BAD = m == 1 ? 0 : 1;
        } else {

            if (m < 31 && n > (SMALL_M_POW - 1)/2) {
                n = SMALL_M_POW - 1 - n;
            }

            for (int i = 1; i <= n; i++) {
                if (i > 2) {
                    long cikLabu = C - BAD;
                    long cikAr3 = (BAD2 * (mPow + 1 - i)) % MOD;
                    long cikSuper = cikLabu - cikAr3;
                    BAD = (cikSuper * INV[i]) % MOD;
                } else {
                    BAD = 0;
                }

                C = (((C * (mPow - i)) % MOD) * INV[i]) % MOD;

                BAD2 = BAD1;
                BAD1 = BAD;
            }
        }

        long GOOD = (C - BAD) % MOD;
        int res = (int) ((((allPos * GOOD) % MOD) * BigInteger.valueOf(C).modInverse(BMOD).intValue()) % MOD);
        if (res < 0) res += MOD;
        return res;
    }
}
