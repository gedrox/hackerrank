package codechef.goodpref;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        for (int t_i = 0; t_i < T; t_i++) {
            char[] s = sc.next().toCharArray();
            int n = sc.nextInt();
            
            int curMod = 0;
            int[] mod = new int[s.length];
            for (int i = 0; i < s.length; i++) {
                curMod += s[i] == 'a' ? 1 : -1;
                mod[i] = curMod;
            }

            long answer = 0;
            
            for (int i = 0; i < s.length; i++) {
                if (mod[i] > 0 || curMod > 0) {
                    if (mod[i] > 0 && curMod >= 0) {
                        answer += n;
                    } else if (mod[i] <= 0) {
                        int dirsa = Math.min(n, -mod[i] / curMod + 1);
                        answer += n - dirsa;
                    } else {
                        int labi = Math.min(n, (mod[i] - 1) / -curMod + 1);
                        answer += labi;
                    }
                }
            }

            System.out.println(answer);
        }
    }
}
