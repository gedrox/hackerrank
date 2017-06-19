package megaprimeNumbers;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

//232323000000000 232323300000000
public class SolutionOld {

    public static int[] PRIMES;
    
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
    
    public static void main(String[] args) throws IOException {
        //TODO: handle numbers up to 11 manually
        Scanner in = new Scanner(System.in);
        long first = in.nextLong();
        long last = in.nextLong();
        
        if (first > last) throw new RuntimeException();
        if (last > 1e15) throw new RuntimeException();
        if (last - first > 1e9) throw new RuntimeException();
        
        int res = 0;
        
        for (int prime : new int[]{2, 3, 5, 7}) {
            if (first <= prime && last >= prime) {
                res++;
                first = prime + 1;
            }
        }
        
        if (first > last) {
            System.out.println(res);
            return;
        }

        String a = String.valueOf(first);
        String b = "0" + String.valueOf(last);
        while (a.length() < b.length()) {
            a = "0" + a;
        }

        res += solve(a.toCharArray(), b.toCharArray());

        System.out.println(res);
    }
    
    @Test
    public void check() throws IOException {
        System.out.println(solve("232323000000000".toCharArray(), "232323900000000".toCharArray()));
    }
    
    static int solve(char[] A, char[] B) throws IOException {
        PRIMES = getPrimes(31623000);
        findValid(A);
        System.err.println("next valid found");
        ArrayList<Long> longs = new ArrayList<>();
        
        while (notGreater(A, B)) {
            if (!div2(A) && !div3(A) && !div5(A) && !div11(A)) {
                long x = 0;
                for (char aX : A) {
                    x = 10 * x + (aX - '0');
                }
                if (x % 7 != 0) {
                    longs.add(x);
                }
            }
            next(A);
        }

        System.err.println("possibilities found " + longs.size());
        
        int out = 0;
        
        int[] mod = new int[PRIMES.length];
        long first = longs.get(0);
        for (int i = 0; i < PRIMES.length; i++) {
            mod[i] = (int) (first % PRIMES[i]);
        }

        // maybe int operations are cheaper?
        int[] ints = new int[longs.size()];
        for (int i = 0; i < longs.size(); i++) {
            ints[i] = (int) (longs.get(i) - first);
        }
        
        int limit = (int) Math.floor(Math.sqrt(longs.get(longs.size() - 1)));
        int maxPrimeIndex = Arrays.binarySearch(PRIMES, limit);
        if (maxPrimeIndex < 0) maxPrimeIndex = -maxPrimeIndex;
        if (maxPrimeIndex >= PRIMES.length) maxPrimeIndex = PRIMES.length - 1;
        int phaseDelim = Math.min(10000, maxPrimeIndex);

        System.err.println("Ready for final sieve");
        
        ArrayList<Long> primes = new ArrayList<>();
        
        for (int x : ints) {
            boolean b = checkPrimeBrute(mod, phaseDelim, x);
            if (b) {
                primes.add(first + x);
                out++;
            }
        }

        System.err.println("2nd phase of sieve");
        long last = primes.get(primes.size() - 1);
        Long[] prArr = primes.toArray(new Long[0]);

        int phaseDelim2 = maxPrimeIndex / 2;
        
        for (int i = phaseDelim2 + 1; i <= maxPrimeIndex; i++) {
            int P = PRIMES[i];
            long f = (first / P) * P;
            if (f % 2 == 0) f += P;

//            int pos = Collections.binarySearch(primes, f);
//            if (pos < 0) pos = -pos - 1;

//            while (true) {
//                while (pos < primes.size() && primes.get(pos) < f) {
//                    pos++;
//                }
//                if (pos >= primes.size()) {
//                    break;
//                }
//                if (primes.get(pos) == f) {
//                    primes.remove(f);
//                    out--;
//                    break;
//                }
//                f += 2 * P;
//            }

            int pos = 0;
            while (f <= last) {
                pos = Arrays.binarySearch(prArr, pos, prArr.length, f);
                if (pos >= 0) {
                    primes.remove(f);
                    out--;
                } else {
                    pos = - pos - 1;
                }
                f += 2 * P;
            }
        }

//        Files.write(Paths.get("candidates.txt"), primes);
//        Files.write(Paths.get("primes.txt"), Arrays.stream(PRIMES).filter((p) -> p <= limit).mapToObj(String::valueOf).collect(Collectors.toList()));

//        long t0 = System.currentTimeMillis();
//        System.err.println("start with " + maxPrimeIndex + " primes");
//        int hash = 0;
//        for (long prime : primes) {
//            for (int i = 5; i <= maxPrimeIndex; i++) {
//                hash ^= (prime % PRIMES[i]);
//            }
//        }
//        System.err.println(hash + " done in " + (System.currentTimeMillis() - t0)/1000 + "s");
        
//        System.err.println(yesTime);
//        System.err.println(noTime);
        
        return out;
    }

    private static boolean checkPrimeBrute(int[] mod, int phaseDelim, int x) {
        for (int i = 5; i <= phaseDelim; i++) {
            int resMod = mod[i] + x % PRIMES[i];
            if (resMod == 0 || resMod == PRIMES[i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean div2(char[] A) {
        return A[A.length - 1] == '2';
    }

    private static boolean div3(char[] A) {
        int sum = 0;
        for (char c : A) {
            sum += (c - '0');
        }
        return sum % 3 == 0;
    }

    private static boolean div5(char[] A) {
        return A[A.length - 1] == '5';
    }

    private static boolean div11(char[] A) {
        int sum1 = 0, sum2 = 0;
        boolean b = true;
        for (char c : A) {
            if (b) sum1 += (c - '0'); else sum2 += (c - '0');
            b = !b;
        }
        return (sum1 - sum2) % 11 == 0;
    }

    private static boolean notGreater(char[] a, char[] b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > b[i]) return false;
            if (a[i] < b[i]) return true;
        }
        return true;
    }

    static void next(char[] A) {
        for (int i = A.length - 1; i >= 0; i--) {
            A[i] = next(A[i]);
            if (A[i] == 'X') {
                A[i] = '2';
            } else {
                break;
            }
        }
    }
    
    static void findValid(char[] A) {
        int i = 0;
        while (A[i] == '0') i++;
        
        while (i < A.length) {
            if (!valid(A[i])) {
                int j = i;
                while (j >= 0) {
                    A[j] = next(A[j]);
                    if (A[j] != 'X') {
                        break;
                    } else {
                        A[j] = '2';
                    }
                    j--;
                }
                
                j = i + 1;
                while (j < A.length) {
                    A[j] = '2';
                    j++;
                }
                
                break;
            }
            i++;
        }
    }

    private static char next(char c) {
        if (c < '2') return '2';
        if (c < '3') return '3';
        if (c < '5') return '5';
        if (c < '7') return '7';
        return 'X';
    }

    private static boolean valid(char c) {
        return c == '2' || c == '3' || c == '5' || c == '7';
    }
    
}
