package chefmay.FAKEBS;

import org.junit.Test;

import java.util.TreeSet;

public class TreeSetTailSize {
    
    TreeSet<Integer> set = new TreeSet<>();

    // 5e6 -- 38ms
    // 1e7 -- 60ms
    // 2e7 -- 106ms
    public static final int L = 5_000_000;

    {
        for (int i = L; i > 0; i--) {
            set.add(i);
        }
    }
    
    @Test
    public void test() {
        System.out.println(set.tailSet(L/2).size());
    }
}
