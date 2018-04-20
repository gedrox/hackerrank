package codechef.workers;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        long[] min = {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
        int[] c = new int[N];
        for (int i = 0; i < N; i++) c[i] = sc.nextInt();
        for (int i = 0; i < N; i++) {
            int t_i = sc.nextInt() - 1;
            min[t_i] = Math.min(min[t_i], c[i]);
        }

        System.out.println(Math.min(min[0] + min[1], min[2]));
    }
}
