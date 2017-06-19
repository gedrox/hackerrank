package palindromictable;

import org.junit.Test;
import org.testng.Assert;

import java.io.*;
import java.util.*;

public class PalindromicTable {

    private static int n;
    private static int m;
    private static int[][] table;

    public static void main(String[] args) throws IOException {
        BufferedReader bi = new BufferedReader(new InputStreamReader(System.in));
        String[] split = bi.readLine().split(" ");
        n = Integer.parseInt(split[0]);
        m = Integer.parseInt(split[1]);
        table = new int[n][m];
        for (int table_i = 0; table_i < n; table_i++) {
            split = bi.readLine().split(" ");
            for (int table_j = 0; table_j < m; table_j++) {
                table[table_i][table_j] = Integer.parseInt(split[table_j]);
            }
        }

        int[] answer = solve();
        System.out.println((answer[2] - answer[0] + 1) * (answer[3] - answer[1] + 1));
        System.out.printf("%d %d %d %d%n", answer[0], answer[1], answer[2], answer[3]);
    }

    private static int[] solve() {
        if (n == 1 && m == 1) {
            return new int[]{0, 0, 0, 0};
        }

        int[] counts = new int[10];
        int[][] singlePos = new int[10][];

        Info x = new Info();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int digit = table[i][j];
                x.flip(digit);
                if (counts[digit] == 0) {
                    singlePos[digit] = new int[]{i, j};
                } else if (counts[digit] == 1) {
                    singlePos[digit] = null;
                }
                counts[digit]++;
            }
        }

        ArrayList<int[]> singles = new ArrayList<>();
        for (int[] singleton : singlePos) {
            if (singleton != null) {
                singles.add(singleton);
            }
        }
        
        boolean hasPositivePairs = false;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] >= 2) {
                hasPositivePairs = true;
                break;
            }
        }

        if (!hasPositivePairs || x.positiveCount < 2) {
            return new int[]{0, 0, 0, 0};
        }

        if (x.cardinality() < 2 && x.positiveCount > 1) {
            return new int[]{0, 0, n - 1, m - 1};
        }

        Info[][] TL = new Info[n][m];
        Info[][] TR = new Info[n][m];
        Info[][] BR = new Info[n][m];
        Info[][] BL = new Info[n][m];

        fill(TL, 0, 0, 1, 1);
        fill(TR, 0, m - 1, 1, -1);
        fill(BR, n - 1, m - 1, -1, -1);
        fill(BL, n - 1, 0, -1, 1);

        ArrayList<int[]> sizes = new ArrayList<>(n * m);
        for (int i = 0; i < n; i++) {
            nextSize:
            for (int j = 0; j < m; j++) {
                int H = i + 1;
                int W = j + 1;

                for (int s1 = 0; s1 < singles.size(); s1++) {
                    for (int s2 = s1 + 1; s2 < singles.size(); s2++) {
                        int x1 = singles.get(s1)[0];
                        int y1 = singles.get(s1)[1];
                        int x2 = singles.get(s2)[0];
                        int y2 = singles.get(s2)[1];
                        if (H <= Math.max(x1, x2)) continue;
                        if (W <= Math.max(y1, y2)) continue;
                        if (H < n - Math.min(x1, x2)) continue;
                        if (W < m - Math.min(y1, y2)) continue;

                        continue nextSize;
                    }
                }

                sizes.add(new int[]{H, W});
            }
        }

        sizes.sort(Comparator.comparing(el -> -el[0] * el[1]));

        for (int[] size : sizes) {

            int H = size[0];
            int W = size[1];

            for (int startI = 0; startI <= n - H; startI++) {
                int endI = startI + H - 1;
                for (int startJ = 0; startJ <= m - W; startJ++) {
                    int endJ = startJ + W - 1;
                    Info result = new Info();

                    result.add(TL[endI][endJ]);
                    result.add(BR[startI][startJ]);

                    if (startJ > 0 && endI < n - 1) {
                        result.add(BL[endI + 1][startJ - 1]);
                    }
                    if (startI > 0 && endJ < m - 1) {
                        result.add(TR[startI - 1][endJ + 1]);
                    }
                    result.deduct(x);

                    if (result.cardinality() < 2) {
                        if (result.positiveCount > 1 || H * W == 1) {
                            return new int[]{startI, startJ, endI, endJ};
                        }
                    }
                }
            }
        }

        return new int[]{0, 0, 0, 0};
    }

    private static void fill(Info[][] TL, int x0, int y0, int d_x, int d_y) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {

                int t_x = x0 + d_x * i;
                int t_y = y0 + d_y * j;

                Info result;
                if (i == 0 && j == 0) {
                    result = new Info();
                    result.flip(table[t_x][t_y]);
                } else {
                    if (i < j) {
                        result = TL[t_x][t_y - d_y].clone();
                        for (int k = 0; k <= i; k++) {
                            result.flip(table[x0 + d_x * k][t_y]);
                        }
                    } else {
                        result = TL[t_x - d_x][t_y].clone();
                        for (int k = 0; k <= j; k++) {
                            result.flip(table[t_x][y0 + d_y * k]);
                        }
                    }
                }
                TL[t_x][t_y] = result;
            }
        }
    }

    static int[] trivialSolve() {

        int maxSize = 1;
        int[] answer = new int[]{0, 0, 0, 0};

        for (int x0 = 0; x0 < n; x0++) {
            for (int y0 = 0; y0 < m; y0++) {
                for (int x1 = x0; x1 < n; x1++) {

                    nextRectangle:
                    for (int y1 = y0; y1 < m; y1++) {

                        int S = (y1 - y0 + 1) * (x1 - x0 + 1);
                        if (S <= maxSize) continue;
                        int[] c = new int[10];
                        for (int i = x0; i <= x1; i++) {
                            for (int j = y0; j <= y1; j++) {
                                c[table[i][j]]++;
                            }
                        }

                        int odds = 0;
                        boolean positivePair = false;
                        for (int i = 0; i < c.length; i++) {
                            int cnt = c[i];
                            if (cnt % 2 == 1) {
                                odds++;
                                if (odds == 2) continue nextRectangle;
                            }
                            if (cnt >= 2 && i > 0) {
                                positivePair = true;
                            }
                        }

                        if (positivePair && odds < 2) {
                            maxSize = S;
                            answer = new int[]{x0, y0, x1, y1};
                        }
                    }
                }
            }
        }

        return answer;
    }

    @Test
    public void test() {

        while (true) {

            long seed = System.currentTimeMillis();
            System.out.println("seed:" + seed);
            Random r = new Random(seed);

            for (int W = 316; W <= 316; W++) {
                for (int H = 316; H <= 316; H++) {
                    n = H;
                    m = W;

                    System.out.println(n + " " + m);
                    table = new int[n][m];
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < m; j++) {
                            if (i == 100 && j == 100) table[i][j] = 8;
                            else if (i == 100 && j == 101) table[i][j] = 9;
                            else if (i == 200 && j == 200) table[i][j] = 6;
                            else if (i == 200 && j == 201) table[i][j] = 7;
                            else table[i][j] = r.nextInt(6);
                            //                        System.out.print(table[i][j] + " ");
                        }
                        //                    System.out.println();
                    }

                    long t0 = System.currentTimeMillis();
                    int[] actual = solve();
                    t0 = System.currentTimeMillis() - t0;


                    //                int[] expected = trivialSolve();

                    System.out.println(Arrays.toString(actual));
                    System.out.println(t0 + "ms");
                    if (t0 > 4000) Assert.fail();
                    //                System.out.println(Arrays.toString(expected));

                    //                Assert.assertEquals((actual[2] - actual[0] + 1) * (actual[3] - actual[1] + 1), (expected[2] - expected[0] + 1) * (expected[3] - expected[1] + 1));
                }
            }
        }
    }

    @Test
    public void testSmall() {

        long seed = System.currentTimeMillis();
        System.out.println("seed:" + seed);
        Random r = new Random(seed);

        for (int W = 0; W <= 100; W++) {
            for (int H = 0; H <= 100; H++) {
                n = H;
                m = W;

                System.out.println(n + " " + m);
                table = new int[n][m];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < m; j++) {
                        table[i][j] = r.nextInt(6);
//                        System.out.print(table[i][j] + " ");
                    }
//                    System.out.println();
                }

                long t0 = System.currentTimeMillis();
                int[] actual = solve();
                t0 = System.currentTimeMillis() - t0;


                int[] expected = trivialSolve();

                System.out.println(Arrays.toString(actual));
                System.out.println(t0 + "ms");
                if (t0 > 4000) Assert.fail();
                System.out.println(Arrays.toString(expected));

                Assert.assertEquals((actual[2] - actual[0] + 1) * (actual[3] - actual[1] + 1), (expected[2] - expected[0] + 1) * (expected[3] - expected[1] + 1));
            }
        }
    }

    static class Info implements Cloneable {
        BitSet bitSet = new BitSet(10);
        int positiveCount;

        public void flip(int bit) {
            bitSet.flip(bit);
            if (bit > 0) positiveCount++;
        }

        public int cardinality() {
            return bitSet.cardinality();
        }

        public void add(Info that) {
            bitSet.xor(that.bitSet);
            positiveCount += that.positiveCount;
        }

        public void deduct(Info that) {
            bitSet.xor(that.bitSet);
            positiveCount -= that.positiveCount;
        }

        @Override
        protected Info clone() {
            Info info = new Info();
            info.bitSet.xor(this.bitSet);
            info.positiveCount = this.positiveCount;
            return info;
        }
    }
}
