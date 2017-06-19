package geometricTrick;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class Solution {

    public static final int[] PRIMES = getPrimes(1000);
    public static final int BITS = 10;

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
        for (int p_i = 0; p_i < PRIMES.length; p_i++) {
            int c = 0;
            while (n % PRIMES[p_i] == 0) {
                n /= PRIMES[p_i];
                c++;
            }
            if (c % 2 == 1) {
                b ^= (1 << ((p_i) % BITS));
            }
        }
        if (n != 1) {
//            b = (1 << bitCnt) - 1;
            b ^= n;
            b %= 1 << BITS;
        }
        return b;
    }

    @Test
    public void testBuckets() {
//        System.out.println(bucket(1));
//        System.out.println(bucket(2));
//        System.out.println(bucket(3));
//        System.out.println(bucket(4));
//        System.out.println(bucket(6));
//        System.out.println(bucket(9));
//        System.out.println(bucket(1000));
//        System.out.println(bucket(400001));

//        System.out.println(PRIMES[PRIMES.length - 1]);

//        System.out.println(bucket(18 * 709));
//        System.out.println(bucket(2 * 709));

        int[] cntPerBuck = new int[1 << BITS];
        for (int i = 1; i <= 500000; i++) {
            cntPerBuck[bucket(i)]++;
        }
        System.out.println(Arrays.toString(cntPerBuck));

        //1000 = 2^3 5^3
    }

    static int geometricTrick(String s) {
        int n = s.length();
        HashSet<String> result = new HashSet<>();

        LinkedList<State> q = new LinkedList<>();
        q.add(new State(0, 1, 1, 1));

        int iters = 0;

        while (!q.isEmpty()) {
            State state = q.pop();
            iters++;
            if (state.check(s)) {
                result.add(state.toString());
//                System.err.println(state);
            }

            for (int p_i = state.primeIndex; p_i < PRIMES.length; p_i++) {
                int times = 1;
                State newState = state;
                while (true) {
                    boolean added = false;
                    newState = newState.multiply12(p_i);
                    if (newState.fits(n)) {
                        added = true;
                        q.add(newState);
                    }

                    State stateForMove = newState;
                    for (int move = 0; move < times; move++) {
                        stateForMove = stateForMove.move1(p_i);
                        if (stateForMove.fits(n)) {
                            added = true;
                            q.add(stateForMove);
                        }
                    }

                    stateForMove = newState;
                    for (int move = 0; move < times; move++) {
                        stateForMove = stateForMove.move2(p_i);
                        if (stateForMove.fits(n)) {
                            added = true;
                            q.add(stateForMove);
                        }
                    }

                    if (!added) break;

                    times++;
                }
//                State newState = state.multiply1(p_i);
//                if (!newState.fits(n)) break;
//                q.add(newState);
            }
//            for (int p_i = state.primeIndex; p_i < PRIMES.length; p_i++) {
//                State newState = state.multiply2(p_i);
//                if (!newState.fits(n)) break;
//                q.add(newState);
//            }
//            for (int p_i = state.primeIndex; p_i < PRIMES.length; p_i++) {
//                State newState = state.multiply12(p_i);
//                if (!newState.fits(n)) break;
//                q.add(newState);
//            }
        }

//        System.err.println(iters);

        return result.size();
    }

    static class State {
        int primeIndex = 0;
        long i, j, k;

        public State(int primeIndex, long i, long j, long k) {
            this.primeIndex = primeIndex;
            this.i = i;
            this.j = j;
            this.k = k;
        }

        boolean fits(int n) {
            return i <= n && j <= n && k <= n;
        }

        public boolean check(String s) {
            return s.charAt((int) (i - 1)) == 'a' && s.charAt((int) (j - 1)) == 'b' && s.charAt((int) (k - 1)) == 'c';
        }

        //        State multiply1(int primeIndex) {
//            return new State(primeIndex, i * PRIMES[primeIndex] * PRIMES[primeIndex], j * PRIMES[primeIndex], k);
//        }
//        State multiply2(int primeIndex) {
//            return new State(primeIndex, i, j * PRIMES[primeIndex], k * PRIMES[primeIndex] * PRIMES[primeIndex]);
//        }
        State multiply12(int primeIndex) {
            return new State(primeIndex + 1, i * PRIMES[primeIndex], j * PRIMES[primeIndex], k * PRIMES[primeIndex]);
        }

        State move1(int primeIndex) {
            return new State(primeIndex + 1, i / PRIMES[primeIndex], j, k * PRIMES[primeIndex]);
        }

        State move2(int primeIndex) {
            return new State(primeIndex + 1, i * PRIMES[primeIndex], j, k / PRIMES[primeIndex]);
        }

        @Override
        public String toString() {
            return i + "," + j + "," + k;
        }
    }

    static int trivial(String s) {
        int result = 0;
        int n = s.length();
        for (long j = 1; j <= n; j++) {
            if (s.charAt((int) (j - 1)) != 'b') continue;
            for (long i_or_k = j - 1; i_or_k > 0; i_or_k--) {
                if ((j * j) % i_or_k == 0) {
                    long k_or_i = (j * j) / i_or_k;
                    if (k_or_i > n) break;
                    if (s.charAt((int) (i_or_k - 1)) == 'a' && s.charAt((int) (k_or_i - 1)) == 'c') {
//                        System.err.println(i_or_k + "," + j + "," + k_or_i);
                        result++;
                    } else if (s.charAt((int) (i_or_k - 1)) == 'c' && s.charAt((int) (k_or_i - 1)) == 'a') {
//                        System.err.println(k_or_i + "," + j + "," + i_or_k);
                        result++;
                    }
                }
            }
        }
        return result;
    }

    @Test
    public void test() {
//        System.err.println(PRIMES.length);
//        for (int n = 1; n < 1000; n++)
        long seed = System.currentTimeMillis();
        System.out.println("seed: " + seed);
        Random r = new Random(seed);
        for (int times = 0; times < 100; times++)
        {
        int n = r.nextInt(500000);
//        System.out.println(n);
        
            char[] c = new char[n];
            for (int i = 0; i < c.length; i++) {
                c[i] = (char) ('a' + r.nextInt(3));
            }

            String s = new String(c);
            long t0 = System.currentTimeMillis();
//            int result = geometricTrick(s);
//            System.out.println(result);
//            System.out.println(System.currentTimeMillis() - t0 + "ms");

//            t0 = System.currentTimeMillis();
//            int result2 = trivial(s);
//            System.out.println(result2);
//            System.out.println(System.currentTimeMillis() - t0 + "ms");

            t0 = System.currentTimeMillis();
            int result3 = bucketSolve(s);
            System.out.println(result3);
            System.out.println(System.currentTimeMillis() - t0 + "ms");
            
//            if (result3 != result2) {
//                Assert.fail(n + "!!!");
//            }
        }
    }

    static int bucketSolve(String s) {
        ArrayList<Integer>[] bA = new ArrayList[1 << BITS];
        ArrayList<Integer>[] bC = new ArrayList[1 << BITS];

        for (int i = 0; i < (1 << BITS); i++) {
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

        for (int i = 0; i < (1 << BITS); i++) {
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
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        String s = in.next();
        int result = bucketSolve(s);
        System.out.println(result);
    }
}
