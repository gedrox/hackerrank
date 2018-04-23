package Horse;

import java.util.Arrays;
import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int t_i = 1; t_i <= T; t_i++) {

            sb.append("Case #").append(t_i).append(":");

            int D = sc.nextInt();
            int N = sc.nextInt();
            int[] K = new int[N];
            int[] S = new int[N];
            int I[] = new int[N];
            for (int i = 0; i < N; i++) {
                K[i] = sc.nextInt();
                S[i] = sc.nextInt();
                I[i] = i;
            }

            double time = Arrays.stream(I).mapToDouble(i -> (0d + D - K[i]) / S[i]).max().orElse(0);
            sb.append(D / time);
            
            sb.append('\n');
        }
        System.out.print(sb);

    }
}
