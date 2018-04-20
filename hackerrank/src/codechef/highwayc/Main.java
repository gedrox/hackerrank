package codechef.highwayc;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;

public class Main {
    
    @Test
    public void testSpeed() {
        int N = 10_000;
        int[] A = new int[N];
        long K = 2_000_000_000;
        Random r = new Random();
        for (int i = 0; i < N; i++) {
            A[i] = r.nextInt(2_000_000_000) + 1;
        }

        long t0 = System.currentTimeMillis();
        BigInteger mult = BigInteger.ONE;
        for (int a : A) {
            mult = mult.multiply(BigInteger.valueOf(a + K));
        }
        System.out.println(System.currentTimeMillis() - t0 + "ms");

        System.out.println(mult.toString().length());
    }

    static int ch = 0;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    static int readInt() throws IOException {
        return (int) readLong();
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
        int T = readInt();
        StringBuilder sb = new StringBuilder();
        for (int t_i = 0; t_i < T; t_i++) {
            int N = readInt();
            int S = readInt();
            int Y = readInt();

            int[] V = readIntArray(N); // velocity
            int[] D = readIntArray(N); // direction
            int[] P = readIntArray(N); // position
            int[] C = readIntArray(N); // car length

            Car[] cars = new Car[N];
            for (int i = 0; i < N; i++) {
                cars[i] = new Car(V[i], D[i], P[i], C[i]);
            }

            double time = 0;
            double ttc = (double) Y / S;

            for (int i = 0; i < N; i++) {
                Car car = cars[i];
                if (car.hits(time, time + ttc)) {
                    time = car.whenPasses();
                }
                time += ttc;
            }

            sb.append(time).append('\n');
        }

        System.out.print(sb);
    }

    private static int[] readIntArray(int n) throws IOException {
        int[] V = new int[n];
        for (int i = 0; i < n; i++) V[i] = readInt();
        return V;
    }

    static class Car {
        int velocity, direction, position, length;

        public Car(int velocity, int direction, int position, int length) {
            this.velocity = velocity;
            this.direction = direction;
            this.position = position;
            this.length = length;
        }

        double posFront(double t) {
            return position + (2 * direction - 1) * velocity * t;
        }

        double posBack(double t) {
            return posFront(t) - (2 * direction - 1) * length;
        }

        boolean hits(double t1, double t2) {
            return posBack(t1) * posFront(t2) <= 0 || Math.abs(posFront(t2)) <= 1e-6;
        }

        double whenPasses() {
            return 1d * ((2 * direction - 1) * length - position) / ((2 * direction - 1) * velocity);
        }
    }
}
