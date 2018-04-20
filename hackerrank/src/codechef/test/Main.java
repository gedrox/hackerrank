package codechef.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;
 
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
    
    public static void main(String[] args) throws IOException {
 
        long t0 = System.currentTimeMillis();
        
        int N = readInt(), M = readInt(), K = readInt();
        int[] A = readIntArray(N);
        int[] P = readIntArray(M);
        int[] D = new int[N];
        
        BigInteger mult = BigInteger.ONE;
        for (int i = 0; i < N; i++) {
            mult = mult.multiply(BigInteger.valueOf(A[i]));
        }
        
        long maxSum = 0;
        for (int i = 0; i < M; i++) {
            maxSum += mult.mod(BigInteger.valueOf(P[i])).intValue();
        }
        
        Random r = new Random(42);
 
        while (System.currentTimeMillis() - t0 < 7800) {
            
            int ai = r.nextInt(N);
            
            BigInteger toAdd = mult.divide(BigInteger.valueOf((long) A[ai] + D[ai]));
            
            int di = r.nextInt(K + 1);
            
            BigInteger newMult = toAdd.multiply(BigInteger.valueOf((long) A[ai] + di));
            
            long maxSum2 = 0;
            for (int i2 = 0; i2 < M; i2++) {
                maxSum2 += newMult.mod(BigInteger.valueOf(P[i2])).intValue();
            }
            
            if (maxSum2 > maxSum) {
                D[ai] = di;
                maxSum = maxSum2;
                mult = newMult;
            }
        }
 
        System.err.println((double) maxSum / M);
        for (int i = 0; i < N; i++) {
            System.out.print(A[i] + D[i] + " ");
        }
    }
}
 