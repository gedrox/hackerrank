package cj18r2.B;

import org.junit.Test;

import java.util.Arrays;

import static cj18r2.B.Solution.*;
import static org.junit.Assert.assertEquals;

public class Tests {
    @Test
    public void sample1() {
        B = 2;
        R = 0;
        int act = solve();
        assertEquals(1, act);
        assertEquals(1, solve2());
    }

    @Test
    public void sample2() {
        B = 4;
        R = 5;
//        int act = Solution.solve();
//        assertEquals(5, act);
        assertEquals(5, solve2());
    }

    @Test
    public void sample500() {
        B = 500;
        R = 500;
        int act = Solution.solve2();
        System.out.println(act);
    }
    
    @Test
    public void testSmall() {
        for (int r = 0; r <= 50; r++) {
            for (int b = 0; b <= 50; b++) {
                R = r;
                B = b;
                if (R >= B)
                System.out.print(solve() + "\t");
                else System.out.print("\t");
                
                assertEquals(r + ", " + b, solve2(), solve());
            }
            System.out.println();
        }
    }
    
}
