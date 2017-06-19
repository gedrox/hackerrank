package satisfactory_pairs;

import java.util.*;

public class SolutionClean {

    public static final Integer[] PRIMES = getPrimes(300000);

    public static void main(String[] args) {
        System.out.println(solve(new Scanner(System.in).nextInt()));
    }

    private static Integer[] getPrimes(int max) {
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

        return primes.toArray(new Integer[primes.size()]);
    }

    static int solve(int n) {
        Fact[] f = new Fact[n + 1];
        Divisors[] d = new Divisors[n + 1];

        for (int i = 1; i <= n; i++) {
            f[i] = new Fact();
        }

        for (int prime : PRIMES) {
            int maxPow = (int) Math.floor(Math.log(n + 1) / Math.log(prime));
            int val = prime;
            for (int pow = 0; pow < maxPow; pow++) {
                for (int index = val; index <= n; index += val) {
                    if (pow == 0) {
                        f[index].times.put(prime, 1);
                    } else {
                        f[index].times.put(prime, f[index].times.get(prime) + 1);
                    }
                }

                val *= prime;
            }
        }

        for (int i = 1; i <= n; i++) {
            d[i] = new Divisors(f[i]);
        }

        int c = 0;

        for (int i = 2; i <= n - 2; i++) {
            int j = n - i;

            // all without "1"
            int add = (d[i].max - 1) * (d[j].max - 1);
            c += add;

            for (int nextJ : d[j].cache) {
                for (int nextI : d[i].cache) {
                    if (lcd(nextI, nextJ) < j) {
                        c--;
                    }
                }
                if (lcd(i, nextJ) < j) {
                    c--;
                }
            }

        }

        c -= d[n].max - 2;

        c /= 2;
        c += n - 2;

        return c;
    }

    private static int lcd(int i, int nextJ) {
        return i * nextJ / gcd(i, nextJ);
    }

    private static int lcd(Fact a, Fact b) {
        int res = 1;
        for (Integer key : a.times.keySet()) {
            if (b.times.containsKey(key)) {
                res *= Math.pow(key, Math.max(a.times.get(key), b.times.get(key)));
            }
        }

        return res;
    }

    private static int gcd(Fact a, Fact b) {
        int res = 1;
        for (Integer key : a.times.keySet()) {
            if (b.times.containsKey(key)) {
                res *= Math.pow(key, Math.min(a.times.get(key), b.times.get(key)));
            }
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

    static class Fact {
        TreeMap<Integer, Integer> times = new TreeMap<>();
    }

    static class Divisors {

        int max = 1;
        int[] mod;
        int[] fact;
        int L;
        ArrayList<Integer> cache = new ArrayList<>();

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

            Iterator it = it();
            it.max--;
            while (it.hasNext()) {
                cache.add(it.next());
            }
            Collections.sort(cache);
        }

        Iterator it() {
            return new Iterator();
        }

        class Iterator {

            int i = 1;
            int max = Divisors.this.max;

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

        }
    }
}
