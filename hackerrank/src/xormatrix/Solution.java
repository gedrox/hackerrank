package xormatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Solution {

    public static void main(String[] args) throws IOException {

        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] split = bi.readLine().split(" ");
        int N = Integer.parseInt(split[0]);
        long M = Long.parseLong(split[1]) - 1;

        int[] line = new int[N];

        split = bi.readLine().split(" ");
        for (int i = 0; i < N; i++) {
            line[i] = Integer.parseInt(split[i]);
        }

        String out = solve(N, M, line);
        System.out.println(out);
    }

    private static String solve(int n, long m, int[] line) {
        long pow2 = 576460752303423488L;

        while (m > 0) {
            while (m < pow2) {
                pow2 /= 2;
            }

            m -= pow2;

            int[] newLine = new int[n];
            for (int i = 0; i < n; i++) {
                newLine[i] = line[i] ^ line[(int) ((i + pow2) % n)];
            }

            line = newLine;
        }

        return Arrays.stream(line).mapToObj(String::valueOf).collect(Collectors.joining(" "));
    }

}
