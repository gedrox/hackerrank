package euler;

import org.junit.Test;

import java.util.Scanner;

public class Euler167 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long k = sc.nextLong();
        
        System.out.println(solve(n, k));
    }

    private static long solve(int n, long k) {
        if (k == 1) return 2;
        if (k <= n + 3) {
            return 2*n + 2*k - 3;
        } else {
            return 4*n + 4 + (k - n - 3);
        }
    }
    
    @Test
    public void test() {
        for (int i = 0; i < 10; i++) {
            System.out.print(solve(2, i + 1) + " ");
        }
    }
}
