package hackerrank;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class Simplese {

    static int[] data = new int[1000000];
    static {
        for (int i = 0; i < data.length; i++) {
            data[i] = (int) (Math.random() * 200);
        }
    }
    
    @Test
    public void test() {
        // sort or not to sort?
//        Arrays.sort(data);

        int cnt = 0;
        for (int j = 0; j < 100; ++j) {
            for (int i : data) {
                if (i < 100) cnt++;
            }
        }
        System.out.println(cnt);

    }
}
