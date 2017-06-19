package geometricTrick;

import java.util.ArrayList;
import java.util.Scanner;

public class BucketSolve {

    public static final int[] PRIMES = getPrimes(707);
    static int BUCKETS = 1000;

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

    static int bucket(int n) {
        int b = 0;
        for (int p_i = 0; p_i < PRIMES.length && n >= ((long) PRIMES[p_i]) * PRIMES[p_i]; p_i++) {
            while (n % PRIMES[p_i] == 0) {
                n /= PRIMES[p_i];
                b ^= PRIMES[p_i];
            }
        }
        if (n != 1) {
            b ^= n;
        }
        b %= BUCKETS;
        return b;
    }

    static int bucketSolve(String s) {
        ArrayList<Integer>[] bA = new ArrayList[BUCKETS];
        ArrayList<Integer>[] bC = new ArrayList[BUCKETS];

        for (int i = 0; i < BUCKETS; i++) {
            bA[i] = new ArrayList<>();
            bC[i] = new ArrayList<>();
        }
        
        int n = s.length();
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'b') continue;
            
            if (s.charAt(i) == 'a') {
                bA[bucket(i + 1)].add(i + 1);
            } else if (s.charAt(i) == 'c') {
                bC[bucket(i + 1)].add(i + 1);
            }
        }

        int result = 0;
        for (int i = 0; i < BUCKETS; i++) {
            for (int a : bA[i]) {
                for (int c : bC[i]) {
                    int b = (int) Math.sqrt(((long) a) * c);
                    if (((long) b) * b == ((long) a) * c) {
                        if (s.charAt(b - 1) == 'b') {
                            result++;
                        }
                    }
                }
            }
        }
        
        return result;
    }

    public static void main(String[] args) {
        System.err.println(PRIMES.length);
        Scanner in = new Scanner(System.in);
        in.nextInt();
        String s = in.next();
        int result = bucketSolve(s);
        System.out.println(result);
    }
    
}
