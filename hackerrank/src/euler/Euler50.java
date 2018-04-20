package euler;

import java.util.*;

public class Euler50 {

    public static final int LIMIT = 10000000;
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
    
    static HashSet<Integer> PRIME_CHECK = new HashSet<>(); 
    static {
        Arrays.stream(PRIMES).forEach(PRIME_CHECK::add);
    }

    public static void main(String[] args) {

        TreeMap<Integer, Long> best = new TreeMap<>(); 
        
        long check = 0;
        for (int c = 1; c < 10000; c += 2) {
            check += PRIMES[c - 1] + PRIMES[c];
            if (isPrime(check)) {
                best.put(c + 1, check);
//                System.err.println(check + ": " + (c + 1));
            }
        }


        System.out.println(best.size());
        check = 3;
        for (int c = 3; c < 10000; c += 2) {
            check += PRIMES[c-1] + PRIMES[c];
            if (isPrime(check)) {
                best.put(c + 1, check);
                System.err.println(check + ": " + (c));
            }
        }
    }

    private static boolean isPrime(long check) {
        if (check <= LIMIT) return PRIME_CHECK.contains((int) check);
        for (int prime : PRIMES) {
            if (check % prime == 0) return false;
        }
        if ((long) LIMIT * LIMIT < check) throw new RuntimeException();
        return true;
    }
}
