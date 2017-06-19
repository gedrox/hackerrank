package numericString;

import org.junit.Test;

import java.util.Scanner;

public class NumericString {
    
//    @Test
//    public void test() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < 300000; i++) {
//            sb.append('9');
//        }
//        System.out.println(getMagicNumber(sb.toString(), 100, 10, 997));
//    }
    
    static int getMagicNumber(String s, int k, int b, int m) {
        int len = s.length();

        int[] a = new int[len];
        for (int i = 0; i < len; i++) {
            a[i] = s.charAt(i) - '0';
        }

        long mod = 0;
        long b_k = 1;
        int sum = 0;
        for (int i = k - 1; i >= 0; i--) {
            mod = (mod + a[i] * b_k) % m;
            b_k = (b_k * b) % m;
        }

        sum += mod;

        for (int i = k; i < len; i++) {
            mod = (mod * b + a[i] - a[i - k] * b_k) % m;
            if (mod < 0) mod += m;
            sum += mod;
        }

        return sum;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        int k = in.nextInt();
        int b = in.nextInt();
        int m = in.nextInt();
        System.out.println(getMagicNumber(s, k, b, m));
    }
}
