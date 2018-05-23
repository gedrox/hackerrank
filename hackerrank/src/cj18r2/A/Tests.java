package cj18r2.A;

import org.junit.*;

import static cj18r2.A.Solution.*;
import static org.junit.Assert.*;

public class Tests {
    @Test
    public void sample1() {
        C = 4;
        B = new int[] {1, 1, 1, 1};
        String act = solve().trim();
        assertEquals("1\n" + "....", act);
    }

    @Test
    public void sample2() {
        C = 3;
        B = new int[]{0, 2, 1};
        String act = solve();
        assertEquals("IMPOSSIBLE", act);
    }

    @Test
    public void sample3() {
        C = 6;
        B = new int[]{3, 0, 0, 2, 0, 1};
        String act = solve().trim();
        assertEquals("3\n" +
                ".//./.\n" +
                "./....\n" +
                "......", act);
    }
}
