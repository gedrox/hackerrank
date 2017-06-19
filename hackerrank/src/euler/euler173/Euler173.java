package euler.euler173;

import java.util.Scanner;

public class Euler173 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        long X = sc.nextLong();
        
        long d = 2;
        long sum = 0;
        while (true) {
            double count = (X + d * d) / 2 / d - d;
            if (count > 0) {
                sum += count;
            } else {
                break;
            }
            d += 2;
        }

        System.out.println(sum);
    }
}
