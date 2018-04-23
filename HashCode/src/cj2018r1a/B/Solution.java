package cj2018r1a.B;

import org.junit.Test;
import java.util.Random;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Solution {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static int R;
    private static int B;
    private static int C;
    private static int[] M;
    private static int[] S;
    private static int[] P;

    static int readInt() throws IOException {
        return (int) readLong();
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

    public static void main(String[] args) throws Exception {
        StringBuilder sb = new StringBuilder();
        int T = readInt();
        for (int t_i = 1; t_i <= T; t_i++) {
            R = readInt();
            B = readInt();
            C = readInt();

            init();

            for (int i = 0; i < C; i++) {
                M[i] = readInt();
                S[i] = readInt();
                P[i] = readInt();
            }
            
            long answer = solve();
            
            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(answer)
                    .append('\n');
        }
        System.out.print(sb);
    }

    @Test
    public void test1() {
        R = 2;
        B = 2;
        C = 2;
        M = new int[] {1, 1};
        S = new int[] {2, 1};
        P = new int[] {3, 2};

        System.out.println(solve());
    }

    @Test
    public void test2() {
        //2 2 2
        //1 2 3
        //2 1 2
        R = 2;
        B = 2;
        C = 2;
        M = new int[] {1, 2};
        S = new int[] {2, 1};
        P = new int[] {3, 2};

        System.out.println(solve());
    }

    @Test
    public void test3() {
        //2 3 3
        //2 1 5
        //2 4 2
        //2 2 4
        //2 5 1
        R = 3;
        B = 4;
        C = 5;
        M = new int[] {2,2,2,2,2};
        S = new int[] {3,1,4,2,5};
        P = new int[] {3,5,2,4,1};

        System.out.println(solve());
    }

    @Test
    public void testBig() {
        Random r = new Random(0);
        R = 1000;
        C = 1000;
        B = (int) 1e9;
        init();
        for (int i = 0; i < C; i++) {
            M[i] = r.nextInt((int) 1e9)+1;
            S[i] = r.nextInt((int) 1e9)+1;
            P[i] = r.nextInt((int) 1e9)+1;
        }


        for (C = 10; C <= 1000; C += 100)
        System.out.println(solve());
    }

    private static void init() {
        M = new int[C];
        S = new int[C];
        P = new int[C];
    }

    private static long solve() {
        
        long low = 0;
        long high = (long) 1.1e18;
        long time;
        long[] counts = new long[C];
        
        while (low < high) {
            
            time = (low + high) / 2;

            for (int i = 0; i < C; i++) {
                counts[i] = Math.max(0, Math.min(M[i], (time - P[i]) / S[i]));
            }
            
            long cnt = 0;
            if (R < C) {
                Arrays.sort(counts);
                for (int i = 0; i < R; i++) {
                    cnt += counts[C - 1 - i];
                }
            } else {
                for (long count : counts) {
                    cnt += count;
                }
            }
            
            if (cnt < B) {
                low = time + 1;
            } else {
                high = time;
            }
        }
        
        return low;
    }
}
