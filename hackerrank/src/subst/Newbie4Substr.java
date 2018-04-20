package subst;

import org.junit.Test;

import java.util.Arrays;
import java.util.Scanner;

public class Newbie4Substr {

    static int[] len;

    static int[] DNAValue(String s) {
//        Integer[] T = createTable(s.toCharArray());

        len = new int[s.length()];
        len[0] = 1;
        kmp(s.toCharArray(), s.toCharArray());

        int[] max = new int[s.length()];
        for (int i = 0; i < len.length; i++) {
            for (int j = i; j < i + len[i] && j < len.length; j++) {
//                if (max[j] >= j - i + 1) break;
                max[j] = Math.max(j - i + 1, max[j]);
            }
            
            len[i] = max[i] <= len[i] ? max[i] : 0;
        }

        return len;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String s = in.next();
        int[] result = DNAValue(s);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + (i != result.length - 1 ? " " : ""));
        }
        System.out.println("");


        in.close();
    }

    public static void test(String text, String word, int exp) {
        char[] textC = text.toCharArray();
        char[] wordC = word.toCharArray();
        int result = kmp(textC, wordC);
        if (result == exp)
            System.out.println("PASSED");
        else {
            System.out.println("FAILED");
            System.out.println("\ttext: " + text);
            System.out.println("\tword: " + word);
            System.out.println("\texp: " + exp + ", res: " + result);
        }

    }
    
/*
        public static void main(String abc[]) {
            System.out.println("Testing KMP");
            test("abc", "a", 0);
            test("abc", "b", 1);
            test("abc", "c", 2);
            test("abc", "d", -1);
            test("catdog", "tdo", 2);
            test("ratatat", "at", 1);
            test("foo", "", 0);
            test("", "bar", -1);
        }
*/

    // W := word
    public static Integer[] createTable(char[] W) {
        Integer[] table = new Integer[W.length];
        int pos = 2; // cur pos to compute in T
        int cnd = 0; // index of W of next character of cur candidate substr

        // first few values are fixed
        table[0] = -1;  // table[0] := -1
        table[1] = 0;   // table[1] := 0

        while (pos < W.length) {
            // first case: substring is still good
            if (W[pos - 1] == W[cnd]) {
                table[pos] = cnd;
                cnd += 1;
                pos += 1;
            } else if (cnd > 0)
                cnd = table[cnd];
            else {
                table[pos] = 0;
                pos += 1;
            }
        }
        return table;
    }

    // S := text string
    // W := word
    public static int kmp(char[] S, char[] W) {
        if (W.length == 0) // substr is empty string
            return 0;
        if (S.length == 0) // text is empty, can't be found
            return -1;
        int m = 1; // index of beg. of current match in S
        int i = 0; // pos. of cur char in W
        Integer[] T = createTable(S);

        while (m < S.length) {
            if (m + i < S.length && W[i] == S[m + i]) {
                if (i == W.length - 1)
                    assert false;
                i += 1;
            } else {
                len[m] = i;
                m++;
                i = 0;
//                m = (m + i - T[i]);
//                if (T[i] > -1)
//                    i = T[i];
//                else
//                    i = 0;
            }
        }

        return -1;
    }

    @Test
    public void testit() {
        String s = "abacabadabacaba";
        System.out.println(Arrays.toString(s.toCharArray()));
        System.out.println(Arrays.toString(DNAValue(s)));
    }
}
