package euler;

import java.util.Arrays;
import java.util.Scanner;

public class Euler30 {
    public static void main(String[] args) {
        
        int n = new Scanner(System.in).nextInt();
        int d[] = new int[10];
        for (int i = 0; i < d.length; i++) {
            d[i] = (int) Math.pow(i, n);
        }
        
        long sum = 0;

        for (int test = 1; test < 10000000; test++) {
            int check = 0;
            int test2 = test;
            while (test2 > 0) {
                check += d[test2 % 10];
                test2 /= 10;
            }
            if (check == test) {
                sum += test;
                System.err.println(test);
            }
        }
        System.out.println(sum);
    }
}
