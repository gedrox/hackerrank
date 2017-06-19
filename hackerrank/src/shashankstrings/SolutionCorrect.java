package shashankstrings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SolutionCorrect {
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

        Integer[][] res = new Integer[len][];

        for (int start = 0; start < len; start++) {
            res[start] = new Integer[len];
        }

        int finRes = 0;

        for (int L = 1; L <= len; L++) {
            for (int start = 0; start <= len - L; start++) {
                int end = start + L - 1;
                res[start][end] = 0;
                if (all[start] == all[end]) {
                    int startBelongs = belongs[start];
                    int endBelongs = belongs[end];
                    if (endBelongs - startBelongs <= 1) {
                        res[start][end] = 1;
                    }

                    int maxSubStartBelongs = Math.min(startBelongs + 1, endBelongs);
                    int maxSubStartPos = Math.min(startPos[maxSubStartBelongs + 1] - 1, end - 1);

                    int minSubEndBelongs = Math.max(endBelongs - 1, startBelongs);
                    int minSubEndPos = Math.max(startPos[minSubEndBelongs], start + 1);

                    for (int subStart = start + 1; subStart <= maxSubStartPos; subStart++) {
                        for (int subEnd = Math.max(minSubEndPos, subStart); subEnd < end; subEnd++) {
                            if (res[subStart][subEnd] > 0) {
                                res[start][end] = (int) (((long) res[start][end] + res[subStart][subEnd]) % MOD);
                            }
                        }
                    }

                    if (startBelongs == 0 && endBelongs == str.length - 1) {
                        finRes = (int) (((long) finRes + res[start][end]) % MOD);
                    }
                }
            }
        }

        return finRes;
    }
}
