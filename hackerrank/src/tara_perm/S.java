package tara_perm;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

public class S {
    public static void main(String[] args) {
        int[] a = {2, 2, 2};
        int max = a[0];

        int l = 0;
        int res = 1;
        for (int i = 0; i < max; i++) {
            for (int j = 0; j < a.length; j++) {
                if (a[j] > i) {
                    int p = (l + 1) - 2 * i;
                    res *= p;
                    l++;
                }
            }
        }
        System.out.println(res);
    }

    @Test
    public void test() {
        HashSet<String> res = permutations("AABBCCDD");
        int s = 0;
        nextRe:
        for (String re : res) {
            for (int i = 1; i < re.length(); i++) {
                if (re.charAt(i) == re.charAt(i-1)) {
                    continue nextRe;
                }
            }
            System.out.print(re + " ");
            s++;
        }
        System.out.println();
//        System.out.println(res);
        System.out.println(s);
    }

    public static HashSet<String> permutations(String s) {
        if (s.length() == 0) return new HashSet<>();
        int length = fact(s.length());
        StringBuilder[] sb = new StringBuilder[length];
        for (int i = 0; i < length; i++) {
            sb[i] = new StringBuilder();
        }
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            int times = length / (i + 1);
            for (int j = 0; j < times; j++) {
                for (int k = 0; k < length / times; k++) {
                    sb[j * length / times + k].insert(k, ch);
                }
            }
        }

        HashSet<String> res = new HashSet<>();
        for (StringBuilder aSb : sb) {
            res.add(aSb.toString());
        }

        return res;
    }

    private static int fact(int length) {
        int res = 1;
        for (int i = 0; i < length - 1; i++) {
            res *= length - i;
        }
        return res;
    }
}
