package troublesort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Solution {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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
            int N = readInt();
            int[] V1 = new int[(N + 1) / 2];
            int[] V2 = new int[N / 2];
            for (int i = 0; i < N; i++) {
                int V_i = readInt();
                if (i % 2 == 0) {
                    V1[i / 2] = V_i;
                } else {
                    V2[i / 2] = V_i;
                }
            }

            Arrays.sort(V1);
            Arrays.sort(V2);

            int prev = Integer.MIN_VALUE;
            
            int answer = -1;
            for (int i = 0; i < N; i++) {
                int val = (i % 2 == 0) ? V1[i / 2] : V2[i / 2];
                if (val < prev) {
                    answer = i - 1;
                    break;
                }
                prev = val;
            }

            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(answer == -1 ? "OK" : answer)
                    .append('\n');
        }
        System.out.print(sb);
    }
}
