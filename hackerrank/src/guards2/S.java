package guards2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class S {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= T; i++) {
            int n = sc.nextInt();
            sb.append("Case #")
                    .append(i)
                    .append(": ")
                    .append(solve(n, sc.next(), sc.next()))
                    .append('\n');
        }
        System.out.println(sb.toString());
        Files.write(Paths.get("out.txt"), sb.toString().getBytes());
//        System.out.println(solve(8, "...X.X..", ".......X"));
    }

    private static int solve(int n, String s1, String s2) {

        int L1 = 0, L2 = 0;
        boolean used1 = false, used2 = false;
        int res = 0;

        for (int i = 0; i <= n; i++) {
            boolean wall1 = i == n || s1.charAt(i) == 'X';
            boolean wall2 = i == n || s2.charAt(i) == 'X';

            if (wall1 && wall2 && L1 == 1 && L2 == 1) {
                res++;
                L1 = 0;
                L2 = 0;
            } else {
                if (wall1) {
                    if (L1 == 1) {
                        if (L2 > 0 && !used2) {
                            used2 = true;
                        } else {
                            res++;
                        }
                    } else if (L1 > 1) {
                        res++;
                    }
                }
                if (wall2) {
                    if (L2 == 1) {
                        if (L1 > 0 && !used1) {
                            used1 = true;
                        } else {
                            res++;
                        }
                    } else if (L2 > 1) {
                        res++;
                    }
                }
            }

            if (!wall1) {
                L1++;
            } else {
                L1 = 0;
                used1 = false;
            }
            if (!wall2) {
                L2++;
            } else {
                L2 = 0;
                used2 = false;
            }
        }

        return res;
    }
}
