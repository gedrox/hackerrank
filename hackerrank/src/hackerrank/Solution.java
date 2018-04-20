package hackerrank;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    enum NESW {
        N(-1, 0), E(0, 1), S(1, 0), W(0, -1);
        int x, y;
        NESW(int x, int y) {
            this.x = x;
            this.y = y;
        }
        NESW right() {
            return NESW.values()[(this.ordinal() + 1) % 4];
        }
        NESW left() {
            return NESW.values()[(this.ordinal() + 3) % 4];
        }
        NESW opp() {
            return NESW.values()[(this.ordinal() + 2) % 4];
        }
    }

    static int n;
    static int[][] g;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        n = in.nextInt();
        char d = in.next().charAt(0);
        int x = in.nextInt();
        int y = in.nextInt();
        in.close();

        g = new int[n][n];
        g[x][y] = 1;

        NESW wind = NESW.valueOf(String.valueOf(d).toUpperCase());

        NESW[] tryDir = {wind, wind.right(), wind.left(), wind.opp()};

        for (int q = 2; q <= n * n; q++) {

            for (NESW dir : tryDir) {
                if (canGo(x, y, dir)) {
                    x += dir.x;
                    y += dir.y;
                    g[x][y] = q;
                    break;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(g[i][j]).append(" ");
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }

    static boolean canGo(int x, int y, NESW dir) {
        return x + dir.x >= 0 && x + dir.x < n && y + dir.y >= 0 && y + dir.y < n && g[x + dir.x][y + dir.y] == 0;
    }
}
