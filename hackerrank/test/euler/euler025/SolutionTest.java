package euler.euler025;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by agedroics on 9/26/2016.
 */
public class SolutionTest {
    @Test
    public void testAll() {
        int[] d = Solution.collectSlow();
        for (int i = 2; i <= 5000; i++) {
            int val = Solution.solveFast(i);
            assertEquals("Fail for i = " + i, d[i]+2, val);
        }
    }
}