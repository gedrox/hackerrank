package hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class BobGame {

    public static final int[][] MOVES = {new int[]{-1, 0}};
    private static char[][] c;
    private static int[][] g;
    private static ArrayList<King> k;
    private static int n;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int q = in.nextInt();
        for (int a0 = 0; a0 < q; a0++) {
            n = in.nextInt();
            c = new char[n][n];
            for (int board_i = 0; board_i < n; board_i++) {
                c[board_i] = in.next().toCharArray();
            }

            g = new int[n][n];
            int xor = 0;

            k = new ArrayList<>();

            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    if (c[i][j] == 'X') {
                        g[i][j] = 9;
                        continue;
                    }
                    boolean[] res = new boolean[4];
                    pickFree(res, i - 1, j);
                    pickFree(res, i, j - 1);
                    pickFree(res, i - 1, j - 1);

                    for (int next = 0; next < res.length; next++) {
                        if (!res[next]) {
                            g[i][j] = next;
                            break;
                        }
                    }
                    
                    if (c[i][j] == 'K') {
                        k.add(new King(i, j));
                        xor ^= g[i][j];
                    }
                }
            }
            
            if (xor == 0) {
                System.out.println("LOSE");
                continue;
            }
            
            int winMoves = 0;

            ArrayList<Move> moves = getAllMoves();
            for (Move move : moves) {
                int change = g[move.king.x][move.king.y] ^ g[move.king.x + move.dir[0]][move.king.y + move.dir[1]];
                if ((xor ^ change) == 0) winMoves++;
            }

            System.out.println("WIN " + winMoves);

//            System.out.println(Arrays.deepToString(g).replace("], [", "]\n["));

//            k = new ArrayList<>();
//            for (int row = 0; row < c.length; row++) {
//                for (int y = 0; y < c[row].length; y++) {
//                    if (c[row][y] == 'K') {
//                        k.add(new King(row, y));
//                    }
//                }
//            }
//
//            int winCnt = 0;
//
//            ArrayList<Move> allMoves = getAllMoves();
//
//            for (Move move : allMoves) {
//                move.exec();
//                if (allLoose()) {
//                    winCnt++;
//                }
//                move.undo();
//            }
//
//            if (winCnt > 0) {
//                System.out.println("WIN " + winCnt);
//            } else {
//                System.out.println("LOSE");
//            }
        }
        in.close();
    }

    private static void pickFree(boolean[] res, int i, int j) {
        if (i >= 0 && j >= 0 && c[i][j] != 'X') res[g[i][j]] = true;
    }

//    private static boolean allLoose() {
//
//        for (Move move : getAllMoves()) {
//            move.exec();
//            if (allLoose()) {
//                move.undo();
//                return false;
//            }
//            move.undo();
//        }
//
//        return true;
//    }

    static boolean canGo(Move move) {
        int newX = move.king.x + move.dir[0];
        if (newX < 0) return false;
        if (newX >= n) return false;
        int newY = move.king.y + move.dir[1];
        if (newY < 0) return false;
        if (newY >= n) return false;
        return c[newX][newY] != 'X';
    }
//
    public static ArrayList<Move> getAllMoves() {
        ArrayList<Move> all = new ArrayList<>();
        for (King king : k) {
            for (int[] dir : MOVES) {
                Move m = new Move(king, dir);
                if (canGo(m)) {
                    all.add(m);
                }
            }
        }
        return all;
    }
//
//
    static class King {
        int x, y;

        public King(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
//
    static class Move {
        King king;
        int[] dir;

        public Move(King king, int[] dir) {
            this.king = king;
            this.dir = dir;
        }

//        public void exec() {
//            king.row += dir[0];
//            king.y += dir[1];
//        }
//
//        public void undo() {
//            king.row -= dir[0];
//            king.y -= dir[1];
//        }
    }
}
