package cj18r2.C;

import org.junit.Test;

import java.util.Random;

import static cj18r2.C.Solution.*;
import static org.junit.Assert.*;

public class Tests {
    @Test
    public void sample1() {
        N = 2;
        A = new int[][] {
                {1, 2},
                {1, 2}
        };
        int act = Solution.solve();
        assertEquals(2, act);
    }


    @Test
    public void sampleBig() {
        N = 100;
        A = new int[N][N];
        Random r = new Random(0);

        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                A[x][y] = (r.nextBoolean() ? -1 : 1) * (r.nextInt(N) + 1);
//                A[x][y] = (x + y) % N + 1;
            }
        }
        
        int act = Solution.solve();
        System.out.println(act);
    }

}
