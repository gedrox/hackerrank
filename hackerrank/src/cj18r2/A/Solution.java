package cj18r2.A;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int C;
    static int[] B;

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

        for (int t_i = 1; t_i <= T; t_i++) {
            readInput();
            String answer = solve();
            sb.append("Case #").append(t_i).append(": ").append(answer).append('\n');
        }

        System.out.print(sb.toString());
    }

    static void readInput() throws IOException {
        C = readInt();
        B = readIntArray(C);
    }

    static String solve() {
        
        StringBuilder sb = new StringBuilder();
        
        if (B[0] == 0 || B[C - 1] == 0) return "IMPOSSIBLE";
        
        int[] target = new int[C];
        int slot = 0;
        int[] rem = B.clone();
        int depth = 1;

        for (int i = 0; i < C; i++) {
            while (rem[slot] == 0) slot++;
            rem[slot]--;
            target[i] = slot;
            depth = Math.max(depth, Math.abs(i - slot) + 1);
        }
        
        char[][] out = new char[depth][C];
        for (int i = 0; i < target.length; i++) {
            if (target[i] < i) {
                for (int j = 0; j < (i - target[i]); j++) {
                    out[j][i - j] = '/';
                }
            }
            if (target[i] > i) {
                for (int j = 0; j < (target[i] - i); j++) {
                    out[j][i + j] = '\\';
                }
            }
        }

        sb.append(depth);
        for (char[] chars : out) {
            sb.append('\n');
            for (char aChar : chars) {
                sb.append(aChar == 0 ? '.' : aChar);
            }
        }
        
        return sb.toString();
    }
}
