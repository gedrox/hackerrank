package decibinary;

import java.util.Arrays;
import java.util.function.Function;

public class Decibinary {
    public static void main(String[] args) {
        Integer[] a = new Integer[110011];
        for (int i = 0; i < a.length; i++) {
            a[i] = i;
        }

        final Function<Integer, Integer> toDB = i -> {
            int res = 0;
            int m = 1;
            while (i > 0) {
                res += m * (i % 10);
                i /= 10;
                m *= 2;
            }
            return res;
        };

        Arrays.sort(a, (o1, o2) -> {
            int diff = toDB.apply(o1) - toDB.apply(o2);
            if (diff != 0) return diff;
            return o1 - o2;
        });

        System.out.println(Arrays.toString(Arrays.copyOf(a, 100)));

        int c[] = new int[51];

        for (Integer i : a) {
            Integer db = toDB.apply(i);
            if (db <= 50) c[db]++;
        }

        System.out.println(Arrays.toString(c));
    }
}
