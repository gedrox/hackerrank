package taste_win;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class SolutionCoolTest {

    @Test
    @Ignore
    public void test() {

        int[] max = {100,
                10, 8, 7, 6, 5,
                4, 3, 3, 2};

        for (int n = 1; n < 10; n++) {
        for (int m = 1; m <= max[n]; m++) {
            System.out.println(n + ", " + m);
            int brute = Solution.solve(n, m);
            int cool = SolutionCool.solve(n, m);
            assertEquals(n + ", " + m, brute, cool);
            System.out.println(cool);
            System.out.println(brute);
        }}
    }

    @Test
    public void test54() {
        System.out.println(SolutionCool.solve(10000000, 10000000));
    }

    @Test
    public void testMax() {
        assertEquals(253223948, SolutionCool.solve(10000000, 10000000));
        assertEquals(19764360, SolutionCool.solve(5, 5));
        assertEquals(763715163, SolutionCool.solve(7223, 2625));
    }

    @Test
    public void testRandom() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            int n = r.nextInt(10000000) + 1;
            int m = r.nextInt(10000000) + 1;
            long t = System.currentTimeMillis();
            int solve = SolutionCool.solve(n, m);
            t = System.currentTimeMillis() - t;
            System.out.printf("(%d, %d) --> %d (%dms)%n", n, m, solve, t);
        }
    }
}