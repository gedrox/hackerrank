package euler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Euler41 {

    public static int[] PRIMES = getPrimes(7654321);

    private static int[] getPrimes(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        boolean p[] = new boolean[(max - 1) / 2];
        for (int i = 0; i < p.length; i++) {
            if (!p[i]) {
                primes.add(2 * i + 3);
                int next = 3 * i + 3;
                while (next < p.length) {
                    p[next] = true;
                    next += 2 * i + 3;
                }
            }
        }

        int[] pr = new int[primes.size()];
        for (int i = 0; i < primes.size(); i++) {
            pr[i] = primes.get(i);
        }

        return pr;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        
        ArrayList<Integer> a = new ArrayList<>();
        next:
        for (int prime : PRIMES) {
            String s = String.valueOf(prime);
            for (int i = 1; i <= s.length(); i++) {
                if (s.indexOf((char)('0' + i)) < 0) continue next;
            }
            a.add(prime);
        }
        
        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            int pos = Collections.binarySearch(a, n);
            if (pos < 0) pos = -pos-2;
            if (pos == -1) {
                System.out.println(-1);
            } else {
                System.out.println(a.get(pos));
            }
        }
    }
}
