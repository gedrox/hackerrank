package euler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Euler52 {

    public static final int LIMIT = 1000000;
    
    static int[] pow = new int[10];
    static {
        pow[0] = 1;
        for (int i = 1; i < 10; i++) {
            pow[i] = pow[i - 1] * 8;
        }
    }
    

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();

        int[] sign = new int[n * k + 1];

        for (int i = 125874; i <= n * k; i++) {
            int j = i;
            while (j > 0) {
                sign[i] += pow[j % 10];
                j /= 10;
            }
        }

        StringBuilder sb = new StringBuilder();

        nextI:
        for (int i = 125874; i <= n; i++) {
            for (int j = 2; j <= k; j++) {
                if (sign[i] != sign[i * j]) continue nextI;
            }
            for (int j = 1; j <= k; j++) {
                sb.append(i * j).append(" ");
            }
            sb.append('\n');
        }

        System.out.print(sb.toString());
    }
}
