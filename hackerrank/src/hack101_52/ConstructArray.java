package hack101_52;

import java.util.Scanner;

public class ConstructArray {

    static int MOD = 1000000007;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int x = sc.nextInt() - 1;

        long c1 = 1;
        long c2 = 0;

        for (int i = 1; i < n; i++) {
            long newC1 = c2 * (k - 1);
            long newC2 = c1 + c2 * (k - 2);

            c1 = newC1 % MOD;
            c2 = newC2 % MOD;
        }

        System.out.println(x == 0 ? c1 : c2);
    }
}
