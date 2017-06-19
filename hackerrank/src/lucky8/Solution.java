package lucky8;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Scanner;

public class Solution {

    static int MOD = 1000000007;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.nextInt();
        String s = sc.next();

        long total = solve(s);

        System.out.println(total % MOD);
    }

    private static long solve(String s) {
        int dig[] = new int[10];

        long total2 = 0;
        long total2_mod4 = 0;
        long total3 = 0;

        for (int i = s.length() - 1; i >= 0; i--) {
            int d = s.charAt(i) - '0';

            total3 = 2 * total3;

            // 3 digit
            if (d % 2 == 0) {
                total3 = total3 + total2;
            } else {
                total3 = total3 + total2_mod4;
            }

            for (int j = 0; j < 10; j += 2) {
                int X = 10 * d + j;
                if (X % 8 == 0) {
                    total2 += dig[j];
                } else if (X % 8 == 4) {
                    total2_mod4 += dig[j];
                }
            }

            dig[d]++;

            total2 = total2 % MOD;
            total2_mod4 = total2_mod4 % MOD;
            total3 = total3 % MOD;
        }

        return (dig[0] + dig[8] + total2 + total3) % MOD;
    }

    @Test
    public void test() {
        int N = 20;
        System.out.println(N);
        while (true) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < N; i++) {
                sb.append((int) Math.floor(Math.random() * 10));
            }

            System.out.println(sb);
            int trivial = trivial(sb.toString());
            System.out.println("Trivial: " + trivial);
            long solve = solve(sb.toString());
            System.out.println("Fancy: " + solve);
            if (trivial != solve) {
                break;
            }
        }
    }

    @Test
    public void big() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200000; i++) {
            sb.append((int) Math.floor(Math.random() * 10));
        }
        System.out.println(solve(sb.toString()));
    }

//    @Test
    public int trivial(String s) {
//        String s = "7925976378739414102528658214910717421053375888825054289109636535844654273199381089251041091874567102923762046831268627347310789471083783848964111881010654645770961481099390249102863779757841314879214288295739283";
//        String s = "7814";
        // 75072
        int n = s.length();
        long c = 0;
        for (int i1 = 0; i1 < n; i1++) {
            char c1 = s.charAt(i1);
            int D1 = c1 - '0';
            if (D1 % 8 == 0) {
//                System.out.println(c1);
                c++;
                c = c % MOD;
            }

            for (int i2 = i1 + 1; i2 < n; i2++) {
                char c2 = s.charAt(i2);
                int D2 = c2 - '0';
                D2 = (10 * D1 + D2);
                if (D2 % 8 == 0) {
                    c++;
                    c = c % MOD;
//                    System.out.println("" + c1 + c2);
                }

                for (int i3 = i2 + 1; i3 < n; i3++) {
                    char c3 = s.charAt(i3);
                    int D3 = c3 - '0';
                    D3 = 10 * D2 + D3;
                    if (D3 % 8 == 0) {
                        c += BigInteger.valueOf(2).modPow(BigInteger.valueOf(i1), BigInteger.valueOf(MOD)).intValue();
                        c = c % MOD;
//                        System.out.println("" + c1 + c2 + c3);
                    }
                }
            }
        }

        //142850986

        return (int) c;
    }
}
