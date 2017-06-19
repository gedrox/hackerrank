//package chess;
//
//import java.util.Objects;
//import java.util.Scanner;
//
//public class Solution {
//
//    static Cell EMPTY_CELL = new Cell() {};
//
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int T = sc.nextInt();
//        for (int t = 0; t < T; t++) {
//
//            Board board = new Board();
//
//            int whiteCount = sc.nextInt();
//            int blackCount = sc.nextInt();
//            int moves = sc.nextInt();
//
//            for (int wh_i = 0; wh_i < whiteCount; wh_i++) {
//                PieceType type = PieceType.from(sc.next());
//                int col = sc.next().charAt(0) - 'A';
//                int row = sc.nextInt() - 1;
//                board.cells[row][col] = new Piece(type, Color.WHITE);
//            }
//
//            for (int bl_i = 0; bl_i < blackCount; bl_i++) {
//                PieceType type = PieceType.from(sc.next());
//                int col = sc.next().charAt(0) - 'A';
//                int row = sc.nextInt() - 1;
//                board.cells[row][col] = new Piece(type, Color.BLACK);
//            }
//
//            System.out.println(board);
//        }
//    }
//
//    static class Board {
//        Cell[][] cells = new Cell[4][4];
//
//        Color moves = Color.WHITE;
//
//        {
//            for (int i = 0; i < 4; i++) {
//                cells[i] = new Cell[4];
//                for (int i1 = 0; i1 < 4; i1++) {
//                    cells[i][i1] = EMPTY_CELL;
//                }
//            }
//        }
//
//        boolean wins() {
//
//        }
//    }
//
//    static class Cell {
//
//    }
//
//    static class Piece extends Cell {
//        PieceType type;
//        Color color;
//
//        public Piece(PieceType type, Color color) {
//            this.type = type;
//            this.color = color;
//        }
//    }
//
//    enum Color {
//        WHITE, BLACK
//    }
//
//    enum PieceType {
//        QUEEN("Q"),
//        ROOK("R"),
//        BISHOP("B"),
//        KNIGHT("N");
//
//        private String q;
//
//        PieceType(String q) {
//            this.q = q;
//        }
//
//        static PieceType from(String q) {
//            for (PieceType pieceType : values()) {
//                if (Objects.equals(pieceType.q, q)) {
//                    return pieceType;
//                }
//            }
//            throw new IllegalArgumentException();
//        }
//    }
//}
