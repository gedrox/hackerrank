package cuttingthestring;

import org.junit.Test;

import java.util.Scanner;

public class CuttingTheString {
    
    static long trivial(String s) {
        int a = 0;
        int n = s.length();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                String rem = s.substring(0, i) + s.substring(j);
                String key = s.substring(i, j);
                
                for (int k = 0; k <= rem.length(); k++) {
                    String res = rem.substring(0, k) + key + rem.substring(k);
                    if (res.equals(s)) a++;
                }
            }
        }
        return a;
    }
    
//    @Test
    public void test() {
        String s = "aaaaa";
        System.out.println(trivial(s));
        System.out.println(countCutsAndInserts(s));
    }
    
    static long countCutsAndInserts(String s) {
        int n = s.length();
        int[][] replen = new int[n + 1][n + 1];

        for (int j = n - 1; j >= 0; j--) {
            for (int i = 0; i < j; i++) {
                replen[i][j] = s.charAt(i) == s.charAt(j) ? replen[i + 1][j + 1] + 1 : 0;
            }
        }

        int[][] reps = new int[n][n];

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < j; i++) {
                int L = j - i;
                if (replen[i][j] >= L) {
                    int k = 1;
                    while (i - k * L >= 0 && j - k * L >= 0 && reps[i - k * L][j - k * L] == -1) {
                        k++;
                    }
                    if (i - k * L >= 0 && j - k * L >= 0 && reps[i - k * L][j - k * L] > 0) {
                        reps[i - k * L][j - k * L]++;
                        reps[i][j] = -1;
                    } else {
                        reps[i][j] = 1;
                    }
                }
            }
        }
        
        long answer = ((long) n + 1) * n / 2;

        for (int len = 1; len <= n / 2; len++) {
            for (int i = 0; i < n - len; i++) {
                int j = i + len;
                if (reps[i][j] != 0) {
                    for (int block = 2; true; block++) {
                        int blockLen = len * block;
                        if (i + blockLen >= n || reps[i][i + blockLen] == 0) break;
                        reps[i][i + blockLen] = 0;
                    }
                    if (reps[i][j] > 0) {
                        answer += posCount[reps[i][j] + 1];
                    }
                }
            }
        }

        return answer;
    }
    
    static long[] posCount = new long[6001];
    static {
        posCount[0] = 0;
        for (int i = 1; i <= 6000; i++) {
            posCount[i] = posCount[i - 1] + i * (i - 1);
        }
    }
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        //  Return the number of successful ways to cut and insert a substring.
        String s = in.next();
        long result = countCutsAndInserts(s);
        System.out.println(result);
    }
}
