package Senators;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int t_i = 1; t_i <= T; t_i++) {
            
            sb.append("Case #").append(t_i).append(":");
            
            int N = sc.nextInt();
            int[] P = new int[N];
            Integer[] I = new Integer[N];
            char[] C = new char[N];
            for (int i = 0; i < N; i++) {
                P[i] = sc.nextInt();
                I[i] = i;
                C[i] = (char) ('A' + i);
            }

            Arrays.sort(I, Comparator.comparing(i -> -P[i]));
            
            // start by getting 2 with equal count
            while (P[I[0]] > P[I[1]]) {
                sb.append(" ").append(C[I[0]]);
                P[I[0]]--;
            }
            
            // remove all but top 2
            for (int i = 2; i < N; i++) {
                for (int j = 0; j < P[I[i]]; j++) {
                    sb.append(" ").append(C[I[i]]);
                }
            }

            for (int i = 0; i < P[I[1]]; i++) {
                sb.append(" ").append(C[I[0]]).append(C[I[1]]);
            }
            
            sb.append('\n');
        }
        System.out.print(sb);
    }
}
