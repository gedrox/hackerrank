package surveillance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

    static int MOD = 1000000007;

    static int[] pow3 = new int[700];
    static {
        pow3[0] = 1;
        for (int i = 1; i < 700; i++) {
            pow3[i] = (int) (((long) pow3[i-1] * 3) % MOD);
        }
    }

    static int[] pow2 = new int[700];
    static {
        pow2[0] = 1;
        for (int i = 1; i < 700; i++) {
            pow2[i] = (pow2[i-1] * 2) % MOD;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bi.readLine());
        StringBuilder sb = new StringBuilder();
        for (int t_i = 0; t_i < T; t_i++) {
            String[] split = bi.readLine().split(" ");
            int m = Integer.parseInt(split[0]);
            int n = Integer.parseInt(split[1]);
            sb.append(solve(m, n)).append("\n");
        }
        System.out.print(sb.toString());
    }

//    @Test
//    public void test() {
//        System.out.println(solve(673, 169));
//        assertEquals(solve(673, 169), 482139266);
//    }

    static int solve(int m, int n) {

        long sum = 0;
        sum += allPossibleSplit1(m);
        sum += allPossibleSplit2and2(m);
        sum += allPossibleExact2(m, n);
        sum += allPossibleNotMoreThan2(m, n);
        sum += allPossibleOther(n);

        while (sum < 0) {
            sum += (- sum / MOD + 1) * MOD;
        }

        return (int) (sum % MOD);
    }

    static int allPossible3byN(int n) {
        int A2 = powMod(3, (n + 2) / 3);
        int B2 = powMod(3, (n + 1) / 3);
        int C2 = powMod(3, n / 3);

        int A1 = mult(powMod(9, n / 3), powMod(3, n % 3));
        int B1 = mult(powMod(9, n / 3), n % 3 == 0 ? 1 : 3);
        int C1 = mult(powMod(9, n / 3), n % 3 == 2 ? 3 : 1);

        return (int) (((long) A2 + B2 + C2 + A1 + B1 + C1) % MOD);
    }

    static int allPossibleSplit1(int m) {
        return (int) (((long) possibleSplit1WithRowNr(0, m) + possibleSplit1WithRowNr(1, m) + possibleSplit1WithRowNr(2, m)) % MOD);
    }

    static int possibleSplit1WithRowNr(int rowNr, int m) {
        return powMod(3, (m + 2 - rowNr) / 3);
    }

    static int allPossibleSplit2and2(int m) {
        return (int) (((long) possibleSplit2and2WithEmptyRow(0, m) + possibleSplit2and2WithEmptyRow(1, m) + possibleSplit2and2WithEmptyRow(2, m)) % MOD);
    }

    static int possibleSplit2and2WithEmptyRow(int rowNr, int m) {
        int pow = ((m + 2) / 3) + ((m + 1) / 3) + (m / 3) - ((m + 2 - rowNr) / 3);
        return powMod(3, pow);
    }

    static int allPossibleExact2(int m, int n) {
        long sum = 0;
        for (int rowNr = 0; rowNr < 3; rowNr++) {
            for (int colNr = 0; colNr < 3; colNr++) {
                sum += possibleExact2(rowNr, colNr, m, n);
            }
        }
        return (int) (sum % MOD);
    }

    static int possibleExact2(int rowNr, int colNr, int m, int n) {
        return mult(powMod(2, (n + 2 - colNr) / 3) - 2, powMod(3, (m + 2 - rowNr) / 3));
    }

    static int possibleExact2Div3(int rowNr, int colNr, int m, int n) {
        return mult(powMod(2, (n + 2 - colNr) / 3) - 2, powMod(3, (m - 1 - rowNr) / 3));
    }

    static int allPossibleNotMoreThan2(int m, int n) {
        long sum = 0;
        for (int rowNr = 0; rowNr < 3; rowNr++) {
            for (int colNr = 0; colNr < 3; colNr++) {
                sum += possibleNotMoreThan2(rowNr, colNr, m, n);
            }
        }
        return (int) (sum % MOD);
    }

    static int possibleNotMoreThan2(int rowNr, int colNr, int m, int n) {
        return mult(powMod(3, (n + 2 - colNr) / 3) - 3 /* when all random is on one line*/ - possibleExact2Div3(rowNr, colNr, 3, n), powMod(2, (m + 2 - rowNr) / 3));
    }

    static int allPossibleOther(int n) {
        return allPossible3byN(n)
                - allPossibleSplit1(3)
                - allPossibleSplit2and2(3)
                - allPossibleExact2(3, n)
                - allPossibleNotMoreThan2(3, n);
    }

    static int powMod(int val, int pow) {
        if (val == 3) {
            return pow3[pow];
        }
        if (val == 9) {
            return pow3[pow * 2];
        }
        if (val == 2) {
            return pow2[pow];
        }
        throw new UnsupportedOperationException();
    }

    static int mult(int val, int k) {
        return (int) ((long) val * k % MOD);
    }
}

