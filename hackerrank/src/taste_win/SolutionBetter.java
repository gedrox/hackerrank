package taste_win;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SolutionBetter {

    @Test
    public void test() {
        System.out.printf("%s\t%s\t%s\t%s%n", "n", "m", "LOOSE", "SIZE");
        for (int m = 1; m < 10; m++) {
            for (int n = 1; n < 10; n++) {
                int WIN = 0;
                List<int[]> c = combinations((int) Math.pow(2, m) - 1, n);
                for (int[] v : c) {
                    int x = 0;
                    for (int i : v) {
                        x ^= (i+1);
                    }
                    if (x != 0) WIN++;
                }
                System.out.printf("%d\t%d\t%d\t%d%n", n, m, c.size() - WIN, c.size());
            }
        }


    }

    List<int[]> combinations(int n, int k) {

        List<int[]> subsets = new ArrayList<>();

        int[] s = new int[k];                  // here we'll keep indices
        // pointing to elements in input array

        if (k <= n) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++) ;
            subsets.add(getSubset(s));
            for (; ; ) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == n - k + i; i--) ;
                if (i < 0) {
                    break;
                } else {
                    s[i]++;                    // increment this item
                    for (++i; i < k; i++) {    // fill up remaining items
                        s[i] = s[i - 1] + 1;
                    }
                    subsets.add(getSubset(s));
                }
            }
        }

        return subsets;
    }

    // generate actual subset by index sequence
    int[] getSubset(int[] subset) {
        int[] result = new int[subset.length];
        for (int i = 0; i < subset.length; i++)
            result[i] = subset[i];
        return result;
    }

}

