package euler;

import java.util.ArrayList;
import java.util.Scanner;

public class Euler46 {

    public static final int LIMIT = 500000;
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
        int[] N = new int[LIMIT];
        
        ArrayList<Integer> sq2 = new ArrayList<>();
        for (int i = 1; i < Math.sqrt(LIMIT / 2); i++) {
            sq2.add(2 * i * i);
        }

        for (int i = 1; i < PRIMES.length; i++) {
            for (int add : sq2) {
                if (PRIMES[i] + add >= LIMIT) break;
                N[PRIMES[i] + add]++;
            }
        }

        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 0; i < t; i++) {
            int n = sc.nextInt();
            System.out.println(N[n]);
        }
    }
}
