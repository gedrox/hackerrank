package w37;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.StringJoiner;

public class SuperiorCharacters {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static int[] n;

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

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        int T = readInt();
        for (int i = 0; i < T; i++) {
            n = readIntArray(26);
            long answer = solve();
            sb.append(answer).append('\n');
        }
        System.out.print(sb.toString());
    }

    static long maximumSuperiorCharacters(int[] freq) {
        long[] f = new long[freq.length + 1];
        int f_last = 0;
        for (int cnt : freq) if (cnt > 0) f[++f_last] = f[f_last - 1] + cnt;
        f[0] = 1;
        int mid = Arrays.binarySearch(f, 0, f_last + 1, f[f_last] / 2 + 1);
        if (mid < 0) mid = - mid - 2;
        return Math.min((f[f_last] - 1) / 2, f[f_last] - f[mid + 1] + f[mid] - 1);
    }

    static String input = ""; 
    static String expect = ""; 
    
    private static long solve() {

        input += Arrays.toString(n).replace(",", "").replace("[", "").replace("]", "") + "\n";

        if (true) {
            long val = maximumSuperiorCharacters(n);
            expect += val + "\n";
            return val;
        }

        long sum = 0;
        for (int i : n) sum += i;
        if (sum < 3) return 0;
        long max = (sum - 1) / 2;
        int mid = 0;
        long sum2mid = -1;
        int min = -1;

        for (; mid < n.length; mid++) {
            if (min == -1 && n[mid] > 0) {
                min = mid;
            }
            if (sum2mid + n[mid] <= max) {
                sum2mid += n[mid];
            } else {
                break;
            }
        }

        if (min == mid) {
            return sum - n[mid];
        }

        long alpha = max - sum2mid;
        long beta = n[mid] - alpha;
        if (sum % 2 == 0) {
            beta--;
        }

        if (beta <= sum2mid) {
            return max;
        } else {
            return max - (beta - sum2mid);
        }

//        System.err.println("Sum is " + sum);
//        System.err.println("Middle is " + mid);
//        System.err.println("Sum2mid " + sum2mid);
//        System.err.println("max " + max);
//        System.err.println("alpha " + alpha);
//        System.err.println("beta " + beta);
//
//        return max;
    }

    @Test
    public void test() {
        n = new int[26];
        n[4] = 2;
        n[10] = 1;
        n[15] = 1;
        Assert.assertEquals(1, solve());

        n = new int[26];
        Assert.assertEquals(0, solve());

        n[10] = (int) 1e9;
        Assert.assertEquals(0, solve());

        n[15] = 10;
        Assert.assertEquals(10, solve());

        n[10] = 10;
        n[15] = (int) 1e9;
        Assert.assertEquals(9, solve());

        String[] vals = {"1 2 2 3 4 0 3 4 4 1 3 1 4 4 1 0 0 0 0 0 4 2 3 2 2 1",
                "1 1 3 3 1 1 4 4 3 1 3 3 3 0 1 2 0 4 2 1 3 0 3 1 1 1",
                "3 3 0 2 2 2 4 1 2 1 1 1 3 3 0 0 3 2 2 4 1 4 4 1 2 1",
                "2 1 4 1 0 2 0 3 1 2 0 3 1 1 2 0 1 4 2 3 2 3 2 0 2 1"
        };
        int[] expected = {25, 24, 25, 21};

        for (int i = 0; i < vals.length; i++) {
            n = Arrays.stream(vals[i].split(" ")).mapToInt(Integer::parseInt).toArray();
            Assert.assertEquals(expected[i], solve());
        }

        n = new int[26];
        n[0] = 101;
        Random r = new Random(0);
        for (int i = 0; i < 101; i++) {
            n[r.nextInt(25) + 1]++;
            Assert.assertEquals(Math.min(n[0] - 1, i + 1), solve());
        }

        n = new int[26];
        for (int i = 0; i < n.length; i++) {
            n[i] = (int) 1e9;
        }
        Assert.assertEquals(12999999999L, solve());

        n = new int[26];
        n[0] = (int) 10;
        n[13] = (int) 112;
        n[25] = (int) 100;

        Assert.assertEquals(109, solve());

        System.out.println(input.length() - input.replaceAll("\n", "").length());
        System.out.println(input);
        System.out.println();
        System.out.println(expect);
    }
}
