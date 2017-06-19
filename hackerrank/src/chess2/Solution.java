package chess2;

import java.io.*;
import java.util.*;

public class Solution {

    static final char EMPTY_CELL = 0;

    static Map<Board, TreeMap<Integer, Boolean>> allCache = new HashMap<>();
    static Map<Board, TreeMap<Integer, Boolean>> anyCache = new HashMap<>();

    static Map<Character, int[][]> dir = new HashMap<>();
    static {
        dir.put('Q', new int[][] {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {1, 0}, {0, 1}, {-1, 0}, {0, -1}});
        dir.put('R', new int[][]{{1, 0}, {0, 1}, {-1, 0}, {0, -1}});
        dir.put('B', new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}});
        dir.put('N', new int[][]{{1, 2}, {1, -2}, {2, 1}, {2, -1}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}});
        dir.put('P', new int[][]{{1, 2}, {1, -2}, {2, 1}, {2, -1}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}});

        dir.put('q', dir.get('Q'));
        dir.put('r', dir.get('R'));
        dir.put('b', dir.get('B'));
        dir.put('n', dir.get('N'));
        dir.put('p', dir.get('P'));
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(bi.readLine());
//        System.err.println(System.currentTimeMillis());

        for (int t = 0; t < T; t++) {
            Board board = new Board();

            String[] parts = bi.readLine().split(" ");
            int whiteCount = Integer.parseInt(parts[0]);
            int blackCount = Integer.parseInt(parts[1]);
            int moves = Integer.parseInt(parts[2]);

            for (int wh_i = 0; wh_i < whiteCount; wh_i++) {
                parts = bi.readLine().split(" ");
                char type = parts[0].charAt(0);
                int col = parts[1].charAt(0) - 'A';
                int row = Integer.parseInt(parts[2]) - 1;
                board.put(row, col, type);
            }

            for (int bl_i = 0; bl_i < blackCount; bl_i++) {
                parts = bi.readLine().split(" ");
                char type = parts[0].toLowerCase().charAt(0);
                int col = parts[1].charAt(0) - 'A';
                int row = Integer.parseInt(parts[2]) - 1;
                board.put(row, col, type);
            }
            System.out.println(anyWin(board, moves - 1) ? "YES" : "NO");

            allCache.clear();
            anyCache.clear();

        }
//        System.err.println(System.currentTimeMillis());
    }

    static boolean anyWin(Board board, int movesLeft) {
        if (anyCache.containsKey(board)) {
            Map.Entry<Integer, Boolean> floorEntry = anyCache.get(board).floorEntry(movesLeft);
            if (floorEntry != null && floorEntry.getValue()) {
                return true;
            }
            Map.Entry<Integer, Boolean> ceilEntry = anyCache.get(board).ceilingEntry(movesLeft);
            if (ceilEntry != null && !ceilEntry.getValue()) {
                return false;
            }
        } else {
            anyCache.put(board, new TreeMap<>());
        }

        Map<Integer, List<Integer>> poss = new HashMap<>();
        // check clear wins first
        for (int pos : board.get(true)) {
            List<Integer> moves = board.possibleMoves(pos);
            poss.put(pos, moves);
            for (int move : moves) {
                if (board.cells[move] == 'q') {
                    anyCache.get(board).put(movesLeft, true);
                    return true;
                }
            }
        }

        for (int pos : poss.keySet()) {
            List<Integer> moves = poss.get(pos);
            for (int move : moves) {
                for (Board newBoard : board.move(pos, move)) {
                    if (newBoard == null) {
                        anyCache.get(board).put(movesLeft, true);
                        return true;
                    }
                    if (movesLeft > 1) {
                        boolean allWin = allWin(newBoard, movesLeft - 1);
                        if (allWin) {
                            anyCache.get(board).put(movesLeft, true);
                            return true;
                        }
                    }
                }
            }
        }
        anyCache.get(board).put(movesLeft, false);
        return false;
    }

    static boolean allWin(Board board, int movesLeft) {

        if (allCache.containsKey(board)) {
            Map.Entry<Integer, Boolean> floorEntry = allCache.get(board).floorEntry(movesLeft);
            if (floorEntry != null && floorEntry.getValue()) {
                return true;
            }
            Map.Entry<Integer, Boolean> ceilEntry = allCache.get(board).ceilingEntry(movesLeft);
            if (ceilEntry != null && !ceilEntry.getValue()) {
                return false;
            }
        } else {
            allCache.put(board, new TreeMap<>());
        }

        Map<Integer, List<Integer>> poss = new HashMap<>();

        // check clear losses first
        for (int pos : board.get(false)) {
            List<Integer> moves = board.possibleMoves(pos);
            poss.put(pos, moves);
            for (int move : moves) {
                if (board.cells[move] == 'Q') {
                    allCache.get(board).put(movesLeft, false);
                    return false;
                }
            }
        }

        for (int pos : poss.keySet()) {
            List<Integer> moves = poss.get(pos);
            for (int move : moves) {
                for (Board newBoard : board.move(pos, move)) {
                    if (newBoard == null) {
                        allCache.get(board).put(movesLeft, false);
                        return false;
                    }

                    boolean anyWin = anyWin(newBoard, movesLeft - 1);
                    if (!anyWin) {
                        allCache.get(board).put(movesLeft, false);
                        return false;
                    }
                }
            }
        }
        allCache.get(board).put(movesLeft, true);
        return true;
    }

    static class Board {

        char[] cells;

        public Board() {
            cells = new char[16];
        }

        public Board(char[] cells) {
            this.cells = cells;
        }

        public char get(int pos) {
            return cells[pos];
        }

        Board[] move(int posFrom, int posTo) {
            if (cells[posTo] == 'q' || cells[posTo] == 'Q') {
                return null;
            }
            char[] newCells = cells.clone();
            char piece = newCells[posFrom];
            newCells[posFrom] = EMPTY_CELL;
            newCells[posTo] = piece;

            if (((piece == 'P') && (posTo >= 12)) || ((piece == 'p') && (posTo < 4))) {
                Board[] boards = new Board[3];
                boolean white = isWhite(piece);

                newCells[posTo] = white ? 'N' : 'n';
                boards[0] = new Board(newCells);

                newCells = newCells.clone();
                newCells[posTo] = white ? 'B' : 'b';
                boards[1] = new Board(newCells);

                newCells = newCells.clone();
                newCells[posTo] = white ? 'R' : 'r';
                boards[2] = new Board(newCells);

                return boards;
            } else {
                return new Board[] {new Board(newCells)};
            }
        }

        public List<Integer> get(boolean isWhite) {
            List<Integer> p = new ArrayList<>();
            for (int i = 0; i < cells.length; i++) {
                char c = cells[i];
                if (c != EMPTY_CELL && (isWhite == isWhite(c))) {
                    p.add(i);
                }

            }
            return p;
        }

        boolean isWhite(char c) {
            return c < 'a';
        }

        public void put(int row, int col, char type) {
            cells[4 * row + col] = type;
        }

        List<Integer> possibleMoves(int pos) {
            char piece = get(pos);
            List<Integer> possibilities = new ArrayList<>();

            if (piece == 'p' || piece == 'P') {
                int rowDiff = piece == 'p' ? -1 : 1;
                {
                    int newPos = Position.add(pos, rowDiff, 0);
                    char cell = get(newPos);
                    if (cell == EMPTY_CELL) {
                        possibilities.add(newPos);
                    }
                }

                for (int colDiff = -1; colDiff <= 1; colDiff += 2) {
                    int newPos = Position.add(pos, rowDiff, colDiff);
                    if (newPos >= 0) {
                        char cell = get(newPos);
                        if (cell != EMPTY_CELL && (isWhite(piece) ^ isWhite(cell))) {
                            possibilities.add(newPos);
                        }
                    }
                }
            } else {


                // special case for knight
                int maxSteps = piece == 'n' || piece == 'N' ? 1 : 3;

                for (int[] d : dir.get(piece)) {
                    for (int steps = 1; steps <= maxSteps; steps++) {
                        int newPos = Position.add(pos, d[0] * steps, d[1] * steps);
                        if (newPos < 0) {
                            break;
                        }
                        char cell = get(newPos);
                        if (cell == EMPTY_CELL) {
                            possibilities.add(newPos);
                        } else {
                            if (isWhite(piece) == isWhite(cell)) {
                                break;
                            } else {
                                possibilities.add(newPos);
                                break;
                            }
                        }
                    }
                }
            }
            return possibilities;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Board board = (Board) o;
            return Arrays.equals(cells, board.cells);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(cells);
        }
    }

    static class Position {
        public static int add(int pos, int row, int col) {
            row = pos / 4 + row;
            col = pos % 4 + col;
            if (row < 0 || row >= 4 || col < 0 || col >= 4) {
                return -1;
            } else {
                return 4 * row + col;
            }
        }
    }

}
