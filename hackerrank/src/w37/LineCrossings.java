package w37;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * https://www.hackerrank.com/contests/w37/challenges/dynamic-line-intersection
 */
public class LineCrossings {

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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
        char oneChar = (char) LineCrossings.ch;
        ch = br.read();
        return oneChar;
    }
    
    public static void main(String[] args) throws IOException {
        init();
        StringBuilder sb = new StringBuilder();
        int n = readInt();
        
        for (int n_i = 0; n_i < n; n_i++) {
            char op = readChar();
            switch (op) {
                case '+':
                case '-':
                    int k = readInt();
                    int b = readInt();
                    add(k, b, op == '+' ? 1 : -1);
                    break;
                case '?':
                    int q = readInt();
                    int res = query(q);
                    sb.append(res).append('\n');
                    break;
                default:
                    throw new RuntimeException("Bad operation");
            }
        }
        System.out.print(sb.toString());
    }

    static int SEP = 320;
    static int[][] small;
    static int[] large;
    
    static void init() {
        small = new int[SEP][SEP];
        large = new int[100_001];
    } 

    private static void add(int k, int b, int diff) {
        b = (b % k + k) % k;
        if (k < SEP) {
            small[k][b % k] += diff;
        } else {
            for (int y = b; y < large.length; y += k) {
                large[y] += diff;
            }
        }
    }

    private static int query(int q) {
        int res = large[q];
        for (int k = 1; k < SEP; k++) {
            res += small[k][q % k];
        }
        return res;
    }
    
    @Test
    public void testSample() {
        init();
        add(1, 0, 1);
        add(2, 0, 1);
        System.out.println(query(1));
        System.out.println(query(2));
    }
    
    @Test
    public void bigger() {
        init();
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        int n = 100_000;
        for (int n_i = 0; n_i < n; n_i++) {
            switch (r.nextInt(3)) {
                case 0: add(r.nextInt(100_000) + 1, r.nextInt(100_001), 1); break;
                case 1: add(r.nextInt(100_000) + 1, r.nextInt(100_001), -1); break;
                case 2: sb.append(query(r.nextInt(100_001))).append('\n'); break;
            }
        }

        System.out.print(sb.toString());
    }

    @Test
    public void addAndQuery() {
        init();
        StringBuilder sb = new StringBuilder();
        Random r = new Random(0);
        int n = 100_000;
        for (int n_i = 0; n_i < n; n_i++) {
            add(r.nextInt(SEP) + 1, r.nextInt(100_001), 1);
        }
        for (int n_i = 0; n_i < n; n_i++) {
            add(SEP, r.nextInt(100_001), 1);
        }
        for (int n_i = 0; n_i < n; n_i++) {
            add(r.nextInt(100_000) + 1, r.nextInt(100_001), 1);
        }
        for (int n_i = 0; n_i < n; n_i++) {
            sb.append(query(r.nextInt(100_001))).append('\n');
        }

        System.out.print(sb.toString());
    }
}
