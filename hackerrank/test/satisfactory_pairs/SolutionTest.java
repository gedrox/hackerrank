package satisfactory_pairs;

import org.junit.Test;

import static org.junit.Assert.*;

public class SolutionTest {

    @Test
    public void testNumbers() {
        int[] exp = {2, 4, 5, 8, 10, 12, 15, 18, 20, 24, 28, 28, 35, 37, 42, 44, 49, 49, 60, 59, 66, 65, 79, 74, 85, 84, 93, 93, 107, 100, 120, 104, 126, 121, 142, 129, 145, 140, 160, 150, 173, 154, 189, 170, 196, 176};
        for (int n = 4; n < 50; n++) {
            assertEquals("Wrong for n=" + n, exp[n - 4], SolutionClean.solve(n));
        }
    }

    @Test
    public void testTiming() {
        long time = getTime2(5000);
        System.err.println(time + "ms");
        assertEquals(1.0, time / 100.0, 0.2);
    }

    @Test
    public void testLong() {
        System.out.println(getTime2(300000) + "ms");
    }

    @Test
    public void testSmall() {
        System.err.println(getTime2(41) + "ms");
    }


//    private long getTime(int n) {
//        long start = System.currentTimeMillis();
//        System.out.println(Solution.solve(n));
//        return System.currentTimeMillis() - start;
//    }

    private long getTime2(int n) {
        long start = System.currentTimeMillis();
        System.out.println(SolutionClean.solve(n));
        return System.currentTimeMillis() - start;
    }

}