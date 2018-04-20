package hard_homework;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.*;

public class SolutionTest {
    @Test
    public void test() {
        for (int n = 100000; n <= 100100; n++) {
            long t = System.currentTimeMillis();
            double solve = Solution.solve(n);
            t = System.currentTimeMillis() - t;
            System.out.printf("%d - %.9f (%dms)%n", n, solve, t);
            assertTrue(t < 3500);
        }
    }

    @Test
    public void testNumbers() {
        double[] exp = {2.524412954, 2.592239396, 2.660065838, 2.727892280, 1.959714862, 1.403526471, 2.339928568, 2.672300216, 2.740126658, 2.807953100, 2.230713339, 1.462535920, 2.155444182, 2.673549325, 2.820187478, 2.888013920, 2.468882694, 1.733534397, 1.970959796, 2.595887220, 2.821436587, 2.968074740, 2.655250492, 1.971703752, 1.734140234, 2.445500420, 2.822685696, 2.969323849, 2.774970782, 2.209873107, 1.497320672, 2.261016034, 2.745023591, 2.970572958, 2.855031602, 2.396240906, 1.712694166, 2.076531648, 2.667361486, 2.971822067, 2.935092422, 2.582608704, 1.950863520, 1.865399879, 2.533845494, 2.894159962, 2.978628353, 2.720383201, 2.137231319, 1.628580317, 2.366587886, 2.816497857, 2.979877462, 2.822049284, 2.323599117, 1.591489012, 2.182103500, 2.738835752, 2.981126571, 2.902110103, 2.509966916, 1.829658366, 1.984202373, 2.605319760, 2.945010098, 2.965344085, 2.647741413, 2.045688841, 1.759839961, 2.456832651, 2.867347993, 2.989181967, 2.769649507, 2.232056639, 1.523020399, 2.287675351, 2.789685888, 2.990431076, 2.869127785, 2.418424438, 1.708453212, 2.089774225, 2.676794026, 2.974087359, 2.932361767, 2.575099625, 1.937313687, 1.880080834, 2.543278034, 2.918198128, 2.978236652, 2.712874122, 2.140514161, 1.654280044, 2.377920117, 2.840536023, 2.999735580};
        for (int n = 3; n < 100; n++) {
            assertEquals(n + " is bad", exp[n-3], Solution.solve(n), 1e-9/2);
        }
    }

    @Test
    public void testBigNumbers() {
        double[] exp = {2.675914857, 2.988700514, 2.909223953, 2.509504509, 1.833487180, 1.984890137, 2.603298424, 2.950920840, 2.965297854, 2.653256369, 2.049146749, 1.764043408, 2.460881578, 2.886822447, 2.994989171, 2.773405880, 2.246530185, 1.527463339, 2.288221287, 2.682435628, 2.997968193, 2.868881093, 2.423928794, 1.712154916, 2.095152103, 2.651516708, 2.974279640, 2.938768257, 2.579762117, 1.936722353, 1.883447588, 2.543441680, 2.924127939, 2.982513947, 2.712586885, 2.144060091, 1.654984097, 2.382579689, 2.847896005, 2.999724254, 2.821281629, 2.332274845, 1.586531766, 2.200165333, 2.677854149, 2.990179549, 2.904875114, 2.499742114, 1.819442124, 1.998128217, 2.610077091, 2.954036663, 2.962560469, 2.644969882, 2.036166399, 1.778318820, 2.470962054, 2.891611101, 2.993892788, 2.766607097, 2.234776468, 1.542685182, 2.299613157, 2.681715764, 2.998587425, 2.863633852, 2.413454103, 1.697602219, 2.107802591, 2.655767811, 2.976538184, 2.935182889, 2.570662211, 1.923168117, 1.897192682, 2.552325109, 2.928012163, 2.980553014, 2.705000158, 2.131625355, 1.669708432, 2.393293330, 2.853433981, 2.999410159, 2.815211712, 2.321118306, 1.571468018, 2.212145184, 2.679481653, 2.991580909, 2.900380546, 2.489909477, 1.805349642, 2.011313856, 2.616565209, 2.957069787, 2.959745087, 2.636551122, 2.023129220, 1.792543433};
        for (int n = 10000; n < 10100; n++) {
            assertTrue(exp[n-10000] - 1e-9/2 <= Solution.solve(n));
        }
    }

    @Test
    public void testNumbers2() {
        for (int n = 100; n < 1000; n++) {
//            System.out.println(n + "\t" + hackerrank.Solution.solve(n));
//            System.out.printf("%.9f, ", hackerrank.Solution.solve(n));
            SolutionBeforeCleanup.solve(n);
        }

        System.err.println(SolutionBeforeCleanup.MAX_POS_DIFF);
        System.err.println(SolutionBeforeCleanup.MAX_VAL_DIFF);
        System.err.println(SolutionBeforeCleanup.MAX_VAL_DIFF1);
        System.err.println(SolutionBeforeCleanup.MAX_VAL_DIFF2);
        System.err.println(SolutionBeforeCleanup.MAX_VAL_DIFF3);
    }

    @Test
    @Ignore
    public void testFast() {
        for (int n = 1500000; n <= 3000000; n++) {
            System.out.println("Check " + n);

            double FAST = Solution.solve(n);
            double SLOW = Solution.solve(n, false);
            assertEquals(FAST, SLOW, 1e-9/2);
        }
    }

    @Test
    public void testPerf() {
        Solution.CHUNK = 10;
        for (int n = 3000000; n > 3; n = (Math.random() < 0.1 ? (int) (n * .9) : n-1)) {
            long t = System.currentTimeMillis();
            double solve = Solution.solve(n);
            t = System.currentTimeMillis() - t;
            System.out.printf("%d - %.9f (%dms)%n", n, solve, t);
//            assertTrue(t < 3500);
        }
    }

    @Test
    public void test100038() {
        //2.7203299964454435
        assertEquals(2.7203299964454435, Solution.solve(100038), 1e-9/2);
//        assertEquals(2.7203299964454435, hackerrank.Solution.solve(1500002), 1e-9/2);
    }

    @Test
    public void collectStats() {

        Random r = new Random();

        int min = 3;
        int max = 100;

        while (min < 3000000) {
            for (int i = 0; i < 10; i++) {
                int next = r.nextInt(max - min) + min;
                long t = System.currentTimeMillis();
                double res = Solution.solve(next);
                double maxRes = Solution.solveCool(next);
                t = System.currentTimeMillis() - t;

                System.out.printf("%d\t%d\t%.9f\t%.9f\t%d\t%.9f%n", next, t, res, maxRes, Solution.MAX_INDEX_DIFF, Solution.MAX_VAL_DIFF);
            }

            min = max;
            max *= 2;
        }

    }

    @Test
    public void testChunks() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {

            Solution.CHUNK = -1;
            Solution.THRESHOLD = -1;

            int next = r.nextInt(3000000) + 3;
            double maxRes = Solution.solveCool(next);

            long t = System.currentTimeMillis();
            double res = Solution.solve(next);
            t = System.currentTimeMillis() - t;

            long t2 = -1;
            double resSlow = -1;
            if (!Objects.equals(String.format("%.9f", maxRes), String.format("%.9f", res))) {
                Solution.CHUNK = 40000;
                Solution.THRESHOLD = 0.1;
                t2 = System.currentTimeMillis();
                resSlow = Solution.solve(next);
                t2 = System.currentTimeMillis() - t2;

            }

            System.out.printf("%d\t%d\t%.9f\t%.9f\t%d\t%.9f\t%.9f\t%d\t%d%n",
                    next, t, res, maxRes, Solution.MAX_INDEX_DIFF, Solution.MAX_VAL_DIFF,
                    resSlow, t2, Solution.INIT_SIZE);

            if (resSlow != -1) assertEquals(resSlow, res, 0);
        }
    }


    @Test
    public void testWeird() {
        System.out.println(Solution.solveCool(1899903));
        System.out.println(Solution.solve(1899903));
        Solution.THRESHOLD = 0.1;
        Solution.CHUNK = 50000;
        System.out.println(Solution.solve(1899903));
    }

}