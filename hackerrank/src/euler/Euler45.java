package euler;

import java.util.Scanner;
import java.util.function.Function;

public class Euler45 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        long n = sc.nextLong();
        int a = sc.nextInt();
        int b = sc.nextInt();

        Function<Long, Long> fun = a == 3 ?
                x -> x * (x + 1) / 2 :
                x -> x * (2 * x - 1);

        long i = 1;
        long res;
        while ((res = fun.apply(i)) < n) {

            long discr = 24 * res + 1;
            long sqrt = Math.round(Math.sqrt(discr));
//            if ((sqrt + 1) * (sqrt + 1) <= discr) {
//                throw new RuntimeException()
//            }
            if (sqrt * sqrt == discr && (sqrt % 6) == 5) {
                System.out.println(res);
            }

            i++;
        }
    }

}
