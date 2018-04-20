package packages;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Perm {
    static int[] pr = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149};
    static int MOD = 1000000007;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        for (int n_i = 0; n_i < n; n_i++) {

            String answer;
            Map<String, Integer> hm = new TreeMap<>();

            String a = sc.next();
            String b = sc.next();

            int h = 0;
            int m = 0;
            int[] aw = new int[26];

            for (int i = 0; i < a.length(); i++) {
                h += pr[a.charAt(i) - 'a'];
                h %= MOD;
                aw[a.charAt(i) - 'a']++;

                m += pr[b.charAt(i) - 'a'];
                m %= MOD;
            }

            for (int j = a.length(); j <= b.length(); j++) {
                if (m == h) {
                    String ss = b.substring(j - a.length(), j);
                    hm.compute(ss, (k, v) -> v != null ? v + 1 : 1);
                }
                
                if (j < b.length()) {
                    m -= pr[b.charAt(j - a.length()) - 'a'];
                    m += pr[b.charAt(j) - 'a'];
                    if (m < 0) m += MOD;
                    else m %= MOD;
                }
            }

            nextBest:
            while (true) {
                int max = 0;
                String best = null;
                for (String ss : hm.keySet()) {
                    if (hm.get(ss) > max) {
                        max = hm.get(ss);
                        best = ss;
                    }
                }
                
                if (best == null) {
                    answer = "-1";
                    break;
                }

                int[] bw = new int[26];
                for (int i = 0; i < a.length(); i++) {
                    bw[best.charAt(i) - 'a']++;
                }
                for (int i = 0; i < 26; i++) {
                    if (aw[i] != bw[i]) {
                        hm.remove(best);
                        continue nextBest;
                    }
                }
                
                answer = best;
                break;
            }

            System.out.println(answer);
        }

    }
}
