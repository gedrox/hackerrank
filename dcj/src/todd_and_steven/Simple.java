package todd_and_steven;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Simple {
    static int MOD = 1000000007;
    
    public static void main(String[] args) {
        ArrayList<Long> all = new ArrayList<>();

        for (long val : todd_and_steven.t) all.add(val);
        for (long val : todd_and_steven.s) all.add(val);

        Collections.sort(all);

        long sum = 0;
        for (int i = 0; i < all.size(); i++) {
            sum += i ^ all.get(i);
            sum %= MOD;
        }

        System.out.println(sum);
    }
}
