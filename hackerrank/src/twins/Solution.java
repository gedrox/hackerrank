package twins;

import java.util.ArrayList;
import java.util.Scanner;

public class Solution {

    public static final int MAX = 1000000;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();

        if (n % 2 == 0) n++;
        if (m % 2 == 0) m--;

        int c = (m - n) / 2 + 1;

        if (c < 2) {
            System.out.println(0);
            return;
        }

        Integer[] pr = getPrimes(MAX); // 1e6

        boolean check[] = new boolean[c];

        for (Integer prime : pr) {
            int start = (n - 1) / prime + 1;
            if (start % 2 == 0) start++;
            if (start == 1) start = 3;

            int index = (start * prime - n) / 2;
            while (index < check.length) {
                check[index] = true;
                index += prime;
            }
        }

        int res = 0;
        for (int i = 0; i < check.length; i++) {
            if (check[i]) {
                continue;
            }
            if (!checkPrime(n + 2 * i)) {
                check[i] = true;
                continue;
            }
            if (i == 0) {
                continue;
            }
            if (!check[i-1]) {
                res++;
            }
        }

        System.out.println(res);
    }

    private static boolean checkPrime(int prime) {
        if (prime == 1) return false;
        for (int i = MAX + 1; i <= Math.sqrt(prime); i += 2) {
            if (prime % i == 0) {
                return false;
            }
        }
        return true;
    }

    private static Integer[] getPrimes(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        boolean p[] = new boolean[(max - 1)/2];
        for (int i = 0; i < p.length; i++) {
            if (!p[i]) {
                primes.add(2*i + 3);
                int next = 3*i + 3;
                while (next < p.length) {
                    p[next] = true;
                    next += 2*i + 3;
                }
            }
        }

        return primes.toArray(new Integer[primes.size()]);
    }
}
