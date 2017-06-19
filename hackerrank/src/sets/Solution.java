package sets;

import java.util.Scanner;

public class Solution {

    static int gcd(int x, int y) {
        if (y == 0) return x;
        if (x < y) return gcd(y, x);
        return gcd(y, x % y);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int c1 = sc.nextInt();
        int c2 = sc.nextInt();

        int[] a = new int[c1];
        int[] b = new int[c2];

        int max = 0, min = 100;
        int gcd = 0, lcd = 0;

        for (int i = 0; i < c1; i++) {
            a[i] = sc.nextInt();
            max = Math.max(a[i], max);
            if (i == 0) {
                lcd = a[i];
            } else {
                lcd = lcd * a[i] / gcd(lcd, a[i]);
            }
        }
        for (int i = 0; i < c2; i++) {
            b[i] = sc.nextInt();
            min = Math.min(b[i], min);
            if (i == 0) {
                gcd = b[i];
            } else {
                gcd = gcd(gcd, b[i]);
            }
        }

        if (gcd % lcd != 0) {
            System.out.println(0);
        } else {
            int X = gcd / lcd;
            if (X == 1) {
                System.out.println(1);
            } else {
                int c = 2;
                for (int i = 2; i < X; i++) {
                    if (X % i == 0) c++;
                }
                System.out.println(c);
            }
        }


        int c = 0;
        next:
        for (int i = max; i <= min; i += max) {
            for (int j = 0; j < c1; j++) {
                if ((i % a[j]) != 0) {
                    continue next;
                }
            }
            for (int j = 0; j < c2; j++) {
                if ((b[j] % i) != 0) {
                    continue next;
                }
            }
            c++;
        }
        System.out.println(c);
    }
}
