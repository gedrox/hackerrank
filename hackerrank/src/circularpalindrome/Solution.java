package circularpalindrome;

import org.junit.Test;

import java.util.Arrays;

public class Solution {

    static int[] prepare(char[] c) {
        int n = c.length;
        int[] L = new int[n];
        for (int i = 0; i < n; i++) {
            int left = i;
            int right = i;

            while (true) {
                left--;
                if (left < 0) left += n;
                if (left == right) break;
                right++;
                if (right >= n) right -= n;
                if (left == right) break;

                if (c[left] == c[right]) {
                    L[i]++;
                } else {
                    break;
                }
            }
        }

        return L;
    }

    @Test
    public void test() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50000; i++) {
            sb.append('a');
        }
        System.out.println("prep done");
        int[] prepare = prepare(sb.toString().toCharArray());
        System.out.println(Arrays.toString(prepare));
    }

}
