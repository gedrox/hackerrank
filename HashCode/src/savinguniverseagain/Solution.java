package savinguniverseagain;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int T = sc.nextInt();
        for (int t_i = 1; t_i <= T; t_i++) {
            int D = sc.nextInt();
            String P = sc.next();

            int n = P.length();
            int[] b = new int[n];
            int c = 1;
            int total = 0;
            for (int i = 0; i < n; i++) {
                if (P.charAt(i) == 'C') {
                    c *= 2;
                } else {
                    b[i] = c;
                    total += c;
                }
            }

            int answer = 0;
            whileLoop:
            while (total > D) {
                for (int i = n - 1; i > 0; i--) {
                    // can switch
                    if (b[i] > 0 && b[i - 1] == 0) {
                        b[i - 1] = b[i] / 2;
                        total -= b[i - 1];
                        b[i] = 0;
                        answer++;

                        continue whileLoop;
                    }
                }

                answer = -1;
                break;
            }

            sb.append("Case #")
                    .append(t_i)
                    .append(": ")
                    .append(answer == -1 ? "IMPOSSIBLE" : answer)
                    .append('\n');
        }
        System.out.print(sb);
    }
}
