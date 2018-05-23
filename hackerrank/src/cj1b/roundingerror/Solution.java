package cj1b.roundingerror;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeMap;

public class Solution {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static int N;
    private static int L;
    private static int[] C;

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
            N = readInt();
            L = readInt();
            C = readIntArray(L);

            int answer = solve();
            
            sb.append("Case #").append(t_i).append(": ").append(answer).append('\n');
        }

        System.out.print(sb.toString());
    }

    @Test
    public void test() {
        N = 7;
        L = 0;
        C = new int[0];
        System.out.println(solve());
    }
    
    private static int solve() {
        
        ArrayList<Integer> newC = new ArrayList<>();
        for (int aC : C) {
            newC.add(aC);
        }
        
        int[] steps = new int[N];
        int lastGood = Integer.MAX_VALUE;
        for (int n = N - 1; n >= 0; n--) {
            if ((100 * n) % N < (N + 1) / 2) {
                steps[n] = lastGood - n;
            } else {
                steps[n] = 0;
                lastGood = n;
            }
        }

//        System.err.println(Arrays.toString(steps));
        
//        int thr = 100 * 1 / N

        ArrayList<Rem> toAdd = new ArrayList<>();
        for (int i = 0; i < C.length; i++) {
            if (steps[C[i]] > 0 && steps[C[i]] < steps[0]) {
                toAdd.add(new Rem(i, steps[C[i]]));
            }
        }
        
        toAdd.sort(Comparator.comparing(r -> r.toAdd));
        
        int remN = N;
        for (int i = 0; i < L; i++) {
            remN -= C[i];
        }

        for (Rem rem : toAdd) {
            if (rem.toAdd <= remN) {
                newC.set(rem.i, C[rem.i] + rem.toAdd);
                remN -= rem.toAdd;
            } else {
                break;
            }
        }
        
        
        while (remN >= steps[0]) {
            remN -= steps[0];
            newC.add(steps[0]);
        }
        
        newC.add(remN);

        int answer = 0;
        for (int c : newC) {
            answer += (100 * c + N / 2) / N;
        }
        
        return answer;
    }
    
    static class Rem {
        int i, toAdd;

        public Rem(int i, int toAdd) {
            this.i = i;
            this.toAdd = toAdd;
        }
    }

}
