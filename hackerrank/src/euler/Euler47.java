package euler;

import java.util.ArrayList;
import java.util.Scanner;

public class Euler47 {

    public static final int LIMIT = 2000000;
//    public static final int LIMIT = 200000;
    public static int[] PRIMES = getPrimes(LIMIT);

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
        int N[] = new int[LIMIT + 10];
//        for (int i = 20; i < N.length; i++) {
//            int j = i;
//            for (int prime : PRIMES) {
//                if (j % prime == 0) {
//                    N[i]++;
//                    while (j % prime == 0) {
//                        j /= prime;
//                    }
//                }
//                if (j == 1) break;
//            }
//        }

        for (int prime : PRIMES) {
            for (int i = prime; i < N.length; i += prime) {
                N[i]++;
            }
        }

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int k = scanner.nextInt();
        
        int b = 0;
        for (int i = 1; i < n + k; i++) {
            if (N[i] == k) {
                b++;
                if (b >= k) {
                    System.out.println(i - k + 1);
                }
            } else {
                b = 0;
            }
        }
    }
}
