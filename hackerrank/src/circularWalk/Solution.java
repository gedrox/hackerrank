package circularWalk;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Solution {

    Random r = new Random(0);
    
    @Test
    public void test() {

        System.out.println(circularWalk(10, 0, 9, 1, 0, 2, 4));

        for (int i = 0; i < 0; i++) {
            long t0 = System.currentTimeMillis();
        
            int n = 10000000;
            int p = r.nextInt(n/100) + 1;
            int s = r.nextInt(n);
            int t = r.nextInt(n);
            int r_0 = r.nextInt(p);
            int g = r.nextInt(p);
            int seed = r.nextInt(p);
    
            System.out.println(circularWalk(n, s, t, r_0, g, seed, p));
            System.out.println(System.currentTimeMillis() - t0 + "ms p:" + p);
        }
    }
    
    static int circularWalk(int n, int s, int t, int r_0, int g, int seed, int p){
        
        int r[] = new int[n];
        r[0] = r_0;
        for (int i = 1; i < n; i++) {
            r[i] = (int) ((((long) r[i-1]) * g + seed) % p);
        }

        System.err.println(Arrays.toString(r));
        
//        System.err.println(Arrays.toString(Arrays.copyOfRange(r, r.length - 10, r.length)));
//        System.err.println("...");
//        System.err.println(Arrays.toString(Arrays.copyOfRange(r, 0, 10)));
        
        int left = s, right = s;
        int prevLeft = s + 1;
        int prevRight = s;
        int steps = 0;
        while (!between(t, left, right, n)) {
            steps++;
            int[] minMax = {left, right};
            boolean broadened = false;
            for (int i = left; i < prevLeft; i++) {
                broadened |= broadenMinMax(n, r, minMax, i);
            }
            for (int i = right; i > prevRight; i--) {
                broadened |= broadenMinMax(n, r, minMax, i);
            }
            if (!broadened) {
                return -1;
            }
            
            prevLeft = left;
            prevRight = right;
            
            left = minMax[0];
            right = minMax[1];
        }
        
        return steps;
    }

    private static boolean broadenMinMax(int n, int[] r, int[] minMax, int i) {
        boolean broadened = false;
        int checkI = ((i % n) + n) % n;
        if (i - r[checkI] < minMax[0]) {
            minMax[0] = i - r[checkI];
            broadened = true;
        }
        if (i + r[checkI] > minMax[1]) {
            minMax[1] = i + r[checkI];
            broadened = true;
        }
        return broadened;
    }

    private static boolean between(int t, int left, int right, int n) {
        // full circle
        if (right - left >= n - 1) return true;
        // between
        if (left <= t && t <= right) return true;
        // overflow left
        if (left < 0 && t >= ((left % n) + n) % n) return true;
        // overflow right
        if (right >= n && t <= right % n) return true;
        return false;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int s = in.nextInt();
        int t = in.nextInt();
        int r_0 = in.nextInt();
        int g = in.nextInt();
        int seed = in.nextInt();
        int p = in.nextInt();
        int result = circularWalk(n, s, t, r_0, g, seed, p);
        System.out.println(result);
    }
}
