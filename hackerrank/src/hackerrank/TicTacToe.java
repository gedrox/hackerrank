package hackerrank;

import java.util.Scanner;

public class TicTacToe {

    static void nextMove(int[][] board) {
        int[] bestMove = findBestMove(board, 1);
        System.out.println(bestMove[0] + " " + bestMove[1]);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        char player;
        int board[][] = new int[3][3];

        //If player is X, I'm the first player.
        //If player is O, I'm the second player.
        player = in.next().charAt(0);

        //Read the board now. The board is a 3x3 array filled with X, O or _.
        for (int i = 0; i < 3; i++) {
            String row = in.next();
            for (int j = 0; j < row.length(); j++) {
                char c = row.charAt(j);
                board[i][j] = c == player ? 1 : (c == '_' ? 0 : -1);
            }
        }

        nextMove(board);
    }

    static int[] findBestMove(int[][] board, int player) {
        int[] best = null;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    board[i][j] = player;
                    int[] nextMove = new int[]{i, j, 0};

                    if (getWinner(board) != 0) {
                        nextMove[2] = 1;
                    } else {
                        // no moves, no winner
                        int[] move = findBestMove(board, -player);
                        if (move == null) {
                            nextMove[2] = 0;
                        } else {
                            nextMove[2] = -move[2];
                        }
                    }

                    board[i][j] = 0;

                    if (best == null || best[2] < nextMove[2]) {
                        best = nextMove;
                        if (best[2] == 1) {
                            return best;
                        }
                    }
                }
            }
        }

        return best;
    }

    static int getWinner(int[][] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == board[i][1] && board[i][2] == board[i][1] && board[i][0] != 0) {
                return board[i][0];
            }
            if (board[0][i] == board[1][i] && board[2][i] == board[1][i] && board[0][i] != 0) {
                return board[0][i];
            }
        }

        if (board[0][0] == board[1][1] && board[2][2] == board[1][1] && board[0][0] != 0) {
            return board[0][0];
        }
        if (board[0][2] == board[1][1] && board[2][0] == board[1][1] && board[0][2] != 0) {
            return board[0][2];
        }

        return 0;
    }
}
