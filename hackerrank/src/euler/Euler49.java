package euler;

import java.util.*;
import java.util.stream.Collectors;

public class Euler49 {

    public static final int LIMIT = 1000000;
    
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

    static int[] pow = {
            1,
            7,
            49,
            343,
            2401,
            16807,
            117649,
            823543,
            5764801,
            40353607
    };

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();

        HashMap<Integer, ArrayList<Integer>> bySign = new HashMap<>();
        int[] sign = new int[LIMIT];

        for (int i : PRIMES) {
            int j = i;
            while (j > 0) {
                sign[i] += pow[j % 10];
                j /= 10;
            }

            bySign.computeIfAbsent(sign[i], x -> new ArrayList<>()).add(i);
        }

        ArrayList<String> out = new ArrayList<>();

        for (ArrayList<Integer> bucket : bySign.values()) {
            for (int i = 0; i < bucket.size(); i++) {
                for (int j = i + 1; j < bucket.size(); j++) {
                    int a = Math.min(bucket.get(i), bucket.get(j));
                    if (a >= n) continue;
                    int b = Math.max(bucket.get(i), bucket.get(j));
                    int diff = b - a;
                    if (b + diff >= LIMIT || sign[b + diff] != sign[a]) continue;
                    if (k == 4) {
                        if (b + 2 * diff >= LIMIT || sign[b + 2 * diff] != sign[a]) continue;
                    }
                    out.add("" + a + b + (b + diff) + (k == 4 ? b + 2 * diff : ""));
                }
            }
        }

        out.sort(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()));
        System.out.println(out.stream().collect(Collectors.joining("\n")));
    }
}
