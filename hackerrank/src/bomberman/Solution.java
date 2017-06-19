package bomberman;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int R = sc.nextInt();
        int C = sc.nextInt();
        int N = sc.nextInt();

        if (N % 2 == 0) {
            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    System.out.print('O');
                }
                System.out.println();
            }
            return;
        }

        if (N > 8) N = N % 4 + 4;
        N = N / 2;

        int[][] b = new int[R][C];
        for (int r = 0; r < R; r++) {
            b[r] = new int[C];
            String s = sc.next();
            for (int c = 0; c < C; c++) {
                b[r][c] = s.charAt(c) == '.' ? 0 : 1;
            }
        }

        int curr = 1;
        for (int t = 0; t < N; t++) {
            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    if (b[r][c] == curr) {
                        if (r > 0 && b[r-1][c] == 1-curr) b[r-1][c] = -1-curr;
                        if (c > 0 && b[r][c-1]==1-curr) b[r][c-1] = -1-curr;
                        if (r < R-1 && b[r+1][c] == 1-curr) b[r+1][c] = -1-curr;
                        if (c < C-1 && b[r][c+1] == 1-curr) b[r][c+1] = -1-curr;
                    }
                }
            }
            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    if (b[r][c] < 0) b[r][c] = -1-b[r][c];
                }
            }
            curr = 1 - curr;
        }

        for (int r = 0; r < R; r++) {
            for (int c = 0; c < C; c++) {
                System.out.print(b[r][c] == curr ? 'O' : '.');
            }
            System.out.println();
        }
    }
}
