package appenddelete;

import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String t = sc.nextLine();
        int k = sc.nextInt();

        System.out.println(solve(s, t, k) ? "Yes" : "No");
    }

    private static boolean solve(String s, String t, int k) {
        if (k >= s.length() + t.length()) {
            return true;
        }

        int common = 0;

        while (s.length() > common && t.length() > common && s.charAt(common) == t.charAt(common)) {
            common++;
        }

        int tail = s.length() + t.length() - 2 * common;
        if (k >= tail && (k - tail) % 2 == 0) {
            return true;
        }

        return false;
    }
}
