package transformtopalindrome;

import java.util.Scanner;

public class TransformToPalindrome {

    private static int[] col;

    static int getCol(int i) {
        if (col[i] == col[col[i]]) return col[i];
        return (col[i] = getCol(col[i]));
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int k = in.nextInt();
        int m = in.nextInt();

        col = new int[n];
//        int[] colS = new int[n];
        for (int i = 0; i < col.length; i++) {
            col[i] = i;
//            colS[i] = 1;
        }
        
        for(int a0 = 0; a0 < k; a0++){
            int x = in.nextInt() - 1;
            int y = in.nextInt() - 1;
            col[getCol(y)] = getCol(x);
//            if (colS[colX] < colS[colY]) {
//                colS[colY] += colS[colX];
//                colS[colX] = 0;
//                col[x] = col[y];
//            } else {
//                colS[colX] += colS[colY];
//                colS[colY] = 0;
//                col[y] = col[x];
//            }
        }

        int[] a = new int[m];
        for(int a_i=0; a_i < m; a_i++){
            a[a_i] = getCol(in.nextInt() - 1);
        }

        int[][] L = new int[m][m];

        for (int len = 1; len <= L.length; len++) {
            for (int start = 0; start <= L.length - len; start++) {
                int end = start + len - 1;
                if (len == 1) {
                    L[start][end] = 1;
                } else if (a[start] == a[end]) {
                    L[start][end] = 2 + L[start + 1][end - 1];
                } else {
                    L[start][end] = Math.max(L[start + 1][end], L[start][end - 1]);   
                }
            }
        }

        System.out.println(L[0][m - 1]);
    }
}
