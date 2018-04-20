package hackerrank;

import java.util.Scanner;

public class CircleAndSquare {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int w = in.nextInt();
        int h = in.nextInt();
        int circleX = in.nextInt();
        int circleY = in.nextInt();
        int r = in.nextInt();
        int x1 = in.nextInt();
        int y1 = in.nextInt();
        int x3 = in.nextInt();
        int y3 = in.nextInt();

        boolean[][] solve = solve(w, h, circleX, circleY, r, x1, y1, x3, y3);
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                System.out.print(solve[j][i] ? "#" : ".");
            }
            System.out.println();
        }
    }
    
    static boolean[][] solve(int w, int h, int x, int y, int r, int x1, int y1, int x3, int y3) {
        boolean[][] out = new boolean[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (c(i, j, x, y, r)) {
                    out[i][j] = true;
                } else if (s(i, j, x1, y1, x3, y3)) {
                    out[i][j] = true;
                }
            }
        }
        return out;
    }

    private static boolean c(int i, int j, int x, int y, int r) {
        return (i - x) * (i - x) + (j - y) * (j - y) <= r * r;
    }

    private static boolean s(int i, int j, int x1, int y1, int x3, int y3) {
        
        double x = .5 * (x1 + x3);
        double y = .5 * (y1 + y3);
        
        double x2 = (x - (y1 - y));
        double y2 = (y + (x1 - x));
        
        double x4 = (2 * x - x2);
        double y4 = (2 * y - y2);
        
        if (i == x1 && j == y1) return true;
        if (2*i == Math.round(2*x2) && 2*j == Math.round(2*y2)) return true;
        if (i == x3 && j == y3) return true;
        if (2*i == Math.round(2*x4) && 2*j == Math.round(2*y4)) return true;
        
        double d1 = Math.atan2(y2 - y1, x2 - x1) - Math.atan2(j - y1, i - x1);
        double d2 = Math.atan2(y3 - y2, x3 - x2) - Math.atan2(j - y2, i - x2);
        double d3 = Math.atan2(y4 - y3, x4 - x3) - Math.atan2(j - y3, i - x3);
        double d4 = Math.atan2(y1 - y4, x1 - x4) - Math.atan2(j - y4, i - x4);

        d1 = norm(d1);
        d2 = norm(d2);
        d3 = norm(d3);
        d4 = norm(d4);
        
//        if (d1 >= 0 && d2 >= 0 && d3 >= 0 && d4 >= 0) return true;
        if (d1 <= 1e-10 && d2 <= 1e-10 && d3 <= 1e-10 && d4 <= 1e-10) return true;
        
        return false;
    }

    private static double norm(double d1) {
        if (d1 > Math.PI) d1 -= 2 * Math.PI;
        if (d1 <= -Math.PI) d1 += 2 * Math.PI;
        return d1;
    }
}
