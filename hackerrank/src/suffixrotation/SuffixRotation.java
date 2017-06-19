package suffixrotation;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class SuffixRotation {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for (int a0 = 0; a0 < q; a0++) {
            String s = in.next();
            System.out.println(solveGreatest(s.toCharArray()));
        }
    }

    static int solveSlow(char[] s) {
        char[] min = s.clone();
        Arrays.sort(min);
        ArrayList<String> best = new ArrayList<>();
        int res = doSolve(s, min, 0, best);
//        System.err.println(best);
        return res;
    }

    static int doSolve(char[] s, char[] min, int i, ArrayList<String> best) {
        if (s.length == 1) return 0;
        int smallest = s.length;
        char[] bestS = new char[0];
        int bestJ = 0;

        ArrayList<String> bestInner = new ArrayList<>();

        for (int j = 0; j < s.length; j++) {
            if (s[j] == min[i]) {
                int res = (j == 0 ? 0 : 1);
                char[] newS = pick(s, j);

                ArrayList<String> inner = new ArrayList<>();
                res += doSolve(newS, min, i + 1, inner);
                if (res < smallest) {
                    smallest = res;
                    bestInner = inner;
                    bestS = newS;
                    bestJ = j;
                }
            }
        }

        if (bestJ != 0) {
            best.add(String.valueOf(bestS));
        }
        best.addAll(bestInner);

        return smallest;
    }

    static int solveBitFaster(char[] s) {
        char[] min = s.clone();
        Arrays.sort(min);
        ArrayList<String> best = new ArrayList<>();
        int res = doSolveBitFaster(s, min, 0, best);
//        System.err.println(best);
        return res;
    }

    static int doSolveBitFaster(char[] s, char[] min, int i, ArrayList<String> best) {
        if (s.length == 1) return 0;
        int smallest = s.length;
        char[] bestS = new char[0];
        int bestJ = 0;

        ArrayList<String> bestInner = new ArrayList<>();

        for (int j = 0; j < s.length; j++) {
            if (s[j] == min[i]) {
                int res = (j == 0 ? 0 : 1);
                char[] newS = pick(s, j);

                ArrayList<String> inner = new ArrayList<>();
                res += doSolveBitFaster(newS, min, i + 1, inner);
                if (res < smallest) {
                    smallest = res;
                    bestInner = inner;
                    bestS = newS;
                    bestJ = j;
                }
                if (j == 0) break;
            }
        }

        if (bestJ != 0) {
            best.add(String.valueOf(bestS));
        }
        best.addAll(bestInner);

        return smallest;
    }

    static int solveBetter(char[] s) {
        char[] min = s.clone();
        Arrays.sort(min);

        return doSolveBetter(s, min, 0);
    }

    static int doSolveBetter(char[] s, char[] min, int i) {
        if (s.length == 1) return 0;

        if (s[0] == min[i]) {
            return doSolveBetter(pick(s, 0), min, i + 1);
        }

        // maybe there is only one?
        if (min[i + 1] != min[i]) {
            for (int j = 1; j < s.length; j++) {
                if (s[j] == min[i]) {
                    return 1 + doSolveBetter(pick(s, j), min, i + 1);
                }
            }
        }

        int c = i + 2;
        while (min[c] == min[i]) c++;
        char next = min[c];

        // there are several. pick longest with min count of following next
        ArrayList<int[]> pos = new ArrayList<>();
        for (int j = 1; j < s.length; j++) {
            if (s[j] == min[i]) {
                int[] p = {j, 1, 0, 0, 0};
                String suff = "";

                char before = s[j - 1];

                while (j + 1 < s.length && s[j + 1] == min[i]) {
                    p[1] = 1;
                    j++;
                }
//                int k = j;
//                char X;
//                while ((X = s[(k + 1) % s.length]) == next || X == min[i]) {
//                    if (Xp[2]++;
//                    j++;
//                }

                // index 3 means next char the same as next
                if (before == s[(j + 1) % s.length] && before == next) {
                    p[3] = 1;
                }

                while (s[(j + 1) % s.length] == next) {
                    p[2] = 1;
                    j++;
                }

                char theNextNext = s[(j + 1) % s.length];
                p[4] = theNextNext;

                // not a bad choice now it seems
//                if (s[(j + 1) % s.length] == min[i]) {
//                    // it is a bad choice
//                    p[3] = 1;
//                }

                pos.add(p);
            }
        }

        // pick the best now (should use all the info now, but let's not focus on it right now)
        int[] bestP = pos.get(0);
        for (int p_i = 1; p_i < pos.size(); p_i++) {
            int[] p = pos.get(p_i);
            if (bestP[3] < p[3]) {
                bestP = p;
            } else if (bestP[3] == p[3]) {
                if (bestP[2] > p[2]) {
                    bestP = p;
                } else if (bestP[2] == p[2] && bestP[4] > p[4]) {
                    bestP = p;
                }
            }
        }

        return 1 + doSolveBetter(pick(s, bestP[0]), min, i + 1);
//
//        for (int j = 0; j < s.length; j++) {
//
//            if (s[j] == min[i]) {
//                int res = (j == 0 ? 0 : 1);
//                char[] newS = pick(s, j);
//
//                res += doSolveBetter(newS, min, i + 1);
//                if (res < smallest) {
//                    smallest = res;
//                }
//                if (j == 0) break;
//            }
//        }
//        return smallest;
    }

    static int solveGreatest(char[] s) {
        int solution = 0;
        int n = s.length;

        // bonus
        int[] p = new int[n];
        p[0] = -1;

        // next pointer
        int[] nxt = new int[n];
        for (int i = 0; i < n; i++) {
            nxt[i] = (i + 1) % n;
        }

        char max = 'a';
        int start = 0;

        for (int i = 0; i < s.length; i++) {
            if (s[i] > max) {
                max = s[i];
                start = i;
            }
        }

        int go = start;

        while (true) {

            // remove duplicates, start with bonus "-1"
            start = go;

            if (nxt[start] == start) {
                return solution;
            }

            do {
                if (p[go] == -1) {
                    while (s[go] == s[nxt[go]]) {
                        int tmp = nxt[go];
                        nxt[go] = nxt[nxt[go]];
                        nxt[tmp] = -1;
                        if (start == tmp) {
                            start = nxt[go];
                        }
                        if (go == nxt[go]) {
                            return solution;
                        }
                    }
                }
                go = nxt[go];
            } while (go != start);

            // now remove dupls with no bonus
            start = go;
            do {
                while (s[go] == s[nxt[go]]) {
                    int tmp = nxt[go];
                    nxt[go] = nxt[nxt[go]];
                    nxt[tmp] = -1;
                    if (start == tmp) {
                        start = nxt[go];
                    }
                    if (go == nxt[go]) {
                        return solution;
                    }
                }
                go = nxt[go];
            } while (go != start);

            // find smallest
            char smallest = 'z';
            start = go;
            do {
                if (s[go] < smallest) {
                    smallest = s[go];
                }
                go = nxt[go];
            } while (go != start);

            if (smallest == max) {
                return solution;
            }

            // count all smallest
            int count = 0;
            int bestBonus = 0;
            int bestBonusCount = 0;
            start = go;
            do {
                if (s[go] == smallest) {
                    count++;
                    if (p[go] == -1) {
                        bestBonus = -1;
                        bestBonusCount++;
                    }
                }
                go = nxt[go];
            } while (go != start);

            // best bonus
            solution += count + bestBonus;

            // remove all and set bonus after each with best bonus
            start = go;
            do {
                // remove the element
                if (s[nxt[go]] == smallest) {
                    int tmp = nxt[go];
                    nxt[go] = nxt[nxt[go]];
                    // set bonuses
                    if (count == 1 || p[tmp] != -1 || bestBonusCount > 1) {
                        p[nxt[go]] = -1;
                    } else {
                        p[nxt[go]] = 0;
                    }
                    // disable link
                    nxt[tmp] = -1;
                    if (start == tmp) {
                        start = nxt[go];
                    }
                } else {
                    p[nxt[go]] = 0;
                }

                go = nxt[go];
            } while (go != start);
        }
    }

    private static char[] pick(char[] s, int j) {
        char[] newS = new char[s.length - 1];
        System.arraycopy(s, j + 1, newS, 0, s.length - j - 1);
        System.arraycopy(s, 0, newS, s.length - j - 1, j);
        return newS;
    }

    @Test
    public void testIt() throws IOException {
        Random r = new Random();
        int n = 1000;
        char[] s = new char[n];
        int c = 0;

        long longest = 0;

        while (c < 100) {
            c++;
            for (int i = 0; i < n; i++) {
                s[i] = (char) ('a' + (r.nextInt(n) % 26));
            }

//            System.out.print(String.valueOf(s) + ": ");
            long t0 = System.currentTimeMillis();
            int answBetter = solveGreatest(s);
            longest = Math.max(longest, System.currentTimeMillis() - t0);
//            int answBetter = solveBitFaster(s);
            System.out.print(answBetter);
//            int answ = solveSlow(s);
//            if (answ != answBetter) {
//                throw new RuntimeException(String.valueOf(s));
//            }
            System.out.println(" OK");

        }

        System.err.println(longest + "ms");
    }

    @Test
    public void testTricky() throws IOException {
//        char[] s = "zazaab".toCharArray();
//        char[] s = "cdaababbcd".toCharArray();
//        char[] s = "bdbabedcaa".toCharArray();
//        char[] s = "cbdebbcebb".toCharArray();
//        char[] s = "cdacfa".toCharArray();
//        char[] s = "dcddfc".toCharArray();
//        char[] s = "cebcefb".toCharArray();
//        char[] s = "cebcefb".toCharArray();
//        char[] s = "cdababcd".toCharArray();
//        char[] s = "ccbdabc".toCharArray();
//        char[] s = "fbcbdca".toCharArray();
        char[] s = "bcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijkl".toCharArray();
//        System.out.println(solveSlow(s));
//        System.out.println(solveBitFaster(s));
//        System.out.println(solveBetter(s));
        for (int i = 0; i < 100; i++) {

            System.out.println(solveGreatest(s));
        }
    }
}

//ccbdabc
//abcccbd
//abbdccc
//abbcccd

// cdaababbcd
// aababbcdcd
// aaabbcdcdb
// aaabbbcdcd
// aaabbbccdd

// cdaababbcd
// abbcdcdaab
// aaabbbcdcd
// aaabbbccdd

// bdbabedcaa
// aabdbabedc
// aaabedcbdb
// aaabbdbedc
// aaabbbedcd
// aaabbbcded
// aaabbbcdde

// bdbabedcaa
// abedcaabdb
// aaabdbbedc
// aaab

// babac
// acbab
// aabcb
// aabbc

// babac
// abacb
// aacbb
// aabbc