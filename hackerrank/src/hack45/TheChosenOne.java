package hack45;

import java.util.Scanner;

public class TheChosenOne {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long[] a = new long[n];
        for(int a_i=0; a_i < n; a_i++){
            a[a_i] = in.nextLong();
        }
        
        if (n == 1) {
            System.out.println(a[0] + 1);
            return;
        }
        
        long[] g = new long[n], r = new long[n];
        
        g[0] = a[0];
        r[0] = a[n - 1];
        
        for (int i = 1; i < n; i++) {
            g[i] = gcd(g[i - 1], a[i]);
            r[i] = gcd(r[i - 1], a[n - 1 - i]);
        }
        
        long target = g[n - 1];
//        System.err.println(target);
        
//        int i = 0;
//        while (g[i] != target) i++;
//        int j = 0;
//        while (r[j] != target) j++;
        
        int left = n;
        long base = a[n - 1];
        
        if (n == 1) {
            System.out.println(base);
            return;
        }
        
        if (g[left - 1] != g[left - 2]) {
            System.out.println(g[left - 2]);
            return;
        }
        
        while (g[left - 1] == g[left - 2]) {
            left--;
            if (left == 1) {
                System.out.println(r[n - 2]);
                return;
            }
        }
        
        while (true) {
            
            base = r[n - 1 - left];

            g[0] = gcd(base, a[0]);
            for (int i = 1; i < left; i++) {
                g[i] = gcd(g[i - 1], a[i]);
                if (g[i] == target) {
                    
                    if (i == left - 1) {
                        System.out.println(g[i - 1]);
                        return;
                    }
                    
                    left = i + 1;
                    break;
                }
            }
        }
    }
    
    static long gcd(long x, long y) {
        while (y != 0) {
            long tmp = x % y;
            x = y;
            y = tmp;
        }
        return x;
    }
}
