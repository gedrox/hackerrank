package shashankstrings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Solution {
    public static final int MOD = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int q = Integer.parseInt(bi.readLine());
        for (int q_i = 0; q_i < q; q_i++) {
            int n = Integer.parseInt(bi.readLine());
            String[] str = new String[n];
            for (int n_i = 0; n_i < n; n_i++) {
                str[n_i] = bi.readLine();
            }
            int finRes = solve(str);
            System.out.println(finRes);
        }
    }

    private static int solve(String[] str) {
        int[] startPos = new int[str.length + 1];

        char[] all = Arrays.stream(str).collect(Collectors.joining()).toCharArray();
        int len = all.length;

        int[] belongs = new int[len];
        int currPos = 0;
        for (int i = 0; i < str.length; i++) {
            for (int b_i = currPos; b_i < currPos + str[i].length(); b_i++) {
                belongs[b_i] = i;
            }
            currPos += str[i].length();
            startPos[i + 1] = currPos;
        }

        int[][] res = new int[len][];
        int[][] resStarts = new int[len][];
        int[][] resEnds = new int[len][];
        int[][] agg = new int[len][];

        for (int start = 0; start < len; start++) {
            res[start] = new int[len];
            resStarts[start] = new int[len];
            resEnds[start] = new int[len];
            agg[start] = new int[len];
        }

        for (int L = 1; L <= len; L++) {

            boolean atLeast3 = L >= 3;
            boolean atLeast2 = L >= 2;

            for (int start = 0; start <= len - L; start++) {
                int end = start + L - 1;
                res[start][end] = 0;

                int startBelongs = belongs[start];
                int endBelongs = belongs[end];

                boolean extraSpaceStart = (start + 1) != startPos[startBelongs + 1];
                boolean extraSpaceEnd = end != startPos[endBelongs];

                if (all[start] == all[end]) {

                    if (endBelongs - startBelongs <= 1) {
                        res[start][end] = 1;
                    }

                    if (atLeast3) {
                        res[start][end] = (int) (((long) res[start][end] + agg[start + 1][end - 1]) % MOD);
                    }

                    if (startBelongs < endBelongs) {
                        if (extraSpaceStart && startPos[startBelongs + 1] <= end - 1) {
                            res[start][end] = (int) (((long) res[start][end] + agg[startPos[startBelongs + 1]][end - 1]) % MOD);
                        }
                        if (extraSpaceEnd && start + 1 <= startPos[endBelongs] - 1) {
                            res[start][end] = (int) (((long) res[start][end] + agg[start + 1][startPos[endBelongs] - 1]) % MOD);
                        }
                        if (extraSpaceStart && extraSpaceEnd && startPos[startBelongs + 1] <= startPos[endBelongs] - 1) {
                            res[start][end] = (int) (((long) res[start][end] + agg[startPos[startBelongs + 1]][startPos[endBelongs] - 1]) % MOD);
                        }
                    }
                }

                agg[start][end] = res[start][end];
                resStarts[start][end] = res[start][end];
                resEnds[start][end] = res[start][end];

                if (atLeast2) {
                    if (atLeast3 && extraSpaceStart && extraSpaceEnd) {
                        agg[start][end] = (int) (((long) agg[start][end] + agg[start + 1][end - 1]) % MOD);
                    }
                    if (extraSpaceEnd) {
                        agg[start][end] = (int) (((long) agg[start][end] + resStarts[start][end - 1]) % MOD);
                    }
                    if (extraSpaceStart) {
                        agg[start][end] = (int) (((long) agg[start][end] + resEnds[start + 1][end]) % MOD);
                    }
                    if (extraSpaceEnd) {
                        resStarts[start][end] = (int) (((long) resStarts[start][end] + resStarts[start][end - 1]) % MOD);
                    }
                    if (extraSpaceStart) {
                        resEnds[start][end] = (int) (((long) resEnds[start][end] + resEnds[start + 1][end]) % MOD);
                    }
                }
            }
        }

        return agg[0][len - 1];
    }

}
