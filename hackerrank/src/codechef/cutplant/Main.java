package codechef.cutplant;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeSet;

public class Main {

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
    
    static int T;
    static int N;
    static int[] A, B;
    
    public static void main(String[] args) throws IOException {
        T = readInt();
        for (int i = 0; i < T; i++) {
            N = readInt();
            A = readIntArray(N);
            B = readIntArray(N);
            
            int answer = solve();
            if (answer != trivial()) {
                throw new RuntimeException("" + trivial());
            }
            System.out.println(answer);
        }
        
    }

    private static int solve() {

        TreeSet<Integer> cuts = new TreeSet<>();

        int result = 0;
        
        for (int i = 0; i < N; i++) {
            
            if (A[i] < B[i]) return -1;
            
            while (!cuts.isEmpty() && cuts.last() > A[i]) {
                cuts.remove(cuts.last());
            }
            while (!cuts.isEmpty() && cuts.first() < B[i]) {
                cuts.remove(cuts.first());
            }
            
            if (A[i] == B[i]) continue;
            
            if (!cuts.contains(B[i])) {
                cuts.add(B[i]);
                result++;
            }
        }
        
        return result;
    }
    
    @Test
    public void testBig() {
        N = 100_000;
        A = new int[N];
        B = new int[N];

        long seed = System.currentTimeMillis();
        Random r = new Random(seed);
        for (int i = 0; i < N; i++) {
            A[i] = N - i + 1;
            B[i] = (N - i)/2;
        }

        System.out.println(solve());
    }
    
    @Test
    public void test() {
        N = 100_000;
        A = new int[N];
        B = new int[N];
        
        while (true) {
            long seed = System.currentTimeMillis();
            Random r = new Random(seed);
            for (int i = 0; i < N; i++) {
                A[i] = r.nextInt(10) + 1;
                B[i] = r.nextInt(A[i]) + 1;
            }

//            System.out.println(Arrays.toString(A));
//            System.out.println(Arrays.toString(B));
//            System.out.println(solve());
//            System.out.println(trivial());
            
            if (solve() != trivial()) {
                throw new RuntimeException(seed + " <-- seed");
            }
            System.out.println("OK");
        }
    }
    
    static int trivial() {
        int[] A = Main.A.clone();
        int result = 0;
        for (int i = 0; i < N; i++) {
            if (A[i] < B[i]) return -1;
            if (A[i] == B[i]) continue;
            
            result++;
            
            // cut
            A[i] = B[i];
            for (int j = i + 1; j < N; j++) {
                if (A[j] < B[i]) break;
                if (B[i] < B[j]) break;
                A[j] = B[i];
            }
        }
        return result;
    }
}
