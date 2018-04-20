package chessagain;

import java.util.Scanner;

public class ChessPawnPromotion {

    static final int[][] KNIGHT = {{1, 2},{2, 1},{-1, 2},{-2, 1},{1, -2},{2, -1},{-1, -2},{-2, -1}};
    static final int[][] ROOK = {{1, 0},{0, 1},{-1, 0},{0, -1}};
    static final int[][] BISHOP = {{1, 1},{-1, 1},{-1, -1},{1, -1}};

    static int waysToGiveACheck(char[][] board) {
        int ways = 0;
        
        int[] whiteKing = null;
        int[] blackKing = null;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 'k') blackKing = new int[] {i, j};
                if (board[i][j] == 'K') whiteKing = new int[] {i, j};
            }
        }
        
        assert whiteKing != null;
        assert blackKing != null;

        for (int col = 0; col < 8; col++) {
            
            if (board[1][col] == 'P' && board[0][col] == '#') {
                
                boolean rookOrBishopWins = false;
                
                for (char target : new char[] {'N', 'R', 'B'}) {
                    // move
                    board[1][col] = '#';
                    board[0][col] = target;
                    
                    if (!isCheck(board, whiteKing) && isCheck(board, blackKing)) {
                        ways++;
                        if (target != 'N') rookOrBishopWins = true;
                    }

                    // undo
                    board[1][col] = 'P';
                    board[0][col] = '#';
                }
                
                if (rookOrBishopWins) {
                    ways++;
                }
            }
        }

        return ways;
    }

    private static boolean isCheck(char[][] board, int[] king) {
        
        boolean opp = board[king[0]][king[1]] == 'k';
        
        char k = opp ? 'K' : 'k';
        char n = opp ? 'N' : 'n';
        char p = opp ? 'P' : 'p';
        char q = opp ? 'Q' : 'q';
        char b = opp ? 'B' : 'b';
        char r = opp ? 'R' : 'r';
        int dir = opp ? -1 : 1;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                char x = board[i][j];
                // king (should be impossible)
                if (x == k) {
                    if (i + dir == king[0] && king[1] >= j - 1 && king[1] <= j + 1) return true;
                }
                // knight
                if (x == n) {
                    for (int[] d : KNIGHT) {
                        if (i + d[0] == king[0] && j + d[1] == king[1]) {
                            return true;
                        }
                    }
                }
                // bishop
                if (x == b || x == q) {
                    for (int[] d : BISHOP) {
                        int i_ = i;
                        int j_ = j;
                        do {
                            i_ += d[0];
                            j_ += d[1];
                            if (i_ == king[0] && j_ == king[1]) return true;
                        } while (valid(i_) && valid(j_) && board[i_][j_] == '#');
                    }
                }
                // rook
                if (x == r || x == q) {
                    for (int[] d : ROOK) {
                        int i_ = i;
                        int j_ = j;
                        do {
                            i_ += d[0];
                            j_ += d[1];
                            if (i_ == king[0] && j_ == king[1]) return true;
                        } while (valid(i_) && valid(j_) && board[i_][j_] == '#');
                    }
                }
                // pawn (should be impossible)
                if (x == p) {
                    if (i + dir == king[0] && (king[1] == j - 1 || king[1] == j + 1)) return true;
                }
            }
        }
        
        return false;
    }

    private static boolean valid(int a) {
        return a >= 0 && a < 8;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for(int a0 = 0; a0 < t; a0++){
            char[][] board = new char[8][8];
            for(int board_i = 0; board_i < 8; board_i++){
                String s = in.next();
                for(int board_j = 0; board_j < 8; board_j++){
                    board[board_i][board_j] = s.charAt(board_j);
                }
            }
            int result = waysToGiveACheck(board);
            System.out.println(result);
        }
        in.close();
    }
}
