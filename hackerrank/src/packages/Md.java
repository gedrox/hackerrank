package packages;

import java.util.Scanner;

public class Md {


    static long gcd(long a, long b) {
        while (a != 0) {
            b = b % a;
            long tmp = a;
            a = b;
            b = tmp;
        }
        return b;
    }

    /*
     * Complete the function below.
     */
    static long sumOfMagicValues(long[] A) {
        // Return the sum of the magic values of all nonempty contiguous subarrays of A modulo 10^9+7.

        long sum = 0;

        for (int i = 0; i < A.length; i++) {
            long min = A[i], max = A[i];
            for (int j = i; j < A.length; j++) {

                // calc if add next
                if (j > i) {

                    //min = Integer.MAX_VALUE;
                    //max = 0;

                    long gcd = A[j];
                    for (int x = j; x >= i; x--) {
                        gcd = gcd(gcd, A[x]);
                        long v = gcd * (x - i + 1);
                        min = Math.min(min, v);
                        max = Math.max(max, v);
                    }


                }

                int s = j - i + 1;
                sum += (max - min) * s;
                sum %= 1000000007;
            }
        }

        return sum;
    }


    private static final Scanner scan = new Scanner(System.in);

    public static void main(String[] args) {
        int n = scan.nextInt();

        long[] A = new long[n];

        for (int AItr = 0; AItr < n; AItr++) {
            A[AItr] = scan.nextInt();

        }

        long result = sumOfMagicValues(A);

        System.out.println(result);
    }
}
