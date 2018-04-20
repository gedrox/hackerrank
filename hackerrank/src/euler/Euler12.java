package euler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Euler12 {

    public static final int[] PRIMES = getPrimes(41100);

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

        ArrayList<Integer> count = new ArrayList<>();
        ArrayList<Integer> index = new ArrayList<>();

        long max = 0;
        int limit = 41100;
        int[] pow = new int[limit];
        for (int i = 1; i < limit; i++) {
            int j = i;
            pow[i] = 1;
            for (int prime : PRIMES) {
                if (j % prime == 0) {
                    int cnt = prime == 2 ? -1 : 0;
                    do {
                        cnt++;
                        j /= prime;
                    } while (j % prime == 0);

                    pow[i] *= (1 + cnt);
                }
                if (j == 1) break;
            }
        }

        for (int i = 1; i < limit - 1; i++) {
            int res = pow[i] * pow[i + 1];
            if (res > max) {
                count.add(res);
                index.add(i);
                max = res;
                if (max > 1000) {
//                    System.err.println(i);
                    break;
                }
//                System.out.println(i + ": " + max);
            }
        }

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 0; i < t; i++) {
            int x = sc.nextInt();
            int pos = Collections.binarySearch(count, x + 1);
            if (pos < 0) {
                pos = -pos - 1;
            }
            int idx = index.get(pos);
            System.out.println(idx * (idx + 1) / 2);
        }
    }
}
