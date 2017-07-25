package RecurrentOnATree;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RecurrentOnATreeTest extends RecurrentOnATree {
    @Test
    public void test() throws InterruptedException {
        int TEST_INDEX = 10000000;
        prepare(20);

        int recResult = recFib(TEST_INDEX);

        System.out.println(2 * (Math.log(TEST_INDEX / 20) / Math.log(2) + 1));
        System.out.println(ccache.size());

        Assert.assertTrue(ccache.size() < 2 * Math.log(TEST_INDEX / 20) / Math.log(2) + 1);
        System.out.println(recResult);

        prepare(TEST_INDEX);
        assertEquals(smallF[TEST_INDEX], recResult);
    }

    @Test
    public void testSpeed() {
        prepare();

        int hash = 0;
        for (int i = 0; i < 1e6; i++) {
            hash ^= recFib(20_000_000_000L - i);
        }
        System.out.println(hash);
    }

    @Test
    public void test3Sum() {
        prepare();

        int x = 5, y = 9, z = 22;

        System.out.println(smallF[x + y + z]);

        long fx = smallF[x];
        long fy = smallF[y];
        long fz = smallF[z];

        long fx1 = smallF[x - 1];
        long fy1 = smallF[y - 1];
        long fz1 = smallF[z - 1];

        long res = fx*fy*fz + fx1*fy1*fz + fx1*fy*fz1 + fx*fy1*fz1 - fx1*fy1*fz1;

        System.out.println((res % MOD + MOD) % MOD);
    }

    @Test
    public void test3SumMinus1() {
        prepare();

        int x = 5, y = 9, z = 22;

        System.out.println(smallF[x + y + z - 1]);

        long fx = smallF[x];
        long fy = smallF[y];
        long fz = smallF[z];

        long fx1 = smallF[x - 1];
        long fy1 = smallF[y - 1];
        long fz1 = smallF[z - 1];

        long res = fx1*fy*fz + fx*fy1*fz + fx*fy*fz1
                - (fx1*fy1*fz + fx1*fy*fz1 + fx*fy1*fz1)
                + 2 * fx1*fy1*fz1;

        System.out.println((res % MOD + MOD) % MOD);
    }

    @Test
    public void test4sum() {
        prepare();

        int x = 2, y = 4, z = 22, u = 9;

        System.out.println(smallF[x + y + z + u]);

        long fx = smallF[x];
        long fy = smallF[y];
        long fz = smallF[z];
        long fu = smallF[u];

        long fx1 = smallF[x - 1];
        long fy1 = smallF[y - 1];
        long fz1 = smallF[z - 1];
        long fu1 = smallF[u - 1];

        long res = fx*fy*fz*fu
                + fx1*fy1*fz*fu + fx1*fy*fz1*fu + fx1*fy*fz*fu1 + fx*fy1*fz1*fu + fx*fy1*fz*fu1 + fx*fy*fz1*fu1
                - (fx*fy1*fz1*fu1 + fx1*fy*fz1*fu1 + fx1*fy1*fz*fu1 + fx1*fy1*fz1*fu)
                + 2 * (fx1*fy1*fz1*fu1);

        System.out.println((res % MOD + MOD) % MOD);
    }

    @Test
    public void test4sumMinus1() {
        prepare();

        int x = 2, y = 4, z = 22, u = 9;

        System.out.println(smallF[x + y + z + u - 1]);

        long fx = smallF[x];
        long fy = smallF[y];
        long fz = smallF[z];
        long fu = smallF[u];

        long fx1 = smallF[x - 1];
        long fy1 = smallF[y - 1];
        long fz1 = smallF[z - 1];
        long fu1 = smallF[u - 1];

        long res = fx1*fy*fz*fu + fx*fy1*fz*fu + fx*fy*fz1*fu + fx*fy*fz*fu1
                - (fx1*fy1*fz*fu + fx1*fy*fz1*fu + fx1*fy*fz*fu1 + fx*fy1*fz1*fu + fx*fy1*fz*fu1 + fx*fy*fz1*fu1)
                + 2 * (fx*fy1*fz1*fu1 + fx1*fy*fz1*fu1 + fx1*fy1*fz*fu1 + fx1*fy1*fz1*fu)
                - 3 * (fx1*fy1*fz1*fu1);

        System.out.println((res % MOD + MOD) % MOD);
    }
    
    @Test
    public void bigTest() {

        Random r = new Random();
        
        int n = 200000;

        ArrayList<Integer>[] next = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            next[i] = new ArrayList<>();
        }

        for (int i = n - 1; i > 0; i--) {
            int parent = r.nextInt(i);
            next[parent].add(i);
            next[i].add(parent);
        }
        
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            c[i] = r.nextInt(100001);
        }
        
        prepare();
        long res = solve(c);
        System.out.println(res);
        Assert.assertTrue(res >= 0);
        Assert.assertTrue(res < MOD);
    }

    @Test
    public void testCase1() {
        prepare();
        setN(4);
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 3);

        int[] c = {2, 1, 1, 1};

        assertEquals(53, solve(c));
    }

    @Test
    public void testCase1Adv() {
        prepare();
        setN(4);
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 3);

        int[] c = {100, 200, 50, 1};


        int[] f = RecurrentOnATree.smallF;
        assertEquals((0L + f[1] + f[50] + f[200] + f[100] + 2 * (0L + f[300] + f[150] + f[101] + f[350] + f[301] + f[151])) % MOD, solve(c));
        System.out.println(solve(c));
        System.out.println(0L + f[1] + f[50] + f[200] + f[100] + 2 * (0L + f[300] + f[150] + f[101] + f[350] + f[301] + f[151]));
    }

    @Test
    public void testCase3() {
        prepare();
        setN(5);
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 3);
        addEdge(1, 4);

        int[] c = {2, 1, 1, 1, 2};

        assertEquals(129, solve(c));
    }
    
    @Test
    public void testCase2() {
        prepare();
        setN(10);
        addEdge(0, 1);
        addEdge(0, 2);
        addEdge(0, 3);
        addEdge(1, 4);
        addEdge(1, 5);
        addEdge(2, 6);
        addEdge(2, 7);
        addEdge(3, 8);
        addEdge(3, 9);
        
        int[] c = new int[n];
        for (int i = 0; i < n; i++) {
            c[i] = 1;
        }

        assertEquals(10+2*18+3*24+5*24+8*24, solve(c));
    }
    
    @Test
    public void testFibCalc() {
        prepare();
        for (int i = - (smallF.length + 100000); i < smallF.length + 100000; i++) {
            assertTrue(recFib(i) >= 0);
            assertTrue(recFib(i) < MOD);
            assertEquals(0, (recFib(i) - recFib(i - 1) - recFib(i - 2)) % MOD);
        }
    }
}
