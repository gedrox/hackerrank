package coprimepaths;

import org.junit.Test;

import java.util.Random;

public class T {

    public static final Random R = new Random();

    public static void main(String[] args) {
        System.out.println("25000 25000");
        for (int i = 0; i < 25000; i++) {

            randNodeValues(R);
        }
        System.out.println();
        for (int i = 2; i <= 25000; i++) {
            System.out.println(i + " " + (R.nextInt(i-1) + 1));
        }
        for (int i = 1; i <= 25000; i++) {
            System.out.println((R.nextInt(25000) + 1) + " " + (R.nextInt(25000) + 1));
        }
    }

    private static void randNodeValues(Random r) {
        int val = 1;
        val *= S.PRIMES[r.nextInt(20)];
        val *= S.PRIMES[r.nextInt(30)];
        val *= S.PRIMES[r.nextInt(50)];

        System.out.print(val + " ");
    }

    @Test
    public void bad() {
        int N = 25000;
        System.out.println(N + " " + N);
        for (int i = 0; i < N; i++) {
            randNodeValues(R);
        }
        System.out.println();
        for (int i = 2; i <= N; i++) {
            System.out.println(i-1 + " " + i);
        }
        for (int i = 1; i <= N; i++) {
            System.out.println(i + " " + N);
        }
    }

    @Test
    public void funky() {
        System.out.println("25000 25000");
        for (int i = 0; i < 25000; i++) {
            randNodeValues(R);
        }
        System.out.println();
        for (int i = 2; i <= 25000; i++) {
            System.out.println(1 + " " + i);
        }
        for (int i = 1; i <= 25000; i++) {
            System.out.println(i + " " + 25000);
        }
    }
}
