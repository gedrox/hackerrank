package cj18r2.B;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution2 {

    static int ch = 0;
    public static final int SIZE = 501;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int R, B;
    static int MAX = 33;

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

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        int T = readInt();

        for (int t_i = 1; t_i <= T; t_i++) {
            readInput();
            int answer = solve2();
            sb.append("Case #").append(t_i).append(": ").append(answer).append('\n');
        }

        System.out.print(sb.toString());
    }

    static void readInput() throws IOException {
        R = readInt();
        B = readInt();
    }

    static int solve2() {

        int[] rmd = getInts();
        rmd[SIZE * R + B] = 0;

        int takeB, takeR;
        
        int max = 0;
        
        for (int i = 0; i < MAX; i++) {
            
            takeR = i;
            takeB = 0;

            int[] rmd2 = getInts();

            for (int j = 1; (i + 1) * j * (i + j - 1) / 2 <= R + B; j++) {
                for (int r = 0; r < SIZE * SIZE; r++) 
                {
                    if (rmd[r] < 0) continue;
                    
                    int remr = r / SIZE;
                    int remb = r % SIZE;
                    
                    if (remr >= takeR && remb >= takeB) {
                        int x = SIZE * (remr - takeR) + (remb - takeB);
                        
                        if (rmd[r] + j > rmd2[x]) {
                            rmd2[x] = rmd[r] + j;
                            max = Math.max(max, rmd2[x]);
                        }
                    }
                }
                
                takeR += i;
                takeB += j;
            }
            
            rmd = rmd2;
        }

        return max - 1;
    }

    private static int[] getInts() {
        int[] rmd = new int[SIZE * SIZE];
        for (int i = 0; i < rmd.length; i++) {
            rmd[i] = Integer.MIN_VALUE;
        }
        return rmd;
    }

}
