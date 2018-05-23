package chefmay.CHSIGN;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static int n;
    private static int[] a;

    static int readInt() throws IOException {
        return (int) readLong();
    }

    private static int[] readIntArray(int n) throws IOException {
        int[] V = new int[n];
        for (int i = 0; i < n; i++) V[i] = readInt();
        return V;
    }

    static long readLong() throws IOException {
        long res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }

    static char readChar() throws IOException {
        while (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') ch = br.read();
        char oneChar = (char) ch;
        ch = br.read();
        return oneChar;
    }

    public static void main(String[] args) throws IOException {
        int t = readInt();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < t; i++) {
            readInput();
            int[] ans = solve();
            for (int an : ans) {
                sb.append(an).append(" ");
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }

    private static int[] solve() {

        Collection<Solution> sol = new LinkedList<>();
        sol.add(new Solution());

        ArrayList<Integer> cans = new ArrayList<>(n/2);

        for (int i = 0; i < n; i++) {
            Solution best = null;
            boolean canWeI = canLee(i);
            if (canWeI) {

                cans.add(i);
                for (Solution s : sol) {
                    if (s.sum == 0 || s.sum - a[i] > 0) {
                        Solution snew = new Solution(s, i);
                        if (best == null || best.all > snew.all) {
                            best = snew;
                        }
                    }
                }
                if (cans.size() >= 5) {
                    sol.removeIf(s -> s.neg == null || s.neg.equals(cans.get(cans.size() - 5)));
                }
            }

            for (Solution s : sol) {
                s.sum += a[i];
                s.all += a[i];
            }

            if (best != null) {
                sol.add(best);
            }
        }
//        System.err.println(System.currentTimeMillis() - t0);

        Solution best = sol.iterator().next();

//        System.err.println("Size in the end is "+ sol.size());
        
        for (Solution s : sol) {
            if (s.all < best.all) {
                best = s;
            }
        }

        int[] newA = a.clone();
        while (best != null && best.neg != null) {
            newA[best.neg] *= -1;
            best = best.prev;
        }

        return newA;
    }

    private static boolean canLee(int i) {
        if (i > 0 && a[i] >= a[i - 1]) {
            return false;
        }
        if (i < n - 1 && a[i] >= a[i + 1]) {
            return false;
        }
        return true;
    }

    private static void readInput() throws IOException {
        n = readInt();
        a = readIntArray(n);
    }

    static class Solution {
//        ArrayList<Integer> neg = new ArrayList<>();
        // sum starting last negative
        long sum = 0;
        long all = 0;
        
        Integer neg = null;
        Solution prev;

        public Solution() {
        }

        public Solution(Solution s, int i) {
//            neg.addAll(s.neg);
//            neg.add(i);
            neg = i;
            prev = s;
            sum = -a[i];
            all = s.all - a[i];
        }
    }

    @Test
    public void samples1() {
        Random r = new Random(0);
        while (true) {
            n = r.nextInt(9) + 2;
            a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = r.nextInt((int) 1e3) + 1;
            }
            int[] answ1 = solve();
            int[] answ2 = brute();

            long s1 = Arrays.stream(answ1).asLongStream().sum();
            long s2 = Arrays.stream(answ2).asLongStream().sum();

            Assert.assertEquals(s2, s1);
        }
    }

    private int[] brute() {

        long best = Long.MAX_VALUE;
        int[] bestA = new int[n];

        nextCase:
        for (int i = 0; i < (1 << n); i++) {
            int[] newA = a.clone();
            for (int i1 = 0; i1 < n; i1++) {
                if ((i & (1 << i1)) != 0) {
                    newA[i1] *= -1;
                }
            }

            long total = 0;
            for (int x = 0; x < n; x++) {
                for (int y = x + 2; y <= n; y++) {
                    long sum = 0;
                    for (int z = x; z < y; z++) {
                        sum += newA[z];
                    }
                    if (sum <= 0) {
                        continue nextCase;
                    }

                    if (x == 0 && y == n) total = sum;
                }
            }

            if (total < best) {
                best = total;
                bestA = newA;
            }
        }

        return bestA;
    }

    @Test
    public void test() {
        n = 6;
        a = new int[]{1, 2, 2, 1, 3, 1};
        int[] newA = solve();
        System.out.println(Arrays.toString(newA));
    }

    @Test
    public void testBig() {
        n = 2_000;
        a = new int[n];
        Random r = new Random(0);
        for (int i = 0; i < n; i++) {
            a[i] = r.nextInt(5) + 1;
        }
        int[] solve = solve();
        long sum = Arrays.stream(solve).asLongStream().sum();
        Assert.assertEquals(4599, sum);
    }

    @Test
    public void testBig2() {
        n = 100_000;
        a = new int[n];
        Random r = new Random(0);
        for (int i = 0; i < n; i++) {
            a[i] = r.nextInt(5) + 1;
        }
        int[] solve = solve();
        long sum = Arrays.stream(solve).asLongStream().sum();
        System.out.println(sum);
    }

    @Test
    public void testRand() {
        Random r = new Random(0);
        while (true) {
            n = 100;
            a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = r.nextInt(500) + 1;
            }
            boolean FASTER = true;
            int[] solve = solve();
            long sum = Arrays.stream(solve).asLongStream().sum();
            System.out.println(sum);
            FASTER = false;
            int[] sSlow = solve();
            long sum2 = Arrays.stream(sSlow).asLongStream().sum();
            Assert.assertEquals(sum2, sum);
        }
    }
}
