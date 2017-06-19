package satisfactory_pairs;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class SolutionGuessing {



    public static final Integer[] PRIMES = getPrimes(300000);



    private static Integer[] getPrimes(int max) {
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
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

    @Test
    public void testConcept() {
        int n = 16;
        int c = solve(n);

        System.out.println(c);
    }

    static int solve(int n) {
        Fact[] f = new Fact[n+1];
        Divisors[] d = new Divisors[n+1];

        for (int i = 1; i <= n; i++) {
            f[i] = new Fact(i);
            d[i] = new Divisors(f[i]);
        }

        int c = 0;

        for (int i = 2; i <= n - 2; i++) {
            int j = n-i;

            // all without "1"
            int add = (d[i].max - 1) * (d[j].max - 1);
//            System.err.println("Add " + add + " for (" + i + ", " + j + ")");
            c += add;


            Divisors.Iterator i_it = d[i].it();

            while (i_it.hasNext()) {
                int nextI = i_it.next();

                Divisors.Iterator d_j = d[j].it();

                while (d_j.hasNext()) {
                    int nextJ = d_j.next();

                    if (nextJ < j && nextI * nextJ / gcd(nextI, nextJ) < j) {
                        c--;
//                        System.err.println(".. remove " + nextI + ", " + nextJ);
                    }

                }
            }
        }

        c -= d[n].max - 2;

//        System.err.println("Remove " + (d[n].max - 2) + " diag points");

        c /= 2;
        c += n - 2;

//        System.err.println("Add (1) " + (n-2));

        return c;
    }

    static int mult(int i) {
        int div = 2;
        int res = 1;
        while (i != 1) {
            int c = 1;
            while (i % div == 0) {
                i /= div;
                c++;
            }
            res *= c;
            div++;
        }
        return res;
    }

    static int gcd(int a, int b) {
        while (a != 0) {
            int tmp = b % a;
            b = a;
            a = tmp;
        }
        return b;
    }

    @Test
    public void testFact() {
        Fact fact = new Fact(1024 * 3);

        Divisors.Iterator d = new Divisors(fact).it();
        while (d.hasNext()) {
            System.out.println(d.next());
        }
    }

    static class Fact {
        TreeMap<Integer, Integer> times = new TreeMap<>();

        public Fact(int x) {
            for (int prime : PRIMES) {
                if (x % prime == 0) {
                    int c = 0;
                    do {
                        c++;
                        x /= prime;
                    } while (x % prime == 0);

                    times.put(prime, c);
                    if (x == 1) return;
                }
            }

        }
    }

    static class Divisors {

        int max = 1;
        int[] mod;
        int[] fact;
        int L;

        public Divisors(Fact f) {
            L = f.times.size();
            mod = new int[L];
            fact = new int[L];

            int i = 0;

            for (int key : f.times.keySet()) {
                max *= f.times.get(key) + 1;
                mod[i] = f.times.get(key) + 1;
                fact[i] = key;

                i++;
            }
        }

        Iterator it() {
            return new Iterator();
        }

        class Iterator {

            int i = 1;

            boolean hasNext() {
                return i < max;
            }

            int next() {

                int R = i;
                int res = 1;
                for (int j = 0; j < L; j++) {
                    res *= Math.pow(fact[j], R % mod[j]);
                    R /= mod[j];
                }

                i++;

                return res;
            }

            void reset() {
                i = 1;
            }

            int nextRank() {
                throw new UnsupportedOperationException();
            }
        }
    }

}
