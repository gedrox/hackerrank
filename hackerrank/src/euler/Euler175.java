package euler;

import org.junit.Test;

public class Euler175 {
    public static void main(String[] args) {
        
        
    }
    
    long solve(long n) {
        long X = 1;
        long Y = 0;
        int a = 0;
        while (n != 0) {
            if (n % 2 == 1) {
                X += Y;
                Y += X * a;
                a = 0;
            } else {
                a++;
            }
            n /= 2;
        }

        return X + Y;
    }
    
    @Test
    public void test() {
        System.out.println(solve(10));
        System.out.println(solve(241));
        System.out.println(solve(240));

        for (int i = 0; i < 100; i++) {
            System.out.println(i + "\t" + solve(i));
        }
    }
}
