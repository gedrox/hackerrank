package euler;

import java.util.Scanner;

public class Euler31 {

    public static final int MOD = 1000000007;

    static int[] vals = {1, 2, 5, 10, 20, 50, 100, 200};

    public static void main(String[] args) {
        long[][] res = new long[8][100001];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < res[i].length; j++) {
                if (i == 0) {
                    res[i][j] = 1;
//                } else if (i == 1) {
//                    res[i][j] = j / 2 + 1;
//                } else if (i == 2) {
//
//                    int sum = sum5(j, 1);
//                    if (j % 2 == 0) {
//                        sum += sum5(j, -2);
//                    } else {
//                        sum += sum5(j, -1);
//                    }
//                    sum %= MOD;
//                    res[i][j] = sum;
                } else {
                    res[i][j] = (res[i - 1][j] + ((j - vals[i] < 0) ? 0 : res[i][j - vals[i]])) % MOD;
                }
            }
        }

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            System.out.println(res[7][n]);
        }
        
        
    }

    private static int sum5(int j, int offset) {
//        if (j == 0) return offset > 0 ? 1 : 0;
        int firstEl = j / 2 + offset;
        if (firstEl <= 0) return 0;
        int lastEl = firstEl % 5;
        int elCount = (firstEl - lastEl) / 5 + 1;
        return (int) ((((long) firstEl + lastEl) * elCount / 2) % MOD);
    }
}