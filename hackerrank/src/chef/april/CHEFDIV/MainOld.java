package chef.april.CHEFDIV;

import java.util.ArrayList;
import java.util.Arrays;

public class MainOld {
    public static void main(String[] args) {
        long t0 = System.currentTimeMillis();
        ArrayList<Integer> primes = new ArrayList<>();
        int LIMIT = 100000;
        int[] max = new int[LIMIT];
        max[1] = 0;

        for (int i = 2; i < LIMIT; i++) {
            int top = 0;
            int c = 1;
            for (int j = 2; j*j <= i; j++) {
                if (i % j == 0) {
                    c++;
                    if (top < max[j]) {
                        top = max[j];
                    }
                    if (j*j < i) {
                        c++;
                        if (top < max[i / j]) {
                            top = max[i / j];
                        }
                    }
                }
            }
            max[i] = top + c + 1;
        }

//        System.out.println(Arrays.toString(max));
        System.out.println("done");
        System.out.println(System.currentTimeMillis() - t0);
        System.out.println(max[1440]);
    }
}
