package ballsAndBoxes;

import org.junit.Test;
import org.testng.Assert;

import java.util.*;

public class Solution {

    public static final boolean DEBUG = false;

    @Test
    public void test2() {
        int n = 5;
        int m = 4;
        
        int[] A = {4, 4, 4, 2, 4};
        int[] C = {1, 1, 1, 5};
        int[][] B = {
                {2, 2, 2, 1},
                {2, 2, 1, 1},
                {2, 1, 0, 1},
                {2, 2, 2, 1},//<=2
                {2, 2, 0, 0},
        };

        for (int seed = 0; seed < 10; seed++) {
            System.out.println(solve(n, m, A, C, B, seed));
        }
    }
    
    @Test
    public void test() {
        
        long seed = System.currentTimeMillis();
        seed = 1495184935968L;
        System.out.println(seed);
        
        Random r = new Random(0);
        
//        int n = 3;
//        int m = 10;
//        int MAX_A = 10;
//        int MAX_C = 10;
//        int MAX_B = 1;
        
        int n = 10;
        int m = 10;
        int MAX_A = 10;
        int MAX_C = 10;
        int MAX_B = 2;
        
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = r.nextInt(MAX_A) + 1;
        }
        int[] C = new int[m];
        for (int i = 0; i < m; i++) {
            C[i] = r.nextInt(MAX_C + 1);
        }
        
        int[][] B = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                B[i][j] = r.nextInt(MAX_B + 1);
            }
        }
        
        long max = -1;

        if (DEBUG) System.out.println(Arrays.toString(A));
        if (DEBUG) System.out.println(Arrays.toString(C));
        if (DEBUG) System.out.println(Arrays.deepToString(B));
        
        for (int seed2 = 0; seed2 < 10; seed2++) {
            long solve = solve(n, m, A, C, B, seed2);
            System.out.println(solve);
    
            if (max == -1) {
                max = solve;
            } else {
                Assert.assertEquals(solve, max, "at seed " + seed2);  
            }
        }
        System.out.println(max);
        
//        solve = solve(n, m, A, C, B, 1e-3);
//        System.out.println(solve);
//        solve = solve(n, m, A, C, B, 1e-2);
//        System.out.println(solve);
//        solve = solve(n, m, A, C, B, 1e-1);
//        System.out.println(solve);
//        solve = solve(n, m, A, C, B, 1);
//        System.out.println(solve);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // color count
        int n = in.nextInt();

        // box count
        int m = in.nextInt();

        // count of balls per color
        int[] A = new int[n];
        for (int A_i = 0; A_i < n; A_i++) {
            A[A_i] = in.nextInt();
        }

        // box capacities
        int[] C = new int[m];
        for (int C_i = 0; C_i < m; C_i++) {
            C[C_i] = in.nextInt();
        }

        // bonus points per color/box
        int[][] B = new int[n][m];
        for (int B_i = 0; B_i < n; B_i++) {
            for (int B_j = 0; B_j < m; B_j++) {
                B[B_i][B_j] = in.nextInt();
            }
        }


        long result = 0;
        
        for (long seed = 0; seed < 100; seed++) {
            result = Math.max(result, solve(n, m, A, C, B, seed));
        }

        System.out.println(result);

    }

    private static long solve(int n, int m, int[] A, int[] C, int[][] B) {
        return solve(n, m, A, C, B, 0);
    }

    private static long solve(int n, int m, int[] origA, int[] origC, int[][] B, long seed) {
        int[] A = origA.clone();
        int[] C = origC.clone();
        
        int[] newA = new int[n];
        int[] newC = new int[m];
        int[][] newB = new int[n][m];

        ArrayList<Integer> shuffleCol = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            shuffleCol.add(i);
        }
        Collections.shuffle(shuffleCol, new Random(seed));

        ArrayList<Integer> shuffleBox = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            shuffleBox.add(i);
        }
        Collections.shuffle(shuffleBox, new Random(seed));

        if (seed == 0) {
            shuffleBox.sort(Comparator.comparing(i -> -origC[i]));
            shuffleCol.sort(Comparator.comparing(i -> -origA[i]));
        }

        for (int i = 0; i < n; i++) {
            newA[i] = A[shuffleCol.get(i)];
            for (int i1 = 0; i1 < m; i1++) {
                newB[i][i1] = B[shuffleCol.get(i)][shuffleBox.get(i1)];
            }
        }
        for (int i1 = 0; i1 < m; i1++) {
            newC[i1] = C[shuffleBox.get(i1)];
        }
//        A = newA;
//        B = newB;
//        C = newC;

        if (DEBUG) {
            System.out.println("--------------");
            System.out.println(Arrays.toString(newA));
            System.out.println(Arrays.toString(newC));
            System.out.println(Arrays.deepToString(newB));
        }
            
        
        long result = 0, oldResult;

        boolean[][] X = new boolean[n][m];
        
        HashSet<Integer>[] state = new HashSet[m];
        for (int i = 0; i < m; i++) {
            state[i] = new HashSet<>();
        }
        
        do {
            oldResult = result;

            while (true) {
                long maxBon = -1;
                int maxCol = -1;
                int maxBox = -1;
                int bestCapacity = Integer.MIN_VALUE;

                for (int col: shuffleCol) {
                    for (int box : shuffleBox) {
                        if (A[col] > 0 && !X[col][box]) {
                            long bonus = B[col][box];
                            if (C[box] <= 0) {
                                bonus += 2 * C[box] - 1;
                            }

                            if (bonus > maxBon || (bonus == maxBon && C[box] > bestCapacity)) {
                                maxBon = bonus;
                                maxCol = col;
                                maxBox = box;
                                bestCapacity = C[box];
                            }
                        }
                    }
                }

                if (maxBon <= 0) {
                    break;
                }

                result += maxBon;
                A[maxCol]--;
                C[maxBox]--;
                X[maxCol][maxBox] = true;

                if (DEBUG) System.out.printf("Put %d in %d for %d%n", maxCol, maxBox, maxBon);
                state[maxBox].add(maxCol);
                
                
            }
            
            for (int from : shuffleBox) {
                for (int to : shuffleBox) {
                    if (from != to) {

                        for (int col : shuffleCol) {
                            if (X[col][from] && !X[col][to]) {
                                int diff = 0;
                                diff += B[col][to] - B[col][from];
                                if (C[from] < 0) {
                                    diff += -2 * C[from] - 1;
                                }
                                if (C[to] <= 0) {
                                    diff += 2 * C[to] - 1;
                                }

                                if (diff > 0) {
                                    result += diff;
                                    X[col][from] = false;
                                    X[col][to] = true;
                                    C[from]++;
                                    C[to]--;

                                    if (DEBUG) System.out.printf("Move %d from %d to %d for %d%n", col, from, to, diff);
                                    state[from].remove(col);
                                    state[to].add(col);
                                }
                            }
                        }
                    }
                }
            }
        } while (result > oldResult);

        if (DEBUG) System.out.println("state: " + Arrays.toString(state));
        
        return result;
    }
}
