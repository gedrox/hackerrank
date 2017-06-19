package decibinary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class D2 {

    static int[] POW2 = new int[31];

    static {
        POW2[0] = 1;
        for (int i = 1; i < POW2.length; i++) {
            POW2[i] = 2 * POW2[i - 1];
        }
    }

    public static void main(String[] args) throws IOException {

        long t0 = System.currentTimeMillis();

        int SIZE = 285113;
        long[][] tc = new long[SIZE][];
        long[] t = new long[SIZE];

        tc[0] = new long[]{1};
        t[0] = 1;

        int twoPow = 0;

        int SPL_I = 0;
        int TOTAL = 4429584;
        long SUM = 1;

        long[] split = new long[TOTAL];
        int[] LEN = new int[TOTAL];
        int[] DIG = new int[TOTAL];

        split[SPL_I] = SUM;

        SPL_I++;

        for (int i = 1; i < SIZE; i++) {
            // max count of digits
            if (i == POW2[twoPow]) {
                twoPow++;
            }

            int minLen = ("" + i).length();
            int maxLen = twoPow;
            tc[i] = new long[maxLen + 1];

            for (int len = minLen; len <= maxLen; len++) {
                int maxDig = Math.min(9, i / POW2[len - 1]);
                tc[i][len] = tc[i][len - 1];
                int ded = i;
                for (int dig = 0; dig < maxDig; dig++) { // 0 is "1"
                    ded -= POW2[len - 1];
                    long add = tc[ded][Math.min(len - 1, tc[ded].length - 1)];
                    if (add > 0) {
                        tc[i][len] += add;
                        SUM += add;

                        split[SPL_I] = SUM;
                        LEN[SPL_I] = len;
                        DIG[SPL_I] = dig + 1;
                        SPL_I++;
                    }
                }
            }

            t[i] = SUM;
        }

        if (SPL_I != TOTAL) throw new RuntimeException("Something wrong");

        System.err.println("done in " + (System.currentTimeMillis() - t0));

        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(sc.readLine());

        StringBuilder out = new StringBuilder();

        for (int q_i = 0; q_i < q; q_i++) {
            long pos = Long.parseLong(sc.readLine());

            if (pos == 1) {
                out.append("0").append('\n');
                continue;
            }

            int dec = Arrays.binarySearch(t, pos);
            if (dec < 0) dec = -dec - 1;

            int last = -1;
            while (true) {
                int i1 = Arrays.binarySearch(split, pos);
                if (i1 < 0) i1 = -i1 - 1;

                for (int i = 0; i < last - 1 - LEN[i1]; i++) {
                    out.append("0");
                }
                out.append(DIG[i1]);
                last = LEN[i1];

                dec -= DIG[i1] * POW2[LEN[i1] - 1];
                if (dec == 0) break;

                pos -= split[i1 - 1];
                pos += t[dec - 1];
            }

            for (int i = 0; i < last - 1; i++) {
                out.append("0");
            }

            out.append('\n');
        }

        System.out.print(out);
    }
}
