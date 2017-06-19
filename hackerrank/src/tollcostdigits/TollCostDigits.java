package tollcostdigits;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

public class TollCostDigits {

    private static final boolean DEBUG = true;
    static ArrayList<int[]>[] roads;
    static boolean[][] digit;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] line = in.readLine().split(" ");
        
        long t0 = System.currentTimeMillis();
        
        int n = Integer.parseInt(line[0]);
        roads = new ArrayList[n];
        
        for (int i = 0; i < n; i++) {
            roads[i] = new ArrayList<>();
        }
        int e = Integer.parseInt(line[1]);
        for (int a0 = 0; a0 < e; a0++) {
            line = in.readLine().split(" ");
            int x = Integer.parseInt(line[0]) - 1;
            int y = Integer.parseInt(line[1]) - 1;
            int r = Integer.parseInt(line[2]);

            roads[x].add(new int[]{y, r % 10});
            roads[y].add(new int[]{x, (1000 - r) % 10});
        }

        boolean[] used = new boolean[n];

        long[] T = new long[10];
        int prevInit = -1;
        
        long t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0, t6 = 0, t7 = 0, t8 = 0;

        digit = new boolean[n][10];
        while (true) {

            long tmp = System.currentTimeMillis();
            
            int init = -1;

            t1 += System.currentTimeMillis() - tmp;
            tmp = System.currentTimeMillis();

            for (int i = prevInit + 1; i < n; i++) {
                if (!used[i]) {
                    init = i;
                    if (DEBUG) System.err.println(init);
                    break;
                }
            }

            t2 += System.currentTimeMillis() - tmp;
            tmp = System.currentTimeMillis();
            
            if (init == -1) {
                break;
            }

            LinkedList<int[]> q = new LinkedList<>();
            q.add(new int[]{init, 0});
            digit[init][0] = true;
            ArrayList<Integer> usedNow = new ArrayList<>();

            t3 += System.currentTimeMillis() - tmp;
            tmp = System.currentTimeMillis();

            while (!q.isEmpty()) {
                int[] next = q.poll();

                int id = next[0];
                int dig = next[1];

                if (!used[id]) {
                    used[id] = true;
                    usedNow.add(id);
                }

                for (int[] dest : roads[id]) {
                    int nextDig = (dig + dest[1]) % 10;
                    if (!digit[dest[0]][nextDig]) {
                        digit[dest[0]][nextDig] = true;
                        q.add(new int[]{dest[0], nextDig});
                    }
                }
            }

            t4 += System.currentTimeMillis() - tmp;
            tmp = System.currentTimeMillis();

            int[] A = new int[10];
            for (int id : usedNow) {
                boolean[] digs = digit[id];
                for (int i = 0; i < digs.length; i++) {
                    if (digs[i]) {
                        A[i]++;
                    }
                }
            }

            t5 += System.currentTimeMillis() - tmp;
            tmp = System.currentTimeMillis();

            long[] B = new long[10];
            for (int d = 0; d < 10; d++) {
                for (int j = 0; j < 10; j++) {
                    B[d] += ((long) A[j]) * A[(d + j) % 10];
                }
            }

            t6 += System.currentTimeMillis() - tmp;
            tmp = System.currentTimeMillis();

            for (int id : usedNow) {
                boolean[] digs = digit[id];
                for (int i = 0; i < digs.length; i++) {
                    for (int j = 0; j < digs.length; j++) {
                        if (digs[i] && digs[j]) {
                            B[(i + 10 - j) % 10]--;
                        }
                    }
                }
            }

            t7 += System.currentTimeMillis() - tmp;
            tmp = System.currentTimeMillis();

            int div = 0;
            for (boolean b : digit[init]) {
                if (b) div++;
            }

            for (int i = 0; i < 10; i++) {
                B[i] /= div;
            }

            for (int i = 0; i < 10; i++) {
                T[i] += B[i];
            }

            prevInit = init;
            t8 += System.currentTimeMillis() - tmp;
        }

        for (int i = 0; i < 10; i++) {
            System.out.println(T[i]);
        }

        if (DEBUG) System.err.printf("%d (%d, %d, %d, %d, %d, %d, %d, %d)%n", System.currentTimeMillis() - t0, t1, t2, t3, t4, t5, t6, t7, t8);
    }
}

