package howmanysubstrings;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

public class S2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        solve(in, System.out);
    }

    static void solve(Scanner in, PrintStream out) {
        int n = in.nextInt();
        int q = in.nextInt();
        String s = in.next();

        long t = System.currentTimeMillis();

        for (int a0 = 0; a0 < q; a0++) {
            int left = in.nextInt();
            int right = in.nextInt();
            int cnt = right - left + 1;
            String[] ss = new String[cnt];
            for (int l = 0; l < cnt; l++) {
                ss[l] =  s.substring(right - l, right + 1);
            }
            Arrays.sort(ss);

            long res = cnt * (cnt + 1) / 2;

            for (int i = 1; i < cnt; i++) {
                for (int j = 0; j < ss[i-1].length() && j < ss[i].length() && ss[i-1].charAt(j) == ss[i].charAt(j); j++) {
                    res--;
                }
            }

            out.println(res);
        }
        System.err.println(System.currentTimeMillis() - t);
    }
}
