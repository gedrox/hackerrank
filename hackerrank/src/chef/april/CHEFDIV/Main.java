package chef.april.CHEFDIV;

import java.util.*;

public class Main {

    private static int[] getPrimes(int max) {
        ArrayList<Integer> primes = new ArrayList<>(1734912);
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

        int[] primes = getPrimes((int) 1e6);

        long A = sc.nextLong();
        long B = sc.nextLong();
        int size = (int) (B - A + 1);
        long[] C = new long[size];
        ArrayList<Integer>[] pows = new ArrayList[size];

        for (int x = 0; x < size; x++) {
            C[x] = x + A;
            pows[x] = new ArrayList<>();
        }

        for (int prime : primes) {
            for (long x = ((A - 1) / prime) * prime + prime; x <= B; x += prime) {
                int c = 0;
                while (C[(int) (x - A)] % prime == 0) {
                    C[(int) (x - A)] /= prime;
                    c++;
                }
                pows[(int) (x - A)].add(c);
            }
        }

        long total = 0;

        for (int i1 = 0; i1 < C.length; i1++) {
            long i = C[i1];
            ArrayList<Integer> pow = pows[i1];

            if (i != 1) {
                pow.add(1);
            }

            Collections.sort(pow);
            total += calc(pow);
        }

        System.out.println(total);
    }

    private static long calc(ArrayList<Integer> pow) {
        if (pow.size() == 0) {
            return 0;
        }

        int last = pow.size() - 1;
        while (last > 0 && Objects.equals(pow.get(last), pow.get(last - 1))) {
            last--;
        }

        long res = 1;
        for (int val : pow) {
            res *= (val + 1);
        }

        int newVal = pow.get(last) - 1;
        if (newVal > 0) {
            pow.set(last, newVal);
        } else {
            pow.remove(last);
        }

        res += calc(pow);

        return res;
    }
}
