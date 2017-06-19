package hc2017r1.beach;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class Beach {

    static int MOD = 1000000007;
    static BigInteger BIG_MOD = BigInteger.valueOf(MOD);

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String s = bi.readLine();

        long longest = 0;

        // file path from "copy as path" feature
        if (s.contains(":") || s.contains("/")) {
            s = s.replace("\"", "");
            bi = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
            s = bi.readLine();
        }

        int q = Integer.parseInt(s);
        StringBuilder sb = new StringBuilder();
        for (int q_i = 1; q_i <= q; q_i++) {
            String[] split = bi.readLine().split(" ");
            int n = Integer.parseInt(split[0]);
            int m = Integer.parseInt(split[1]);
            int[] r = new int[n];
            for (int i = 0; i < n; i++) {
                r[i] = Integer.parseInt(bi.readLine());
            }

            long t0 = System.currentTimeMillis();
            int answer = solve(n, m, r);
            longest = Math.max(longest, System.currentTimeMillis() - t0);

            sb.append("Case #")
                    .append(q_i)
                    .append(": ")
                    .append(answer)
                    .append('\n');

            System.out.print('.');
        }

        Files.write(Paths.get("src/hc2017r1/i-love-beach.txt"), sb.toString().getBytes());

        System.out.println();
        System.out.print(sb.toString());
        System.out.println();
        System.out.printf("Longest run was %dms%n", longest);
    }

    static int solve(int n, int m, int[] r) {
        assert n == r.length;

        if (r.length == 1) {
            return m;
        }

        int occupy = 2 * IntStream.of(r).sum();

        // Could just get 2 min and 2 max in O(n) time
        Arrays.sort(r);

        int minExtraSpace = m - 1 - (occupy - r[0] - r[1]);
        int factorialBase = Math.max(0, minExtraSpace);

        int maxExtraSpace = m - 1 - (occupy - r[r.length - 1] - r[r.length - 2]);
        int factorialMax = maxExtraSpace + n;

        // even with biggest on sides it does not fit
        if (maxExtraSpace < 0) {
            return 0;
        }

        int[] F = new int[factorialMax - factorialBase + 1];
        F[0] = 1;
        for (int i = 1; i < F.length; i++) {
            F[i] = (int) ((((long) factorialBase + i) * F[i - 1]) % MOD);
        }

        int[] F_INV = new int[F.length];
        for (int i = 0; i < F.length; i++) {
            F_INV[i] = BigInteger.valueOf(F[i]).modInverse(BIG_MOD).intValue();
        }

        long total = 0;
        for (int A = 0; A < n; A++) {
            for (int B = A + 1; B < n; B++) {
                int extraSpace = m - 1 - (occupy - r[A] - r[B]);
                if (extraSpace < 0) continue;

                total += (((long) F[extraSpace + n - factorialBase])
                        * F_INV[extraSpace - factorialBase]) % MOD;
                total %= MOD;
            }
        }

        // now divide by n, n-1 and multiply by 2
        return BigInteger.valueOf(2 * total)
                .multiply(BigInteger.valueOf(n).modInverse(BIG_MOD))
                .multiply(BigInteger.valueOf(n - 1).modInverse(BIG_MOD))
                .mod(BIG_MOD)
                .intValue();
    }

    @Test
    public void testSimple() {
        System.out.println(solve(3, 6, new int[]{1, 1, 1}));
        System.out.println(solve(2, 5, new int[]{1, 2}));
        System.out.println(solve(3, 2000, new int[]{1, 2, 3}));
        System.out.println(solve(5, 22, new int[]{1, 2, 3, 4, 5}));
        System.out.println(solve(1, 10, new int[]{50}));
    }

    @Test
    public void random() throws IOException {
        Random r = new Random();
        int T = 100;
        StringBuilder sb = new StringBuilder();
        sb.append(T).append('\n');
        for (int i = 0; i < T; i++) {
            int n = r.nextInt(2000) + 1;
            int m = r.nextInt(1000000000) + 1;
            sb.append(n).append(" ").append(m).append('\n');
            for (int i1 = 0; i1 < n; i1++) {
                int r_i = r.nextInt(2000) + 1;
                sb.append(r_i).append('\n');
            }
        }
        Files.write(Paths.get("src/hc2017r1/beach-rand.in"), sb.toString().getBytes());
    }
}

