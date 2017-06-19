package howmanysubstrings;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class S3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        solve(in, System.out);
    }

    static void solve(Scanner in, PrintStream out) {
        int n = in.nextInt();
        int q = in.nextInt();
        String s = in.next();

        long t = System.currentTimeMillis();

        Integer[] ss = new Integer[n];
        for (int i = 0; i < n; i++) {
            ss[i] = i;
        }
        Arrays.sort(ss, (o1, o2) -> {
            int len1 = n - o1;
            int len2 = n - o2;
            int lim = Math.min(len1, len2);
            int k = 0;
            while (k < lim) {
                char c1 = s.charAt(o1 + k);
                char c2 = s.charAt(o2 + k);
                if (c1 != c2) {
                    return c1 - c2;
                }
                k++;
            }
            return len1 - len2;
        });

        int[] eq = new int[n];
        for (int i = 1; i < n; i++) {
            int o1 = ss[i];
            int o2 = ss[i-1];
            int len1 = n - o1;
            int len2 = n - o2;
            int lim = Math.min(len1, len2);
            int k = 0;
            while (k < lim) {
                char c1 = s.charAt(o1 + k);
                char c2 = s.charAt(o2 + k);
                if (c1 != c2) {
                    break;
                }
                k++;
            }

            eq[i] = k;
        }

        System.err.println(System.currentTimeMillis() - t);
        t = System.currentTimeMillis();

        for (int a0 = 0; a0 < q; a0++) {
            int left = in.nextInt();
            int right = in.nextInt();
            int cnt = right - left + 1;

            long res = cnt * (cnt + 1) / 2;

            int last = -1;
            int ceq = -1;
            for (int i1 = 0; i1 < ss.length; i1++) {
                int s1 = ss[i1];
                ceq = Math.min(ceq, eq[i1]);
                if (s1 <= right && s1 >= left) {
                    if (last != -1) {
                        int max = Math.min(right - s1, right - last) + 1;
                        int i = Math.min(ceq, max);
                        res -= i;

                        if (i != right - s1 + 1) {
                            last = s1;
                            ceq = Integer.MAX_VALUE;
                        }
                    } else {
                        last = s1;
                        ceq = Integer.MAX_VALUE;
                    }
                }
            }

            out.println(res);
        }
        System.err.println(System.currentTimeMillis() - t);
    }
}
