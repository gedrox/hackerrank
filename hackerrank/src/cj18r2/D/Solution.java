package cj18r2.D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int R, C;
    static char[][] A;
    // ugly
    static final Coloring[] COLORINGS = {
        new Coloring(new char[][]{{'W'}}),
        new Coloring(new char[][]{{'B'}}),   
        new Coloring(new char[][]{{'B'}, {'W'}}),
        new Coloring(new char[][]{{'W'}, {'B'}}),
        new Coloring(new char[][]{{'W', 'B'}}),
        new Coloring(new char[][]{{'B', 'W'}}),
        new Coloring(new char[][]{{'W', 'W'}, {'W', 'B'}}),
        new Coloring(new char[][]{{'W', 'W'}, {'B', 'W'}}),
        new Coloring(new char[][]{{'W', 'B'}, {'W', 'W'}}),
        new Coloring(new char[][]{{'W', 'B'}, {'B', 'W'}}),
        new Coloring(new char[][]{{'W', 'B'}, {'B', 'B'}}),
        new Coloring(new char[][]{{'B', 'W'}, {'W', 'W'}}),
        new Coloring(new char[][]{{'B', 'W'}, {'W', 'B'}}),
        new Coloring(new char[][]{{'B', 'W'}, {'B', 'B'}}),
        new Coloring(new char[][]{{'B', 'B'}, {'W', 'B'}}),
        new Coloring(new char[][]{{'B', 'B'}, {'B', 'W'}}),
    };
    
    static class Coloring {
        char[][] pattern;
        char nw, ne, sw, se;

        public Coloring(char[][] pattern) {
            this.pattern = pattern;
            nw = pattern[0][0];
            sw = pattern.length > 1 ? pattern[1][0] : nw;
            ne = pattern[0].length > 1 ? pattern[0][1] : nw;
            se = pattern.length > 1 && pattern[1].length > 1 ? pattern[1][1] : (pattern.length > 1 ? sw : ne);
        }
    }
    
    static int readInt() throws IOException {
        return (int) readLong();
    }
    private static int[] readIntArray(int n) throws IOException {
        int[] V = new int[n];
        for (int i = 0; i < n; i++) V[i] = readInt();
        return V;
    }
    static long readLong() throws IOException {
        long res = 0;
        int sign = 1;
        while ((ch < '0' || ch > '9') && ch != '-') ch = br.read();
        if (ch == '-') {
            sign = -1;
            ch = br.read();
        }
        while (ch >= '0' && ch <= '9') {
            res = 10 * res + (ch - '0');
            ch = br.read();
        }
        return sign * res;
    }
    static String readString() throws IOException {
        StringBuilder str = new StringBuilder();
        while (!(ch != ' ' && ch != '\n' && ch != '\r' && ch != '\t' && ch != 0)) {
            ch = br.read();
        }
        while (ch != ' ' && ch != '\n' && ch != '\r' && ch != '\t' && ch != 0) {
            str.append((char) ch);
            ch = br.read();
        }
        return str.toString();
    }

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        int T = readInt();

        for (int t_i = 1; t_i <= T; t_i++) {
            readInput();
            int answer = solve();
            sb.append("Case #").append(t_i).append(": ").append(answer).append('\n');
        }

        System.out.print(sb.toString());
    }

    static void readInput() throws IOException {
        R = readInt();
        C = readInt();
        A = new char[R][];
        for (int i = 0; i < R; i++) {
            A[i] = readString().toCharArray();
        }
    }

    static int solve() {
        
        int max = 0;
        
        // how many to split in front
        for (int splitR = 0; splitR < R; splitR++) {
            for (int splitC = 0; splitC < C; splitC++) {
                for (Coloring col : COLORINGS) {
                    
                    boolean[][] connected = new boolean[R + 2][C + 2];
                    
                    int count = 0;
                    boolean hasPattern = false;
                    for (int r = 0; r < R; r++) {
                        for (int c = 0; c < C; c++) {
                            boolean n = r < splitR;
                            boolean w = c < splitC;
                            char match = (n && w) ? col.nw : (n ? col.ne : (w ? col.sw : col.se));
                            
                            if (match == A[r][c]) {
                                connected[r + 1][c + 1] = true;
                                count++;
                            }

                            if (!hasPattern) {
                                boolean patternOk = true;
                                patternMatch:
                                for (int i = 0; i < col.pattern.length; i++) {
                                    for (int j = 0; j < col.pattern[i].length; j++) {
                                        if (r + i == R || c + j == C) {
                                            patternOk = false;
                                            break patternMatch;
                                        }
                                        if (A[r + i][c + j] != col.pattern[i][j]) {
                                            patternOk = false;
                                            break patternMatch;
                                        }
                                    }
                                }
                                
                                hasPattern = patternOk;
                            }
                            
                        }
                    }
                    
                    if (count <= max || !hasPattern) continue;
                    
                    int[][] q = new int[count][];

                    int low = 0, high = 0, lastLow = 0;
                    
                    // largest connected
                    for (int x = 0; x < R; x++) {
                        for (int y = 0; y < C; y++) {
                            
                            if (!connected[x + 1][y + 1]) continue;
                            
                            q[high++] = new int[] {x, y};
                            connected[x + 1][y + 1] = false;
                            
                            while (low < high) {
                                int[] next = q[low++];
                                for (int[] diff : new int[][] {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                                    if (connected[next[0] + diff[0] + 1][next[1] + diff[1] + 1]) {
                                        connected[next[0] + diff[0] + 1][next[1] + diff[1] + 1] = false;
                                        q[high++] = new int[] {next[0] + diff[0], next[1] + diff[1]};
                                    }
                                }
                            }

                            max = Math.max(max, high - lastLow);
                            lastLow = low;
                        }
                    }
                    
                }
            }
        }
        
        return max;
    }
}
