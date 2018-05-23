package cj1b.signs;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

public class Solution {
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static int S;
    private static int[] D, A, B;

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
    static char readChar() throws IOException {
        while (ch == ' ' || ch == '\n' || ch == '\r' || ch == '\t') ch = br.read();
        char oneChar = (char) ch;
        ch = br.read();
        return oneChar;
    }

    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        int T = readInt();

        for (int t_i = 1; t_i <= T; t_i++) {
            readInput();
            int[] answer = solve();

            sb.append("Case #").append(t_i).append(": ")
                    .append(answer[0])
                    .append(" ")
                    .append(answer[1])
                    .append('\n');
        }

        System.out.print(sb.toString());
    }

    private static int[] solve() {
        int[][] v = new int[2][S];
        for (int i = 0; i < S; i++) {
            v[0][i] = D[i] + A[i];
            v[1][i] = D[i] - B[i];
        }

        int max = 0;
        int cnt = 0;

        HashSet<Integer> starts = new HashSet<>();
        
        for (int dir = 0; dir < 2; dir++) {
            for (int start = 0; start < S; start++) {
                int v1 = v[dir][start];
                if (start > 0 && v[dir][start - 1] == v1) continue;
                int end = start + 1;
                while (end < S && v[dir][end] == v1) end++;
                if (end < S) {
                    int v2 = v[1-dir][end];
                    
                    if (start > 0 && v[1-dir][start - 1] == v2) continue;
                    
                    end++;
                    while (end < S && (v[dir][end] == v1 || v[1-dir][end] == v2)) end++;
                } else {
                    if (start == 0) return new int[] {S, 1};
                    continue;
                }
                
                if (end - start > max) {
                    max = end - start;
                    starts.clear();
                    starts.add(start);
                } else if (end - start == max) {
                    starts.add(start);
                }
            }
        }
        
        return new int[] {max, starts.size()};
    }
    
    @Test
    public void test() {
        S = 5;
        D = new int[] {2,6,8,11,13};
        A = new int[] {7,3,10,11,9};
        B = new int[] {12,11,1,12,14};
        
        int[] v = solve();

        System.out.println(v[0]);
        System.out.println(v[1]);
    }

    @Test
    public void test2() {
        S = 100_000;
        D = new int[S];
        A = new int[S];
        B = new int[S];

        Random r = new Random(0);
        for (int i = 0; i < S; i++) {
            D[i] = i;
            A[i] = -i + r.nextInt(2) * r.nextInt(3);
            B[i] = i + r.nextInt(2) * r.nextInt(3);
        }
        
        int[] v = solve();

        System.out.println(v[0]);
        System.out.println(v[1]);
    }
    
    private static int[] solveWrng() {

        HashMap<Integer, Integer> east = new HashMap<>();
        HashMap<Integer, Integer> west = new HashMap<>();
        HashMap<Integer, HashMap<Integer, Integer>> x = new HashMap<>();

        BiFunction<Integer, Integer, Integer> intIncr = (k, v) -> v == null ? 1 : v + 1;
        for (int i = 0; i < S; i++) {
            east.compute(D[i] + A[i], intIncr);
            west.compute(D[i] - B[i], intIncr);
            x.computeIfAbsent(D[i] + A[i], (k) -> new HashMap<>())
                    .compute(D[i] - B[i], intIncr);
        }
        
        Integer[] E = east.keySet().toArray(new Integer[0]);
        Integer[] W = west.keySet().toArray(new Integer[0]);
        
        // descending
        Arrays.sort(E, Comparator.comparing(k -> -east.get(k)));
        Arrays.sort(W, Comparator.comparing(k -> -west.get(k)));

        int max = 0;
        int cnt = 0;

        for (int e : E) {
            for (int w : W) {
                int sum = east.get(e) + west.get(w) - x.get(e).getOrDefault(w, 0);
                if (sum < max) break;
                if (sum == max) cnt++; else {
                    max = sum;
                    cnt = 1;
                }
            }
        }
        
        return new int[] {max, cnt};
    }

    private static void readInput() throws IOException {
        S = readInt();
        D = new int[S];
        A = new int[S];
        B = new int[S];
        for (int i = 0; i < S; i++) {
            D[i] = readInt();
            A[i] = readInt();
            B[i] = readInt();
        }
    }
}
