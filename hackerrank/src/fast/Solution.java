package fast;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

public class Solution {

    static HashMap<Integer, Integer> cache = new HashMap<>(10000);
    static int[] tail = new int[240];
    static {
        cache.put(1, 1);
    }
    static int[] max = new int[5003];
    static int max_i = 0;
    static int longest = 0;

    static int collatzSequenceLen(int n) {
        if (n == 0) {
            return 0;
        }
        int cnt = 0;
        while (!cache.containsKey(n)) {
            tail[cnt++] = n;
            if (n % 2 == 0) {
                n = n/2;
            } else {
                n = 3 * n + 1;
            }
        }
        int base = cache.get(n);
        while (--cnt >= 0) {
            cache.put(tail[cnt], ++base);
        }
        return base;
    }

    static void fillMax(int n) {
        while (n > max_i) {
            int size = collatzSequenceLen(++max_i);
            if (size >= longest) {
                longest = size;
                max[max_i] = max_i;
            } else {
                max[max_i] = max[max_i - 1];
            }
        }
    }

    static int collatzSequenceSum(int T, int A, int B) {
        int n = 0;
        int result = 0;
        while (--T >= 0) {
            n = (A*n + B) % 5003;
            if (n > max_i) fillMax(n);
            result += max[n];
        }

        return result;
    }
    
    @Test
    public void testIt() {
        int result = collatzSequenceSum(200, 1000, 1000);
        System.out.println(result);

        System.out.println(Arrays.toString(max));
    }

    @Test
    public void test() {

        cache = new HashMap<>(10000);
        cache.put(1, 1);

        int[] max = new int[5003];
        max[0] = 0;
        max[1] = 1;
        int longest = 1;
        for (int i = 2; i < 5003; i++) {
            int size = collatzSequenceLen(i);
            if (size > longest) {
                longest = size;
                max[i] = i;
            } else {
                max[i] = max[i - 1];
            }
        }

        System.out.println(Arrays.toString(max));
        
//        cache = new int[8_153_621];
//        cache[1] = 1;
        
        
        int maxlen = 0;
        for (int i = 0; i < 5003; i++) {
            int len = collatzSequenceLen(i);
            if (len > maxlen) maxlen = len;
//            System.out.println(len);
        }

        System.out.println("Maxlen=" + maxlen);

//        for (int i = cache.length - 1; i >= 0; i--) {
//            if (cache[i] != 0) {
//                System.out.println(i);
//                break;
//            }
//        }
//
//        int cntNonZero = 0;
//        int firstZero = 0;
//        for (int i1 = 0; i1 < cache.length; i1++) {
//            int i = cache[i1];
//            if (i != 0) cntNonZero++;
//            if (i == 0 && firstZero == 0) {
//                firstZero = i1;
//            }
//        }
//        System.out.println(cntNonZero);
//        System.out.println(firstZero);
    }
}
