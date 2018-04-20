package hackerrank;

import java.util.Arrays;

public class BinaryNoise {
    public static void main(String[] args) {
        int n = 5;
        char[] out = new char[n];
        Arrays.fill(out, '0');
        for (int i = 1; i <= 1 << n; i++) {
            System.out.println(out);
            int pos = Integer.numberOfTrailingZeros(i) % n;
            out[pos] = (char) ('1' - (i >> pos + 1) % 2);
        }
    }
}
