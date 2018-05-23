package chefmay.DBFB;

import java.util.Scanner;

public class Main {

    private static final int MOD = 1_000_000_007;
    private static int m;
    private static int n;
    private static int[] a;
    private static int[] b;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int i = 0; i < T; i++) {
            readInput(sc);
            int answer = solve();
            System.out.println(answer);
        }
    }

    private static int solve() {
        
        int f1 = 1, f2 = 0;

        for (int i = 2; i < n; i++) {
            int f3 = (f1 + f2) % MOD;
            f1 = f2;
            f2 = f3;
        }
        
        int sumA = sum(a), sumB = sum(b);
        
        int answer = (int) (((((long) sumA * f2 + (long) sumB * (f1 + f2)) % MOD) * m) % MOD);
        
        return (answer + MOD) % MOD;
    }

    private static int sum(int[] a) {
        int sum = 0;
        for (int i : a) {
            sum += i;
            sum %= MOD;
        }
        return sum;
    }

    private static void readInput(Scanner sc) {
        m = sc.nextInt();
        n = sc.nextInt();
        init();
        for (int i1 = 0; i1 < m; i1++) a[i1] = sc.nextInt();
        for (int i1 = 0; i1 < m; i1++) b[i1] = sc.nextInt();
    }

    private static void init() {
        a = new int[m];
        b = new int[m];
    }
}
