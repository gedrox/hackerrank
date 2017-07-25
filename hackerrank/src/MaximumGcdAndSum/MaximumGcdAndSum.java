package MaximumGcdAndSum;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;

public class MaximumGcdAndSum {

    static int max = 1000000;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        String[] split = br.readLine().split(" ");
        int[] A = new int[n];
        for (int A_i = 0; A_i < n; A_i++) {
            A[A_i] = Integer.parseInt(split[A_i]);
        }

        split = br.readLine().split(" ");
        int[] B = new int[n];
        for (int B_i = 0; B_i < n; B_i++) {
            B[B_i] = Integer.parseInt(split[B_i]);
        }

        int res = solve(A, B);
        
        System.out.println(res);
    }

    private static int solve(int[] A, int[] B) {
        int[][] d = new int[max + 1][120];
        int[] d_i = new int[max + 1];
        for (int i = 2; i <= Math.sqrt(max); i++) {
            for (int j = i * i; j < d.length; j += i) {
                d[j][d_i[j]++] = i;
            }
        }

        int[] amap = new int[max + 1];
        int[] bmap = new int[max + 1];

        for (int a : A) {
            if (amap[a] != 0) continue;

            amap[a] = a;
            for (int i = 0; i < d_i[a]; i++) {
                int div = d[a][i];
                if (amap[a / div] < a) {
                    amap[a / div] = a;
                }
                if (amap[div] < a) {
                    amap[div] = a;
                }
            }
            if (a > amap[1]) {
                amap[1] = a;
            }
        }

        for (int b : B) {
            if (bmap[b] != 0) continue;

            bmap[b] = b;
            for (int i = 0; i < d_i[b]; i++) {
                int div = d[b][i];
                if (bmap[b / div] < b) {
                    bmap[b / div] = b;
                }
                if (bmap[div] < b) {
                    bmap[div] = b;
                }
            }
            if (b > bmap[1]) {
                bmap[1] = b;
            }
        }

        int i = max;
        while (amap[i] == 0 || bmap[i] == 0) {
            while (amap[i] == 0) i--;
            while (bmap[i] == 0) i--;
        }

        return amap[i] + bmap[i];
    }
    
    static int trivialSolve(int A[], int[] B) {
        int max = 0;
        int maxSum = 0;

        for (int a : A) {
            for (int b : B) {
                int g = gcd(a, b);
                if (g > max || (g == max && a + b > maxSum)) {
                    max = g;
                    maxSum = a + b;
                }
            }
        }
        return maxSum;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            a = a % b;
            int tmp = a;
            a = b;
            b = tmp;
        }
        return a;
    }
    
    @Test
    public void test() {
        int[] A = {3, 1, 4, 2, 8};
        int[] B = {5, 2, 12, 8, 3};
        System.out.println(solve(A, B));
        System.out.println(trivialSolve(A, B));
    }
    
    @Test
    public void testLarge() {
        int n = 100;
        int[] A = new int[n];
        int[] B = new int[n];
        Random r = new Random(0);
        for (int i = 0; i < n; i++) {
            A[i] = r.nextInt(1000000) + 1;
            B[i] = r.nextInt(1000000) + 1;
        }

//        System.out.println(Arrays.toString(A));
//        System.out.println(Arrays.toString(B));
        
        Assert.assertEquals(trivialSolve(A, B), solve(A, B));
    }
}
