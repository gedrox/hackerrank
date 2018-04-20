package MaximumGcdAndSum;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MaximumGcdAndSumSlow {

    static int max = 1000000;

    public static int[] PRIMES;
    private static int[] primes;

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

    @Test
    public void test() {
        getD();
    }
    
    public static void main(String[] args) throws IOException {

        primes = getPrimes(1000);

        ArrayList<Integer>[] d = getD();

        int[] amap = new int[max + 1];
        int[] bmap = new int[max + 1];

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        String[] split = br.readLine().split(" ");
        for (int A_i = 0; A_i < n; A_i++) {
            int a = Integer.parseInt(split[A_i]);
            if (amap[a] != 0) continue;

            amap[a] = a;
            for (int div : d[a]) {
                if (amap[a / div] < a) {
                    amap[a / div] = a;
                }
                if (amap[div] < a) {
                    amap[div] = a;
                }
            }
        }

        split = br.readLine().split(" ");
        for (int B_i = 0; B_i < n; B_i++) {
            int b = Integer.parseInt(split[B_i]);
            if (bmap[b] != 0) continue;

            bmap[b] = b;
            for (int div : d[b]) {
                if (amap[b / div] < b) {
                    amap[b / div] = b;
                }
                if (amap[div] < b) {
                    amap[div] = b;
                }
            }
        }

        int i = max;
        while (amap[i] == 0 && bmap[i] == 0) {
            while (amap[i] == 0) i--;
            while (bmap[i] == 0) i--;
        }

        int res = amap[i] + bmap[i];

        System.out.println(res);
    }

    private static ArrayList<Integer>[] getD() {
        ArrayList<Integer>[] d = new ArrayList[max + 1];
        for (int i = 0; i < d.length; i++) {
            d[i] = new ArrayList<>();
        }

//        for (int prime : primes) {
//            for (int j = prime * prime; j < d.length; j += prime) {
//                d[j].add(prime);
//            }
//        }
        
        for (int i = 2; i <= Math.sqrt(max); i++) {
            for (int j = i * i; j < d.length; j += i) {
                d[j].add(i);
            }
        }
        
        return d;
    }
}
