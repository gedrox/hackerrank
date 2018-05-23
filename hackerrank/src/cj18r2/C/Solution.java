package cj18r2.C;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Solution {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static int N;
    static int[][] A;

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
        N = readInt();
        A = new int[N][N];
        for (int i = 0; i < N; i++) {
            A[i] = readIntArray(N);
        }
    }
    

    static int solve() {
        int TOTAL = 0;
        int V = 2 * N + 2;
        int START = 0;
        int END = 2*N + 1;

        for (int type = -N; type <= N; type++) {
            if (type == 0) continue;

            int[][] graph = new int[V][V];

            for (int j = 0; j < N; j++) {
                graph[START][col(j)] = 1;
                graph[row(j)][END] = 1;
            }

            for (int x = 0; x < N; x++) {
                for (int y = 0; y < N; y++) {
                    if (A[x][y] == type) {
                        graph[col(x)][row(y)] = 1;
                    }
                }
            }
            
            do {
                int[] max = new int[V];
                max[START] = 1;

                int[] prev = new int[V];
                prev[START] = -1;

                int[] q = new int[V];
                q[0] = START;
                int low = 0, high = 1;

                while (low != high && max[END] == 0) {
                    int next = q[low++];

                    for (int i = 0; i < V; i++) {
                        // TODO: for actual max flow we would need to use max(max[next], g[next, i])
                        if (graph[next][i] > 0 && graph[next][i] > max[i]) {
                            max[i] = graph[next][i];
                            q[high++] = i;
                            prev[i] = next;
                        }
                    }
                }

                if (max[END] > 0) {
                    TOTAL += max[END];
                    
                    int x = END;
                    while (prev[x] != -1) {
                        graph[prev[x]][x] -= max[END];
                        graph[x][prev[x]] += max[END];
                        x = prev[x];
                    }
                    
                } else {
                    break;
                }
                
            } while (true);
        }
        
        return N * N - TOTAL;
    }
    
    static int col(int i) { return i + 1; }
    static int row(int i) { return i + N + 1; }
    
    static int solveSlow() {
        
        if (N > 4) return -1;
        int best = N * N - 1;

        nextCase:
        for (int i = 0; i < (1 << (N*N)); i++) {
            
            int count = Integer.bitCount(i);
            
            if (count >= best) continue;

            for (int j = 0; j < N; j++) {
                
                // check row
                boolean[] hit = new boolean[2 * N + 1];
                for (int k = 0; k < N; k++) {
                    if ((i & (1<<(j * N + k))) == 0) {
                        if (hit[A[j][k] + N]) continue nextCase;
                        hit[A[j][k] + N] = true;
                    }
                }

                hit = new boolean[2 * N + 1];
                for (int k = 0; k < N; k++) {
                    if ((i & (1<<(k * N + j))) == 0) {
                        if (hit[A[k][j] + N]) continue nextCase;
                        hit[A[k][j] + N] = true;
                    }
                }
            }
            
            best = Math.min(best, count);
        }
        
        return best;
    }
}
