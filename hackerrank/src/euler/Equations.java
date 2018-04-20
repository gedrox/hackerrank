package euler;

import java.util.HashSet;
import java.util.Scanner;

public class Equations {
    public static void main(String[] args) {
        int n = new Scanner(System.in).nextInt();
        HashSet<Integer> x = new HashSet<>();
        int c = 0;
        for (int i = 1; i <= n; i++) {
            if (i * i > n) break;
            if (n % i == 0) {
                int y = i * (i + n / i) / gcd(i, n/i);
                if (x.add(y)) {
                    c += i*i==n ? 1 : 2;
                }
            }
        }
        System.out.println(c);
    }
    
    static int gcd(int a, int b) {
        while (a != 1) {
            int c = b % a;
            b = a;
            a = c;
        }
        return b;
    }
}
