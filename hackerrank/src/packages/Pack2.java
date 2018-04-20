package packages;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Pack2 {


    /*
     * Complete the function below.
     */
    static int maximumPackages(int[] S, int[] K, int[] R, int[] C) {
        // Return the maximum number of packages that can be put in the containers.
        Integer[] s_i = new Integer[S.length];
        Integer[] r_i = new Integer[R.length];
        for (int i = 0; i < s_i.length; i++) s_i[i] = i;
        for (int i = 0; i < r_i.length; i++) r_i[i] = i;

        Arrays.sort(s_i, Comparator.comparing(i -> S[i]));
        Arrays.sort(r_i, Comparator.comparing(i -> R[i]));

        int a = 0;
        int si = 0;
        int ri = 0;
        int ki = 0;
        int ci = 0;
        while (true) {
            if (S[s_i[si]] < Math.sqrt(2) * R[r_i[ri]]) {
                a++;
                ki++;
                ci++;
            } else {
                ri++;
                ci = 0;
                if (ri == r_i.length) break;
            }
            
            if (ci == C[r_i[ri]]) {
                ri++;
                ci = 0;
            }
            if (ki == K[s_i[si]]) {
                si++;
                ki = 0;
            }
            
            if (si == s_i.length) break;
            if (ri == r_i.length) break;
        }
        
        return a;
    }


    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        int n = scan.nextInt();
        int m = scan.nextInt();

        int[] S = new int[n];

        for (int SItr = 0; SItr < n; SItr++) {
            S[SItr] = scan.nextInt();

        }

        int[] K = new int[n];

        for (int KItr = 0; KItr < n; KItr++) {
            K[KItr] = scan.nextInt();
        }

        int[] R = new int[m];

        for (int RItr = 0; RItr < m; RItr++) {
            R[RItr] = scan.nextInt();
        }

        int[] C = new int[m];

        for (int CItr = 0; CItr < m; CItr++) {
            C[CItr] = scan.nextInt();

        }

        int result = maximumPackages(S, K, R, C);

        System.out.println(result);
    }
}
