import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class euler29 {
    
    @Test
    public void test() {
        HashSet<Long> set = new HashSet<>();
        int n = 9;
        for (int i = 2; i <= n; i++) {
            for (int j = 2; j <= n; j++) {
                if (!set.add((long) Math.pow(i, j))) {
                    System.err.println("Repeating " + i + "^" + j);
                }
            }
        }
        System.out.println(set.size());
    }
    
    public static void main(String[] args) {

        int n = new Scanner(System.in).nextInt();
        
        long t0 = System.currentTimeMillis();
        
        int MAX = 100000;
//        MAX = (int) (MAX * Math.log(MAX) / Math.log(2));
        
        boolean[] isComplex = new boolean[MAX + 1];
        boolean[] isPrime = new boolean[MAX + 1];

        ArrayList<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= MAX; i++) {
            
            int gcd = 0;
            
            int j = i;
            int[] power = new int[primes.size() + 1];
            for (int primeIndex = 0; primeIndex < primes.size() && primes.get(primeIndex) <= j; primeIndex++) {
                int prime = primes.get(primeIndex);
                if (j % prime == 0) {
                    while (j % prime == 0) {
                        power[primeIndex]++;
                        j /= prime;
                    }

                    gcd = gcd(gcd, power[primeIndex]);
                }
            }
            
            if (j != 1) {
                power[primes.size()] = 1;
                gcd = 1;
                primes.add(j);
                isPrime[j] = true;
            }
            
            isComplex[i] = (gcd > 1);
        }

        long count = ((long) n - 1) * (n - 1);

        for (int a = 2; a <= Math.sqrt(n); a++) {
            if (!isComplex[a]) {
                for (int b = 4; b <= n * Math.log(n)/Math.log(a) + 1; b++) {
                    boolean hadFirst = (b <= n);
                    if (b >= isPrime.length || !isPrime[b]) 
                    {
                        long test = (long) a * a;
                        int pow = 2;
                        while (test <= n) {
                            if (b % pow == 0 && b/pow <= n) {
                                if (!hadFirst) {
                                    hadFirst = true;
                                } else {
//                                    System.err.println("Repeating " + a + "^" + b + "=" + test + "^" + (b / pow));
                                    count--;
                                }
                            }
                            pow++;
                            test *= a;
                        }
                    }
                }
            }
        }

        System.out.println(count);
        System.err.println(System.currentTimeMillis() - t0);
    }

    private static int gcd(int a, int b) {
        if (a > b) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        while (a != 0) {
            int tmp = b % a;
            b = a;
            a = tmp;
        }
        return b;
    }
}
