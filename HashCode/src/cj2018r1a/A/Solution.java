package cj2018r1a.A;

import org.junit.Test;

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t_i = 1; t_i <= T; t_i++) {
            int R = sc.nextInt();
            int C = sc.nextInt();
            int H = sc.nextInt();
            int V = sc.nextInt();
            
            int[][] w = new int[R][C];
            for (int i = 0; i < R; i++) {
                String line = sc.next();
                for (int j = 0; j < line.length(); j++) {
                    if (line.charAt(j) == '@') {
                        w[i][j] = 1;
                    }
                }
            }

            int answer = solve(R, C, H, V, w);

            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(answer == -1 ? "IMPOSSIBLE" : "POSSIBLE")
                    .append('\n');
        }
        System.out.print(sb);
    }

    private static int solve(int R, int C, int H, int V, int[][] w) {

        long sum = sumChips(w);
        
        if (sum % ((V + 1) * (H + 1)) != 0 || V >= C || H >= R) {
            return -1;
        }
        
        if (sum == 0) return 1;

        int[] hSplit = new int[H + 2];
        hSplit[0] = 0;
        int h_i = 1;

        int split = (int) (sum / (H + 1));
        int curr = 0;
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                curr += w[i][j];
            }
            if (curr == split) {
                hSplit[h_i++] = i + 1;
                curr = 0;
            } else if (curr > split) {
                return -1;
            }
        }

        int[] vSplit = new int[V + 2];
        vSplit[0] = 0;
        int v_i = 1;
        split = (int) (sum / (V + 1));
        curr = 0;
        for (int j = 0; j < C; j++) {
            for (int i = 0; i < R; i++) {
                curr += w[i][j];
            }
            if (curr == split) {
                vSplit[v_i++] = j + 1;
                curr = 0;
            } else if (curr > split) {
                return -1;
            }
        }
        
        // now validate
        split = (int) (sum / (V + 1) / (H + 1));
        for (int i = 0; i <= H; i++) {
            for (int j = 0; j <= V; j++) {
                curr = 0;
                for (int r = hSplit[i]; r < hSplit[i + 1]; r++) {
                    for (int c = vSplit[j]; c < vSplit[j + 1]; c++) {
                        curr += w[r][c];
                    }
                }
                if (curr != split) return -1;
            }
        }

        return 1;
    }

    private static long sumChips(int[][] w) {
        long sum = 0;
        for (int[] ints : w) {
            for (int anInt : ints) {
                sum += anInt;
            }
        }
        return sum;
    }
    
//    @Test
//    public void test1() {
//        int res = solve(3, 6, 1, 1, new int[][]{{0, 1, 1, 0, 0, 1}, {0, 0, 0, 0, 0, 1}, {1, 0, 1, 0, 1, 1}});
//        System.out.println(res);
//    }
//
//    @Test
//    public void test2() {
//        int res = solve(4, 3, 1, 1, new int[][]{{1,1,1}, {1,0,1}, {1,0,1}, {1,1,1}});
//        System.out.println(res);
//    }
//
//    @Test
//    public void test3() {
//        int res = solve(4, 5, 1, 1, new int[4][5]);
//        System.out.println(res);
//    }
//    
    @Test
    public void big() {
        int R = 100;
        int C = 50;
        int H = R - 1;
        int V = C - 1;
        int w[][] = new int[R][C];

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                w[i][j] = 1;
            }
        }

        int solve = solve(R, C, H, V, w);
        System.out.println(solve);
    }
}
