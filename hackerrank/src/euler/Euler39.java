package euler;

import java.io.*;
import java.util.*;

public class Euler39 {
    
        static int MAX = 5000000;

        public static void main(String[] args) {
            HashSet<String>[] t = new HashSet[MAX/2 + 1];
            double sqrt = Math.sqrt(MAX / 2);
            for (int m = 2; m <= sqrt; m++) {
                for (int n = 1; n < m; n++) {
                    int pos = 2 * m * (m + n);
                    if (pos > MAX) break;
                    int cnt = 1;
                    int a1 = m*m-n*n;
                    int b1 = 2*m*n;
                    int a = Math.max(a1, b1);
                    int b = Math.min(a1, b1);
                    while (cnt * pos <= MAX) {
                        if (t[cnt*pos/2] == null) t[cnt*pos/2] = new HashSet<>();
                        t[cnt*pos/2].add((a*cnt) + "," + (b*cnt));
                        cnt++;
                    }
                }
            }

            //System.err.println(Arrays.toString(t));

            ArrayList<Integer> maxSoFar = new ArrayList<>();
            int max = 0;
            for (int p = 0; p <= MAX/2; p++) {
                if (t[p] != null && t[p].size() > max) {
                    max = t[p].size();
                    maxSoFar.add(2*p);
                }
            }

            System.out.println(maxSoFar);
        }
}
