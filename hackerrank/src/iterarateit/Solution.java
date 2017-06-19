package iterarateit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.TreeSet;

public class Solution {
    public static void main(String[] args) throws IOException {

        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(bi.readLine());
        int a[] = new int[n];

        String[] split = bi.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            a[i] = Integer.parseInt(split[i]);
        }

        int res = solveTrivial(a);

        System.out.println(res);
    }

    static int solveTrivial(int[] a) {
        int res = 0;
//        System.err.println(Arrays.toString(a));
        Arrays.sort(a);

        while (a.length > 0) {
            TreeSet<Integer> B = new TreeSet<>();

            int minDiff = Integer.MAX_VALUE;

            for (int i = 0; i < a.length - 1; i++) {
                int diff = a[i + 1] - a[i];
                if (diff == 1) {
                    return res + a[a.length - 1] - a[0] + 1;
                }
                if (diff != 0 && diff < minDiff) {
                    minDiff = diff;
                }
            }

            boolean isCommonDivisor = true;

            for (int i = 0; i < a.length - 1; i++) {
                if (a[i] % minDiff != 0) {
                    isCommonDivisor = false;
                    break;
                }
            }

            if (isCommonDivisor) {
                for (int i = 0; i < a.length; i++) {
                    a[i] = a[i] / minDiff;
                }
            }

            for (int i = 0; i < a.length - 1; i++) {
                for (int j = i + 1; j < a.length; j++) {
                    int a1 = a[j] - a[i];
                    B.add(a1);
                }
            }
            B.remove(0);
            a = B.stream().mapToInt(i -> i).toArray();
            res++;

//            System.err.println(Arrays.toString(a));
        }
        return res;
    }
}
