package codechef.chefpar;

import org.junit.Test;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Main {
//        private static final InputStream IN = System.in;
    private static final InputStream IN = IN();
    public static int[] PRIMES;
    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(IN));
    private static int N;
    private static int M;
    private static int K;
    private static int[] A;
    private static int[] P;
    private static int[] D;

    static FileInputStream IN() {
        try {
            return new FileInputStream("src/codechef/chefpar/4_0.in");
//            return System.in;
        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public static void main(String[] args) throws IOException {


        readInput();

        long t0 = System.currentTimeMillis();
        oldSolve(t0);
//        solve(t0);

        for (int i = 0; i < N; i++) {
            System.out.print(A[i] + D[i] + " ");
        }
    }
    
    static class Step implements Comparable<Step> {
        int index;
        BigInteger value;

        public Step(int index, BigInteger value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Step o) {
            return this.value.compareTo(o.value);
        }
    }
    
    private static void solve(long t0) {

//        Arrays.sort(A);

        System.err.println("start");
        if (D == null) D = new int[N];

        System.out.println("Initial result is " + getResult(productA()));

        BigInteger mult = productA();
        BigInteger multP = product(P);

        BigInteger target = multP.subtract(mult.mod(multP));

        System.err.println("steps start");

        Step[] steps = new Step[N];
        for (int i = 0; i < A.length; i++) {
            steps[i] = new Step(i, mult.divide(BigInteger.valueOf((long) A[i] + D[i])).mod(multP));
        }
        Arrays.sort(steps);

        System.err.println("steps done");

        int pos = Arrays.binarySearch(steps, new Step(-1, target));
        System.err.println("pos found " + pos);
        D[steps[-pos - 2].index] += 1;
        
        System.out.println("Later result is " + getResult(productA()));

        BigInteger X = mult.divide(multP);
    }
    
    static void oldSolve(long t0) {

        D = new int[N];
        BigInteger mult = productA();
        
        long maxSum = getResult(mult);
        
        Random r = new Random(42);
        int step = 0;

        while (System.currentTimeMillis() - t0 < 7900) {
            step++;
            int ai = r.nextInt(N);

            BigInteger toAdd = mult
                    .divide(BigInteger.valueOf((long) A[ai] + D[ai]));
            
            int di = r.nextInt(K + 1);

            BigInteger newMult = toAdd
                    .multiply(BigInteger.valueOf((long) A[ai] + di));

            int oldD = D[ai];
            D[ai] = di;
            long maxSum2 = getResult(newMult);

            if (maxSum2 > maxSum) {
                maxSum = maxSum2;
                mult = newMult;
                System.err.println(step + ": " + maxSum2);
            } else {
                D[ai] = oldD;
            }
        }

    }

    private static long getResult(BigInteger mult) {
        long sum = 0;
        for (int p : P) {
            sum += mult.mod(BigInteger.valueOf(p)).intValue();
        }
        return sum;
    }

    private static BigInteger product(int[] array) {
        BigInteger mult = BigInteger.ONE;
        for (int anArray : array) {
            mult = mult.multiply(BigInteger.valueOf(anArray));
        }
        return mult;
    }

    private static BigInteger productA() {
        BigInteger mult = BigInteger.ONE;
        for (int i = 0; i < A.length; i++) {
            mult = mult.multiply(BigInteger.valueOf((long) A[i] + D[i]));
        }
        return mult;
    }

    private static void readInput() throws IOException {
        N = readInt();
        M = readInt();
        K = readInt();
        A = readIntArray(N);
        P = readIntArray(M);
    }

    private static int nextPrime(int x) {
        if (PRIMES == null) {
            PRIMES = getPrimes(2_000_000_000);
        }
        int pos = Arrays.binarySearch(PRIMES, x);
        if (pos < 0) {
            pos = -pos - 1;
        } else {
            pos++;
        }
        return PRIMES[pos];
    }

    private static int[] getPrimes(int max) {
        long t0 = System.currentTimeMillis();
        ArrayList<Integer> primes = new ArrayList<>();
        primes.add(2);
        boolean p[] = new boolean[(max - 1) / 2];
        for (int i = 0; i < p.length; i++) {
            if (!p[i]) {
                primes.add(2 * i + 3);
                long next = 3L * i + 3;
                while (next < p.length) {
                    p[(int) next] = true;
                    next += 2 * i + 3;
                }
            }
        }

        int[] pr = new int[primes.size()];
        for (int i = 0; i < primes.size(); i++) {
            pr[i] = primes.get(i);
        }
        System.err.println("done generating primes in " + (System.currentTimeMillis() - t0));

        return pr;
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void sampleData1() throws IOException {

        for (int c = 1; c <= 4; c++) {
            for (int x = 0; x < 10; x++) {

                Random rnd = new Random(1000 * c + x);
                int INF = 1_000_000_000;
                N = (c == 1) ? 100 : ((c == 4) ? 10_000 : 1_000);
                M = (c == 1) ? 10_000 : ((c == 4) ? 100 : 1_000);
                K = rnd.nextInt(((c == 1) ? 100_000 : ((c == 2) ? 1_000 : 1_000_000)) + 1);
                A = new int[N];
                for (int i = 0; i < N; i++) {
                    A[i] = rnd.nextInt(INF) + 1;
                }

                int X = rnd.nextInt(INF / 2 - 1) + INF / 10 + INF;

                P = new int[M];
                for (int i = 0; i < M; i++) {
                    P[i] = nextPrime(X);
                    X = P[i];
                }

                StringBuilder sb = new StringBuilder();
                sb.append(N).append(' ').append(M).append(' ').append(K).append('\n');
                for (int i = 0; i < N; i++) {
                    sb.append(A[i]).append(' ');
                }
                sb.append('\n');
                for (int i = 0; i < M; i++) {
                    sb.append(P[i]).append(' ');
                }
                sb.append('\n');

                String fn = String.format("src/codechef/chefpar/%d_%d.in", c, x);
                Files.write(Paths.get(fn), sb.toString().getBytes());
            }
        }
    }
}
