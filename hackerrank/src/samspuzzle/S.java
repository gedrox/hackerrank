package samspuzzle;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class S {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int a[] = new int[n * n];

        for(int puzzle_i=0; puzzle_i < n; puzzle_i++){
            for(int puzzle_j=0; puzzle_j < n; puzzle_j++){
                a[puzzle_i * n + puzzle_j] = in.nextInt();
            }
        }
        Square s = new Square(a);
        int was = s.calc();

        Random r = new Random();

        int max = s.max();

        long T0;
        long[] T = new long[3];

        long T00 = System.currentTimeMillis();

        int h = s.n;

        while (s.movePos < 500 && h >= 2) {
            int best = -1;
            int[] move = new int[]{0, 0, 2, 1};
            int rand = 1;
            for (int x = 0; x <= s.n - h; x++) {
                for (int y = 0; y <= s.n - h; y++) {

                    T0 = System.currentTimeMillis();
                    int[] calcOld = s.calc(x, y, h);
                    T[0] += System.currentTimeMillis() - T0;

                    int imprX = calcOld[2] - calcOld[0];
                    int imprY = calcOld[3] - calcOld[1];

                    for (int times = 1; times <= 3 && s.movePos + times <= 500; times++) {
                        int calc = (times == 1) ? imprY : ((times == 2) ? (imprY + imprX) : imprX);
                        calc = 6 * calc / times;
                        boolean match = false;
                        if (calc > best) {
                            match = true;
                            best = calc;
                            rand = 1;
                        } else if (calc == best /* && r.nextInt(rand++) == 0*/) {
                            match = true;
                        }
                        if (match) {
                            move = new int[]{x, y, h, times};
                        }
                    }
                }
            }
            if (best > 0) {
                for (int i = 0; i < move[3]; i++) {
                    s.r(move[0], move[1], move[2]);
                }

                if ((n - h + 1d) / (n - 1d) < s.movePos / 500d) {
                    h--;
                }
            } else {
                h--;
            }
        }

        T[2] += System.currentTimeMillis() - T00;

        System.out.println(s.movePos);
        for (int i = 0; i < s.movePos; i++) {
            System.out.println((s.moves[3 * i] + 1) + " " + (s.moves[3 * i + 1] + 1) + " " + s.moves[3 * i + 2]);
        }

        System.err.println(Arrays.toString(T));
        System.err.println("Was " + was + ", is " + s.calc() + ", max " + max);
    }

    static class Square {
        int n;
        final int a[];
        int[][] undo = new int[500][];
        int[] moves = new int[1500];
        int movePos = 0;

        Square(int[] a) {
            this.a = a;
            n = (int) Math.sqrt(a.length);
            for (int i = 0; i < undo.length; i++) {
                undo[i] = new int[a.length];
            }
        }

        void r(int x, int y, int h) {
            System.arraycopy(a, 0, undo[movePos], 0, a.length);
            int[] u = undo[movePos];

            moves[3 * movePos] = x;
            moves[3 * movePos + 1] = y;
            moves[3 * movePos + 2] = h;


            for (int i = x; i < x + h; i++) {
                for (int j = y; j < y + h; j++) {
                    a[n * (x + j - y) + (y + h - 1 - (i - x))] = u[i * n + j];
                }
            }

            movePos++;
        }

        void undo() {
            movePos--;
            System.arraycopy(undo[movePos], 0, a, 0, a.length);
        }

        int[] calc(int x, int y, int h) {
            int[] r = {0, 0, 0, 0}; // good x, good y, bad x, bad y
            for (int i = x; i < x + h; i++) {
                for (int j = y; j < y + h; j++) {
                    for (int j2 = y; j2 < j; j2++) {
                        if (a[i * n + j2] < a[i * n + j]) r[0]++;
                    }
                    for (int i2 = x; i2 < i; i2++) {
                        if (a[i2 * n + j] < a[i * n + j]) r[1]++;
                    }
                }
            }

            r[2] = h * h * (h - 1) / 2 - r[0];
            r[3] = h * h * (h - 1) / 2 - r[1];

            return r;
        }

        @Override
        public String toString() {
            String s = "";
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < n; y++) {
                    s += a[x * n + y] + "\t";
                }
                s += '\n';
            }
            s += "res = " + calc();
            return s;
        }

        public int max() {
            return n * n * (n - 1);
        }

        public int calc() {
            int[] calc = calc(0, 0, n);
            return calc[0] + calc[1];
        }
    }
}
