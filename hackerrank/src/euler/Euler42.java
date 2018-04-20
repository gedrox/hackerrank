package euler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Euler42 {

    private static ArrayList<Integer>[] matches;
    private static ArrayList<Integer>[][] starts;
    private static ArrayList<Integer>[][] ends;
    private static int n;

    public static void main(String[] args) {
        int[] div = {2, 3, 5, 7, 11, 13, 17};
        n = new Scanner(System.in).nextInt();
        int c = n - 2;

        matches = new ArrayList[c];

        starts = new ArrayList[c][1000];
        ends = new ArrayList[c][1000];

        for (int i = c - 1; i >= 0; i--) {
            matches[i] = new ArrayList<>();
            for (int i1 = 0; i1 < 1000; i1++) {
                starts[i][i1] = new ArrayList<>();
                ends[i][i1] = new ArrayList<>();
            }

            for (int j = div[i]; j < 1000; j += div[i]) {
                if (ok3(j, n)) {
                    matches[i].add(j);
                    ends[i][j % 100].add(j);
                    starts[i][j / 10].add(j);
                }
            }
        }

        ArrayList<Long> answer = new ArrayList<>();
        boolean[] hit = new boolean[10];
        char[] str = new char[n + 1];
        recursion(c - 1, answer, matches[c - 1], hit, str);

        long sum = 0;
        for (Long aLong : answer) {
            sum += aLong;
        }

        System.out.println(sum);
    }

    private static void recursion(int i, ArrayList<Long> answer, ArrayList<Integer> startWith, boolean[] hit, char[] str) {
        for (int base : startWith) {
            if (hit[base / 100]) continue;

            // first in
            if (i == n - 3) {
                hit[base % 10] = true;
                hit[(base / 10) % 10] = true;
                str[i + 3] = (char) ('0' + base % 10);
                str[i + 2] = (char) ('0' + ((base / 10) % 10));
            }

            hit[base / 100] = true;

            str[i + 1] = (char) ('0' + base / 100);

            if (i == 0) {
                //if (hit[0]) {
                for (int i1 = 0; i1 <= n; i1++) {
                    if (!hit[i1]) {
                        str[0] = (char) ('0' + i1);
                        break;
                    }
                }
                answer.add(Long.valueOf(new String(str)));
                //}
            } else {
                recursion(i - 1, answer, ends[i - 1][base / 10], hit, str);
            }

            hit[base / 100] = false;

            if (i == n - 3) {
                hit[base % 10] = false;
                hit[(base / 10) % 10] = false;
            }
        }
    }

    private static boolean ok3(int j, int n) {
        boolean[] rep = new boolean[10];
        for (int i = 0; i < 3; i++) {
            int digit = j % 10;
            if (digit > n || rep[digit]) return false;
            rep[digit] = true;
            j /= 10;
        }
        return true;
    }
}
